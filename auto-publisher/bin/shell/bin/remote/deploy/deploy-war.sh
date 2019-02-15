#!/usr/bin/env bash

function _status(){
	local pid=$(pgrep -f "${PTAG}")
	if [ -n "${pid}" ];then
		APP_STATUS="RUN"
	else
		APP_STATUS="STOP"
	fi
}

function _check_rollback(){
	if [ $1 -ne 0 ];then
		echo "rollback,$2 success"
	else
		echo "rollback,$2 fail"
	fi
}

function _ip(){
	local host=$(hostname)
	host=${host%%.*}
	IP=$(grep "${host}" /etc/hosts |head -n1|awk -F" " '{print $1}')
}

function _persionlize_config(){
	_ip
	local config_path=$(cat $1|jq .local|sed 's/"//g')
	if [ "${config_path}" == "null" ];then
		echo "persionlized config file format is illegal"
		return 1
	fi
	local index=0
	local ip=$(cat $1|jq .items[${index}].ip|sed 's/"//g')
	while [ ! "${ip}" == "null" ]
	do
		if [ "${ip}" == "${IP}" ];then
			local j=0
			local from=$(cat $1|jq .items[${index}].items[${j}].from|sed 's/"//g')
			while [  ! "${from}" == "null" ]
			do
				local to=$(cat $1|jq .items[${index}].items[${j}].to|sed 's/"//g')
				if [ -e "${PKG_CONF_HOME}/${PROJECT}/${to}" ];then
					mv "${PKG_CONF_HOME}/${PROJECT}/${to}" "${PKG_CONF_HOME}/${PROJECT}/${to}.${TIME}.save"
				fi
				cp -rf "${PKG_CONF_HOME}/${PROJECT}/${config_path}/${from}" "${PKG_CONF_HOME}/${PROJECT}/${to}"
				j=$(expr ${j} + 1)
				from=$(cat $1|jq .items[${index}].items[${j}].from|sed 's/"//g')
			done
			break
		fi
		index=$(expr ${index} + 1)
		ip=$(cat $1|jq .items[${index}].ip|sed 's/"//g')
	done
}

function _rollback_war(){
	if [ -e "${war}" ];then
		rm -rf "${war}"
		_check_rollback $? "remove application package deploy home"
	fi
	if [ -e "${PWAR}.save" ];then
		mv ${PWAR}.save ${PWAR}
		_check_rollback $? "restore application package"
	fi
}

function _start(){
	#Non-interactive shell,the default does not load profile ,so point out to load environment variables
	source ~/.bash_profile
	source /etc/profile
	for ((i=0;i<3;i++))
	do
		for ((j=0;j<10;j++))
		do
			sleep 1
			local pid=$(pgrep -f "${PTAG}")
			if [ -n "${pid}" ];then
				break
			fi
		done
		if [[ ${j} -ge 10 ]];then
			break
		fi
		nohup ${PKG_HOME}/bin/startup.sh > /dev/null 2>&1 &
	done
	if [[ ${i} -ge 3 ]];then
		echo "startup application fail"
		return 1
	else
		echo "startup application success"
	fi
}

function _shutdown(){
	if [[ "${APP_STATUS}" == "RUN" ]] ;then
		for ((i=0;i<3;i++))
		do
			sleep 1
			local pid=$(pgrep -f "${PTAG}")
			if [ -n "${pid}" ];then
				pkill -f "${PTAG}"
			else
				break
			fi
		done
		if [[ ${i} -ge 3 ]];then
			echo "Shutdown application fail"
			exit 1
		else
			echo "Shutdown application success"
		fi
	fi
}

#shell start
if [ $# -lt 3 ];then
	echo "The input parameters are incorrect
usage: deploy-war.sh [PKG_RLSE_HOME] [PKG_CONF_HOME] [PKG_HOME]"
	exit 1
fi

PKG_RLSE_HOME=$1
PKG_CONF_HOME=$2
BLD_NUM=$(basename ${PKG_RLSE_HOME})
PROJECT=$(basename ${PKG_RLSE_HOME%/*})
PACKAGE=$(ls ${PKG_RLSE_HOME}/|grep ".tar.gz"|head -n1)
PKG_HOME="$3/tomcat/tomcat_${PROJECT}"
TOMCAT_WEBAPPS="${PKG_HOME}/webapps"
PTAG="${PKG_HOME}/bin/bootstrap.jar"
TIME=$(date +%Y%m%d)

#check tomcat home
if [ ! -e "${PKG_HOME}" ];then
	echo "can't found tomcat home('${PKG_HOME}')"
	exit 1
fi
echo "check tomcat home success"

#check startup file
SCRIPT="${PKG_HOME}/bin/startup.sh"
if [ ! -e "${SCRIPT}" ];then
	echo "can't found startup script file in project(${SCRIPT})"
	return 1
fi
echo "check application startup file success"

#check tar.gz
if [ $(ls ${PKG_RLSE_HOME}/|wc -w) -lt 2 ];then
	echo "extract the application package('${PACKAGE}')"
	tar -xf "${PKG_RLSE_HOME}/${PACKAGE}" -C "${PKG_RLSE_HOME}/"
fi
echo "check application package extracted success"

#check war
WAR=$(find ${PKG_RLSE_HOME}/ -path \*/*${PROJECT}*.war|head -n1)
if [ -z "${WAR}" ];then
	echo "can't found the application war package"
	exit 1
fi
echo "check application war package success"

#check release file
RELEASE_FILE=$(find ${PKG_RLSE_HOME}/ -path \*/RELEASE|sort -n|head -n1)
if [ -n "${RELEASE_FILE}" ];then
	build=$(grep "Release version" ${RELEASE_FILE} |awk -F":" '{print $2}')
	if [[ ${BLD_NUM} -ne ${build} ]];then
		echo "release file build numbers(${build}) != package build number(${BLD_NUM})"
		exit 1
	fi
	echo "check release file build number success"
fi

#backup
PWAR=$(find ${TOMCAT_WEBAPPS}/ -path \*/${PROJECT}*.war|sort -n|head -n1)
#if [ -n "${PWAR}" ];then
#	PLINK=$(readlink ${PWAR})
#	if [ -n "${PLINK}" ];then
#		echo "backup pre-application war('${PLINK}')"
#	fi
#fi

_status
_shutdown
if [ "${WAR}" == "${PLINK}" ];then
	echo "application current version = previous version ,don't cover it"
else
	#backup
	if [ -e "${PWAR}" ];then
		mv ${PWAR} ${PWAR}.save
	fi
	war="${TOMCAT_WEBAPPS}/${PROJECT}.war"
	ln -sf "${WAR}" "${war}"
	if [ $? -ne 0 ];then
		echo "create soft link from '${war}' to '${WAR}' fail"
		#rollback
		_rollback_war
		_start
		exit 1
	fi
	echo "create soft link from '${war}' to '${WAR}' success"

	PACKAGE_CONFIG=$(find ${PKG_RLSE_HOME}/ -path \*/config|sort -n|head -n1)
	if [ -n "${PACKAGE_CONFIG}" ];then
		if [ ! -e "${PKG_CONF_HOME}/${PROJECT}" ] || [ "$(ls -A ${PKG_CONF_HOME}/${PROJECT})" == "" ];then
			mkdir -p "${PKG_CONF_HOME}/${PROJECT}"
			chmod 755 "${PKG_CONF_HOME}/${PROJECT}"
			cp -rf "${PACKAGE_CONFIG}/." "${PKG_CONF_HOME}/${PROJECT}"
			if [ $? -ne 0 ];then
				echo "copy configuration file from '${PACKAGE_CONFIG}' to '${PKG_CONF_HOME}/${PROJECT}' fail"
				#rollback
				rm -rf "${PKG_CONF_HOME}/${PROJECT}"
				_check_rollback $? "remove application package config('${PKG_CONF_HOME}/${PROJECT}')"
				_rollback_war
				_start
				exit 1
			fi
			if [ -e "${PACKAGE_CONFIG}/provisioner.json" ];then
				_persionlize_config "${PACKAGE_CONFIG}/provisioner.json"
				if [ $? -ne 0 ];then
					echo "copy configuration file from '${PACKAGE_CONFIG}' to '${PKG_CONF_HOME}/${PROJECT}' fail"
					rm -rf "${PKG_CONF_HOME}/${PROJECT}"
					_check_rollback $? "remove application package config('${PKG_CONF_HOME}/${PROJECT}')"
					_rollback_war
					_start
					exit 1
				fi
			fi
		fi
		if [ -z $(readlink ${PACKAGE_CONFIG}) ];then
			mv "${PACKAGE_CONFIG}" "${PACKAGE_CONFIG}.${TIME}.save"
			ln -sf "${PKG_CONF_HOME}/${PROJECT}" "${PACKAGE_CONFIG}"
		fi
		echo "initialize the application configuration file success"
	else
		echo "application config file is already exist,don't cover it"
	fi
	echo "check application config file success"
fi
rm -rf ${PWAR}.save
_start
_status
echo "application status(${APP_STATUS})，pid='${PID}'"
echo "deploy script finish action,you should check the result by yourself"

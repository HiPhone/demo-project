#!/usr/bin/env bash

function _pid(){
	PID=$(ps -ef|grep ".*${PROJECT}.*.jar"|grep -v 'grep'|grep -v 'deploy.sh'|head -n1|awk -F' ' '{print $2}')
}

function _status(){
	_pid
	if [ -n "${PID}" ];then
		APP_STATUS="RUN"
	else
		APP_STATUS="STOP"
	fi
}

function _rollback_jar(){
	if [ -e "${PKG_HOME}" ];then
		rm -rf ${PKG_HOME} ||
		echo "remove deploy's package fail"
	fi
	if [ -e "${PKG_HOME}.save" ];then
		mv ${PKG_HOME}.save ${PKG_HOME} ||
		( echo "rollback back jar fail,you should run command 'mv ${PKG_HOME}.save ${PKG_HOME}' by yourself" &&
		  exit 1 )
	fi

}

function _start(){
    #for batch project
    if [ ! -e "${SCRIPT}" ];then
		echo "can't found start-up script file('${SCRIPT}'),maybe you should start-up by yourself"
	    return 1
	fi

	#Non-interactive shell,the default does not load profile ,so point out to load environment variables
	source ~/.bash_profile
	#TODO
	source /etc/profile

	chmod u+x ${SCRIPT} ||
	( echo "add +x to start-up script file('${SCRIPT}')" &&
	  exit 1 )
	for ((i=0;i<3;i++))
	do
		for ((j=0;j<10;j++))
		do
			sleep 1
			_pid
			if [ -z "${PID}" ];then
				break
			fi
		done
		if [[ ${j} -ge 10 ]];then
			break
		fi
        nohup ${SCRIPT} start > /dev/null 2>&1 &
	done
	if [[ ${i} -ge 3 ]];then
		echo "startup application fail"
		return 1
	else
		echo "startup application success"
	fi
}

#TODO: add health check function if app is running
#function _checkHealth(){
#}

#clearn redundant package
function _clear_rdd_pkg(){
	local clear_path=${PKG_RLSE_HOME%\/*}
	for item in $(ls ${clear_path})
	do
		if [ "${item}" == "${BLD_NUM}" ];then
			continue
		fi
		if [ -n "${item}" ];then
			( rm -rf "${clear_path}/${item}" &&
			echo "clearn redudant package(${clear_path}/${item}) success" ) ||
			echo "clearn redudant package(${clear_path}/${item}) fail"
		fi
	done
}

function _shutdown(){
	if [[ "${APP_STATUS}" == "RUN" ]];then
		for ((i=0;i<3;i++))
		do
			sleep 1
			_pid
			if [ -n "${PID}" ];then
				kill -9 "${PID}" ||
				( echo "shutdown application fail,deploy package fail" &&
				  exit 1 )
			else
				break
			fi
		done
		if [[ ${i} -ge 3 ]];then
			echo "shutdown application fail,deploy package fail"
			exit 1
		else
			echo "shutdown application success"
		fi
	fi
}

#shell start
if [ $# -lt 3 ];then
	echo "
The input parameters are incorrect.
usage: deploy-jar.sh [PKG_RLSE_HOME] [PKG_CONF_HOME] [PKG_HOME]"
	exit 1
fi

PKG_RLSE_HOME=$1
PKG_CONF_HOME=$2
BLD_NUM=$(basename ${PKG_RLSE_HOME})
PROJECT=$(basename ${PKG_RLSE_HOME%/*})
PACKAGE=$(ls ${PKG_RLSE_HOME}/|grep ".tar.gz"|head -n1)
PKG_HOME="$3/standalone/${PROJECT}"
TIME=$(date +%Y%m%d)

if [ -z "${BLD_NUM}" ] || [ -z "${PROJECT}" ] || [ -z "${PACKAGE}" ];then
	echo "parameters error, deploy package fail"
	exit 1
fi

#check tar.gz
if [ $(ls ${PKG_RLSE_HOME}/|wc -w) -lt 2 ];then
	echo "extract the application package('${PACKAGE}')"
	( tar -xf "${PKG_RLSE_HOME}/${PACKAGE}" -C "${PKG_RLSE_HOME}/" &&
	echo "extract package success" ) ||
	( echo "extract package fail" &&
	exit 1 )
fi
PROJECT_HOME="${PKG_RLSE_HOME}/$(echo $(basename ${PACKAGE})|sed 's/.tar.gz//g')"
if [ ! -e "${PROJECT_HOME}" ];then
	PROJECT_HOME=${PKG_RLSE_HOME}
	if [ ! -e "${PROJECT_HOME}" ];then
		echo "can't found the package release home"
		exit 1
	fi
fi
echo "check application package extracted success"

#check jar
JAR=$(find ${PKG_RLSE_HOME}/ -path \*/*${PROJECT}*.jar|sort -n|head -n1)
if [ -z "${JAR}" ];then
	echo "can't found the application jar package"
	exit 1
fi
echo "check application jar package success"

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

#deploy
_status
_shutdown
if [ "${PROJECT_HOME}" == "${PLINK}" ];then
	echo "application current version = previous version ,don't cover it"
else
	#backup pre-softlink
	if [ -e "${PKG_HOME}" ];then
		mv ${PKG_HOME} ${PKG_HOME}.save ||
		( echo "backup package fail,deploy package fail" &&
		  exit 1 )
	fi
	ln -sf "${PROJECT_HOME}" "${PKG_HOME}"
	if [ $? -ne 0 ];then
		echo "create soft link from '${PKG_HOME}' to '${PROJECT_HOME}' fail,deploy package fail"
		#rollback
		_rollback_jar
		_start
		exit 1
	fi
	echo "create soft link from '${PKG_HOME}' to '${PROJECT_HOME}' success"

    #check startup file
	ALLSCRIPT=`find ${PKG_HOME}/bin/  -type f -name '*.sh'`
	for arg in $ALLSCRIPT
	do
        chmod u+x $arg
	done
    SCRIPT="${PKG_HOME}/bin/${PROJECT}.sh"
    if [ ! -e "${SCRIPT}" ];then
    	echo "can't found start-up script file(${SCRIPT}),do you sure this project is a batch project?"
    	#for batch project
    	#return 1
    fi
    echo "check application startup file success"

	PACKAGE_CONFIG=$(find ${PKG_RLSE_HOME}/ -path \*/config|sort -n|head -n1)
	if [ -n "${PACKAGE_CONFIG}" ];then
		if [ ! -e "${PKG_CONF_HOME}/${PROJECT}" ] || [ "$(ls -A ${PKG_CONF_HOME}/${PROJECT})" == "" ];then
			mkdir -p "${PKG_CONF_HOME}/${PROJECT}" ||
			( echo "init config's directory fail,deploy package fail" &&
			  _rollback_jar &&
			  _start &&
			  exit 1 )
			chmod 755 "${PKG_CONF_HOME}/${PROJECT}" ||
			( echo "change config-directory's mode fail,deploy package fail" &&
			  _rollback_jar &&
			  _start &&
			  exit 1 )
			cp -rf "${PACKAGE_CONFIG}/." "${PKG_CONF_HOME}/${PROJECT}"
			if [ $? -ne 0 ];then
				echo " copy configuration file from '${PACKAGE_CONFIG}' to '${PKG_CONF_HOME}/${PROJECT}' fail"
				rm -rf "${PKG_CONF_HOME}/${PROJECT}" ||
				echo "remove application package config('${PKG_CONF_HOME}/${PROJECT}') fail,deploy package fail"
				_rollback_jar
				_start
				exit 1
			fi
			#Destory:presion-config code
			#if [ -e "${PACKAGE_CONFIG}/provisioner.json" ];then
			#	_persionlize_config "${PACKAGE_CONFIG}/provisioner.json"
			#	if [ $? -ne 0 ];then
			#		echo " copy configuration file from '${PACKAGE_CONFIG}' to '${PKG_CONF_HOME}/${PROJECT}' fail"
			#		rm -rf "${PKG_CONF_HOME}/${PROJECT}"
			#		_check_rollback $? "remove application package config('${PKG_CONF_HOME}/${PROJECT}')"
			#		_rollback_jar
			#		_start
			#		exit 1
			#	fi
			#fi
		fi
		if [ -z $(readlink ${PACKAGE_CONFIG}) ];then
			( mv "${PACKAGE_CONFIG}" "${PACKAGE_CONFIG}.${TIME}.save" &&
			ln -sf "${PKG_CONF_HOME}/${PROJECT}" "${PACKAGE_CONFIG}" ) ||
			( echo "create soft link form '${PACKAGE_CONFIG}' to '${PKG_CONF_HOME}/${PROJECT}' fail,deploy package fail" &&
			  exit 1 )
		fi
		echo "initialize the application configuration file success"
	else
		echo "application config file is already exist,don't cover it"
	fi
	echo "check application config file success"
fi
_start
_status
_clear_rdd_pkg
#TODO
rm -rf ${PKG_HOME}.save
echo "application status(${APP_STATUS})，pid='${PID}'"
echo "deploy script finish action,you should check the result by yourself"
#!/usr/bin/env bash

. ${BIN}/function/common.sh

function _init_ext_conf_from_file(){
	_file_exist $1
	local oldifs=$IFS
	IFS=$'\n'
	for line in $(cat $1)
	do
		if [[ ${line} =~ "#" ]] || [[ -z "${line}" ]];then
			continue
		fi
		if [[ ! ${line} =~ "=" ]];then
			_log -e "bad format '${line}'"
			IFS=$oldifs
			return 1
		fi
		key=$(echo ${line}|awk -F"=" '{print $1}')
		value=$(echo ${line}|awk -F"=" '{print $2}')
		if [[ -z "${key}" ]] || [[ -z "${value}" ]];then
			_log -e "bad format '${line}'"
			IFS=$oldifs
			return 1
		fi
		if [ -z "${!key}" ];then
			line=$(echo ${line}|tr -s ["\n","\r"])
			export ${line}
		else
			_log -i "' ${key}=${!key} ' is already define,don't cover it"
		fi
	done
	IFS=$oldifs
}

function _init_pre_conf(){
	if [ -z "${DEF_CONF_FILE}" ];then
		DEF_CONF_FILE="${BIN}/../config/env"
		if [ -z "${ENV}" ];then
			export 'ENV=dev'
		fi
		export "DEF_CONF_FILE=${DEF_CONF_FILE}/deploy-${ENV}.cfg"
	fi
}

function _init_conf_from_file(){
	_init_pre_conf
	#load config from file if CONF_FILE specified
	if [ -n "${CONF_FILE}" ] && [ -e "${CONF_FILE}" ];then
		( _log -i "Loading environment value from specified file(${CONF_FILE})" &&
		_init_ext_conf_from_file ${CONF_FILE} ) ||
		( echo "load environment value fail" &&
		  return 1 )
	fi
	#load config from default file
	if [ "${CONF_FILE}" != "${DEF_CONF_FILE}" ] && [ -e "${DEF_CONF_FILE}" ];then
		( _log -i "Loading environment value from default file(${DEF_CONF_FILE})" &&
		_init_ext_conf_from_file ${DEF_CONF_FILE} ) ||
		( echo "load environment value fail" &&
		  return 1 )
	fi
	_log -i "init config form file success"
}
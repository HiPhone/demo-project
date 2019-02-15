#!/usr/bin/env bash

if [ -z "${HANDLER}" ];then
	export HANDLER=$(date +%s)
fi

function _file_exist(){
	if [ -z "${1}" ] || [ ! -e ${1} ];then
		_log -e "can't found the file('${1}')"
		return 1
	fi
}

function _log(){
	if [ -n "${LOG_HOME}" ] && [ ! -e ${LOG_HOME} ];then
		mkdir -p ${LOG_HOME} ||
		( echo "can't init log home('${LOG_HOME}')" &&
		  return 1 )
	fi
	local date=$(date "+%Y-%m-%d %H:%M:%S")
	local name=$(whoami)
	local level=''
	case ${1} in
		-i)
		level='INFO'
		;;
		-e)
		level='ERROR'
		;;
		-w)
		level='WARN'
		;;
		-d)
		level='DEBUG'
		;;
		*)
		level='UNDEFINE'
		;;
	esac
	local msg=${2}
	if [[ "${3}" == "filter" ]] && [[ ! "${msg}" =~ "UNREACHABLE!" ]] && [[ ! "${msg}" =~ "FAILED!" ]];then
		#filter ansible un-useable msg,NOTE:msg should save to file,then filter
		echo -e "${msg}" > ${BIN}/${HANDLER}
		msg=$(cat ${BIN}/${HANDLER}|sed '/"changed":/,/"stdout_lines":/d'|sed 's/]//g')
		rm -rf ${BIN}/${HANDLER}
	fi
	echo -e "[$date $name] ${level} : ${msg}"
	if [ -n "${LOG_NAME}" ];then
		echo -e "[${HANDLER}][$date $name] ${level} : ${msg}" >> ${LOG_HOME}/${LOG_NAME} ||
		echo -e "[$date $name] ERROR : can't write log to file('${LOG_HOME}/${LOG_NAME}')"
	fi
}
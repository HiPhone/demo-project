#!/usr/bin/env bash

#Exit immediately if a command exits with a non-zero exit status.
set -o errexit

#for debug
#set -x

function _usage(){
	echo "
Usage: authorized.sh <cluster_name> [cluster_ssh_auth_file] [-e {extended_parameters}] ...
To achieve ssh free key authentication.

Options:
	cluster_auth_file
		file name that keep cluster ssh auth information,
		if not specified,the default use of \${PROVISIONER_HOME}/shell/config/auth/\${CLUSTER_NAME}.auth
	-e {extended_parameters}
		add extended parameter,requested format:'key=value',parameters are separated by space(' ')
"
}

#function define
function _init_def_conf(){
	export AUTH_FILE=${AUTH_FILE:=${BIN}/../config/auth/${CLUSTER_NAME}.auth}
	export INV_NAME=${INV_NAME:=${CLUSTER_NAME}}
	export INV_HOME=${INV_HOME:=/etc/ansible/inventory}
	export LOG_NAME=${LOG_NAME:=${CLUSTER_NAME}-auth-$(date +%Y%m%d).log}
	export LOG_HOME=${LOG_HOME:=/logs/auto-publisher}
	export ENV=${ENV:=dev}

	local msg="
Environment value(

	ENV='${ENV}',
	CLUSTER_NAME='${CLUSTER_NAME}',
	AUTH_FILE='${AUTH_FILE}',
	INV_HOME='${INV_HOME}',
	INV_NAME='${INV_NAME}',
	LOG_HOME='${LOG_HOME}',
	LOG_NAME='${LOG_NAME}'

)"
	_log -i "${msg}"
	_log -i "init default config success"
}

function _parse_auth_file(){
	_file_exist ${AUTH_FILE}
    if [[ $(sed 's/\,/ /g' ${AUTH_FILE} | wc -w) -lt 3 ]];then
        _log -e "bad format of the file(${AUTH_FILE}),parse ssh auth file failed"
        exit 2
    fi

    local index=0
    local oldifs=${IFS}
    IFS=$'\n'
    for line in $(cat ${AUTH_FILE})
    do
        IPS[${index}]=$(echo ${line}|awk -F"," '{print $1}')
        USERS[${index}]=$(echo ${line}|awk -F"," '{print $2}')
        PAWS[${index}]=$(echo ${line}|awk -F"," '{print $3}')
        index=$(expr ${index} + 1)
    done
    IFS=${oldifs}
    _log -i "parse ssh auth file success"
}

function _create_auth_key(){
	if [ ! -e "${HOME}/.ssh/id_rsa" ];then
		_log -i "create auth key (${HOME}/.ssh/id_rsa)"
		ssh-keygen -t rsa -P '' -f "${HOME}/.ssh/id_rsa"
	else
		_log -i "auth key(${HOME}/.ssh/id_rsa) is already exists"
	fi
	_log -i "create auth key success"
}

function _create_temp_auth_file(){
	mkdir -p /etc/ansible/.tmp
	local content=""
	local index=0
	for IP in ${IPS[@]}
	do
		if [ -z "${IP}" ];then
			break
		fi
		content="${content}${IP}\n${IP} ansible_ssh_user=${USERS[$index]}\n${IP} ansible_ssh_pass=${PAWS[$index]}\n"
		index=$(expr $index + 1)
	done
	echo -e ${content} > "/etc/ansible/.tmp/${CLUSTER_NAME}"
	_log -i "create temp auth file success"
}

function _create_project_auth_file(){
	if [[ -n "${1}" ]] && [[ ! -e $(dirname ${1}) ]];then
		mkdir -p $(dirname ${1})
		_log -i "create the auth file path '$(dirname ${1})'"
	fi
	local content="[${CLUSTER_NAME}]\n"
	local index=0
       	for IP in ${IPS[@]}
       	do
               	if [ -z "${IP}" ];then
                       	break
               	fi
		content="${content}${IP} ansible_ssh_user=${USERS[$index]}\n"
		index=$(expr $index + 1)
       	done
	echo -e ${content} > $1
	_log -i "create auth file '${CLUSTER_NAME}' success"

}

function _authorized(){
	_file_exist "/etc/ansible/.tmp/${CLUSTER_NAME}"
	for user in ${USERS[@]}
	do
		local feedback=$(ansible ${IPS[$index]} -m authorized_key -a "user=${user} key='{{ lookup('file','${HOME}/.ssh/id_rsa.pub')}}' manage_dir=yes" -i "/etc/ansible/.tmp/${CLUSTER_NAME}")
		_log -i "${feedback}"
		if [[ -z ${feedback} ]] || [[ ${feedback} =~ "UNREACHABLE" ]];then
			_log -e "${IPS[$index]},${user} authorized failed,please check it"
			exit 3
		fi
		index=$(expr $index + 1)
	done
	rm -rf "/etc/ansible/.tmp/${CLUSTER_NAME}"
	_create_project_auth_file "${INV_HOME}/.${ENV}/${INV_NAME}"
	_log -i "${feedback}"
	_test
	_log -i "auth finish."
}

function _test(){
	_file_exist "${INV_HOME}/.${ENV}/${INV_NAME}"
	local feedback=$(ansible ${CLUSTER_NAME} -m ping -i ${INV_HOME}/.${ENV}/${INV_NAME})
	_log -i "Authorized test:${feedback}"
	if [[ -z ${feedback} ]] || [[ ${feedback} =~ "UNREACHABLE" ]];then
		_log -i "Authorized result: fail"
		_log -i "Please manually clean up plaintest key file(${AUTH_FILE})"
	else
		_log -i "Authorized result: success"
		rm -rf ${AUTH_FILE}
		_log -i "Auto clean up plaintest key file(${AUTH_FILE})"
	fi
}

# shell start
BIN=$(cd `dirname $0`;pwd)
. ${BIN}/function/common.sh
. ${BIN}/function/env.sh
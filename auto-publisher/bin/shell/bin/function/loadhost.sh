#!/usr/bin/env bash

#Exit immediately if a command exits with a non-zero exit status.
set -o errexit

function _usage(){
        echo "
Usage: loadhost.sh <project-name> [-e {extended_parameters}]
Options:
	-e {extended_parameters}
		add extended parameter,requested format:'key=value',parameters are separated by space(' ')
"
}

function _load_auth_host_list() {
	AUTH_FILE="${INV_HOME}/.${ENV}/${PROJECTNAME}"
	_file_exist ${AUTH_FILE}
	host_list=`ansible all --list-hosts -i ${AUTH_FILE} | grep -v "hosts" | awk -F' ' '{print $1}'`
	if [ $? -eq 0 ];then
		_log -i "load host list from auth file: ${AUTH_FILE}"
		_log -i "$host_list"
	else
		_log -e "load host list from auth file failed."
	fi
}

# shell start
# fit for 'BIN'
BIN=$(dirname $(cd `dirname $0`;pwd))
. ${BIN}/function/common.sh
. ${BIN}/function/env.sh

if [ $# -lt 1 ];then
        echo "The number of input parameters does not meet the requirments"
        _usage
        exit 1
fi

PROJECTNAME=$1
INV_HOME=''
ENV=''

_parse_ext_conf ${@:2}

export INV_HOME=${INV_HOME:=/etc/ansible/inventory}
export ENV=${ENV:=dev}

_load_auth_host_list

#!/usr/bin/env bash

#Exit immediately if a command exits with a non-zero exit status.
set -o errexit
#for debug
#set -x

function _usage(){
	echo "
Usage：deploy.sh [product] [project] [bld_num] [pkg_type] [depl_model] [EXTEND_OPTION] [extend_item]...
Deploy a deployable package to a remote host cluster.
Example: deploy.sh mkt cbs-config 23 jar

	[product]
		application product name
	[project]
		application project name
	[bld_num]
		application build number
	[pkg_type]
		application package type, jar|war
	[EXTEND_OPTION]
		-e
			add extension values.
	[extend_item]
		extension item,format 'key=value'.
"
}

#function define
function _init_def_conf(){

	export ENV=${ENV:=dev}
	if [ "${ENV}" == "production" ];then
		export CD_URL=${CD_URL:=http://xxxxxxx/pub/publish}
	elif [ "${ENV}" == "dev" ];then
		export CD_URL=${CD_URL:=http://xxxxxxx/pub/publish}
	elif [ "${ENV}" == "test" ];then
		export CD_URL=${CD_URL:=http://xxxxxxx/pub/publish}
	fi
	export PKG_HOME=${PKG_HOME:=/data}
	export PKG_CONF_HOME=${PKG_CONF_HOME:=${PKG_HOME}/config}
	PKG_RLSE_HOME=${PKG_RLSE_HOME:=${PKG_HOME}/release}
	export PKG_RLSE_HOME=${PKG_RLSE_HOME}/${PRODUCT}/${PROJECT}/${BLD_NUM}
	export INV_NAME=${INV_NAME:=${PROJECT}}
	export INV_HOME=${INV_HOME:=/etc/ansible/inventory}
	export LOG_NAME=${LOG_NAME:=${PROJECT}-deploy-$(date +%Y%m%d).log}
	export LOG_HOME=${LOG_HOME:=/logs/auto-publisher}

	local msg="
Environment value(

	ENV='${ENV}',
	CD_URL='${CD_URL}',
	CONF_FILE='${CONF_FILE}',
	DEF_CONF_FILE='${DEF_CONF_FILE}',
	PKG_HOME='${PKG_HOME}',
	PKG_RLSE_HOME='${PKG_RLSE_HOME}',
	PRI_TOKEN='${PRI_TOKEN}',
	HANDLER='${HANDLER}',
	LOG_HOME='${LOG_HOME}',
	LOG_NAME='${LOG_NAME}',
	INV_HOME='${INV_HOME}',
	INV_NAME='${INV_NAME}'

)"
	_log -i "${msg}"
	_log -i "init default config success"
}

function _init_app_info(){
	PRODUCT=$1
	PROJECT=$2
	BLD_NUM=$3
	PKG_TYPE=$4
	DEPL_MODEL=$5

	local msg="
application information(

	PRODUCT='${PRODUCT}',
	PROJECT='${PROJECT}',
	BLD_NUM='${BLD_NUM}',
	PKG_TYPE='${PKG_TYPE}'

)"
	_log -i "${msg}"
	_log -i "init application information success"
}

function _download(){
	_file_exist ${INV_HOME}/.${ENV}/${INV_NAME}
	local url="${CD_URL}/${PRODUCT}"
	if [ -n "${PRI_TOKEN}" ];then
		url="${url}/tree/master/${PROJECT}/bld${BLD_NUM}?private_token=${PRI_TOKEN}"
	else
		url="${url}/${PROJECT}/bld${BLD_NUM}"
	fi
	local tmp="/usr/tmp/${HANDLER}"
	wget -k -nv -P "${tmp}/" ${url}
	local file="${tmp}/$(ls ${tmp}|head -n1)"
	# get the application package's url
	grep -o '<a.*tar.gz.*>.*</a>' ${file}|grep -o '".*"'|sed 's/"//g'|
	while read href
	do
        if [ -n ${href} ];then
			local pkg=$(basename ${href})
			if [ -n "${PRI_TOKEN}" ];then
				href=$(echo "${href}"|sed 's/blob/raw/g')
				href="${href}?private_token=${PRI_TOKEN}"
			fi
			_log -i "cluster('${PROJECT}') download package('${pkg}') into the release home from URL('${href}')"
			local feedback=$(ansible ${PROJECT} -m script -a "${BIN}/remote/download.sh "${href}" "${PKG_RLSE_HOME}"" -i ${INV_HOME}/.${ENV}/${INV_NAME})
			rm -rf "${tmp}"
			_log -i "${feedback}" "filter"
			if [[ "${feedback}" =~ "package download fail" ]];then
				_log -i "download application package fail"
				exit 1
			else
				_log -i "download application package success"
			fi
		fi
	done
}

function _deploy(){
	local deploy_shell="${BIN}/remote/deploy/deploy-${PKG_TYPE}.sh"
	_file_exist ${deploy_shell}
	local feedback=$(ansible ${PROJECT} -m script -a "${deploy_shell} "${PKG_RLSE_HOME}" "${PKG_CONF_HOME}" "${PKG_HOME}"" -i ${INV_HOME}/.${ENV}/${INV_NAME})
	_log -i "${feedback}" "filter"
	if [[ "${feedback}" =~ "deploy package fail" ]];then
		_log -i "deploy application package fail"
		exit 1
	else
		_log -i "deploy application package success"
	fi
}

#shell start
BIN=$(cd `dirname $0`;pwd)
. ${BIN}/function/common.sh
. ${BIN}/function/env.sh

#remote config
CONF_FILE=''
PKG_HOME=''
PKG_RLSE_HOME=''
PKG_CONF_HOME=''

if [ $# -lt 4 ];then
	echo "The number of input parameters does not meet the requirments"
	_usage
	exit 1
fi
_parse_ext_conf ${@:5}
echo "parse extended parameters success"
_init_conf_from_file
_init_app_info $@
_init_def_conf
_download
_deploy
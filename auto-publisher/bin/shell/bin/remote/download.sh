#!/usr/bin/env bash

if [ $# -lt 2 ];then
	echo "The input parameters are incorrect.
Usage: download.sh [pkg_url] [pkg_rlse_home]"
	exit 1
fi
PKG_URL=$1
PKG_RLSE_HOME=$2

if [ ! -e ${PKG_RLSE_HOME} ];then
	( mkdir -p ${PKG_RLSE_HOME} &&
	chmod 755 ${PKG_RLSE_HOME} ) ||
	( echo "package download fail" &&
	exit 1 )
fi

if [ "$(ls -A ${PKG_RLSE_HOME})" == "" ];then
	PKG=$(basename ${PKG_URL})
	wget -c -nv -P "${PKG_RLSE_HOME}/" "${PKG_URL}"
	if [ $? -eq 0 ];then
	    if [[ "${PKG}" =~ "?" ]];then
	    	( mv "${PKG_RLSE_HOME}/${PKG}" "${PKG_RLSE_HOME}/${PKG%\?*}" &&
			echo "package download success" &&
			exit 0 ) ||
			( echo "package download fail" &&
			exit 1 )
	    fi
		echo "package download success"
	else
		echo "package download fail"
	fi
else
	echo "package download success"
fi

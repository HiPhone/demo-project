#!/usr/bin/env bash
# 用于在linux环境下启动java spring boot服务的脚本，本启动脚本的命名必须与jar包命名一致才起作用
set -e
# 获取目录
bin=`dirname "$0"`
APP_HOME=`cd "$bin"/..; pwd`
PKG_NAME=`basename "$0"`
# 配置jvm参数，开启远程jvm调试
JAVA_OPTS="${JAVA_OPTS} -Xmx2048m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

function print_usage(){
        echo " "
        echo " Usage:$PKG_NAME [parameters]"
        echo " Requried parameter is one of:"
        echo "   start      start $PKG_NAME"
        echo "   stop       stop  $PKG_NAME"
        echo "   status     show status of $PKG_NAME"
        echo "   "
        echo " Optional parameters"
        echo "   -d     for debug usage,which load executable jar from target folder"
        echo ""
}
if [ $# = 0 ];then
        print_usage
        exit
fi
# 获取相对路径下的文件名
function get_mainjar()
{
	CONF_PATH="$APP_HOME"/config/
	for jar in "$APP_HOME"/*.jar
	do
		PRG_NAME=${jar}
	done
}
# 获取相对路径下的文件名
function get_mainjar_debug()
{
	CONF_PATH="$APP_HOME"/src/main/resources/config/
	for jar in "$APP_HOME"/target/*.jar
	do
		PRG_NAME=${jar}
	done
}

function load_classpath()
{
        for jar in "$APP_HOME"/lib/*.jar
                do CLASSPATH=${CLASSPATH}:${jar}
        done
        for jar in "$APP_HOME"/target/*.jar
                do CLASSPATH=${CLASSPATH}:${jar}
        done
        for jar in "$APP_HOME"/target/lib/*.jar
                do CLASSPATH=${CLASSPATH}:${jar}
        done
}
function load_java()
{
	if [[ ${DEBUG} == 'false' ]]; then
		get_mainjar
	else
		get_mainjar_debug
	fi
	# 获取java命令的绝对路径
	if [ "$JAVA_HOME" != "" ]; then
		JAVA_HOME=$JAVA_HOME
	else
		JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")
	fi
	if [ "$JAVA_HOME"  = "" ]; then
		echo "Error: JAVA_HOME is not set."
		exit 1
	fi

	load_classpath
	# 输出各种参数，以便检查正确性
	echo \$LOG_HOME=$LOG_HOME
	echo \$JAVA_OPTS="${JAVA_OPTS}"
	echo \$DEBUG=$DEBUG
	echo \$PRG_NAME=$PRG_NAME
	echo \$JAVA_HOME=$JAVA_HOME
	echo \$CLASS_PATH=$CLASSPATH
	echo \$spring.config.location=$CONF_PATH
	echo \$logging.config=$CONF_PATH/log4j2.xml
}
function start(){
        load_java
        RUNNING=`ps -ef|grep $PRG_NAME|grep -v grep|awk '{print $2}'`
        if [ -n "$RUNNING" ]; then
                echo "$PRG_NAME is running! $RUNNING"
        else

                echo "App is running in virtual machine as backend mode"
                echo "nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $PRG_NAME --spring.config.location=$CONF_PATH  --logging.config=$CONF_PATH/log4j2.xml & ..... "
		exec nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $PRG_NAME   --spring.config.location=$CONF_PATH   --logging.config=$CONF_PATH/log4j2.xml  >/dev/null 2>&1 &
		sleep 2s
                if [ $? -eq 0 ]; then
                        echo "$PRG_NAME start success "
                else
                        echo "$PRG_NAME start failed "
                        exit 1
                fi
        fi
}
function status(){
        load_java
        processid=`pgrep -f "$PRG_NAME"`
        if [ $processid ];then
                echo "$PRG_NAME is running as process $processid"
        else
                echo "$PRG_NAME is not running. "
        fi
}
function stop(){
        load_java
        echo " stopping $PRG_NAME..."
        pkill -f "$PRG_NAME"
        echo "$PRG_NAME is stopped!"
}
ARGS=`getopt -o d -lserver.port: -- "$@"`
if [ $? != 0 ];then
	print_usage
	exit 1;
fi
eval set -- "$ARGS"
DEBUG='false'
while true; do
     case "$1" in
        -d) DEBUG='true'; shift;;
        --server.port)
		SERVER_PORT="$2";
		shift 2;;
        --) shift;;
        start) start; break;;
        status) status; break;;
        stop) stop; break;;
        *) echo "Error: unexpected option $1...";
            print_usage; exit 1;;
 esac
done
exit $?;
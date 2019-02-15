package org.hiphone.auto.publisher.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author HiPhone
 */
public class ShellExecutor {

    /**
     * 无参数的shell脚本运行方法
     * @param command shell脚本命令
     * @return shell脚本运行结果
     * @throws Exception shell脚本运行其中发生的异常
     */
    public static String run(String command) throws Exception {
        return run(command, null);
    }

    /**
     * 有参数的shell脚本运行方法
     * @param command shell脚本命令
     * @param envs 脚本参数
     * @return hell脚本运行结果
     * @throws Exception shell脚本运行其中发生的异常
     */
    public static String run(String command, String[] envs) throws Exception {
        Process process = Runtime.getRuntime().exec(command, envs);
        process.waitFor();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line).append("\n");
        }
        return stringBuffer.toString();
    }

}

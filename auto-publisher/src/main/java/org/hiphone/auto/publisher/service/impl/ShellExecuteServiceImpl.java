package org.hiphone.auto.publisher.service.impl;

import com.google.common.io.Files;
import org.hiphone.auto.publisher.constants.Constant;
import org.hiphone.auto.publisher.entitys.BaseParam;
import org.hiphone.auto.publisher.exception.BusinessException;
import org.hiphone.auto.publisher.exception.ReturnMsg;
import org.hiphone.auto.publisher.service.ShellExcuteService;
import org.hiphone.auto.publisher.utils.AssertUtils;
import org.hiphone.auto.publisher.utils.ShellExecutor;
import org.hiphone.auto.publisher.utils.ShellJointer;

import java.io.File;
import java.util.Map;

/**
 * @author HiPhone
 */
public class ShellExecuteServiceImpl implements ShellExcuteService {

    @Override
    public String runConfigIpsShell(BaseParam param) throws Exception {
        String result = ShellExecutor.run(ShellJointer.joinShellCommand(param));
        return result.substring(result.lastIndexOf(":" + 1));
    }

    @Override
    public String runConfigAuthShell(BaseParam param) throws Exception {
        createAuthFile(param);
        return ShellExecutor.run(ShellJointer.joinShellCommand(param));
    }

    /**
     * 创建认证信息文件
     * @param param 参数
     * @throws Exception 抛出的异常信息
     */
    private void createAuthFile(BaseParam param) throws Exception {
        Map<String, String> map = param.getRequiredParams();
        String hosts = map.get("host").replace("\\r\\n", "\\n");
        String project = map.get("project");

        AssertUtils.allNotNull(ReturnMsg.SSH_AUTH_ERROR, hosts, project);

        //检查host格式
        String[] rows = hosts.split("\\n");
        for (String row : rows) {
            String[] columns = row.split(",");
            if (columns.length != 3) {
                throw new BusinessException(ReturnMsg.HOST_INFO_ERROR);
            }
        }

        String fileName = Constant.AUTH_CONFIG + project + ".auth";
        Files.write(hosts.getBytes(), new File(fileName));
        param.getRequiredKey().remove(Constant.AUTH_HOSTS);
    }

    @Override
    public String runDeployShell(BaseParam param) throws Exception {
        return ShellExecutor.run(ShellJointer.joinShellCommand(param));
    }

}

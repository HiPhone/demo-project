package org.hiphone.auto.publisher.service.impl;

import org.hiphone.auto.publisher.entitys.BaseParam;
import org.hiphone.auto.publisher.service.IpsService;
import org.hiphone.auto.publisher.utils.ShellExecutor;
import org.hiphone.auto.publisher.utils.ShellJointer;
import org.springframework.stereotype.Service;

/**
 * @author HiPhone
 */
@Service
public class IpsServiceImpl implements IpsService {

    @Override
    public String configIps(BaseParam param) throws Exception {
        return ShellExecutor.run(ShellJointer.joinShellCommand(param));
    }
}

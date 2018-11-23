package org.hiphone.eureka.pressure.test.service.impl;

import org.hiphone.eureka.pressure.test.constants.ReturnCode;
import org.hiphone.eureka.pressure.test.entitys.ResultMessage;
import org.hiphone.eureka.pressure.test.service.GetAppPressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HiPhone
 */
@Service
public class GetAppPressServiceImpl implements GetAppPressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetAppPressServiceImpl.class);

    @Value("${eureka.pressure.address}")
    private String eurekaServerListString;

    @Autowired
    private RestTemplate restTemplate;

    private String[] eurekaServerList;
    private AtomicInteger count = new AtomicInteger(0);
    private AtomicBoolean started = new AtomicBoolean(false);

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 16);

    @PostConstruct
    private void init() {
        eurekaServerList = eurekaServerListString.split(",");
    }

    @Override
    public ResultMessage getApplicationPress(boolean randomRegister, int intervalMillis, int totalCount) {
        ResultMessage resultMessage = new ResultMessage(
                ReturnCode.SUCCESS.getCode(),
                ReturnCode.SUCCESS.getMessage(),
                null
        );

        if (!started.compareAndSet(false, true)) {
            resultMessage.setData("GetApplication pressure already start! please wait for the operation complete!");
        } else {
            this.count.set(0);
            Random random = new Random();

            List<Future<Object>> resultList = new ArrayList<>();
            for (int index = 0; index < totalCount; index++) {
                if (intervalMillis != 0) {
                    try {
                        Thread.sleep(intervalMillis);
                    } catch (InterruptedException e) {
                        LOGGER.error("wait intervalMillis get interrupted......");
                        e.printStackTrace();
                    }
                }

                final int registerServerNum = randomRegister ? random.nextInt(eurekaServerList.length) : 0;

                Future<Object> submit = executorService.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        restTemplate.getForEntity(eurekaServerList[registerServerNum] + "/apps", String.class);
                        LOGGER.info("Get application pressure count: {}", count.getAndIncrement());
                        return null;
                    }
                });
                resultList.add(submit);
            }

            for (Future<Object> future: resultList) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    LOGGER.error("Future task get interrupted!");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    LOGGER.error("Future task get Execution error!");
                    e.printStackTrace();
                }
            }
            started.set(false);
            resultMessage.setData("get application  pressure test started!");
        }
        return resultMessage;
    }


}

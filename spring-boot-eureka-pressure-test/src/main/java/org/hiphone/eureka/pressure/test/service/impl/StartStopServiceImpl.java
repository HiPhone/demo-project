package org.hiphone.eureka.pressure.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.hiphone.eureka.pressure.test.constants.ReturnCode;
import org.hiphone.eureka.pressure.test.entitys.ResultMessage;
import org.hiphone.eureka.pressure.test.service.StartStopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class StartStopServiceImpl implements StartStopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartStopServiceImpl.class);

    @Value("${eureka.pressure.address}")
    private String eurekaServerListString;

    @Autowired
    private RestTemplate restTemplate;

    private String[] eurekaServerList;

    private List<List<String[]>> instanceRegisterServers;

    @PostConstruct
    private void initPressureMsg() {
        eurekaServerList = eurekaServerListString.split(",");
        instanceRegisterServers = new ArrayList<>(eurekaServerList.length);
        for (int index = 0; index < eurekaServerList.length; index++) {
            instanceRegisterServers.add(new ArrayList<String[]>());
        }
    }

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    private AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public ResultMessage startPressureTest(int appCount, int instancePerApp, int heartBeatInterval, boolean randomRegister, boolean fluentRegister) {
        ResultMessage resultMessage = new ResultMessage(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), null);

        if (!started.compareAndSet(false, true)) {
            resultMessage.setData("压力测试已经启动，请先停止再启动");
        } else {
            Random random = new Random();
            Integer instanceStartInterval = null;
            if(fluentRegister){
                instanceStartInterval = 60000 / (instancePerApp * appCount);
            }
            //使用多线程并行发送注册请求
            for (int appId = 0; appId < appCount; appId++) {
                final String applicationName = "EUREKA-APP-" + appId;
                for (int instanceId = 0; instanceId < instancePerApp; instanceId++) {

                    final  int eurekaServerNum = randomRegister? random.nextInt(instanceRegisterServers.size()) : 0;
                    List<String[]> list = instanceRegisterServers.get(eurekaServerNum);
                    final String instanceName = applicationName + "-" + instanceId;
                    list.add(new String[]{applicationName, instanceName});

                    //开始注册
                    Callable<Object> startRegister = new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            //发送注册请求
                            register(applicationName, instanceName, eurekaServerList[eurekaServerNum]);
                            LOGGER.info("register instance: {}", instanceName);

                            //发送心跳包
                            Runnable run = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        sendHeartBeat(applicationName, instanceName, eurekaServerList[eurekaServerNum]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    LOGGER.info("heartbeat sent, instance name is {}", instanceName);
                                }
                            };
                            executorService.scheduleWithFixedDelay(run, heartBeatInterval, heartBeatInterval, TimeUnit.SECONDS);
                            return null;
                        }
                    };

                    if (fluentRegister) {
                        try {
                            Thread.sleep(instanceStartInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    executorService.submit(startRegister);
                }
            }
            resultMessage.setData("压力测试配置完成，已开始");
        }

        return resultMessage;
    }

    @Override
    public ResultMessage stopPressureTest() {
        executorService.shutdown();
        executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        //删除注册的instance
        for (int i = 0; i < instanceRegisterServers.size(); i++) {
            String eurekaServer = eurekaServerList[i];
            List<String[]> instanceList = instanceRegisterServers.get(i);
            for (int j = 0; j < instanceList.size(); j++) {
                String[] strings = instanceList.get(j);

                HttpHeaders headers = new HttpHeaders();
                headers.add("DiscoveryIdentity-Name", "DefaultClient");
                headers.add("DiscoveryIdentity-Version", "1.4");
                headers.add("Content-Length", "0");
                headers.add("x-netflix-discovery-replication", "false");
                HttpEntity entity = new HttpEntity(headers);

                String url = eurekaServer + "apps/" + strings[0] + "/" + strings[1];
                restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            }
        }

        started.set(false);
        return new ResultMessage(
                ReturnCode.SUCCESS.getCode(),
                ReturnCode.SUCCESS.getMessage(),
                "停止压力测试成功"
        );
    }

    private void sendHeartBeat(String applicationName, String instanceName, String eurekaServer) {

        URI eurekaUri = URI.create(eurekaServer);
        String requestUrl = new StringBuffer()
                .append(eurekaServer)
                .append("apps/")
                .append(applicationName)
                .append("/")
                .append(instanceName)
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("DiscoveryIdentity-Name", "DefaultClient");
        headers.add("DiscoveryIdentity-Version", "1.4");
        headers.add("DiscoveryIdentity-Id", eurekaUri.getHost());
        headers.add("Content-Length", "0");
        headers.add("Host", eurekaUri.getHost() + ":" + eurekaUri.getPort());
        headers.add("x-netflix-discovery-replication", "false");

        Map<String,Object> body = new HashMap<>();
        body.put("STATUS", "UP");
        body.put("lastDirtyTimestamp",System.currentTimeMillis());

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(requestUrl, HttpMethod.PUT, entity, String.class, body);
        LOGGER.info("finish send a heartbeat to instance {}, response is {}", instanceName, exchange);
    }

    /**
     * 向eureka注册的操作
     * @param applicationName  注册应用的名称
     * @param instanceName  注册实例的名称
     * @param eurekaServer eureka的地址
     */
    private void register(String applicationName, String instanceName, String eurekaServer) {
        URI eurekaUri = URI.create(eurekaServer);

        String requstUrl = new StringBuffer()
                .append(eurekaServer)
                .append("apps/")
                .append(applicationName)
                .toString();

        //构造http头信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("x-netflix-discovery-replication", "false");
        headers.add("DiscoveryIdentity-Name", "DefaultClient");
        headers.add("DiscoveryIdentity-Version", "1.4");
        headers.add("DiscoveryIdentity-Id", eurekaUri.getHost());
        headers.add("Host", eurekaUri.getHost() + ":" + eurekaUri.getPort());

        JSONObject requestBody = this.constructRegisterBody(applicationName, instanceName);

        HttpEntity entity = new HttpEntity(requestBody, headers);

        ResponseEntity<String> exchange = restTemplate.exchange(requstUrl, HttpMethod.POST, entity, String.class);

        LOGGER.info("finish a pressure request, response is {}", exchange);
    }

    /**
     * 构建压力测试请求的请求体
     * @param applicationName 应用的名称
     * @param instanceName 应用实例的名称
     * @return 构建完成的请求体
     */
    private JSONObject constructRegisterBody(String applicationName, String instanceName) {
        JSONObject instance = new JSONObject();
        JSONObject instanceBody = new JSONObject();

        instanceBody.put("instanceId", instanceName);
        instanceBody.put("hostName", "Eureka-Pressure-Tester");
        instanceBody.put("app", applicationName);
        instanceBody.put("ipAddr", "localhost");
        instanceBody.put("status", "UP");
        instanceBody.put("overriddenstatus", "UNKNOWN");
        instanceBody.put("countryId", 1);
        instanceBody.put("homePageUrl", "http://localhost:8081/");
        instanceBody.put("statusPageUrl", "http://localhost:8081/info");
        instanceBody.put("healthCheckUrl", "http://localhost:8081/health");
        instanceBody.put("vipAddress", "JZY-REG");
        instanceBody.put("secureVipAddress", "JZY-REG");
        instanceBody.put("isCoordinatingDiscoveryServer", "false");
        instanceBody.put("lastUpdatedTimestamp", System.currentTimeMillis());
        instanceBody.put("lastDirtyTimestamp", System.currentTimeMillis());

        JSONObject portMsg = new JSONObject();
        portMsg.put("$", 8080);
        portMsg.put("@enabled", "true");
        instanceBody.put("port", portMsg);

        JSONObject securePortMsg = new JSONObject();
        securePortMsg.put("$", 443);
        securePortMsg.put("@enable", "false");
        instanceBody.put("securePort", securePortMsg);

        JSONObject dataCenterMsg = new JSONObject();
        dataCenterMsg.put("@class", "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo");
        dataCenterMsg.put("name", "MyOwn");
        instanceBody.put("dataCenterInfo", dataCenterMsg);

        JSONObject leastInfoMsg = new JSONObject();
        leastInfoMsg.put("renewalIntervalInSecs",30);
        leastInfoMsg.put("durationInSecs",90);
        leastInfoMsg.put("registrationTimestamp",0);
        leastInfoMsg.put("lastRenewalTimestamp",0);
        leastInfoMsg.put("evictionTimestamp",0);
        leastInfoMsg.put("serviceUpTimestamp",0);
        instanceBody.put("leaseInfo", leastInfoMsg);

        JSONObject metaMsg = new JSONObject();
        metaMsg.put("@class", "java.util.Collections$EmptyMap");
        instanceBody.put("metadata", metaMsg);

        instance.put("instance", instanceBody);

        return instance;
    }
}

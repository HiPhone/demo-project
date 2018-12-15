package org.hiphone.swagger.center.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自动扫描eureka使用的方法类
 * @author HiPhone
 */
@Slf4j
public class AutoScanHelper {

    @Value("${eureka.client.service-url.defaultZone}")
    private String defaultZone;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 到eureka中获取注册服务的信息
     * @return 注册服务信息的list
     */
    public List<String> getAppNameFromEureka() {
        String[] eurekaUrls = this.constructUrls(defaultZone.split(","));
        String eurekaUrl = this.getRandomUrl(eurekaUrls);
        JSONObject eurekaApplicationJson = null;

        try {
            log.info("Starting to get the application number from eureka ...");
            eurekaApplicationJson = restTemplate.getForEntity(eurekaUrl, JSONObject.class).getBody();
        } catch (Exception e) {
            log.warn("One eureka node is down! the node is " + eurekaUrl);
        }
        return this.getApplicationNameList(eurekaApplicationJson);
    }


    /**
     * 构造eureka请求信息的接口链接
     * @param eurekaUrls defaultZone
     * @return eureka的apps接口
     */
    private String[] constructUrls(String[] eurekaUrls) {
        for (int index = 0; index < eurekaUrls.length; index++) {
            if (eurekaUrls[index].endsWith("/")) {
                eurekaUrls[index] += "apps";
            } else {
                eurekaUrls[index] += "/apps";
            }
        }
        return eurekaUrls;
    }

    /**
     * 从eureka集群中获取单个节点的请求url
     * @param eurekaUrls eureka集群的地址
     * @return 其中一个节点
     */
    private String getRandomUrl(String[] eurekaUrls) {
        Random random = new Random();
        int index = random.nextInt(eurekaUrls.length) % (eurekaUrls.length + 1);
        return eurekaUrls[index];
    }

    /**
     * 解析eureka数据的Json，获取应用名的List
     * @param eurekaJson eureka的json
     * @return list
     */
    private List<String> getApplicationNameList(JSONObject eurekaJson) {
        List<String> applicationNameList = new ArrayList<>();
        if (eurekaJson != null) {
            JSONObject applications = eurekaJson.getJSONObject("applications");
            JSONArray applicationArray = applications.getJSONArray("application");
            for (int index = 0; index < applicationArray.size(); index++) {
                JSONObject app = applicationArray.getJSONObject(index);
                if (app.getString("name").equals(Constant.FILTER_SERVICE_NAME)) {
                    continue;
                }
                applicationNameList.add(app.getString("name"));
            }
        }
        return applicationNameList;
    }
}

package org.hiphone.swagger.center.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.swagger.center.constants.Constant;
import org.hiphone.swagger.center.service.ApiBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 自动扫描eureka使用的方法类
 * @author HiPhone
 */
@Slf4j
@Component
public class AutoScanHelper {

    @Value("${eureka.client.service-url.defaultZone}")
    private String defaultZone;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate loadBalanced;

    @Autowired
    private ApiBackendService apiBackendService;

    private List<String> dbServiceNames = new ArrayList<>();
    private String[] eurekaUrls = null;

    @PostConstruct
    private void init() {
        dbServiceNames = apiBackendService.queryAllServiceNames();
        eurekaUrls = constructUrls(defaultZone.split(","));
        log.info("serviceId list initialized success, the size of service id list is {}", dbServiceNames.size());
    }

    /**
     * 到eureka中获取注册服务的信息
     * @return 注册服务信息的list
     */
    public List<String> getApplicationNamesFromEureka() {
        String eurekaUrl = getRandomUrl(eurekaUrls);
        JSONObject eurekaApplicationJson = null;

        try {
            log.info("Starting to get the application number from eureka ...");
            eurekaApplicationJson = restTemplate.getForEntity(eurekaUrl, JSONObject.class).getBody();
        } catch (Exception e) {
            log.warn("One eureka node is down! the node is {}", eurekaUrl);
        }
        return getApplicationNameList(eurekaApplicationJson);
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

    /**
     * 构造ribbon请求URL
     * @param serviceName 服务名称
     * @return 构造完成的ribbon请求url
     */
    private static String constructRequestUrl(String serviceName) {
        return new StringBuilder()
                .append(Constant.URL_PREFIX)
                .append(serviceName)
                .append(Constant.URL_SUFFIX)
                .toString();
    }

    /**
     * 获取每个serviceName对应的api-docs
     * @param serviceNames serviceName 的 list
     * @return serviceName与api-docs对应的map
     */
    public Map<String, JSONObject> getSwaggerApiMap (List<String> serviceNames) {
        Map<String, JSONObject> swaggerApiDocsMap = new HashMap<>();

        serviceNames.forEach(s -> {
            String requestUrl = constructRequestUrl(s);
            JSONObject swaggerApiDocs = null;

            for (int index = 1; index <= Constant.SCAN_TRY_TIMES; index++) {
                try {
                    log.info("Trying to fetch swagger api-docs from {} throws eureka service...... Try count is {}", requestUrl, index);
                    swaggerApiDocs = loadBalanced.getForEntity(requestUrl, JSONObject.class).getBody();
                } catch (Exception e) {
                    log.info("Fetching fail! Try again then......");
                }
                if (swaggerApiDocs != null && swaggerApiDocs.toJSONString().contains(Constant.JUDGE_STRING)) {
                    swaggerApiDocsMap.put(s, swaggerApiDocs);
                }
            }
        });

        return swaggerApiDocsMap;
    }

    public void commitChangesToDatabase(Map<String, JSONObject> swaggerApiDocsMap) {

    }
}

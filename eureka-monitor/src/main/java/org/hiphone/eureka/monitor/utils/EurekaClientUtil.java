package org.hiphone.eureka.monitor.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.hiphone.eureka.monitor.entitys.ApplicationInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author HiPhone
 */
@Slf4j
@Component
public class EurekaClientUtil {

    private static final Integer TRY_TIME = 5;
    private static final String EUREKA_APPLICATIONS = "applications";
    private static final String EUREKA_APPLICATION = "application";
    private static final String EUREKA_INSTANCE = "instance";
    private static final String EUREKA_PORT = "port";
    private static final String EUREKA_LEASE_INFO = "leaseInfo";
    private static final String EUREKA_NAME = "name";
    private static final String EUREKA_HOSTNAME = "hostName";
    private static final String EUREKA_IPADDRESS = "ipAddr";
    private static final String EUREKA_REGISTER_TIME = "registrationTimestamp";
    private static final String EUREKA_INSTANCE_ID = "instanceId";


    @Autowired
    private RestTemplate restTemplate;

    /**
     * 请求集群的eureka信息
     * @param clusterId 集群id
     * @param clusterUrls 集群的所有节点的访问地址
     * @return eureka的json
     */
    public JSONObject getEurekaDataJson(String clusterId, String[] clusterUrls) {
        JSONObject eurekaJson = null;
        //默认访问最后一个配置的url
        int selectedIndex = clusterUrls.length - 1;
        String selectedUrl = clusterUrls[selectedIndex];

        for (int iindex = 0; iindex < TRY_TIME; iindex++) {
            try {
                eurekaJson = restTemplate.getForEntity(selectedUrl, JSONObject.class).getBody();
            } catch (Exception e) {
                log.error("A eureka node of data center : {} maybe down, the url is {}", clusterId, selectedUrl);
                //更换节点
                if (selectedIndex > 0) {
                    selectedIndex--;
                    selectedUrl = clusterUrls[selectedIndex];
                } else {
                    selectedIndex = clusterUrls.length - 1;
                    selectedUrl = clusterUrls[selectedIndex];
                }
            }
            if (eurekaJson != null) {
                break;
            }
        }
        return eurekaJson;
    }

    /**
     * 解析eureka的元数据信息，提取有用信息, downtime设置为默认值
     * @param clusterId 集群id
     * @param eurekaJson eureka的元数据json
     * @return eureka信息封装的类的set
     */
    public Set<ApplicationInstanceDto> getEurekaServiceInstanceSet(String clusterId, JSONObject eurekaJson) {
        JSONObject applicationsObj = eurekaJson.getJSONObject(EUREKA_APPLICATIONS);
        JSONArray applicationArray = applicationsObj.getJSONArray(EUREKA_APPLICATION);

        Set<ApplicationInstanceDto> eurekaServiceInstanceSet = new LinkedHashSet<>();
        //解析eureka的元数据
        for (Object o : applicationArray) {
            JSONObject application = (JSONObject) o;
            JSONArray instances = application.getJSONArray(EUREKA_INSTANCE);
            for (Object obj : instances) {
                JSONObject singleInstance = (JSONObject) obj;
                JSONObject port = singleInstance.getJSONObject(EUREKA_PORT);
                JSONObject leaseInfo = singleInstance.getJSONObject(EUREKA_LEASE_INFO);

                ApplicationInstanceDto applicationInstanceDto = new ApplicationInstanceDto();
                applicationInstanceDto.setInstanceId(singleInstance.getString(EUREKA_INSTANCE_ID));
                applicationInstanceDto.setClusterId(clusterId);
                applicationInstanceDto.setApplicationName(application.getString(EUREKA_NAME));
                applicationInstanceDto.setHostname(singleInstance.getString(EUREKA_HOSTNAME));
                applicationInstanceDto.setServicePort(port.getInteger("$"));
                applicationInstanceDto.setCurrentState(0);
                applicationInstanceDto.setIpAddress(singleInstance.getString(EUREKA_IPADDRESS));
                applicationInstanceDto.setRegisterTime(new Date(leaseInfo.getLong(EUREKA_REGISTER_TIME)));
                applicationInstanceDto.setDownTime(new Date(0));
                eurekaServiceInstanceSet.add(applicationInstanceDto);
            }
        }
        return eurekaServiceInstanceSet;
    }

}

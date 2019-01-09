package org.hiphone.eureka.monitor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author HiPhone
 */
@Component
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "eureka")
public class EurekaDataCenterConfig {

    /**
     * 获取eureka数据中心的地址，key是集群的id， value是集群的地址(使用逗号隔开)
     */
    @NotNull
    private Map<String, String> clusters;

}

package org.hiphone.eureka.monitor;

import org.hiphone.eureka.monitor.constants.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author HiPhone
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(Constant.TIMEOUT_VALUE);
        httpRequestFactory.setReadTimeout(Constant.TIMEOUT_VALUE);
        httpRequestFactory.setConnectTimeout(Constant.TIMEOUT_VALUE);
        return new RestTemplate(httpRequestFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

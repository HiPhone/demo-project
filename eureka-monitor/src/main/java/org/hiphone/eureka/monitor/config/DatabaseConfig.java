package org.hiphone.eureka.monitor.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.hiphone.eureka.monitor.constants.Constant;
import org.hiphone.eureka.monitor.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源配置类
 * @author HiPhone
 */
@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.type}")
    private String dbType;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.tomcat.max-active}")
    private Integer maxActive;

    @Value("${spring.datasource.tomcat.initial-size}")
    private Integer initialSize;

    @Value("${spring.datasource.tomcat.min-idle}")
    private Integer minIdle;

    @Value("${spring.datasource.tomcat.max-wait}")
    private Integer maxWait;

    @Value("${spring.datasource.tomcat.time-between-eviction-runs-millis}")
    private Long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.tomcat.min-evictable-idle-time-millis}")
    private Long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.tomcat.validation-query}")
    private String validationQuery;

    @Value("${spring.datasource.tomcat.test-while-idle}")
    private Boolean testWhileIdle;

    @Value("${spring.datasource.tomcat.test-on-borrow}")
    private Boolean testOnBorrow;

    @Value("${spring.datasource.tomcat.test-on-return}")
    private Boolean testOnReturn;

    @Value("${spring.datasource.tomcat.pool-prepared-statements}")
    private Boolean poolPreparedStatements;

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(EncryptUtil.decryptStringByBase64(password));
        dataSource.setDbType(dbType);
        dataSource.setDriverClassName(driverClassName);

        dataSource.setInitialSize(initialSize);
        dataSource.setInitialSize(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);

        return dataSource;
    }
}

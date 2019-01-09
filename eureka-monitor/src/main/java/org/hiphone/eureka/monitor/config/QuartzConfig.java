package org.hiphone.eureka.monitor.config;

import org.hiphone.eureka.monitor.task.EurekaStatusChecker;
import org.hiphone.eureka.monitor.utils.EncryptUtil;
import org.quartz.*;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author HiPhone
 */
@Configuration
@EnableScheduling
public class QuartzConfig {

    @Value("${org.quartz.scheduler.instance-name}")
    private String instanceName;

    @Value("${org.quartz.scheduler.rmi.export}")
    private String export;

    @Value("${org.quartz.scheduler.rmi.proxy}")
    private String proxy;

    @Value("${org.quartz.scheduler.wrap-job-execution-in-user-transaction}")
    private String wrapJobExecutionInUserTransaction;

    @Value("${org.quartz.thread-pool.class}")
    private String threadPoolClass;

    @Value("${org.quartz.thread-pool.thread-count}")
    private String threadCount;

    @Value("${org.quartz.thread-pool.thread-priority}")
    private String threadPriority;

    @Value("${org.quartz.thread-pool.threads-inherit-context-class-loader-of-initializing-thread}")
    private String threadsInheritContextClassLoaderOfInitializingThread;

    @Value("${org.quartz.job-store.misfire-threshold}")
    private String misfireThreshold;

    @Value("${org.quartz.job-store.class}")
    private String jobStoreClass;

    @Value("${org.quartz.job-store.table-prefix}")
    private String tablePrefix;

    @Value("${org.quartz.job-store.data-source}")
    private String dataSource;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.tomcat.max-active}")
    private String maxConnections;


    @Bean
    public Properties quartzProperties() {
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", instanceName);
        properties.setProperty("org.quartz.scheduler.rmi.export", export);
        properties.setProperty("org.quartz.scheduler.rmi.proxy", proxy);
        properties.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction", wrapJobExecutionInUserTransaction);
        properties.setProperty("org.quartz.threadPool.class", threadPoolClass);
        properties.setProperty("org.quartz.threadPool.threadCount", threadCount);
        properties.setProperty("org.quartz.threadPool.threadPriority", threadPriority);
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", threadsInheritContextClassLoaderOfInitializingThread);
        properties.setProperty("org.quartz.jobStore.misfireThreshold", misfireThreshold);
        properties.setProperty("org.quartz.jobStore.class", jobStoreClass);
        properties.setProperty("org.quartz.jobStore.tablePrefix", tablePrefix);
        properties.setProperty("org.quartz.jobStore.dataSource", dataSource);
        properties.setProperty("org.quartz.dataSource.qzDS.driver", driver);
        properties.setProperty("org.quartz.dataSource.qzDS.URL", url);
        properties.setProperty("org.quartz.dataSource.qzDS.user", user);
        properties.setProperty("org.quartz.dataSource.qzDS.password", EncryptUtil.decryptStringByBase64(password));
        properties.setProperty("org.quartz.dataSource.qzDS.maxConnections", maxConnections);
        properties.setProperty("org.quartz.jobStore.useProperties", "true");
        return properties;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(quartzProperties());
        factory.setJobFactory(jobFactory);
        return factory;
    }

    /**
     * 初始化quartz监听器
     */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    /**
     * 通过SchedulerFactoryBean获取scheduler实例
     */
    @Bean
    public Scheduler scheduler() {
        return schedulerFactory().getScheduler();
    }

    @Autowired
    private JobFactory jobFactory;

    @Autowired
    private Scheduler scheduler;

    private void createTask(String name, String group, String className, String description) throws Exception {

        //构建job信息
        @SuppressWarnings("unchecked")
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(className))
                .withIdentity(name, group).withDescription(description).build();

        // 默认crontab表达式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ?");
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group).withDescription(description)
                .withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new Exception("create quartz job failed ", e);
        }
    }

    @PostConstruct
    public void initQuartzJob() throws Exception {
        if (scheduler.getJobDetail(new JobKey(EurekaStatusChecker.class.getName(), "eureka-monitor")) == null) {
            createTask(EurekaStatusChecker.class.getName(), "eureka-monitor", EurekaStatusChecker.class.getName(), "eureka监控定时任务");
        }
    }


}

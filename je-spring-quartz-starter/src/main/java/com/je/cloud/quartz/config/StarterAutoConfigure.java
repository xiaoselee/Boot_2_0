package com.je.cloud.quartz.config;


import com.je.cloud.quartz.factory.JobFactory;
import com.je.cloud.quartz.listener.ScheduleJobInitListener;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Properties;


@EnableConfigurationProperties(QuartzInfoConfig.class)
@Component
public class StarterAutoConfigure {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private JobFactory jobFactory;

    @Resource
    QuartzInfoConfig quartzConfig;


    @Bean
    public ScheduleJobInitListener scheduleJobInitListener() {
        return new ScheduleJobInitListener();
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        log.info("-------------> SchedulerFactoryBean init");
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try {
            schedulerFactoryBean.setOverwriteExistingJobs(true);
            Properties properties = properties();
            if (properties != null) {
                schedulerFactoryBean.setQuartzProperties(properties);
            }
            schedulerFactoryBean.setJobFactory(jobFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedulerFactoryBean;
    }


    // 指定quartz.properties，可在配置文件中配置相关属性
    public Properties properties() throws IOException {
        if (quartzConfig.getPropertiesFilePath() != null) {
            log.info("-------------> quartzProperties init");
            PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
            propertiesFactoryBean.setLocation(new ClassPathResource(quartzConfig.getPropertiesFilePath()));
            propertiesFactoryBean.afterPropertiesSet();
            return propertiesFactoryBean.getObject();
        } else {
            return null;
        }
    }

    // 创建schedule
    @Bean(name = "scheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        log.info("-------------> scheduler init");
        return schedulerFactoryBean.getScheduler();
    }

}
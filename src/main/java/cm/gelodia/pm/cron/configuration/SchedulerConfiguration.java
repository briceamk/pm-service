package cm.gelodia.pm.cron.configuration;


import cm.gelodia.pm.cron.util.CustomJobFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@RequiredArgsConstructor
public class SchedulerConfiguration {

    private final DataSource dataSource;
    private final ApplicationContext applicationContext;
    private final QuartzProperties quartzProperties;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        CustomJobFactory customJobFactory = new CustomJobFactory();
        customJobFactory.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setOverwriteExistingJobs(true);
        factoryBean.setDataSource(dataSource);
        factoryBean.setQuartzProperties(properties);
        factoryBean.setJobFactory(customJobFactory);
        return factoryBean;
    }
}

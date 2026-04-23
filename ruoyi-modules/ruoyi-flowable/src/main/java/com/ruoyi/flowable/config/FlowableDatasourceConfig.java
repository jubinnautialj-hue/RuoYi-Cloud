package com.ruoyi.flowable.config;

import javax.sql.DataSource;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FlowableDatasourceConfig
{
    @Bean(name = "flowableDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.flowable")
    public DataSource flowableDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "flowableTransactionManager")
    public PlatformTransactionManager flowableTransactionManager(
            @Qualifier("flowableDataSource") DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> engineConfigurationConfigurer(
            @Qualifier("flowableDataSource") DataSource dataSource,
            @Qualifier("flowableTransactionManager") PlatformTransactionManager transactionManager)
    {
        return engineConfiguration -> {
            engineConfiguration.setDataSource(dataSource);
            engineConfiguration.setTransactionManager(transactionManager);
        };
    }
}

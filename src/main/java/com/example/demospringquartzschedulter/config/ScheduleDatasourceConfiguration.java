package com.example.demospringquartzschedulter.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demospringquartzschedulter",
        entityManagerFactoryRef = "scheduleEntityManager",
        transactionManagerRef = "scheduleTransactionManager")
public class ScheduleDatasourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties scheduleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.configuration")
    public DataSource scheduleDataSource() {
        return scheduleDataSourceProperties()
            .initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "scheduleEntityManager")
    public LocalContainerEntityManagerFactoryBean scheduleEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(scheduleDataSource())
                .properties(properties())
                .packages("com.example.demospringquartzschedulter")
                .build();
    }

    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        return properties;
    }

    @Bean
    public PlatformTransactionManager scheduleTransactionManager(
            final @Qualifier("scheduleEntityManager") LocalContainerEntityManagerFactoryBean scheduleTransactionManager) {
        return new JpaTransactionManager(Objects.requireNonNull(scheduleTransactionManager.getObject()));
    }
}

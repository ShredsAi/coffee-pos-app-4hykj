package ai.shreds.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class InfrastructureJpaConfig {

    @Value("${spring.jpa.hibernate.ddl-auto:update}")
    private String ddlAuto;

    @Value("${spring.jpa.properties.hibernate.dialect:org.hibernate.dialect.PostgreSQLDialect}")
    private String dialect;

    @Value("${spring.jpa.show-sql:true}")
    private boolean showSql;

    @Value("${spring.jpa.properties.hibernate.format_sql:true}")
    private boolean formatSql;
    
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size:50}")
    private int batchSize;
    
    @Value("${spring.jpa.properties.hibernate.jdbc.time_zone:UTC}")
    private String timeZone;
    
    @Value("${spring.jpa.properties.hibernate.query.timeout:10000}")
    private int queryTimeout;
    
    @Value("${spring.jpa.properties.hibernate.cache.use_second_level_cache:false}")
    private boolean useSecondLevelCache;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("ai.shreds.domain.entities", "ai.shreds.infrastructure.repositories");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(showSql);
        vendorAdapter.setGenerateDdl(true);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaProperties(jpaProperties());
        
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
        // Configure default transaction timeout
        transactionManager.setDefaultTimeout(30); // 30 seconds default timeout
        return transactionManager;
    }

    @Bean
    public Properties jpaProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        props.setProperty("hibernate.dialect", dialect);
        props.setProperty("hibernate.format_sql", String.valueOf(formatSql));
        
        // Performance optimizations
        props.setProperty("hibernate.jdbc.batch_size", String.valueOf(batchSize));
        props.setProperty("hibernate.order_inserts", "true");
        props.setProperty("hibernate.order_updates", "true");
        props.setProperty("hibernate.jdbc.batch_versioned_data", "true");
        
        // Query optimizations
        props.setProperty("hibernate.query.timeout", String.valueOf(queryTimeout));
        props.setProperty("hibernate.jdbc.time_zone", timeZone);
        
        // Connection handling
        props.setProperty("hibernate.connection.provider_disables_autocommit", "true");
        props.setProperty("hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_RELEASE_AFTER_STATEMENT");
        
        // Cache configuration
        props.setProperty("hibernate.cache.use_second_level_cache", String.valueOf(useSecondLevelCache));
        props.setProperty("hibernate.cache.use_query_cache", "false");
        
        // Physical naming strategy for database column naming conventions
        props.setProperty("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        
        return props;
    }
}
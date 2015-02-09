package me.smartco.akstore.transaction.spring;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by libin on 15-1-9.
 */
@Configuration
@ComponentScan(basePackages="me.smartco.akstore")
@EnableJpaRepositories(basePackages={"me.smartco.akstore.transaction.repository"})
public class JpaConfiguration  {

    private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";

    @Bean
    public PropertiesPropertySource propertySource() throws IOException {
        return new ResourcePropertySource("classpath:"+System.getenv("env")+"/db.properties");
    }

    @Bean
    public DataSource dataSource(PropertiesPropertySource env) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty(PROPERTY_NAME_DATABASE_DRIVER).toString());
        dataSource.setUrl(env.getProperty(PROPERTY_NAME_DATABASE_URL).toString());
        dataSource.setUsername(env.getProperty(PROPERTY_NAME_DATABASE_USERNAME).toString());
        dataSource.setPassword(env.getProperty(PROPERTY_NAME_DATABASE_PASSWORD).toString());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan("me.smartco.akstore.transaction.model");
        Properties properties=new Properties();
        properties.setProperty("javax.persistence.schema-generation.database.action","none");
        lef.setJpaProperties(properties);
        return lef;
    }

    public SchemaExport schemaExport(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean){
        HibernateEntityManagerFactory hibernateEntityManagerFactory= (HibernateEntityManagerFactory)localContainerEntityManagerFactoryBean.getNativeEntityManagerFactory();
        //hibernateEntityManagerFactory.
        //SchemaExport dbExport = new SchemaExport(sessionFactory.)
        return null;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        //hibernateJpaVendorAdapter.getJpaPropertyMap().put("hibernate.hbm2ddl.auto","create");
        //hibernateJpaVendorAdapter.getJpaPropertyMap().put("javax.persistence.schema-generation.database.action","create");

        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}

package com.project.utopia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class ApplicationConfig {




    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory() throws IOException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.project.utopia.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }


    public ApplicationConfig() throws FileNotFoundException {
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() throws IOException {
        Properties props = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = ApplicationConfig.class.getClassLoader().getResourceAsStream(propFileName);
        props.load(inputStream);

        String username = props.getProperty("user");
        String password = props.getProperty("password");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://laiproject-instance.cmukzxtzloql.us-west-1.rds.amazonaws.com:3306/utopia?createDatabaseIfNotExist=true&serverTimezone=UTC");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        return hibernateProperties;
    }
}

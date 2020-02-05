package com;

import com.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;


//@SpringBootApplication()
@EnableAutoConfiguration
@ComponentScan
@Configuration
@EnableJpaRepositories

public class Main {

    /*@Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;
*/
    public static void main(String[] args) throws Exception {
        SpringApplication.run(com.Main.class, args);
    }
    @Bean
    public Person getPeron(){return new Person();}

}
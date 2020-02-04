package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.sql.DataSource;


@SpringBootApplication()
public class Main {

    /*@Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;
*/
    public static void main(String[] args) throws Exception {
        SpringApplication.run(com.Main.class, args);
    }
}
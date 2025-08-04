package com.projectecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ProjectecommerceApplication{

    public static void main(String[] args){
        SpringApplication.run(ProjectecommerceApplication.class, args);
    }

}

package com.ncut.backmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.ncut.backmanagement.dao"})
public class BackManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackManagementApplication.class, args);
    }
}

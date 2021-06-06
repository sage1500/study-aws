package com.example.demo.web.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.demo.web.domain.repository")
public class MyBatisConfig {
    
}

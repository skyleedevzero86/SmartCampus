package com.sleekydz86.server;


import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@EnableAsync
@SpringBootApplication
@MapperScan(basePackages = "com.sleekydz86.server", annotationClass = Mapper.class)
public class MarketApiApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		SpringApplication.run(MarketApiApplication.class, args);
	}

}

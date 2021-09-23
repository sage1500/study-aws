package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	@Value("${HOST1:no}")
	private String host1;

	@Value("${HOST2:no}")
	private String host2;

	@Value("${my.env.env1:no}")
	private String env1;

	@Value("${my.env.env2:no}")
	private String env2;

	@Value("${my.env2.env3:no}")
	private String env3;

	public static void main(String[] args) {
		var ctx = SpringApplication.run(DemoApplication.class, args);
		ctx.getBean(DemoApplication.class).run();
	}

	void run() {
		log.info("★START");
		log.info("★host1={}", host1);
		log.info("★host2={}", host2);
		log.info("★env1={}", env1);
		log.info("★env2={}", env2);
		log.info("★env3={}", env3);
		log.info("★END");
	}
}

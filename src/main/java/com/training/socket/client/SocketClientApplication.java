package com.training.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketClientApplication {
	public static  Logger  logger=LoggerFactory.getLogger(SocketClient.class);

	public static void main(String[] args) {
		logger.info("程序开始启动。。。");
		SpringApplication.run(SocketClientApplication.class, args);
		logger.info("我启动成功啦！");
	}

}


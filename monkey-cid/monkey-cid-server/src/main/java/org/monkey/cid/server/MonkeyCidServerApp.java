package org.monkey.cid.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MonkeyCidServerApp {
	public static void main(String[] args) {
		SpringApplication.run(MonkeyCidServerApp.class, args);
	}
}

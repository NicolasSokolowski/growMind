package com.brainplus.growMind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.brainplus.growMind")
public class GrowMindApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrowMindApplication.class, args);
	}

}

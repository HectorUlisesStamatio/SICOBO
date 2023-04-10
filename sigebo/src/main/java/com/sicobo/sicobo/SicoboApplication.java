package com.sicobo.sicobo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SicoboApplication {

	public static void main(String[] args) {
		SpringApplication.run(SicoboApplication.class, args);
	}

}

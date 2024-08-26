package com.example.techlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableAspectJAutoProxy
public class TechlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechlogApplication.class, args);
	}

}

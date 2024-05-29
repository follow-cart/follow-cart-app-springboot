package com.capstone_design.followcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.capstone_design.followcart.model")
@EnableJpaRepositories(basePackages = "com.capstone_design.followcart.repository")
public class FollowCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(FollowCartApplication.class, args);
	}
}

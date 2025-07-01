package com.example.project_two;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class ProjectTwoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTwoApplication.class, args);
	}

}

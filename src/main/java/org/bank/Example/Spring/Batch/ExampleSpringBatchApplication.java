package org.bank.Example.Spring.Batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableBatchProcessing
@SpringBootApplication
public class ExampleSpringBatchApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExampleSpringBatchApplication.class, args);

	}
}

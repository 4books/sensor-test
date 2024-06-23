package com.example.sensor;

import com.example.sensor.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SensorApplication implements CommandLineRunner {

	private final KafkaProducerService producerService;

	public SensorApplication(KafkaProducerService producerService) {
		this.producerService = producerService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SensorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		producerService.generateTestData(1_000_000);
	}
}

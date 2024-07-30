package com.example.trading_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.trading_backend.model")
public class TradingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingBackendApplication.class, args);
	}
}
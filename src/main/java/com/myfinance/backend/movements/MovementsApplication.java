package com.myfinance.backend.movements;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://192.168.1.9:5173")
@SpringBootApplication
public class MovementsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovementsApplication.class, args);
	}

}

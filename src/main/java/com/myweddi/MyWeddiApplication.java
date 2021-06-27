package com.myweddi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootApplication
public class MyWeddiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyWeddiApplication.class, args);
	}

}

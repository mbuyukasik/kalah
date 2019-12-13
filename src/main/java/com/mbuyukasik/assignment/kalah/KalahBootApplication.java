package com.mbuyukasik.assignment.kalah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * KalahBootApplication is the main class of spring boot application.
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@SpringBootApplication
@Configuration
public class KalahBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(KalahBootApplication.class, args);
	}

}

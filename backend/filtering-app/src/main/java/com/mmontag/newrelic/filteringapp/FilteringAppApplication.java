package com.mmontag.newrelic.filteringapp;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilteringAppApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(FilteringAppApplication.class, args);
	}
}

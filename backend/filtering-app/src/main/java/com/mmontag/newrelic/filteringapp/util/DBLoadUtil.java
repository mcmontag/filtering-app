package com.mmontag.newrelic.filteringapp.util;

import java.io.IOException;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.mmontag.newrelic.filteringapp.model.Customer;
import com.mmontag.newrelic.filteringapp.repository.CustomerRepository;

@Component
public class DBLoadUtil {
    @Autowired
    CustomerRepository customers;

    @PostConstruct
    public void loadData() throws IOException {
        try (var scanner = new Scanner(new ClassPathResource("data.txt").getInputStream())) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                var first_last_company = line.split("__");

                customers.save(new Customer(first_last_company[0], first_last_company[1], first_last_company[2]));
            }
        }
    }
}

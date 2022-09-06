package com.mmontag.newrelic.filteringapp.repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mmontag.newrelic.filteringapp.model.Customer;

@Primary
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public List<Customer> findByCompanyName(String companyName);
    
    public List<Customer> findByFirstNameContainsIgnoringCaseOrLastNameContainsIgnoringCase(String firstName, String lastName);
}

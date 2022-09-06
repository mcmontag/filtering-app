package com.mmontag.newrelic.filteringapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Model representing a "customer", with fields for first/last name and company name.
 */
@Getter
@Setter
@Entity
@Table(name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column
    @JsonProperty
    private String firstName;

    @Column
    @JsonProperty
    private String lastName;

    @Column
    @JsonProperty
    private String companyName;

    public Customer() {}

    public Customer(String firstName, String lastName, String companyName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
    }
}

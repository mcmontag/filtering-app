package com.mmontag.newrelic.filteringapp.services.controllers.customer;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mmontag.newrelic.filteringapp.exception.SortingException;
import com.mmontag.newrelic.filteringapp.model.Customer;
import com.mmontag.newrelic.filteringapp.repository.CustomerRepository;
import com.mmontag.newrelic.filteringapp.services.SortingService;
import com.mmontag.newrelic.filteringapp.services.SortingService.SortDirection;
import com.mmontag.newrelic.filteringapp.services.controllers.util.ControllerUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(methods = RequestMethod.GET)
@Component
@RestController
@RequestMapping(path="/customers")
public class CustomerController implements ControllerUtils {
    
    private static final Pattern ASCENDING_PARAM_REGEX = Pattern.compile("(?i)^(1|a(sc(ending)?)?)$");

    private static final Pattern DESCENDING_PARAM_REGEX = Pattern.compile("(?i)^(-1|d(esc(ending)?)?)$");

    @Autowired
    CustomerRepository customers;

    @Autowired
    SortingService<Customer> sortingService;

    @PostConstruct
    void setupSortingService() {
        sortingService.registerField("firstName", nullChecked(c -> c.getFirstName().toUpperCase()));
        sortingService.registerField("lastName", nullChecked(c -> c.getLastName().toUpperCase()));
        sortingService.registerField("companyName", nullChecked(c -> c.getCompanyName().toUpperCase()));
    }

    @GetMapping
    public List<Customer> getCustomers(@RequestParam(required=false) Integer limit, @RequestParam(required=false) String sortBy, @RequestParam(required=false) String sortDirection) {
        log.info("GET -> /customers (params: [limit={}, sortField={}, sortDirection={}])", limit, sortBy, sortDirection);
        return firstN(limit, applySort(customers.findAll(), sortBy, sortDirection));
    }

    @GetMapping(path="/search/{needle}")
    public List<Customer> searchWithinNames(@PathVariable String needle, @RequestParam(required=false) Integer limit) {
        log.info("/search/{}", needle);
        return firstN(limit, whereNameContains(needle));
    }

    @GetMapping(path="/search", params="limit")
    public List<Customer> searchWithinNames(@RequestParam(required=false) Integer limit) {
        log.info("/search?limit={}", limit);
        return firstN(limit, customers.findAll());
    }
    
    // Returns the first N elements of a list, with safeguards in place to prevent indexing/null pointer exceptions and odd behavior.
    private <T> List<T> firstN(Integer n, List<T> input) {
        if (input == null || input.isEmpty() || n == null || n <= 0 || n >= input.size()) {
            return input;
        } else {
            return input.stream().limit(n).collect(Collectors.toList()); 
        }
    }

    // Applies a sorting strategy to the given list of customers based on the value of the sortBy and sortDirection parameter.
    // If the sortBy parameter is invalid, it is simply ignored. Note that this is an in-place sort.
    private List<Customer> applySort(List<Customer> input, String sortField, String sortDirectionAsString) {
        if (!sortingService.isSortableBy(sortField)) {
            return input;
        }

        try {
            var sortDirection = parseSortDirection(sortDirectionAsString);
            return sortingService.sort(input, sortField, sortDirection);

        } catch (SortingException e) {
            log.error(e.getMessage());
            return input;
        }
    }

    // A shorthand/convenience method for a JPA method, which searches the first and last name fields for a substring (case-insensitive)
    private List<Customer> whereNameContains(String needle) {
        return customers.findByFirstNameContainsIgnoringCaseOrLastNameContainsIgnoringCase(needle, needle);
    }

    // Translates values for the /search endpoint's sortDirection param to SortingService.SortDirection's enum values.
    // Note that SortingService.sort DOES accept null values, so we can return null from this method. 
    private SortDirection parseSortDirection(String sortDirectionAsString) {
        if (sortDirectionAsString == null) {
            return null;

        } else if (ASCENDING_PARAM_REGEX.matcher(sortDirectionAsString).matches()) {
            return SortDirection.ASCENDING;

        } else if (DESCENDING_PARAM_REGEX.matcher(sortDirectionAsString).matches()) {
            return SortDirection.DESCENDING;

        } else {
            return null;
        }
    }

    private Function<Customer, String> nullChecked(Function<Customer, String> getter) {
        return c -> {
            try {
                return getter.apply(c);
            } catch (NullPointerException | IndexOutOfBoundsException | NoSuchElementException ex) {
                    return null;
            }
        };
    }
}

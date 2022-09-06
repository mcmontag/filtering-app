package com.mmontag.newrelic.filteringapp.services.controllers.customer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mmontag.newrelic.filteringapp.model.Customer;
import com.mmontag.newrelic.filteringapp.repository.CustomerRepository;
import com.mmontag.newrelic.filteringapp.services.SortingService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomerControllerTest {

    @Mock
    CustomerRepository _customerRepository;

    @Autowired
    SortingService<Customer> sortingService;

    @InjectMocks
    CustomerController customerController;

    @BeforeEach
    void setupController() {
        customerController.sortingService = sortingService;
        customerController.setupSortingService();
    }

    @Test
    void test_getCustomers_no_params() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, null, null);
        
        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_null() {
        var _customers = createSortTestData();

        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", null);
        
        _customers.sort(Comparator.comparing(Customer::getFirstName));

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_lastName_null() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "lastName", null);
        
        _customers.sort(Comparator.comparing(Customer::getLastName));

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_companyName_null() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "companyName", null);
        
        _customers.sort(Comparator.comparing(Customer::getCompanyName));

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_1() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", "1");
        
        _customers.sort(Comparator.comparing(Customer::getFirstName));
        
        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_a() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", "a");
        
        _customers.sort(Comparator.comparing(Customer::getFirstName));

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_asc() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", "asc");
        
        _customers.sort(Comparator.comparing(Customer::getFirstName));

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_ascending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", "ascending");
        
        _customers.sort(Comparator.comparing(Customer::getFirstName));

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_minus1() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", "-1");
        
        _customers.sort(Comparator.comparing(Customer::getFirstName).reversed());

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_d() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", "d");
        
        _customers.sort(Comparator.comparing(Customer::getFirstName).reversed());

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_desc() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", "desc");
        
        _customers.sort(Comparator.comparing(Customer::getFirstName).reversed());

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_firstName_descending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "firstName", "descending");
        
        _customers.sort(Comparator.comparing(Customer::getFirstName).reversed());

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_lastName_ascending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "lastName", "ascending");
        
        _customers.sort(Comparator.comparing(Customer::getLastName));
      
        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_lastName_descending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "lastName", "descending");
        
        _customers.sort(Comparator.comparing(Customer::getLastName).reversed());
      
        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_companyName_ascending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "companyName", "ascending");
        
        _customers.sort(Comparator.comparing(Customer::getCompanyName));

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_null_companyName_descending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(null, "companyName", "descending");
        
        _customers.sort(Comparator.comparing(Customer::getCompanyName).reversed());

        mockListsEqual(_customers, output);
    }

    @Test
    void test_getCustomers_positiveValidIndex_companyName_descending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(3, "companyName", "descending");
        
        _customers.sort(Comparator.comparing(Customer::getCompanyName).reversed());

        mockListsEqual(_customers.subList(0, 3), output);
    }

    @Test
    void test_getCustomers_positiveValidIndex_firstName_ascending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(2, "firstName", "ascending");

        _customers.sort(Comparator.comparing(Customer::getFirstName));

        mockListsEqual(_customers.subList(0, 2), output);
    }

    @Test // should return full-length list.
    void test_getCustomers_nonPositiveIndex_firstName_ascending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(0, "firstName", "ascending");

        _customers.sort(Comparator.comparing(Customer::getFirstName));

        mockListsEqual(_customers, output);
    }

    @Test // should return full-length list.
    void test_getCustomers_positiveOutOfBoundsIndex_firstName_ascending() {
        var _customers = createSortTestData();
        when(_customerRepository.findAll()).thenReturn(_customers);

        var output = customerController.getCustomers(_customers.size() + 100, "firstName", "ascending");

        _customers.sort(Comparator.comparing(Customer::getFirstName));

        mockListsEqual(_customers, output);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSearchWithinNames() {
        var _mockList = (List<Customer>) mock(List.class);
        var testInput = "abc";
        when(_customerRepository.findByFirstNameContainsIgnoringCaseOrLastNameContainsIgnoringCase(anyString(), anyString())).thenReturn(_mockList);

        var returned = customerController.searchWithinNames(testInput, null);
        
        verify(_customerRepository).findByFirstNameContainsIgnoringCaseOrLastNameContainsIgnoringCase(eq(testInput), eq(testInput));
        assertTrue(returned == _mockList);
    }
    
    private List<Customer> createSortTestData() {
        var _customers = new ArrayList<Customer>();

        var c1 = mock(Customer.class);
        var c2 = mock(Customer.class);
        var c3 = mock(Customer.class);
        var c4 = mock(Customer.class);
        var c5 = mock(Customer.class);
    
        when(c1.getFirstName()).thenReturn("Alice");
        when(c1.getLastName()).thenReturn("Zanzibar");
        when(c1.getCompanyName()).thenReturn("Middlish");
        when(c1.getId()).thenReturn(1L);

        when(c2.getFirstName()).thenReturn("Don");
        when(c2.getLastName()).thenReturn("Anderson");
        when(c2.getCompanyName()).thenReturn("Middlish");
        when(c2.getId()).thenReturn(2L);
        
        when(c3.getFirstName()).thenReturn("Tom");
        when(c3.getLastName()).thenReturn("Thompson");
        when(c3.getCompanyName()).thenReturn("Wayback");
        when(c3.getId()).thenReturn(3L);
        
        when(c4.getFirstName()).thenReturn("Edith");
        when(c4.getLastName()).thenReturn("Middleton");
        when(c4.getCompanyName()).thenReturn("Early Co.");
        when(c4.getId()).thenReturn(4L);

        when(c5.getFirstName()).thenReturn("Judy");
        when(c5.getLastName()).thenReturn("Clarkson");
        when(c5.getCompanyName()).thenReturn("EarlyMiddle");
        when(c5.getId()).thenReturn(5L);

        _customers.add(c1);
        _customers.add(c2);
        _customers.add(c3);
        _customers.add(c4);
        _customers.add(c5);

        return _customers;
    }

    private void mockListsEqual(List<Customer> mockList1, List<Customer> mockList2) {
        assertTrue(mockList1.size() == mockList2.size());
        for (int i = 0; i < mockList1.size(); ++i) {
            System.err.println("list1: " + mockList1.get(i).getFirstName() + ", list2: " + mockList2.get(i).getFirstName());
            assertTrue(mockList1.get(i).getId() == mockList2.get(i).getId());
        }
    }
}

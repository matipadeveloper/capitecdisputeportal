package za.co.capitec.capitecdisputeportalserver.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.capitec.capitecdisputeportalserver.entities.Customer;
import za.co.capitec.capitecdisputeportalserver.exception.CustomerNotFoundException;
import za.co.capitec.capitecdisputeportalserver.repositories.CustomerRepository;
import za.co.capitec.capitecdisputeportalserver.requests.CustomerRequest;
import za.co.capitec.capitecdisputeportalserver.responses.CustomerResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;
    private CustomerRequest testCustomerRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setIdNumber("1234567890");
        testCustomer.setName("John");
        testCustomer.setSurname("Doe");
        testCustomer.setEmail("john.doe@example.com");

        testCustomerRequest = new CustomerRequest();
        testCustomerRequest.setIdNumber("1234567890");
        testCustomerRequest.setName("John");
        testCustomerRequest.setSurname("Doe");
        testCustomerRequest.setEmail("john.doe@example.com");
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = Collections.singletonList(testCustomer);
        when(customerRepository.findAll()).thenReturn(customers);

        
        List<CustomerResponse> result = customerService.getAllCustomers();

        
        assertEquals(1, result.size());
        assertEquals("1234567890", result.get(0).getIdNumber());
        assertEquals("John", result.get(0).getName());
        assertEquals("Doe", result.get(0).getSurname());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
    }

    @Test
    void testGetCustomerById_Found() {
        when(customerRepository.findById("1234567890")).thenReturn(Optional.of(testCustomer));
        
        Optional<CustomerResponse> result = customerService.getCustomerById("1234567890");
        
        assertTrue(result.isPresent());
        assertEquals("1234567890", result.get().getIdNumber());
        assertEquals("John", result.get().getName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById("nonexistent")).thenReturn(Optional.empty());
        
        Optional<CustomerResponse> result = customerService.getCustomerById("nonexistent");
        
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        
        CustomerResponse result = customerService.createCustomer(testCustomerRequest);
        
        assertEquals("1234567890", result.getIdNumber());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_Found() {
        when(customerRepository.findById("1234567890")).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        
        CustomerResponse result = customerService.updateCustomer("1234567890", testCustomerRequest);
        
        assertEquals("1234567890", result.getIdNumber());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
            customerService.updateCustomer("nonexistent", testCustomerRequest)
        );
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepository.existsById("1234567890")).thenReturn(true);
        
        customerService.deleteCustomer("1234567890");
        
        verify(customerRepository).existsById("1234567890");
        verify(customerRepository).deleteById("1234567890");
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.existsById("nonexistent")).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () ->
            customerService.deleteCustomer("nonexistent")
        );
    }
}

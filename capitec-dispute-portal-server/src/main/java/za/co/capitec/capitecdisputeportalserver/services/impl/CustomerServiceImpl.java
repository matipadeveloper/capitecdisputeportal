package za.co.capitec.capitecdisputeportalserver.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.capitec.capitecdisputeportalserver.entities.Customer;
import za.co.capitec.capitecdisputeportalserver.exception.CustomerNotFoundException;
import za.co.capitec.capitecdisputeportalserver.requests.CustomerRequest;
import za.co.capitec.capitecdisputeportalserver.responses.CustomerResponse;
import za.co.capitec.capitecdisputeportalserver.repositories.CustomerRepository;
import za.co.capitec.capitecdisputeportalserver.services.CustomerService;
import za.co.capitec.capitecdisputeportalserver.utils.CustomerMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerResponse> getCustomerById(String idNumber) {
        return customerRepository.findById(idNumber)
                .map(CustomerMapper::convertToResponse);
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setIdNumber(customerRequest.getIdNumber());
        customer.setName(customerRequest.getName());
        customer.setSurname(customerRequest.getSurname());
        customer.setEmail(customerRequest.getEmail());

        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.convertToResponse(savedCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(String idNumber, CustomerRequest customerRequest) {
        return customerRepository.findById(idNumber)
                .map(customer -> {
                    customer.setName(customerRequest.getName());
                    customer.setSurname(customerRequest.getSurname());
                    customer.setEmail(customerRequest.getEmail());

                    return CustomerMapper.convertToResponse(customerRepository.save(customer));
                })
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + idNumber));
    }

    @Override
    public void deleteCustomer(String idNumber) {
        if (!customerRepository.existsById(idNumber)) {
            throw new CustomerNotFoundException("Customer not found with id: " + idNumber);
        }
        customerRepository.deleteById(idNumber);
    }
}

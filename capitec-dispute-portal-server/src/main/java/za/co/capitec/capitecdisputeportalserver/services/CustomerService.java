package za.co.capitec.capitecdisputeportalserver.services;

import za.co.capitec.capitecdisputeportalserver.requests.CustomerRequest;
import za.co.capitec.capitecdisputeportalserver.responses.CustomerResponse;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerResponse> getAllCustomers();
    Optional<CustomerResponse> getCustomerById(String idNumber);
    CustomerResponse createCustomer(CustomerRequest customerRequest);
    CustomerResponse updateCustomer(String idNumber, CustomerRequest customerRequest);
    void deleteCustomer(String idNumber);
}

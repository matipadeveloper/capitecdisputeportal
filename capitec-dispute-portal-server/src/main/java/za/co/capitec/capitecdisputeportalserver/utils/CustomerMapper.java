package za.co.capitec.capitecdisputeportalserver.utils;

import za.co.capitec.capitecdisputeportalserver.entities.Customer;
import za.co.capitec.capitecdisputeportalserver.responses.CustomerResponse;

public class CustomerMapper {

    public static CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setIdNumber(customer.getIdNumber());
        response.setName(customer.getName());
        response.setSurname(customer.getSurname());
        response.setEmail(customer.getEmail());
        return response;
    }
}

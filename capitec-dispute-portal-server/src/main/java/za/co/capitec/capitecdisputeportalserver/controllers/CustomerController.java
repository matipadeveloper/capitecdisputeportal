package za.co.capitec.capitecdisputeportalserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.capitecdisputeportalserver.requests.CustomerRequest;
import za.co.capitec.capitecdisputeportalserver.responses.CustomerResponse;
import za.co.capitec.capitecdisputeportalserver.services.CustomerService;
import za.co.capitec.capitecdisputeportalserver.utils.ValidatorUtil;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "APIs for managing customers")
@SecurityRequirement(name = "Bearer Authentication")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve all customers")
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{idNumber}")
    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by their ID number")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String idNumber) {
        ValidatorUtil.validateCustomerIdNumber(idNumber);
        return customerService.getCustomerById(idNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create customer", description = "Create a new customer")
    public CustomerResponse createCustomer(@RequestBody CustomerRequest customerRequest) {
        ValidatorUtil.validateCustomerRequest(customerRequest);
        return customerService.createCustomer(customerRequest);
    }

    @PutMapping("/{idNumber}")
    @Operation(summary = "Update customer", description = "Update an existing customer")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable String idNumber, @RequestBody CustomerRequest customerRequest) {
        ValidatorUtil.validateCustomerIdNumber(idNumber);
        ValidatorUtil.validateCustomerRequest(customerRequest);
        return ResponseEntity.ok(customerService.updateCustomer(idNumber, customerRequest));
    }

    @DeleteMapping("/{idNumber}")
    @Operation(summary = "Delete customer", description = "Delete a customer")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String idNumber) {
        ValidatorUtil.validateCustomerIdNumber(idNumber);
        customerService.deleteCustomer(idNumber);
        return ResponseEntity.noContent().build();
    }
}

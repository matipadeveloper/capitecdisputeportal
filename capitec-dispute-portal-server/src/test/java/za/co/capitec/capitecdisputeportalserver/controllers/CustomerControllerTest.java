package za.co.capitec.capitecdisputeportalserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import za.co.capitec.capitecdisputeportalserver.exception.CustomerNotFoundException;
import za.co.capitec.capitecdisputeportalserver.requests.CustomerRequest;
import za.co.capitec.capitecdisputeportalserver.responses.CustomerResponse;
import za.co.capitec.capitecdisputeportalserver.services.CustomerService;
import za.co.capitec.capitecdisputeportalserver.config.TestSecurityConfig;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerResponse testCustomerResponse;
    private CustomerRequest testCustomerRequest;

    @BeforeEach
    void setUp() {
        testCustomerResponse = new CustomerResponse();
        testCustomerResponse.setIdNumber("1234567890");
        testCustomerResponse.setName("John");
        testCustomerResponse.setSurname("Doe");
        testCustomerResponse.setEmail("john.doe@example.com");

        testCustomerRequest = new CustomerRequest();
        testCustomerRequest.setIdNumber("1234567890");
        testCustomerRequest.setName("John");
        testCustomerRequest.setSurname("Doe");
        testCustomerRequest.setEmail("john.doe@example.com");
    }

    @Test
    void testGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Collections.singletonList(testCustomerResponse));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idNumber").value("1234567890"))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].surname").value("Doe"));
    }

    @Test
    void testGetCustomerById_Found() throws Exception {
        when(customerService.getCustomerById("1234567890")).thenReturn(Optional.of(testCustomerResponse));

        mockMvc.perform(get("/api/customers/1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNumber").value("1234567890"))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void testGetCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerById("9999999999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/9999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(testCustomerResponse);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idNumber").value("1234567890"))
                .andExpect(jsonPath("$.name").value("John"));

        verify(customerService).createCustomer(any(CustomerRequest.class));
    }

    @Test
    void testUpdateCustomer_Success() throws Exception {
        when(customerService.updateCustomer(eq("1234567890"), any(CustomerRequest.class))).thenReturn(testCustomerResponse);

        mockMvc.perform(put("/api/customers/1234567890")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idNumber").value("1234567890"));

        verify(customerService).updateCustomer(eq("1234567890"), any(CustomerRequest.class));
    }

    @Test
    void testUpdateCustomer_NotFound() throws Exception {
        when(customerService.updateCustomer(eq("9999999999"), any(CustomerRequest.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(put("/api/customers/9999999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomerRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customers/1234567890"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer("1234567890");
    }
}

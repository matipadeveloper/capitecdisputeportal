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
import za.co.capitec.capitecdisputeportalserver.config.TestSecurityConfig;
import za.co.capitec.capitecdisputeportalserver.enums.TransactionStatus;
import za.co.capitec.capitecdisputeportalserver.exception.CustomerNotFoundException;
import za.co.capitec.capitecdisputeportalserver.exception.TransactionNotFoundException;
import za.co.capitec.capitecdisputeportalserver.requests.PaymentTransactionRequest;
import za.co.capitec.capitecdisputeportalserver.responses.PaymentTransactionResponse;
import za.co.capitec.capitecdisputeportalserver.services.PaymentTransactionService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentTransactionController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class PaymentTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentTransactionService paymentTransactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentTransactionResponse testTransactionResponse;
    private PaymentTransactionRequest testTransactionRequest;

    @BeforeEach
    void setUp() {
        testTransactionResponse = new PaymentTransactionResponse();
        testTransactionResponse.setId(1L);
        testTransactionResponse.setTransactionReference("TXN-001");
        testTransactionResponse.setStatus(TransactionStatus.PAID);
        testTransactionResponse.setAmount(new BigDecimal("100.50"));
        testTransactionResponse.setCustomerIdNumber("1234567890");
        testTransactionResponse.setCustomerName("John Doe");
        testTransactionResponse.setCustomerEmail("john.doe@example.com");

        testTransactionRequest = new PaymentTransactionRequest();
        testTransactionRequest.setTransactionReference("TXN-001");
        testTransactionRequest.setStatus(TransactionStatus.PAID);
        testTransactionRequest.setAmount(new BigDecimal("100.50"));
        testTransactionRequest.setCustomerIdNumber("1234567890");
    }

    @Test
    void testGetAllTransactions() throws Exception {
        when(paymentTransactionService.getAllTransactions()).thenReturn(Collections.singletonList(testTransactionResponse));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].transactionReference").value("TXN-001"))
                .andExpect(jsonPath("$[0].status").value("PAID"));
    }

    @Test
    void testGetTransactionById_Found() throws Exception {
        when(paymentTransactionService.getTransactionById(1L)).thenReturn(Optional.of(testTransactionResponse));

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transactionReference").value("TXN-001"));
    }

    @Test
    void testGetTransactionById_NotFound() throws Exception {
        when(paymentTransactionService.getTransactionById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTransactionsByCustomerIdNumber() throws Exception {
        when(paymentTransactionService.getTransactionsByCustomerIdNumber("1234567890"))
                .thenReturn(Collections.singletonList(testTransactionResponse));

        mockMvc.perform(get("/api/transactions/customer/1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerIdNumber").value("1234567890"));
    }

    @Test
    void testCreateTransaction() throws Exception {
        when(paymentTransactionService.createTransaction(any(PaymentTransactionRequest.class)))
                .thenReturn(testTransactionResponse);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTransactionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionReference").value("TXN-001"))
                .andExpect(jsonPath("$.status").value("PAID"));

        verify(paymentTransactionService).createTransaction(any(PaymentTransactionRequest.class));
    }

    @Test
    void testCreateTransaction_CustomerNotFound() throws Exception {
        when(paymentTransactionService.createTransaction(any(PaymentTransactionRequest.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTransactionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTransaction_Success() throws Exception {
        when(paymentTransactionService.updateTransaction(eq(1L), any(PaymentTransactionRequest.class)))
                .thenReturn(testTransactionResponse);

        mockMvc.perform(put("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTransactionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionReference").value("TXN-001"));

        verify(paymentTransactionService).updateTransaction(eq(1L), any(PaymentTransactionRequest.class));
    }

    @Test
    void testUpdateTransaction_NotFound() throws Exception {
        when(paymentTransactionService.updateTransaction(eq(999L), any(PaymentTransactionRequest.class)))
                .thenThrow(new TransactionNotFoundException("Transaction not found"));

        mockMvc.perform(put("/api/transactions/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTransactionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTransaction() throws Exception {
        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());

        verify(paymentTransactionService).deleteTransaction(1L);
    }
}

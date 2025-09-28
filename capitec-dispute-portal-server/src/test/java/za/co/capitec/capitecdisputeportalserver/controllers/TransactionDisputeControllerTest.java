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
import za.co.capitec.capitecdisputeportalserver.enums.DisputeStatus;
import za.co.capitec.capitecdisputeportalserver.exception.DisputeNotFoundException;
import za.co.capitec.capitecdisputeportalserver.exception.TransactionNotFoundException;
import za.co.capitec.capitecdisputeportalserver.requests.TransactionDisputeRequest;
import za.co.capitec.capitecdisputeportalserver.responses.TransactionDisputeResponse;
import za.co.capitec.capitecdisputeportalserver.services.TransactionDisputeService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionDisputeController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class TransactionDisputeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionDisputeService transactionDisputeService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionDisputeResponse testDisputeResponse;
    private TransactionDisputeRequest testDisputeRequest;

    @BeforeEach
    void setUp() {
        testDisputeResponse = new TransactionDisputeResponse();
        testDisputeResponse.setId(1L);
        testDisputeResponse.setSubject("Unauthorized transaction");
        testDisputeResponse.setDescription("I did not authorize this transaction");
        testDisputeResponse.setStatus(DisputeStatus.OPEN);
        testDisputeResponse.setTransactionId(1L);
        testDisputeResponse.setTransactionReference("TXN-001");
        testDisputeResponse.setTransactionAmount(new BigDecimal("100.50"));
        testDisputeResponse.setCustomerName("John Doe");
        testDisputeResponse.setCustomerEmail("john.doe@example.com");

        testDisputeRequest = new TransactionDisputeRequest();
        testDisputeRequest.setSubject("Unauthorized transaction");
        testDisputeRequest.setDescription("I did not authorize this transaction");
        testDisputeRequest.setStatus(DisputeStatus.OPEN);
        testDisputeRequest.setTransactionId(1L);
    }

    @Test
    void testGetAllDisputes() throws Exception {
        
        when(transactionDisputeService.getAllDisputes()).thenReturn(Collections.singletonList(testDisputeResponse));

         
        mockMvc.perform(get("/api/disputes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].subject").value("Unauthorized transaction"))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    void testGetDisputeById_Found() throws Exception {
        
        when(transactionDisputeService.getDisputeById(1L)).thenReturn(Optional.of(testDisputeResponse));

         
        mockMvc.perform(get("/api/disputes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subject").value("Unauthorized transaction"));
    }

    @Test
    void testGetDisputeById_NotFound() throws Exception {
        when(transactionDisputeService.getDisputeById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/disputes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetDisputesByTransactionId() throws Exception {
        when(transactionDisputeService.getDisputesByTransactionId(1L))
                .thenReturn(Collections.singletonList(testDisputeResponse));

        mockMvc.perform(get("/api/disputes/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].transactionId").value(1));
    }

    @Test
    void testCreateDispute() throws Exception {
        when(transactionDisputeService.createDispute(any(TransactionDisputeRequest.class)))
                .thenReturn(testDisputeResponse);

        mockMvc.perform(post("/api/disputes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDisputeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subject").value("Unauthorized transaction"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(transactionDisputeService).createDispute(any(TransactionDisputeRequest.class));
    }

    @Test
    void testCreateDispute_TransactionNotFound() throws Exception {
        when(transactionDisputeService.createDispute(any(TransactionDisputeRequest.class)))
                .thenThrow(new TransactionNotFoundException("Transaction not found"));

        mockMvc.perform(post("/api/disputes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDisputeRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateDispute_Success() throws Exception {
        when(transactionDisputeService.updateDispute(eq(1L), any(TransactionDisputeRequest.class)))
                .thenReturn(testDisputeResponse);

        mockMvc.perform(put("/api/disputes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDisputeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Unauthorized transaction"));

        verify(transactionDisputeService).updateDispute(eq(1L), any(TransactionDisputeRequest.class));
    }

    @Test
    void testUpdateDispute_NotFound() throws Exception {
        when(transactionDisputeService.updateDispute(eq(999L), any(TransactionDisputeRequest.class)))
                .thenThrow(new DisputeNotFoundException("Dispute not found"));

        mockMvc.perform(put("/api/disputes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDisputeRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDispute() throws Exception {
        mockMvc.perform(delete("/api/disputes/1"))
                .andExpect(status().isNoContent());

        verify(transactionDisputeService).deleteDispute(1L);
    }
}

package za.co.capitec.capitecdisputeportalserver.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.capitec.capitecdisputeportalserver.entities.Customer;
import za.co.capitec.capitecdisputeportalserver.entities.PaymentTransaction;
import za.co.capitec.capitecdisputeportalserver.entities.TransactionDispute;
import za.co.capitec.capitecdisputeportalserver.enums.DisputeStatus;
import za.co.capitec.capitecdisputeportalserver.enums.TransactionStatus;
import za.co.capitec.capitecdisputeportalserver.exception.DisputeNotFoundException;
import za.co.capitec.capitecdisputeportalserver.exception.TransactionNotFoundException;
import za.co.capitec.capitecdisputeportalserver.repositories.DisputeRepository;
import za.co.capitec.capitecdisputeportalserver.repositories.PaymentTransactionRepository;
import za.co.capitec.capitecdisputeportalserver.requests.TransactionDisputeRequest;
import za.co.capitec.capitecdisputeportalserver.responses.TransactionDisputeResponse;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionDisputeServiceImplTest {

    @Mock
    private DisputeRepository disputeRepository;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @InjectMocks
    private TransactionDisputeServiceImpl transactionDisputeService;

    private TransactionDispute testDispute;
    private PaymentTransaction testTransaction;
    private Customer testCustomer;
    private TransactionDisputeRequest testDisputeRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setIdNumber("1234567890");
        testCustomer.setName("John");
        testCustomer.setSurname("Doe");
        testCustomer.setEmail("john.doe@example.com");

        testTransaction = new PaymentTransaction();
        testTransaction.setId(1L);
        testTransaction.setTransactionReference("TXN-001");
        testTransaction.setStatus(TransactionStatus.PAID);
        testTransaction.setAmount(new BigDecimal("100.50"));
        testTransaction.setCustomer(testCustomer);

        testDispute = new TransactionDispute();
        testDispute.setId(1L);
        testDispute.setSubject("Unauthorized transaction");
        testDispute.setDescription("I did not authorize this transaction");
        testDispute.setStatus(DisputeStatus.OPEN);
        testDispute.setTransaction(testTransaction);

        testDisputeRequest = new TransactionDisputeRequest();
        testDisputeRequest.setSubject("Unauthorized transaction");
        testDisputeRequest.setDescription("I did not authorize this transaction");
        testDisputeRequest.setStatus(DisputeStatus.OPEN);
        testDisputeRequest.setTransactionId(1L);
    }

    @Test
    void testGetAllDisputes() {
        List<TransactionDispute> disputes = Collections.singletonList(testDispute);
        when(disputeRepository.findAll()).thenReturn(disputes);

        List<TransactionDisputeResponse> result = transactionDisputeService.getAllDisputes();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Unauthorized transaction", result.get(0).getSubject());
        assertEquals(DisputeStatus.OPEN, result.get(0).getStatus());
    }

    @Test
    void testGetDisputeById_Found() {
        when(disputeRepository.findById(1L)).thenReturn(Optional.of(testDispute));
        
        Optional<TransactionDisputeResponse> result = transactionDisputeService.getDisputeById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Unauthorized transaction", result.get().getSubject());
    }

    @Test
    void testGetDisputeById_NotFound() {
        when(disputeRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<TransactionDisputeResponse> result = transactionDisputeService.getDisputeById(999L);
        
        assertFalse(result.isPresent());
    }

    @Test
    void testGetDisputesByTransactionId() {
        List<TransactionDispute> disputes = Collections.singletonList(testDispute);
        when(disputeRepository.findByTransaction_Id(1L)).thenReturn(disputes);

        List<TransactionDisputeResponse> result = transactionDisputeService.getDisputesByTransactionId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getTransactionId());
    }

    @Test
    void testCreateDispute_Success() {
        when(paymentTransactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));
        when(disputeRepository.save(any(TransactionDispute.class))).thenReturn(testDispute);

        TransactionDisputeResponse result = transactionDisputeService.createDispute(testDisputeRequest);
        
        assertEquals("Unauthorized transaction", result.getSubject());
        assertEquals(DisputeStatus.OPEN, result.getStatus());
        assertEquals(1L, result.getTransactionId());
        verify(disputeRepository).save(any(TransactionDispute.class));
    }

    @Test
    void testCreateDispute_TransactionNotFound() {
        when(paymentTransactionRepository.findById(999L)).thenReturn(Optional.empty());
        testDisputeRequest.setTransactionId(999L);
         
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionDisputeService.createDispute(testDisputeRequest);
        });
    }

    @Test
    void testUpdateDispute_Success() {
        when(disputeRepository.findById(1L)).thenReturn(Optional.of(testDispute));
        when(disputeRepository.save(any(TransactionDispute.class))).thenReturn(testDispute);

        TransactionDisputeResponse result = transactionDisputeService.updateDispute(1L, testDisputeRequest);

        assertEquals("Unauthorized transaction", result.getSubject());
        verify(disputeRepository).save(any(TransactionDispute.class));
    }

    @Test
    void testUpdateDispute_DisputeNotFound() {
        when(disputeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DisputeNotFoundException.class, () -> {
            transactionDisputeService.updateDispute(999L, testDisputeRequest);
        });
    }

    @Test
    void testDeleteDispute() {
        when(disputeRepository.existsById(1L)).thenReturn(true);
        
        transactionDisputeService.deleteDispute(1L);

        verify(disputeRepository).existsById(1L);
        verify(disputeRepository).deleteById(1L);
    }

    @Test
    void testDeleteDispute_NotFound() {
        when(disputeRepository.existsById(999L)).thenReturn(false);
         
        assertThrows(DisputeNotFoundException.class, () ->
            transactionDisputeService.deleteDispute(999L)
        );
    }
}

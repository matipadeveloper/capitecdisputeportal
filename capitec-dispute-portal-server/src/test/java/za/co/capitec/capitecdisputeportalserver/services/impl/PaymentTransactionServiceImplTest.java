package za.co.capitec.capitecdisputeportalserver.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.capitec.capitecdisputeportalserver.entities.Customer;
import za.co.capitec.capitecdisputeportalserver.entities.PaymentTransaction;
import za.co.capitec.capitecdisputeportalserver.enums.TransactionStatus;
import za.co.capitec.capitecdisputeportalserver.exception.CustomerNotFoundException;
import za.co.capitec.capitecdisputeportalserver.exception.TransactionNotFoundException;
import za.co.capitec.capitecdisputeportalserver.repositories.CustomerRepository;
import za.co.capitec.capitecdisputeportalserver.repositories.PaymentTransactionRepository;
import za.co.capitec.capitecdisputeportalserver.requests.PaymentTransactionRequest;
import za.co.capitec.capitecdisputeportalserver.responses.PaymentTransactionResponse;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionServiceImplTest {

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private PaymentTransactionServiceImpl paymentTransactionService;

    private PaymentTransaction testTransaction;
    private Customer testCustomer;
    private PaymentTransactionRequest testTransactionRequest;

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

        testTransactionRequest = new PaymentTransactionRequest();
        testTransactionRequest.setTransactionReference("TXN-001");
        testTransactionRequest.setStatus(TransactionStatus.PAID);
        testTransactionRequest.setAmount(new BigDecimal("100.50"));
        testTransactionRequest.setCustomerIdNumber("1234567890");
    }

    @Test
    void testGetAllTransactions() {
        List<PaymentTransaction> transactions = Collections.singletonList(testTransaction);
        when(paymentTransactionRepository.findAll()).thenReturn(transactions);

        List<PaymentTransactionResponse> result = paymentTransactionService.getAllTransactions();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("TXN-001", result.get(0).getTransactionReference());
        assertEquals(TransactionStatus.PAID, result.get(0).getStatus());
    }

    @Test
    void testGetTransactionById_Found() {
        when(paymentTransactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));
        
        Optional<PaymentTransactionResponse> result = paymentTransactionService.getTransactionById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("TXN-001", result.get().getTransactionReference());
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(paymentTransactionRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<PaymentTransactionResponse> result = paymentTransactionService.getTransactionById(999L);
        
        assertFalse(result.isPresent());
    }

    @Test
    void testGetTransactionsByCustomerIdNumber() {
        List<PaymentTransaction> transactions = Collections.singletonList(testTransaction);
        when(paymentTransactionRepository.findByCustomer_IdNumber("1234567890")).thenReturn(transactions);

        List<PaymentTransactionResponse> result = paymentTransactionService.getTransactionsByCustomerIdNumber("1234567890");

        assertEquals(1, result.size());
        assertEquals("1234567890", result.get(0).getCustomerIdNumber());
    }

    @Test
    void testCreateTransaction_Success() {
        when(customerRepository.findById("1234567890")).thenReturn(Optional.of(testCustomer));
        when(paymentTransactionRepository.save(any(PaymentTransaction.class))).thenReturn(testTransaction);

        PaymentTransactionResponse result = paymentTransactionService.createTransaction(testTransactionRequest);

        assertEquals("TXN-001", result.getTransactionReference());
        assertEquals(TransactionStatus.PAID, result.getStatus());
        assertEquals("1234567890", result.getCustomerIdNumber());
        verify(paymentTransactionRepository).save(any(PaymentTransaction.class));
    }

    @Test
    void testCreateTransaction_CustomerNotFound() {
        when(customerRepository.findById("nonexistent")).thenReturn(Optional.empty());
        testTransactionRequest.setCustomerIdNumber("nonexistent");
         
        assertThrows(CustomerNotFoundException.class, () -> {
            paymentTransactionService.createTransaction(testTransactionRequest);
        });
    }

    @Test
    void testUpdateTransaction_Success() {
        when(paymentTransactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));
        when(paymentTransactionRepository.save(any(PaymentTransaction.class))).thenReturn(testTransaction);

        PaymentTransactionResponse result = paymentTransactionService.updateTransaction(1L, testTransactionRequest);

        assertEquals("TXN-001", result.getTransactionReference());
        verify(paymentTransactionRepository).save(any(PaymentTransaction.class));
    }

    @Test
    void testUpdateTransaction_TransactionNotFound() {
        when(paymentTransactionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> {
            paymentTransactionService.updateTransaction(999L, testTransactionRequest);
        });
    }

    @Test
    void testDeleteTransaction() {
        when(paymentTransactionRepository.existsById(1L)).thenReturn(true);

        paymentTransactionService.deleteTransaction(1L);

        verify(paymentTransactionRepository).existsById(1L);
        verify(paymentTransactionRepository).deleteById(1L);
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(paymentTransactionRepository.existsById(999L)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () ->
            paymentTransactionService.deleteTransaction(999L)
        );
    }
}

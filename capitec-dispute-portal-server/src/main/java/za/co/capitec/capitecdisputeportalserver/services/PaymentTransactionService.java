package za.co.capitec.capitecdisputeportalserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.capitec.capitecdisputeportalserver.entities.PaymentTransaction;
import za.co.capitec.capitecdisputeportalserver.repositories.PaymentTransactionRepository;
import za.co.capitec.capitecdisputeportalserver.requests.PaymentTransactionRequest;
import za.co.capitec.capitecdisputeportalserver.responses.PaymentTransactionResponse;

import java.util.List;
import java.util.Optional;

public interface PaymentTransactionService {
    List<PaymentTransactionResponse> getAllTransactions();
    Optional<PaymentTransactionResponse> getTransactionById(Long id);
    List<PaymentTransactionResponse> getTransactionsByCustomerIdNumber(String customerIdNumber);
    PaymentTransactionResponse createTransaction(PaymentTransactionRequest transactionRequest);
    PaymentTransactionResponse updateTransaction(Long id, PaymentTransactionRequest transactionRequest);
    void deleteTransaction(Long id);
}

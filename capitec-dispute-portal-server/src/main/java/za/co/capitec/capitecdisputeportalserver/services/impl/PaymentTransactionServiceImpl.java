package za.co.capitec.capitecdisputeportalserver.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.capitec.capitecdisputeportalserver.entities.PaymentTransaction;
import za.co.capitec.capitecdisputeportalserver.entities.Customer;
import za.co.capitec.capitecdisputeportalserver.requests.PaymentTransactionRequest;
import za.co.capitec.capitecdisputeportalserver.responses.PaymentTransactionResponse;
import za.co.capitec.capitecdisputeportalserver.repositories.PaymentTransactionRepository;
import za.co.capitec.capitecdisputeportalserver.repositories.CustomerRepository;
import za.co.capitec.capitecdisputeportalserver.services.PaymentTransactionService;
import za.co.capitec.capitecdisputeportalserver.utils.PaymentTransactionMapper;
import za.co.capitec.capitecdisputeportalserver.exception.CustomerNotFoundException;
import za.co.capitec.capitecdisputeportalserver.exception.TransactionNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public PaymentTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository, CustomerRepository customerRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<PaymentTransactionResponse> getAllTransactions() {
        return paymentTransactionRepository.findAll()
                .stream()
                .map(PaymentTransactionMapper::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentTransactionResponse> getTransactionById(Long id) {
        return paymentTransactionRepository.findById(id)
                .map(PaymentTransactionMapper::convertToResponse);
    }

    @Override
    public List<PaymentTransactionResponse> getTransactionsByCustomerIdNumber(String customerIdNumber) {
        return paymentTransactionRepository.findByCustomer_IdNumber(customerIdNumber)
                .stream()
                .map(PaymentTransactionMapper::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentTransactionResponse createTransaction(PaymentTransactionRequest transactionRequest) {
        Customer customer = customerRepository.findById(transactionRequest.getCustomerIdNumber())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + transactionRequest.getCustomerIdNumber()));

        PaymentTransaction transaction = PaymentTransactionMapper.convertToEntity(transactionRequest, customer);
        PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);
        return PaymentTransactionMapper.convertToResponse(savedTransaction);
    }

    @Override
    public PaymentTransactionResponse updateTransaction(Long id, PaymentTransactionRequest transactionRequest) {
        return paymentTransactionRepository.findById(id)
                .map(transaction -> {
                    Customer customer = transaction.getCustomer();

                    if (!transaction.getCustomer().getIdNumber().equals(transactionRequest.getCustomerIdNumber())) {
                        customer = customerRepository.findById(transactionRequest.getCustomerIdNumber())
                                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + transactionRequest.getCustomerIdNumber()));
                    }

                    PaymentTransactionMapper.updateEntityFromRequest(transaction, transactionRequest, customer);
                    return PaymentTransactionMapper.convertToResponse(paymentTransactionRepository.save(transaction));
                })
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
    }

    @Override
    public void deleteTransaction(Long id) {
        if (!paymentTransactionRepository.existsById(id)) {
            throw new TransactionNotFoundException("Transaction not found with id: " + id);
        }
        paymentTransactionRepository.deleteById(id);
    }
}

package za.co.capitec.capitecdisputeportalserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.capitecdisputeportalserver.requests.PaymentTransactionRequest;
import za.co.capitec.capitecdisputeportalserver.responses.PaymentTransactionResponse;
import za.co.capitec.capitecdisputeportalserver.services.PaymentTransactionService;
import za.co.capitec.capitecdisputeportalserver.utils.ValidatorUtil;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Payment Transactions", description = "APIs for managing payment transactions")
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @Autowired
    public PaymentTransactionController(PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieve all payment transactions")
    public List<PaymentTransactionResponse> getAllTransactions() {
        return paymentTransactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieve a specific payment transaction by its ID")
    public ResponseEntity<PaymentTransactionResponse> getTransactionById(@PathVariable Long id) {
        ValidatorUtil.validatePathId(id);
        return paymentTransactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerIdNumber}")
    @Operation(summary = "Get transactions by customer ID", description = "Retrieve all transactions for a specific customer")
    public List<PaymentTransactionResponse> getTransactionsByCustomerIdNumber(@PathVariable String customerIdNumber) {
        ValidatorUtil.validateCustomerIdNumber(customerIdNumber);
        return paymentTransactionService.getTransactionsByCustomerIdNumber(customerIdNumber);
    }

    @PostMapping
    @Operation(summary = "Create transaction", description = "Create a new payment transaction")
    public PaymentTransactionResponse createTransaction(@RequestBody PaymentTransactionRequest transactionRequest) {
        ValidatorUtil.validateTransactionRequest(transactionRequest);
        return paymentTransactionService.createTransaction(transactionRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction", description = "Update an existing payment transaction")
    public ResponseEntity<PaymentTransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody PaymentTransactionRequest transactionRequest) {
        ValidatorUtil.validatePathId(id);
        ValidatorUtil.validateTransactionRequest(transactionRequest);
        return ResponseEntity.ok(paymentTransactionService.updateTransaction(id, transactionRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Delete a payment transaction")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        ValidatorUtil.validatePathId(id);
        paymentTransactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}

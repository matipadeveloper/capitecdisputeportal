package za.co.capitec.capitecdisputeportalserver.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.capitec.capitecdisputeportalserver.requests.TransactionDisputeRequest;
import za.co.capitec.capitecdisputeportalserver.responses.TransactionDisputeResponse;
import za.co.capitec.capitecdisputeportalserver.services.TransactionDisputeService;
import za.co.capitec.capitecdisputeportalserver.utils.ValidatorUtil;

import java.util.List;

@RestController
@RequestMapping("/api/disputes")
@Tag(name = "Transaction Disputes", description = "APIs for managing transaction disputes")
@SecurityRequirement(name = "Bearer Authentication")
public class TransactionDisputeController {

    private final TransactionDisputeService transactionDisputeService;

    @Autowired
    public TransactionDisputeController(TransactionDisputeService transactionDisputeService) {
        this.transactionDisputeService = transactionDisputeService;
    }

    @GetMapping
    @Operation(summary = "Get all disputes", description = "Retrieve all transaction disputes")
    public List<TransactionDisputeResponse> getAllDisputes() {
        return transactionDisputeService.getAllDisputes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dispute by ID", description = "Retrieve a specific dispute by its ID")
    public ResponseEntity<TransactionDisputeResponse> getDisputeById(@PathVariable Long id) {
        ValidatorUtil.validatePathId(id);
        return transactionDisputeService.getDisputeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get disputes by transaction ID", description = "Retrieve all disputes for a specific transaction")
    public List<TransactionDisputeResponse> getDisputesByTransactionId(@PathVariable Long transactionId) {
        ValidatorUtil.validatePathId(transactionId);
        return transactionDisputeService.getDisputesByTransactionId(transactionId);
    }

    @PostMapping
    @Operation(summary = "Create dispute", description = "Create a new transaction dispute")
    public TransactionDisputeResponse createDispute(@RequestBody TransactionDisputeRequest disputeRequest) {
        ValidatorUtil.validateDisputeRequest(disputeRequest);
        return transactionDisputeService.createDispute(disputeRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update dispute", description = "Update an existing transaction dispute")
    public ResponseEntity<TransactionDisputeResponse> updateDispute(@PathVariable Long id, @RequestBody TransactionDisputeRequest disputeRequest) {
        ValidatorUtil.validatePathId(id);
        ValidatorUtil.validateDisputeRequest(disputeRequest);
        return ResponseEntity.ok(transactionDisputeService.updateDispute(id, disputeRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete dispute", description = "Delete a transaction dispute")
    public ResponseEntity<Void> deleteDispute(@PathVariable Long id) {
        ValidatorUtil.validatePathId(id);
        transactionDisputeService.deleteDispute(id);
        return ResponseEntity.noContent().build();
    }

}

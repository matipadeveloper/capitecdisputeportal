package za.co.capitec.capitecdisputeportalserver.services;

import za.co.capitec.capitecdisputeportalserver.requests.TransactionDisputeRequest;
import za.co.capitec.capitecdisputeportalserver.responses.TransactionDisputeResponse;
import java.util.List;
import java.util.Optional;

public interface TransactionDisputeService {
    List<TransactionDisputeResponse> getAllDisputes();
    Optional<TransactionDisputeResponse> getDisputeById(Long id);
    List<TransactionDisputeResponse> getDisputesByTransactionId(Long transactionId);
    TransactionDisputeResponse createDispute(TransactionDisputeRequest disputeRequest);
    TransactionDisputeResponse updateDispute(Long id, TransactionDisputeRequest disputeRequest);
    void deleteDispute(Long id);
}

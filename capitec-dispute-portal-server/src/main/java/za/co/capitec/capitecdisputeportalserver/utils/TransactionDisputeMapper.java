package za.co.capitec.capitecdisputeportalserver.utils;

import za.co.capitec.capitecdisputeportalserver.entities.TransactionDispute;
import za.co.capitec.capitecdisputeportalserver.entities.PaymentTransaction;
import za.co.capitec.capitecdisputeportalserver.requests.TransactionDisputeRequest;
import za.co.capitec.capitecdisputeportalserver.responses.TransactionDisputeResponse;

public class TransactionDisputeMapper {

    public static TransactionDisputeResponse convertToResponse(TransactionDispute dispute) {
        TransactionDisputeResponse response = new TransactionDisputeResponse();
        response.setId(dispute.getId());
        response.setSubject(dispute.getSubject());
        response.setDescription(dispute.getDescription());
        response.setStatus(dispute.getStatus());

        if (dispute.getTransaction() != null) {
            PaymentTransaction transaction = dispute.getTransaction();
            response.setTransactionId(transaction.getId());
            response.setTransactionReference(transaction.getTransactionReference());
            response.setTransactionAmount(transaction.getAmount());

            if (transaction.getCustomer() != null) {
                response.setCustomerName(transaction.getCustomer().getName() + " " + transaction.getCustomer().getSurname());
                response.setCustomerEmail(transaction.getCustomer().getEmail());
            }
        }

        return response;
    }

    public static TransactionDispute convertToEntity(TransactionDisputeRequest request, PaymentTransaction transaction) {
        TransactionDispute dispute = new TransactionDispute();
        dispute.setSubject(request.getSubject());
        dispute.setDescription(request.getDescription());
        dispute.setStatus(request.getStatus());
        dispute.setTransaction(transaction);
        return dispute;
    }

    public static void updateEntityFromRequest(TransactionDispute dispute, TransactionDisputeRequest request, PaymentTransaction transaction) {
        dispute.setSubject(request.getSubject());
        dispute.setDescription(request.getDescription());
        dispute.setStatus(request.getStatus());
        dispute.setTransaction(transaction);
    }

}

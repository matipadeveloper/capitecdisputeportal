package za.co.capitec.capitecdisputeportalserver.utils;

import za.co.capitec.capitecdisputeportalserver.entities.PaymentTransaction;
import za.co.capitec.capitecdisputeportalserver.entities.Customer;
import za.co.capitec.capitecdisputeportalserver.requests.PaymentTransactionRequest;
import za.co.capitec.capitecdisputeportalserver.responses.PaymentTransactionResponse;

public class PaymentTransactionMapper {

    public static PaymentTransactionResponse convertToResponse(PaymentTransaction transaction) {
        PaymentTransactionResponse response = new PaymentTransactionResponse();
        response.setId(transaction.getId());
        response.setTransactionReference(transaction.getTransactionReference());
        response.setStatus(transaction.getStatus());
        response.setAmount(transaction.getAmount());
        response.setDescription(transaction.getDescription());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setMerchantName(transaction.getMerchantName());

        if (transaction.getCustomer() != null) {
            response.setCustomerIdNumber(transaction.getCustomer().getIdNumber());
            response.setCustomerName(transaction.getCustomer().getName() + " " + transaction.getCustomer().getSurname());
            response.setCustomerEmail(transaction.getCustomer().getEmail());
        }

        return response;
    }

    public static PaymentTransaction convertToEntity(PaymentTransactionRequest request, Customer customer) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionReference(request.getTransactionReference());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setMerchantName(request.getMerchantName());
        transaction.setStatus(request.getStatus());
        transaction.setCustomer(customer);
        return transaction;
    }

    public static void updateEntityFromRequest(PaymentTransaction transaction, PaymentTransactionRequest request, Customer customer) {
        transaction.setTransactionReference(request.getTransactionReference());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setMerchantName(request.getMerchantName());
        transaction.setStatus(request.getStatus());
        transaction.setCustomer(customer);
    }

}

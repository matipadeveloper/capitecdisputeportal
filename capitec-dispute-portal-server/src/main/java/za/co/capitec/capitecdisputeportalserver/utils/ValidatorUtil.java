package za.co.capitec.capitecdisputeportalserver.utils;

import za.co.capitec.capitecdisputeportalserver.exception.InvalidRequestException;
import za.co.capitec.capitecdisputeportalserver.requests.CustomerRequest;
import za.co.capitec.capitecdisputeportalserver.requests.LoginRequest;
import za.co.capitec.capitecdisputeportalserver.requests.PaymentTransactionRequest;
import za.co.capitec.capitecdisputeportalserver.requests.TransactionDisputeRequest;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static void validatePathId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidRequestException("Invalid ID: ID must be a positive number");
        }
    }


    public static void validateCustomerIdNumber(String customerIdNumber) {
        if (customerIdNumber == null || customerIdNumber.trim().isEmpty()) {
            throw new InvalidRequestException("Customer ID number cannot be null or empty");
        }
        if (customerIdNumber.length() != 10 || !customerIdNumber.matches("\\d+")) {
            throw new InvalidRequestException("Customer ID number must be exactly 10 digits");
        }
    }

    public static void validateTransactionRequest(PaymentTransactionRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Transaction request cannot be null");
        }

        if (request.getTransactionReference() == null || request.getTransactionReference().trim().isEmpty()) {
            throw new InvalidRequestException("Transaction reference cannot be null or empty");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Transaction amount must be greater than zero");
        }

        if (request.getCustomerIdNumber() == null || request.getCustomerIdNumber().trim().isEmpty()) {
            throw new InvalidRequestException("Customer ID number cannot be null or empty");
        }

        validateCustomerIdNumber(request.getCustomerIdNumber());
    }

    public static void validateCustomerRequest(CustomerRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Customer request cannot be null");
        }

        validateCustomerIdNumber(request.getIdNumber());

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Customer name cannot be null or empty");
        }

        if (request.getSurname() == null || request.getSurname().trim().isEmpty()) {
            throw new InvalidRequestException("Customer surname cannot be null or empty");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new InvalidRequestException("Customer email cannot be null or empty");
        }

        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new InvalidRequestException("Invalid email format");
        }
    }

    public static void validateDisputeRequest(TransactionDisputeRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Dispute request cannot be null");
        }

        if (request.getSubject() == null || request.getSubject().trim().isEmpty()) {
            throw new InvalidRequestException("Dispute subject cannot be null or empty");
        }

        if (request.getSubject().length() > 200) {
            throw new InvalidRequestException("Dispute subject cannot exceed 200 characters");
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new InvalidRequestException("Dispute description cannot be null or empty");
        }

        if (request.getTransactionId() == null || request.getTransactionId() <= 0) {
            throw new InvalidRequestException("Transaction ID must be a positive number");
        }
    }

    public static void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Login request cannot be null");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new InvalidRequestException("Username cannot be null or empty");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new InvalidRequestException("Password cannot be null or empty");
        }

        if (request.getUsername().length() < 3 || request.getUsername().length() > 50) {
            throw new InvalidRequestException("Username must be between 3 and 50 characters");
        }
    }
}

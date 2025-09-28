package za.co.capitec.capitecdisputeportalserver.requests;

import lombok.Data;
import za.co.capitec.capitecdisputeportalserver.enums.DisputeStatus;

@Data
public class TransactionDisputeRequest {
    private String subject;
    private String description;
    private DisputeStatus status;
    private Long transactionId;
}

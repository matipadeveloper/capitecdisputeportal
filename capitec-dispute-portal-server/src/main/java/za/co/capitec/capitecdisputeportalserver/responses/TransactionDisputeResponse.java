package za.co.capitec.capitecdisputeportalserver.responses;

import lombok.Data;
import za.co.capitec.capitecdisputeportalserver.enums.DisputeStatus;
import java.math.BigDecimal;

@Data
public class TransactionDisputeResponse {
    private Long id;
    private String subject;
    private String description;
    private DisputeStatus status;
    private Long transactionId;
    private String transactionReference;
    private BigDecimal transactionAmount;
    private String customerName;
    private String customerEmail;
}

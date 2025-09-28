package za.co.capitec.capitecdisputeportalserver.requests;

import lombok.Data;
import za.co.capitec.capitecdisputeportalserver.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentTransactionRequest {
    private String transactionReference;
    private TransactionStatus status;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private String merchantName;
    private String customerIdNumber;
}

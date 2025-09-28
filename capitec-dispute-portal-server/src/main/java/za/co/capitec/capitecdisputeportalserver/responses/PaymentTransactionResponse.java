package za.co.capitec.capitecdisputeportalserver.responses;

import lombok.Data;
import za.co.capitec.capitecdisputeportalserver.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentTransactionResponse {
    private Long id;
    private String transactionReference;
    private TransactionStatus status;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private String merchantName;
    private String customerIdNumber;
    private String customerName;
    private String customerEmail;
}

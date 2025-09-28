package za.co.capitec.capitecdisputeportalserver.entities;

import jakarta.persistence.*;
import lombok.Data;
import za.co.capitec.capitecdisputeportalserver.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment_transaction")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "merchant_name")
    private String merchantName;

    @ManyToOne
    @JoinColumn(name = "customer_id_number", referencedColumnName = "id_number")
    private Customer customer;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TransactionDispute dispute;

}

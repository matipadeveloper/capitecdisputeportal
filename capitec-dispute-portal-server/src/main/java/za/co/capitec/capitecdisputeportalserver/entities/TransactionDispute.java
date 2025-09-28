package za.co.capitec.capitecdisputeportalserver.entities;

import jakarta.persistence.*;
import lombok.Data;
import za.co.capitec.capitecdisputeportalserver.enums.DisputeStatus;

@Entity
@Data
@Table(name = "transaction_dispute")
public class TransactionDispute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_dispute_id")
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DisputeStatus status;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private PaymentTransaction transaction;

}

package za.co.capitec.capitecdisputeportalserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.capitec.capitecdisputeportalserver.entities.PaymentTransaction;

import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    List<PaymentTransaction> findByCustomer_IdNumber(String idNumber);
}

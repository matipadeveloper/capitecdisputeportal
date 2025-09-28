package za.co.capitec.capitecdisputeportalserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.capitec.capitecdisputeportalserver.entities.TransactionDispute;

import java.util.List;

public interface DisputeRepository extends JpaRepository<TransactionDispute, Long> {
    List<TransactionDispute> findByTransaction_Id(Long transactionId);
}

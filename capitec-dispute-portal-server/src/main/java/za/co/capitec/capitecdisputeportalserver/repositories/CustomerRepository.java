package za.co.capitec.capitecdisputeportalserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.capitec.capitecdisputeportalserver.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}

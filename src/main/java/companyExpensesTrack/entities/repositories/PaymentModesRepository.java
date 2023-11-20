package companyExpensesTrack.entities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import companyExpensesTrack.entities.PaymentMode;
@Repository
public interface PaymentModesRepository extends JpaRepository<PaymentMode,String>{

}

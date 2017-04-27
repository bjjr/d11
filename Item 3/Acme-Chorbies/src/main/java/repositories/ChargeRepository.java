
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Charge;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Integer> {

	@Query("select c from Charge c where c.user.id = ?1")
	Collection<Charge> findChargesByUser(int userId);

	@Query("select sum(c.amount) from Charge c where c.paid = true")
	Double totalBenefit();

	@Query("select sum(c.amount) from Charge c where c.paid = false")
	Double totalDue();

	@Query("select sum(c.amount) from Charge c")
	Double theoreticalBenefit();

}

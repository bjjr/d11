
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Fee;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Integer> {

	@Query("select f.value from Fee f where f.feeType = 1")
	Double getFeeManagers();

	@Query("select f.value from Fee f where f.feeType = 2")
	Double getFeeChorbies();

}

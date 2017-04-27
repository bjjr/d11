
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Charge;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Integer> {

}

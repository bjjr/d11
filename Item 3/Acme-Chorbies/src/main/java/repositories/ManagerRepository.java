
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m from Manager m where m.userAccount.id = ?1")
	Manager findByUserAccountId(int userAccountId);

	@Query("select e.manager from Event e group by e.manager order by count(e) desc")
	Collection<Manager> findManagersSortedByNumberEvents();

	@Query("select c.user.name, sum(c.amount) from Charge c where c.paid = false and type(c.user)=domain.Manager group by c.user")
	List<String[]> findManagersWithDebts();

}

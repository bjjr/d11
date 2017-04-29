
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Broadcast;
import domain.Chorbi;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast, Integer> {

	@Query("select b from Broadcast b where ?1 MEMBER OF b.uninformedChorbies")
	public Collection<Broadcast> findNonReceivedBroadcasts(Chorbi chorbi);
}

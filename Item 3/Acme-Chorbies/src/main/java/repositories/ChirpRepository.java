
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	@Query("select c from Chirp c where c.copy = false and c.sender.id = ?1")
	Collection<Chirp> findChirpsSent(int userId);

	@Query("select c from Chirp c where c.copy = true and c.recipient.id = ?1")
	Collection<Chirp> findChirpsReceived(int userId);

	@Query("select count(cp)*1.0/(select count(c) from Chorbi c) from Chirp cp where cp.copy is true")
	Double findAvgChirpsRecPerChorbi();

	@Query("select count(cp) from Chirp cp group by cp.recipient order by count(cp) desc")
	Collection<Long> findMaxChirpsRecPerChorbi();

	@Query("select count(cp) from Chirp cp group by cp.recipient order by count(cp) asc")
	Collection<Long> findMinChirpsRecPerChorbi();

	@Query("select count(cp)*1.0/(select count(c) from Chorbi c) from Chirp cp where cp.copy is true")
	Double findAvgChirpsSendPerChorbi();

	@Query("select count(cp) from Chirp cp group by cp.sender order by count(cp) desc")
	Collection<Long> findMaxChirpsSendPerChorbi();

	@Query("select count(cp) from Chirp cp group by cp.sender order by count(cp) asc")
	Collection<Long> findMinChirpsSendPerChorbi();

	@Query("select cp.sender.id from Chirp cp group by cp.sender.id")
	List<Integer> findAllChorbiesWhoSendChirp();

	@Query("select cp.recipient.id from Chirp cp group by cp.recipient.id")
	List<Integer> findAllChorbiesWhoReceiveChirp();

}


package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chorbi;
import domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select e from Event e where ((SUBSTRING(e.moment, 1, 4) == SUBSTRING(CURRENT_DATE, 1, 4)) and ((SUBSTRING(e.moment, 6, 2) - SUBSTRING(CURRENT_DATE, 6, 2) < 1) and (e.availableSeats > 0))")
	Collection<Event> findEventsLessOneMonthSeatsAvailables();

	@Query("select c from Chorbi c join c.events e where e.id = ?1")
	Collection<Chorbi> findChorbiesByEvent(int eventId);
}

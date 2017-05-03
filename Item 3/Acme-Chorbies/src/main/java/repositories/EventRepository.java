
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chorbi;
import domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select e from Event e where FUNCTION('DATEDIFF', e.moment, CURRENT_DATE) <= 30 and FUNCTION('DATEDIFF', e.moment, CURRENT_DATE) > 0 and e.availableSeats > 0")
	Collection<Event> findEventsLessOneMonthSeatsAvailables();

	@Query("select c from Chorbi c join c.events e where e.id = ?1")
	Collection<Chorbi> findChorbiesByEvent(int eventId);

	@Query("select count(c) from Chorbi c join c.events e where e.id = ?1")
	Integer findNumChorbiesByEvent(int eventId);

	@Query("select e from Event e where e.manager.id = ?1")
	Collection<Event> findManagerEvents(int managerId);
}

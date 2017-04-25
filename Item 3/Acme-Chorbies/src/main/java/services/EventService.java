
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.EventRepository;
import domain.Event;

@Service
@Transactional
public class EventService {

	// Managed repository --------------------------------------

	@Autowired
	private EventRepository	eventRepository;

	// Supporting services

	@Autowired
	private ActorService	actorService;


	// Constructor ---------------------------------------------

	public EventService() {
		super();
	}

	// Simple CRUD methods -------------------------------------

	public Collection<Event> findAll() {
		Assert.isTrue((this.actorService.checkAuthority("MANAGER"))
			|| (!(this.actorService.checkAuthority("MANAGER") && this.actorService.checkAuthority("CHORBI") && this.actorService.checkAuthority("ADMIN") && this.actorService.checkAuthority("BANNED"))));
		Collection<Event> res;

		res = this.eventRepository.findAll();
		Assert.notNull(res);

		return res;
	}

	public Event findOne(final int eventId) {
		Assert.isTrue(eventId != 0);

		Event res;

		res = this.eventRepository.findOne(eventId);
		Assert.notNull(res);

		return res;

	}

	//Other business methods

	public Collection<Event> findEventsLessOneMonthSeatsAvailables() {
		Collection<Event> res;

		res = this.eventRepository.findEventsLessOneMonthSeatsAvailables();
		Assert.notNull(res);

		return res;
	}
}

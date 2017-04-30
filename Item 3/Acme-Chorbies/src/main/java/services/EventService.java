
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import domain.Broadcast;
import domain.Chorbi;
import domain.Event;
import domain.Manager;

@Service
@Transactional
public class EventService {

	// Managed repository ---------------------------

	@Autowired
	private EventRepository		eventRepository;

	// Supporting services --------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private BroadcastService	broadcastService;

	// Validator -----------------------------------

	@Autowired
	private Validator			validator;


	// Constructor ---------------------------------

	public EventService() {
		super();
	}

	// Simple CRUD methods --------------------------

	public Event create() {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));

		Event res;

		res = new Event();
		res.setAvailableSeats(0);

		return res;
	}

	/*
	 * Any change made to an event must be notified to the chorbies who
	 * have registered. A chirp must be sent from event's manager's account.
	 */

	public Event save(final Event event) {
		Event res;
		Broadcast broadcast;

		if (event.getId() != 0) {
			broadcast = this.getModificationsBroadcast(event);
			this.broadcastService.update(broadcast);
		} else {
			Manager principal;
			principal = this.managerService.findByPrincipal();
			event.setAvailableSeats(event.getSeats());
			event.setManager(principal);
		}

		res = this.eventRepository.save(event);

		return res;
	}

	/*
	 * When an event is cancelled/deleted chorbies registered to the event
	 * will receive a chirp.
	 */

	public void delete(final Event event) {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));

		Broadcast broadcast;

		this.checkManager(event);

		this.eventRepository.delete(event);

		broadcast = this.getDeletionBroadcast(event);
		this.broadcastService.update(broadcast);
	}

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

	// Other business methods -----------------------

	public Event reconstruct(final Event event, final BindingResult bindingResult) {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));

		Event res, original;
		Manager manager;
		int availableSeats, attendees;

		original = this.findOne(event.getId());

		this.checkManager(event);

		manager = this.managerService.findByPrincipal();
		attendees = original.getSeats() - original.getAvailableSeats();

		if (event.getSeats() < attendees) // New number of seats must be greater or equal to the number of attendees
			bindingResult.rejectValue("seats", "event.error.seats");

		res = event;
		availableSeats = event.getSeats() - attendees;

		res.setManager(manager);
		res.setAvailableSeats(availableSeats);

		this.validator.validate(res, bindingResult);

		return res;
	}

	public Event findOneToEdit(final int eventId) {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));
		Assert.isTrue(eventId != 0);

		Event res;

		res = this.findOne(eventId);

		this.checkManager(res);

		return res;
	}

	/**
	 * Avoid post-hacking cheking if the event belongs to the principal.
	 * Edition forms contains the id as a hidden attribute.
	 * 
	 * @param event
	 *            The event that is going to be modified/deleted.
	 */

	private void checkManager(final Event event) {
		Event retrieved;
		Manager principal;

		retrieved = this.findOne(event.getId());
		principal = this.managerService.findByPrincipal();

		Assert.isTrue(retrieved.getManager().equals(principal));
	}

	/**
	 * Creates a broadcast listing the changes made to an event
	 * for chorbies who have registered to that event.
	 * 
	 * @param event
	 *            The event that has been modified.
	 * @return The notification chirp to be sended.
	 */

	private Broadcast getModificationsBroadcast(final Event event) {
		String subject, text;
		Event original;
		Broadcast res;

		res = this.broadcastService.create();
		original = this.eventRepository.findOne(event.getId());

		subject = "The event / El evento: " + event.getTitle() + " has been modified / ha sido modificado";
		text = "This has been changed / Esto ha cambiado:\n";

		if (!original.getMoment().equals(event.getMoment()))
			text += "· Date / Fecha\n";
		if (!original.getDescription().equals(event.getDescription()))
			text += "· Description / Descripción\n";
		if (!original.getPicture().equals(event.getPicture()))
			text += "· Picture / Imagen\n";
		if (original.getSeats() != event.getSeats())
			text += "· Seats / Plazas";

		res.setSubject(subject);
		res.setText(text);
		res.setManager(event.getManager());

		return res;
	}

	/**
	 * Creates a broadcast informing chorbies who have registered to the event
	 * that it's been cancelled.
	 * 
	 * @param event
	 *            The cancelled event.
	 * @return The notification chirp.
	 */

	private Broadcast getDeletionBroadcast(final Event event) {
		String subject, text;
		Broadcast res;

		res = this.broadcastService.create();

		subject = "The event / El evento: " + event.getTitle() + "has been cancelled / ha sido cancelado";
		text = "Sorry for any inconvenience / Disculpe las molestias";

		res.setSubject(subject);
		res.setText(text);
		res.setManager(event.getManager());

		return res;
	}

	public Collection<Chorbi> findChorbiesByEvent(final int eventId) {
		Assert.isTrue(eventId != 0);
		Collection<Chorbi> res;

		res = this.eventRepository.findChorbiesByEvent(eventId);

		return res;
	}

	public Collection<Event> findEventsLessOneMonthSeatsAvailables() {
		Collection<Event> res;

		res = this.eventRepository.findEventsLessOneMonthSeatsAvailables();
		Assert.notNull(res);

		return res;
	}
}

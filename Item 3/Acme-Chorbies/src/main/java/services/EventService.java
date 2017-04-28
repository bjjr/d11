
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import domain.Chirp;
import domain.Chorbi;
import domain.Event;
import domain.Manager;

@Service
@Transactional
public class EventService {

	// Managed repository ---------------------------

	@Autowired
	private EventRepository	eventRepository;

	// Supporting services --------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ManagerService	managerService;

	// Validator -----------------------------------

	@Autowired
	private Validator		validator;


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
		Chirp chirp;

		if (event.getId() != 0) {
			chirp = this.getModificationsChirp(event);
			this.sendNotificationChirp(chirp, event);
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

		Chirp chirp;

		this.checkManager(event);

		this.eventRepository.delete(event);

		chirp = this.getDeletionChirp(event);
		this.sendNotificationChirp(chirp, event);
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
	 * Create a notification chirp listing the changes made to an event
	 * for chorbies who have registered to that event.
	 * 
	 * @param event
	 *            The event that has been modified.
	 * @return The notification chirp to be sended.
	 */

	private Chirp getModificationsChirp(final Event event) {
		String subject, text;
		Event original;
		Chirp res;
		Manager manager;

		res = this.chirpService.create();
		original = this.eventRepository.findOne(event.getId());
		manager = this.managerService.findByPrincipal();

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
		res.setSender(manager);
		res.setAttachments(new HashSet<String>());

		return res;
	}

	/**
	 * Create a notification chirp informing chorbies who have registered to the event
	 * that it's going to be cancelled.
	 * 
	 * @param event
	 *            The cancelled event.
	 * @return The notification chirp.
	 */

	private Chirp getDeletionChirp(final Event event) {
		String subject, text;
		Chirp res;
		Manager manager;

		res = this.chirpService.create();
		manager = this.managerService.findByPrincipal();

		subject = "The event / El evento: " + event.getTitle() + "has been cancelled / ha sido cancelado";
		text = "Sorry for any inconvenience / Disculpe las molestias";

		res.setSubject(subject);
		res.setText(text);
		res.setSender(manager);
		res.setAttachments(new HashSet<String>());

		return res;
	}

	/**
	 * Send a notification chirp to chorbies registered to an event.
	 * 
	 * @param chirp
	 *            The notification chirp
	 * @param event
	 *            The event that is going to be modified or deleted.
	 */

	private void sendNotificationChirp(final Chirp chirp, final Event event) {
		Collection<Chorbi> chorbies;
		Chirp saved;

		chorbies = this.findChorbiesByEvent(event.getId());

		for (final Chorbi c : chorbies) {
			chirp.setRecipient(c);
			saved = this.chirpService.send(chirp);
			this.chirpService.saveCopy(saved);
		}
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

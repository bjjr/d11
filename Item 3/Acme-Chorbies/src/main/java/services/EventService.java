
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

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


	// Constructor ---------------------------------

	public EventService() {
		super();
	}

	// Simple CRUD methods --------------------------

	public Event create() {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));

		Event res;

		res = new Event();

		return res;
	}

	/*
	 * Any change made to an event must be notified to the chorbies who
	 * have registered. A chirp must be sent from event's manager's account.
	 */

	public Event save(final Event event) {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));

		Event res;
		Chirp chirp;

		res = this.eventRepository.save(event);

		if (event.getId() != 0) {
			chirp = this.getModificationsChirp(event);
			this.sendNotificationChirp(chirp, event);
		}

		return res;
	}

	/*
	 * When an event is cancelled/deleted chorbies registered to the event
	 * will receive a chirp.
	 */

	public void delete(final Event event) {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));

		Chirp chirp;

		this.eventRepository.delete(event);

		chirp = this.getDeletionChirp(event);
		this.sendNotificationChirp(chirp, event);
	}

	// Other business methods -----------------------

	public Event reconstruct(final Event event, final BindingResult bindingResult) {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));

		Event res;
		Manager manager;

		res = event;
		manager = this.managerService.findByPrincipal();
		res.setManager(manager);

		return res;
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
}

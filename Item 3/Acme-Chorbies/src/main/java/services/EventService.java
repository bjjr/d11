
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import domain.Broadcast;
import domain.Charge;
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

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private ChargeService		chargeService;

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
		Manager manager;

		manager = this.managerService.findByPrincipal();

		Assert.notNull(manager.getCreditCard(), "You must register a credit card");
		Assert.isTrue(this.creditCardService.isCreditCardDateValid(manager.getCreditCard()));

		res = new Event();
		res.setAvailableSeats(0);
		res.setManager(manager);

		return res;
	}

	/*
	 * Any change made to an event must be notified to the chorbies who
	 * have registered. A chirp must be sent from event's manager's account.
	 */

	public Event save(final Event event) {
		Assert.notNull(event);

		Event res;
		Broadcast broadcast;

		broadcast = null;

		if (event.getId() == 0) {
			Charge charge;
			Manager principal;

			principal = this.managerService.findByPrincipal();
			charge = this.chargeService.create(principal);

			this.chargeService.save(charge);
		}

		if (this.actorService.checkAuthority("MANAGER") && event.getId() != 0)
			broadcast = this.getModificationsBroadcast(event);

		res = this.eventRepository.save(event);

		if (this.actorService.checkAuthority("MANAGER") && event.getId() != 0)
			this.broadcastService.update(broadcast);

		return res;
	}

	public void flush() {
		this.eventRepository.flush();
	}

	/*
	 * When an event is cancelled/deleted chorbies registered to the event
	 * will receive a chirp.
	 */

	public void delete(final Event event) {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));
		Assert.notNull(event);

		Broadcast broadcast;
		Event original;
		Collection<Chorbi> attendees;

		this.checkManager(event);

		original = this.findOne(event.getId());
		broadcast = this.getDeletionBroadcast(original);
		this.broadcastService.update(broadcast);

		attendees = this.findChorbiesByEvent(original.getId());

		for (final Chorbi c : attendees) {
			c.getEvents().remove(original);
			this.chorbiService.save(c);
		}

		this.eventRepository.delete(original);
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
		Assert.notNull(event);

		Manager manager;
		Event res;
		Date currentDate;

		currentDate = new Date(System.currentTimeMillis());
		manager = this.managerService.findByPrincipal();
		res = event;

		if (event.getId() == 0)
			res.setAvailableSeats(event.getSeats());
		else {
			Event original;
			int availableSeats, attendees;

			original = this.findOne(event.getId());

			this.checkManager(event);

			attendees = original.getSeats() - original.getAvailableSeats();

			if (event.getSeats() < attendees) // New number of seats must be greater or equal to the number of attendees
				bindingResult.rejectValue("seats", "event.error.seats");

			availableSeats = event.getSeats() - attendees;
			res.setAvailableSeats(availableSeats);
		}

		if (res.getMoment() != null)
			if (res.getMoment().before(currentDate))
				bindingResult.rejectValue("moment", "javax.validation.constraints.Future.message");

		res.setManager(manager);

		this.validator.validate(res, bindingResult);

		return res;
	}

	public Event findOneToEdit(final int eventId) {
		Assert.isTrue(this.actorService.checkAuthority("MANAGER"));
		Assert.isTrue(eventId != 0);

		Event res;
		Date currentDate;

		res = this.findOne(eventId);
		currentDate = new Date(System.currentTimeMillis());

		Assert.isTrue(res.getMoment().after(currentDate), "Past events cannot be modified");

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
		Assert.notNull(event);

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

	public Broadcast getModificationsBroadcast(final Event event) {
		Assert.notNull(event);

		String subject, text;
		Event original;
		Broadcast res;
		Collection<Chorbi> uninformedChorbies;

		res = this.broadcastService.create();
		original = this.eventRepository.findOne(event.getId());
		uninformedChorbies = this.findChorbiesByEvent(event.getId());

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
		res.setUninformedChorbies(uninformedChorbies);

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

	public Broadcast getDeletionBroadcast(final Event event) {
		Assert.notNull(event);

		String subject, text;
		Broadcast res;
		Collection<Chorbi> uninformedChorbies;
		Event original;

		res = this.broadcastService.create();
		original = this.findOne(event.getId());

		subject = "The event / El evento: " + event.getTitle() + "has been cancelled / ha sido cancelado";
		text = "Sorry for any inconvenience / Disculpe las molestias";
		uninformedChorbies = this.findChorbiesByEvent(event.getId());

		res.setSubject(subject);
		res.setText(text);
		res.setManager(original.getManager());
		res.setUninformedChorbies(uninformedChorbies);

		return res;
	}

	public void registerChorbi(final Event event) {
		Assert.notNull(event);
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));

		Chorbi chorbi;
		Date currentDate;

		chorbi = this.chorbiService.findByPrincipal();
		currentDate = new Date(System.currentTimeMillis());

		Assert.isTrue(!chorbi.getEvents().contains(event), "You are already registered to this event");
		Assert.isTrue(event.getMoment().after(currentDate), "You can't register to past events");
		Assert.isTrue(event.getAvailableSeats() > 0);

		chorbi.getEvents().add(event);
		event.setAvailableSeats(event.getAvailableSeats() - 1);

		this.chorbiService.save(chorbi);
		this.save(event);
	}

	public void unregisterChorbi(final Event event) {
		Assert.notNull(event);
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));

		Chorbi chorbi;
		Date currentDate;

		chorbi = this.chorbiService.findByPrincipal();
		currentDate = new Date(System.currentTimeMillis());

		Assert.isTrue(chorbi.getEvents().contains(event), "You were not registered to this event");
		Assert.isTrue(event.getMoment().after(currentDate), "You can't unregister from past events");

		chorbi.getEvents().remove(event);
		event.setAvailableSeats(event.getAvailableSeats() + 1);

		this.chorbiService.save(chorbi);
		this.save(event);
	}

	public Collection<Chorbi> findChorbiesByEvent(final int eventId) {
		Assert.isTrue(eventId != 0);
		Collection<Chorbi> res;

		res = this.eventRepository.findChorbiesByEvent(eventId);

		return res;
	}

	public Collection<Chorbi> findChorbiesByManagerEvents(final Manager manager) {
		final Collection<Event> events = this.findManagerEvents(manager.getId());

		final Collection<Chorbi> allChorbiesByManagerEvent = new HashSet<>();

		for (final Event e : events)
			allChorbiesByManagerEvent.addAll(this.findChorbiesByEvent(e.getId()));

		return allChorbiesByManagerEvent;
	}
	public Collection<Event> findEventsLessOneMonthSeatsAvailables() {
		Collection<Event> res;

		res = this.eventRepository.findEventsLessOneMonthSeatsAvailables();
		Assert.notNull(res);

		return res;
	}

	public Collection<Event> findManagerEvents(final int managerId) {
		Assert.isTrue(managerId != 0);

		Collection<Event> res;

		res = this.eventRepository.findManagerEvents(managerId);

		return res;
	}
}

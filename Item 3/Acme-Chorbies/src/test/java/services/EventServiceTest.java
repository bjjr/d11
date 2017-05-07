
package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import utilities.AbstractTest;
import domain.Broadcast;
import domain.Charge;
import domain.Chorbi;
import domain.Event;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EventServiceTest extends AbstractTest {

	// System under test ----------------------------

	@Autowired
	private EventService		eventService;

	// Services -------------------------------------

	@Autowired
	private ChargeService		chargeService;

	@Autowired
	private BroadcastService	broadcastService;

	@Autowired
	private ChorbiService		chorbiService;


	// Tests ----------------------------------------

	/*
	 * Use case: An actor who is authenticated as a manager must be able
	 * to register an event as long as he/she has a valid credit card.
	 * Then the manager must have a new charge.
	 * 
	 * Functional requierements: An actor who is authenticated as a manager must be able to manage the events
	 * that he or she organises, which includes listing, registering, modifying, and deleting them.
	 * In order to register a new event, he must have registered a valid credit card that must not expire
	 * in less than one day. Every time he or she registers an event, the system will simulate that he or she's charged a 1.00 euro fee.
	 * 
	 * Expected errors:
	 * - A manager tries to register a new event but he/she does not have a valid credit card -> IllegalArgumentException
	 * - A manager tries to register a new event on the past -> IllegalArgumentException
	 */

	@Test
	public void createDriver() {
		final Object testingData[][] = {
			{ // Successful test
				"manager3", "01/01/2020 00:00", null
			}, { // Manager without valid credit card
				"manager1", "01/01/2020 00:00", IllegalArgumentException.class
			}, { // A manager tries to create an event on the past
				"manager3", "01/01/2000 00:00", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: An actor who is authenticated as a manager must be able
	 * to edit a registered event. A broadcast must be sent to the appropiate chorbies.
	 * 
	 * Functional requierements: An actor who is authenticated as a manager must be able to manage the events
	 * that he or she organises, which includes listing, registering, modifying, and deleting them.
	 * In order to register a new event, he must have registered a valid credit card that must not expire
	 * in less than one day. Every time he or she registers an event, the system will simulate that he or she's charged a 1.00 euro fee.
	 * 
	 * Expected errors:
	 * - A manager tries to set a number of seats lower than the number of current attendees -> IllegalArgumentException
	 * - A manager tries to edit an event that he/she did not register -> IllegalArgumentException
	 */

	@Test
	public void editionDriver() {
		final Object testingData[][] = {
			{ // Successful test
				"manager2", 1835, 10, null
			}, { // Setting an invalid number of seats
				"manager3", 1836, 1, IllegalArgumentException.class
			}, { // A manager tries to edit an event that he/she did not register
				"manager2", 1836, 10, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editionTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able
	 * to register in an event.
	 * 
	 * Functional requierements: An actor who is authenticated as a chorbi must be able to register to an event
	 * as long as there are enough seats available.
	 * 
	 * Expected errors:
	 * - A chorbi tries to register in a past event -> IllegalArgumentException
	 * - A chorbi tries to register in an event with no avaible seats -> IllegalArgumentException
	 */

	@Test
	public void registrationDriver() {
		final Object testingData[][] = {
			{ // Successful test
				"chorbi6", 1836, null
			}, { // Registering in a past event
				"chorbi7", 1842, IllegalArgumentException.class
			}, { // Registering in an event with no seats available
				"chorbi8", 1835, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registrationTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able
	 * to unregister from an event.
	 * 
	 * Functional requierements: An actor who is authenticated as a chorbi must be able to un-register from an event
	 * to which he or she's registered.
	 * 
	 * Expected errors:
	 * - A chorbi tries to unregister from a past event -> IllegalArgumentException
	 * - A chorbi tries to unregister from an event that he/she is not registered in -> IllegalArgumentException
	 */

	@Test
	public void unregistrationDriver() {
		final Object testingData[][] = {
			{ // Successful test
				"chorbi3", 1835, null
			}, { // Unregistering from a past event
				"chorbi3", 1842, IllegalArgumentException.class
			}, { // Unregistering from an event in which the manager is not registered
				"chorbi8", 1835, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.unregistrationTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Templates ------------------------------------

	protected void createTemplate(final String username, final String moment, final Class<?> expected) {
		Class<?> caught;
		DateTimeFormatter dtf;
		DateTime dt;

		dtf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
		dt = dtf.parseDateTime(moment);

		caught = null;

		try {
			this.authenticate(username);

			Event event, reconstructed, saved;
			DataBinder dataBinder;
			BindingResult binding;
			Collection<Charge> previousCharges, currentCharges;

			previousCharges = this.chargeService.findChargesByUser();

			// Simulating form

			event = this.eventService.create(); // This method looks for manager's credit card
			event.setTitle("Testing");
			event.setMoment(dt.toDate());
			event.setDescription("Testing");
			event.setPicture("http://test.com");
			event.setSeats(25);

			// Creating a binding

			dataBinder = new DataBinder(event, "event");
			binding = dataBinder.getBindingResult();

			reconstructed = this.eventService.reconstruct(event, binding);
			Assert.isTrue(!binding.hasErrors()); // If the moment was wrong the binding will have errors

			saved = this.eventService.save(reconstructed);
			this.eventService.flush();

			Assert.isTrue(saved.getId() != 0);

			currentCharges = this.chargeService.findChargesByUser();

			Assert.isTrue(currentCharges.size() - previousCharges.size() == 1); // Check if the manager has been charged

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void editionTemplate(final String username, final Integer eventId, final Integer seats, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			Event event, copy, reconstructed, saved;
			DataBinder dataBinder;
			BindingResult binding;
			Collection<Broadcast> previousBroadcasts, currentBroadcasts;
			List<Chorbi> chorbies;
			Chorbi testingChorbi;

			chorbies = new LinkedList<>(this.eventService.findChorbiesByEvent(eventId));
			testingChorbi = chorbies.get(0);

			previousBroadcasts = this.broadcastService.findNonReceivedBroadcasts(testingChorbi);

			// Simulating form

			if (seats == 1) {
				copy = this.eventService.findOneToEdit(eventId);
				event = this.eventService.create();
				event.setId(copy.getId());
				event.setVersion(copy.getVersion());
				event.setTitle(copy.getTitle());
				event.setMoment(copy.getMoment());
				event.setManager(copy.getManager());
				event.setPicture(copy.getPicture());
				event.setSeats(copy.getSeats());
				event.setAvailableSeats(copy.getAvailableSeats());
				event.setDescription(copy.getDescription());
			} else
				event = this.eventService.findOneToEdit(eventId);

			event.setSeats(seats);

			// Creating a binding

			dataBinder = new DataBinder(event, "event");
			binding = dataBinder.getBindingResult();

			reconstructed = this.eventService.reconstruct(event, binding);
			Assert.isTrue(!binding.hasErrors()); // If the moment was wrong the binding will have errors

			saved = this.eventService.save(reconstructed);
			this.eventService.flush();
			this.broadcastService.flush();

			Assert.isTrue(saved.getSeats() == event.getSeats());

			currentBroadcasts = this.broadcastService.findNonReceivedBroadcasts(testingChorbi);

			Assert.isTrue(currentBroadcasts.size() - previousBroadcasts.size() == 1);

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void registrationTemplate(final String username, final Integer eventId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			Event event;
			Chorbi principal;

			event = this.eventService.findOne(eventId);
			principal = this.chorbiService.findByPrincipal();

			this.eventService.registerChorbi(event);

			Assert.isTrue(principal.getEvents().contains(event));

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void unregistrationTemplate(final String username, final Integer eventId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			Event event;
			Chorbi principal;

			event = this.eventService.findOne(eventId);
			principal = this.chorbiService.findByPrincipal();

			this.eventService.unregisterChorbi(event);

			Assert.isTrue(!principal.getEvents().contains(event));

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

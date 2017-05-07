/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Broadcast;
import domain.Manager;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BroadcastServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private BroadcastService	broadcastService;

	// Services ------------------------------------------------------

	@Autowired
	private EventService		eventService;

	@Autowired
	private ManagerService		managerService;


	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a manager must be able to:
	 * Broadcast a chirp to the chorbies who have registered to any of the events that he or she manages
	 * Expected errors:
	 * - A non registered user tries to create a broadcast --> IllegalArgumentException
	 * - A non manager user tries to create a broadcast --> IllegalArgumentException
	 */

	@Test
	public void sendBroadcastDriver() {
		final Object testingData[][] = {
			{
				"manager2", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"chorbi1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.sendBroadcastTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void sendBroadcastTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			final Broadcast broadcast = this.broadcastService.create();
			broadcast.setSubject("Prueba");
			broadcast.setText("Prueba 2");

			final Manager manager = this.managerService.findByPrincipal();

			broadcast.setManager(manager);
			broadcast.setUninformedChorbies(this.eventService.findChorbiesByManagerEvents(manager));

			final Broadcast broadcastSaved = this.broadcastService.update(broadcast);

			final Collection<Broadcast> allBroadcasts = this.broadcastService.findAll();
			Assert.isTrue(allBroadcasts.contains(broadcastSaved));

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}

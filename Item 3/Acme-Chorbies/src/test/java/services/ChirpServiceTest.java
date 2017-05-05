
package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Chirp;
import domain.Chorbi;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ChirpService	chirpService;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;


	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Chirp to another chorbi
	 * Expected errors:
	 * - A non registered user tries to send a chirp --> IllegalArgumentException
	 * - An actor logged as administrator tries to send a chirp --> IllegalArgumentException
	 */

	@Test
	public void sendChirpDriver() {
		final Object testingData[][] = {
			{    //An actor unauthenticated cannot send chirps
				null, 1857, IllegalArgumentException.class
			}, { //An administrator cannot send chirps
				"admin", 1857, IllegalArgumentException.class
			}, { // Successful test
				"chorbi1", 1857, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.sendChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Browse the list of chirps that he or she's got
	 */

	@Test
	public void findChirpsReceivedTest() {
		Collection<Chirp> chirpsReceived;
		Boolean isSent;

		this.authenticate("chorbi3");

		chirpsReceived = this.chirpService.findChirpsReceived();
		isSent = false;

		this.unauthenticate();

		for (final Chirp c : chirpsReceived)
			if (!c.getCopy())
				isSent = true;
		Assert.isTrue(!isSent);
	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Browse the list of chirps that he or she's sent
	 */

	@Test
	public void findChirpsSentTest() {
		Collection<Chirp> chirpsSent;
		Boolean isReceived;

		this.authenticate("chorbi3");

		chirpsSent = this.chirpService.findChirpsSent();
		isReceived = false;

		this.unauthenticate();

		for (final Chirp c : chirpsSent)
			if (c.getCopy())
				isReceived = true;
		Assert.isTrue(!isReceived);
	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Reply a chirp that he/she has
	 * Expected errors:
	 * - A chorbi tries to reply a chirp of other chorbi --> IllegalArgumentException
	 * - A chorbi tries to reply a chirp send by him/her --> IllegalArgumentException
	 */

	@Test
	public void replyChirpDriver() {
		final Object testingData[][] = {
			{    //A chorbi cannot reply chirps of other chorbies
				"chorbi1", 1856, IllegalArgumentException.class
			}, { //A chorbi cannot reply a chirp send by him/her
				"chorbi2", 1877, IllegalArgumentException.class
			}, { // Successful test
				"chorbi2", 1880, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.replyChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Resend a chirp that he/she has sent
	 * Expected errors:
	 * - A chorbi tries to resend a chirp of other chorbi --> IllegalArgumentException
	 * - A chorbi tries to resend a chirp received by him/her --> IllegalArgumentException
	 */

	@Test
	public void resendChirpDriver() {
		final Object testingData[][] = {
			{    //A chorbi cannot resend a chirp of other chorbi
				"chorbi1", 1880, 1860, IllegalArgumentException.class
			}, { //A chorbi cannot resend a chirp received by him/her
				"chorbi3", 1878, 1860, IllegalArgumentException.class
			}, { // Successful test
				"chorbi3", 1881, 1859, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.resendChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Erase any of the chirps that he or she's got or sent
	 * Expected errors:
	 * - A chorbi tries to delete a chirp of another chorbi --> IllegalArgumentException
	 * - An administrator tries to delete a chirp --> IllegalArgumentException
	 */

	@Test
	public void deleteChirpDriver() {
		final Object testingData[][] = {
			{    //A chorbi cannot delete a chirp of other chorbies
				"chorbi1", 1879, IllegalArgumentException.class
			}, { //An administrator cannot delete a chirp
				"admin", 1879, IllegalArgumentException.class
			}, { // Successful test
				"chorbi3", 1879, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	@Test
	public void testfindMinChirpsRecPerChorbi() {
		this.authenticate("admin");

		Long min;

		min = this.chirpService.findMinChirpsRecPerChorbi();
		Assert.isTrue(min.equals(0L));

		this.unauthenticate();
	}

	@Test
	public void testfindMaxChirpsRecPerChorbi() {
		this.authenticate("admin");

		Long max;

		max = this.chirpService.findMaxChirpsRecPerChorbi();
		Assert.isTrue(max.equals(12L));

		this.unauthenticate();
	}

	@Test
	public void testfindAvgChirpsRecPerChorbi() {
		this.authenticate("admin");

		Double avg;

		avg = this.chirpService.findAvgChirpsRecPerChorbi();
		Assert.isTrue(avg.equals(0.8));

		this.unauthenticate();
	}

	@Test
	public void testfindMinChirpsSendPerChorbi() {
		this.authenticate("admin");

		Long min;

		min = this.chirpService.findMinChirpsSendPerChorbi();
		Assert.isTrue(min.equals(0L));

		this.unauthenticate();
	}

	@Test
	public void testfindMaxChirpsSendPerChorbi() {
		this.authenticate("admin");

		Long max;

		max = this.chirpService.findMaxChirpsSendPerChorbi();
		Assert.isTrue(max.equals(14L));

		this.unauthenticate();
	}

	@Test
	public void testfindAvgChirpsSendPerChorbi() {
		this.authenticate("admin");

		Double avg;

		avg = this.chirpService.findAvgChirpsSendPerChorbi();
		Assert.isTrue(avg.equals(0.8));

		this.unauthenticate();
	}

	/*
	 * USE CASE WITH 10 TESTS
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Browse the list of chorbies who have registered to the system and chirp to one of them
	 * Expected errors:
	 * - An unauthenticated actor tries to list chorbies and send a chirp --> IllegalArgumentException
	 * - An administrator tries to send a chirp --> IllegalArgumentException
	 * - A chorbi tries to send a chirp to himself/herself --> IllegalArgumentException
	 * - A chorbi tries to send a chirp without subject --> ConstraintViolationException
	 * - A chorbi tries to send a chirp without text --> ConstraintViolationException
	 * - A chorbi tries to send a chirp to an administrator --> NullPointerException
	 * - A chorbi tries to send a chirp to a banned chorbi --> IllegalArgumentException
	 */

	@Test
	public void listChirpsReceivedAndReplyDriver() {
		final Object testingData[][] = {
			{    // Successful test
				"chorbi1", 1856, "Subject", "Text", null
			}, { // Successful test
				"chorbi2", 1859, "Subject", "Text", null
			}, { // Successful test
				"chorbi3", 1855, "Subject", "Text", null
			}, { // An unauthenticated actor cannot list chorbies and send chirps
				null, 1855, "Subject", "Text", IllegalArgumentException.class
			}, { // An administrator cannot send chirps
				"admin", 1855, "Subject", "Text", IllegalArgumentException.class
			}, { // A chorbi cannot send a chirp to himself/herself
				"chorbi1", 1855, "Subject", "Text", IllegalArgumentException.class
			}, { // A chorbi cannot send a chirp without subject
				"chorbi1", 1856, "", "Text", ConstraintViolationException.class
			}, { // A chorbi cannot send a chirp without text
				"chorbi1", 1856, "Subject", "", ConstraintViolationException.class
			}, { // A chorbi cannot send a chirp to an administrator
				"chorbi1", 1809, "Subject", "Text", NullPointerException.class
			}, { // A chorbi cannot send a chirp to a banned chorbi
				"chorbi1", 1864, "Subject", "Text", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listChorbiesAndSendChirpTemplate((String) testingData[i][0], (int) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	@Test
	public void testFindChorbiesSortedByNumberEvents() {
		this.authenticate("admin");

		List<Chorbi> res;

		res = new LinkedList<>(this.chorbiService.findChorbiesSortedByNumberEvents());

		Assert.isTrue(res.get(0).equals(this.chorbiService.findOne(1857)));
		Assert.isTrue(res.get(1).equals(this.chorbiService.findOne(1855)));
		Assert.isTrue(res.get(2).equals(this.chorbiService.findOne(1856)));

		this.unauthenticate();
	}

	// Templates --------------------------------------------------------------

	protected void sendChirpTemplate(final String username, final int recipientId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Chirp created, sent, copy;
			final Chorbi recipient;

			this.authenticate(username);
			recipient = this.chorbiService.findOne(recipientId);
			created = this.chirpService.create(recipientId);
			created.setSubject("Subject test");
			created.setText("Text test");
			sent = this.chirpService.send(created);

			copy = this.chirpService.saveCopy(sent);
			this.chirpService.flush();

			this.unauthenticate();

			Assert.isTrue(!sent.getCopy());
			Assert.isTrue(copy.getCopy());
			Assert.isTrue(sent.getRecipient().equals(recipient));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void replyChirpTemplate(final String username, final int chirpId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Chirp chirp, reply, copy, result;

			this.authenticate(username);
			chirp = this.chirpService.findOne(chirpId);
			reply = this.chirpService.reply(chirpId);
			reply.setText("Test reply");
			result = this.chirpService.send(reply);
			copy = this.chirpService.saveCopy(result);
			this.chirpService.flush();
			this.unauthenticate();

			Assert.isTrue(result.getSubject().contains("RE:"));
			Assert.isTrue(result.getSender().equals(chirp.getRecipient()));
			Assert.isTrue(result.getRecipient().equals(chirp.getSender()));
			Assert.isTrue(!result.getCopy());
			Assert.isTrue(copy.getCopy());
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void resendChirpTemplate(final String username, final int chirpId, final int recipientId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Chirp copy, resend, result;
			Chorbi recipient;

			this.authenticate(username);
			recipient = this.chorbiService.findOne(recipientId);
			resend = this.chirpService.resend(chirpId);
			resend.setRecipient(recipient);
			result = this.chirpService.send(resend);
			copy = this.chirpService.saveCopy(result);
			this.chirpService.flush();

			Assert.isTrue(result.getSubject().contains("FW:"));
			Assert.isTrue(result.getSender().equals(this.chorbiService.findByPrincipal()));
			Assert.isTrue(!result.getCopy());
			Assert.isTrue(copy.getCopy());

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteChirpTemplate(final String username, final int chirpId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Chirp chirp;

			chirp = this.chirpService.findOne(chirpId);

			this.authenticate(username);
			this.chirpService.delete(chirp);
			this.chirpService.flush();
			Assert.isTrue(!this.chirpService.findChirpsSent().contains(chirp) && !this.chirpService.findChirpsReceived().contains(chirp));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void listChorbiesAndSendChirpTemplate(final String username, final int recipientId, final String subject, final String text, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			List<Chorbi> chorbies;
			Chirp created, sent, copy;

			this.authenticate(username);

			chorbies = (List<Chorbi>) this.chorbiService.findNonBannedChorbies();
			created = this.chirpService.create(recipientId);
			created.setSubject(subject);
			created.setText(text);

			sent = this.chirpService.send(created);
			this.chirpService.flush();
			copy = this.chirpService.saveCopy(sent);
			this.chirpService.flush();

			Assert.isTrue(!sent.getCopy());
			Assert.isTrue(copy.getCopy());
			Assert.isTrue(chorbies.size() > 0);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

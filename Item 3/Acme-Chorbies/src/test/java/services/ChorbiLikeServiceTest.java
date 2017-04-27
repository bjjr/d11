
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
import domain.Chorbi;
import domain.ChorbiLike;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChorbiLikeServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ChorbiLikeService	chorbiLikeService;

	// Services ------------------------------------------------------

	@Autowired
	private ChorbiService		chorbiService;


	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Like another chorbi
	 * Expected errors:
	 * - A chorbi tries to like him/herself --> IllegalArgumentException
	 * - A non registered user tries to like another chorbi --> IllegalArgumentException
	 * - An administrator tries to like another chorbi --> IllegalArgumentException
	 * - A chorbi tries to like another chorbi who have been already liked by him/her --> IllegalArgumentException
	 */

	@Test
	public void createChorbiLikeDriver() {
		final Object testingData[][] = {
			{    // A chorbi cannot like him/herself
				"chorbi1", 1013, IllegalArgumentException.class
			}, { // A non registered user cannot like another chorbi
				null, 1013, IllegalArgumentException.class
			}, { // An administrator cannot like another chorbi
				"admin", 1016, IllegalArgumentException.class
			}, { // A chorbi cannot like another chorbi who have been already liked by him/her
				"chorbi2", 1016, IllegalArgumentException.class
			}, { // Successful test
				"chorbi1", 1015, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createChorbiLikeTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Cancel a like
	 * Expected errors:
	 * - A non registered user tries to cancel some like --> IllegalArgumentException
	 * - An administrator tries to cancel some like --> IllegalArgumentException
	 * - A chorbi tries to cancel some like that not belongs to him/her --> IllegalArgumentException
	 */

	@Test
	public void cancelChorbiLikeDriver() {
		final Object testingData[][] = {
			{ // A non registered user cannot cancel some like
				null, 1013, IllegalArgumentException.class
			}, { // An administrator cannot cancel some like
				"admin", 1016, IllegalArgumentException.class
			}, { // A chorbi cannot cancel some like that not belongs to him/her
				"chorbi2", 1017, IllegalArgumentException.class
			}, { // Successful test
				"chorbi2", 1016, null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.cancelChorbiLikeTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void testfindMinLikesPerChorbi() {
		this.authenticate("admin");

		Long min;

		min = this.chorbiLikeService.findMinLikesPerChorbi();
		Assert.isTrue(min.equals(0L));

		this.unauthenticate();
	}

	@Test
	public void testfindMaxLikesPerChorbi() {
		this.authenticate("admin");

		Long max;

		max = this.chorbiLikeService.findMaxLikesPerChorbi();
		Assert.isTrue(max.equals(7L));

		this.unauthenticate();
	}

	@Test
	public void testfindAvgLikesPerChorbi() {
		this.authenticate("admin");

		Double avg;

		avg = this.chorbiLikeService.findAvgLikesPerChorbi();
		Assert.isTrue(avg.equals(1.2));

		this.unauthenticate();
	}

	// Ancillary methods ------------------------------------------------------

	protected void createChorbiLikeTemplate(final String username, final int chorbiId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			ChorbiLike cl, saved;
			final Chorbi principal;
			final Collection<ChorbiLike> chorbiLikes;
			final Chorbi chorbi;

			this.authenticate(username);

			principal = this.chorbiService.findByPrincipal();
			chorbi = this.chorbiService.findOne(chorbiId);
			cl = this.chorbiLikeService.create(chorbi);

			saved = this.chorbiLikeService.save(cl);
			this.chorbiLikeService.flush();
			chorbiLikes = this.chorbiLikeService.findChorbiLikesByLiker(principal.getId());

			Assert.isTrue(chorbiLikes.contains(saved));

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void cancelChorbiLikeTemplate(final String username, final int chorbiId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			final Chorbi principal;
			final Collection<ChorbiLike> chorbiLikes;
			final Chorbi chorbi;

			this.authenticate(username);

			principal = this.chorbiService.findByPrincipal();
			chorbi = this.chorbiService.findOne(chorbiId);

			this.chorbiLikeService.cancelChorbiLike(chorbi);
			this.chorbiLikeService.flush();
			chorbiLikes = this.chorbiLikeService.findAll();

			for (final ChorbiLike c : chorbiLikes)
				Assert.isTrue(!(c.getLiker().equals(principal) && c.getLiked().equals(chorbi)));

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}

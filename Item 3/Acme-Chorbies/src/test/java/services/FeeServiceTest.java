
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Fee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FeeServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private FeeService	feeService;


	// Supporting services ----------------------------------------------------

	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as an administrator must be able to:
	 * Change the fee that is charged to managers and chorbies
	 * Expected errors:
	 * - An actor logged as chorbi tries to change a fee --> IllegalArgumentException
	 * - An administrator tries to change a fee introducing a not valid value --> ConstraintViolationException
	 */

	@Test
	public void changeFeeDriver() {
		final Object testingData[][] = {
			{    // Successful test
				"admin", 1845, 4.0, null
			}, { // Successful test
				"admin", 1846, 7.0, null
			}, { //An actor logged as chorbi cannot change a fee
				"chorbi1", 1846, 12.0, IllegalArgumentException.class
			}, { //An administrator cannot introduce a not valid value for a fee
				"admin", 1845, -2.0, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.changeFeeTemplate((String) testingData[i][0], (int) testingData[i][1], (double) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	// Templates --------------------------------------------------------------

	protected void changeFeeTemplate(final String username, final int feeId, final double feeValue, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Fee fee, saved;

			this.authenticate(username);
			fee = this.feeService.findOne(feeId);
			fee.setValue(feeValue);
			saved = this.feeService.save(fee);
			this.feeService.flush();

			this.unauthenticate();

			Assert.isTrue(saved.getValue() >= 0.0);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

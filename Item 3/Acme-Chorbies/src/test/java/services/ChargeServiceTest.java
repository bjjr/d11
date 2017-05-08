
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Charge;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChargeServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ChargeService	chargeService;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;


	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a chorbi or manager must be able to:
	 * Pay a charge not paid yet
	 * Expected errors:
	 * - A manager tries to pay a charge already paid --> IllegalArgumentException
	 * - A chorbi tries to pay a charge of another chorbi or a manager --> IllegalArgumentException
	 */

	@Test
	public void payChargeDriver() {
		final Object testingData[][] = {
			{    // A chorbi or a manager cannot pay a charge already paid
				"manager3", 1847, IllegalArgumentException.class
			}, { // A chorbi cannot pay a charge of a manager 
				"chorbi1", 1849, IllegalArgumentException.class
			}, { // Successful test
				"manager3", 1849, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.payChargeTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * Use case: An actor who is authenticated as an administrator must be able to:
	 * Generate charges to chorbies
	 * Expected errors:
	 * - A non registered user tries to generate charges to chorbies --> IllegalArgumentException
	 * - A chorbi tries to generate charges to chorbies --> IllegalArgumentException
	 */

	@Test
	public void generateChargesToChorbiesDriver() {
		final Object testingData[][] = {
			{    // A non registered user cannot generate charges to chorbies
				null, IllegalArgumentException.class
			}, { // A chorbi cannot generate charges to chorbies
				"chorbi1", IllegalArgumentException.class
			}, { // Successful test
				"admin", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.generateChargesToChorbiesTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	@Test
	public void testEconomicStatistics() {
		this.authenticate("admin");

		final Double totalBenefit, totalDue, theoreticalBenefit;

		theoreticalBenefit = this.chargeService.theoreticalBenefit();
		totalBenefit = this.chargeService.totalBenefit();
		totalDue = this.chargeService.totalDue();

		Assert.isTrue(totalBenefit.equals(133.0));
		Assert.isTrue(totalDue.equals(153.0));
		Assert.isTrue(theoreticalBenefit.equals(286.0));

		this.unauthenticate();
	}

	// Templates --------------------------------------------------------------

	protected void payChargeTemplate(final String username, final int chargeId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Charge charge, saved;

			this.authenticate(username);

			charge = this.chargeService.findOne(chargeId);
			saved = this.chargeService.pay(charge);

			this.unauthenticate();

			Assert.isTrue(saved.isPaid());
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void generateChargesToChorbiesTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Integer chorbies;
			Integer chargesBefore, chargesAfter;

			chorbies = this.chorbiService.findAll().size();
			chargesBefore = this.chargeService.findAll().size();

			this.authenticate(username);

			this.chargeService.generateChargesToChorbies();
			chargesAfter = this.chargeService.findAll().size();

			this.unauthenticate();

			Assert.isTrue(chargesAfter.equals(chargesBefore + chorbies));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}

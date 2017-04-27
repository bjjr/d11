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
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Banner;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BannerServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private BannerService	bannerService;


	// Services ------------------------------------------------------

	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a administrator must be able to:
	 * Change the banners that are displayed on the welcome page
	 * Expected errors:
	 * - A customer tries to apply for an owned trip --> IllegalArgumentException
	 * - A non registered user tries to create an banner --> IllegalArgumentException
	 * - An administrator tries to create an banner --> IllegalArgumentException
	 * - A customer tries to apply for a trip already applied --> IllegalArgumentException
	 */

	@Test
	public void editBannerDriver() {
		final Object testingData[][] = {
			{ // Successful test.
				"admin", "http://www.ikea.com/PIAimages/0416866_PE577783_S5.JPG", null
			}, { // A non registered user cannot edit an banner.
				null, "http://www.ikea.com/PIAimages/0416866_PE577783_S5.JPG", IllegalArgumentException.class
			}, {    // An administrator inputs a string that is not an url.
				"admin", "not url string", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editBannerTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	//	/*
	//	 * Checks if the query findAvgBannersPerOffer works
	//	 */
	//
	//	@Test
	//	public void testFindAvgBannersPerOffer() {
	//		Double avg;
	//
	//		this.authenticate("admin");
	//
	//		avg = this.bannerService.findAvgBannersPerOffer();
	//
	//		Assert.isTrue(avg.equals(1.0));
	//
	//		this.unauthenticate();
	//
	//	}

	// Ancillary methods ------------------------------------------------------

	protected void editBannerTemplate(final String username, final String url, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Banner banner, saved;
			final Collection<Banner> banners;

			this.authenticate(username);

			banner = this.bannerService.findRandomBanner();

			banner.setPath(url);

			saved = this.bannerService.save(banner);
			this.bannerService.flush();

			banners = this.bannerService.findAll();

			Assert.isTrue(banners.contains(saved));

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}

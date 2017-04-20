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
import domain.SearchTemplate;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SearchTemplateServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private SearchTemplateService	searchTemplateService;


	// Services ------------------------------------------------------

	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Change his or her profile
	 * Expected errors:
	 * - A non registered user tries to change his searchTemplate --> IllegalArgumentException
	 * - An user but not a chorbi tries to change his searchTemplate --> IllegalArgumentException
	 * - A chorbi tries to change Gender to a non allowed value --> IllegalArgumentException
	 * - A chorbi tries to change Relationship to a non allowed value --> IllegalArgumentException
	 */

	@Test
	public void editSearchTemplateDriver() {
		final Object testingData[][] = {
			{
				null, "FRIENDSHIP", "WOMAN", IllegalArgumentException.class
			}, {
				"admin", "FRIENDSHIP", "WOMAN", IllegalArgumentException.class
			}, {
				"chorbi5", "FRIENDSHIP", "WOMAN", null
			}, {
				"chorbi5", "FRIENDSHIP", "OTRACOSA", IllegalArgumentException.class
			}, {
				"chorbi5", "OTRACOSA", "WOMAN", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.editSearchTemplateTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}
	// Ancillary methods ------------------------------------------------------

	protected void editSearchTemplateTemplate(final String username, String relationship, String gender, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			SearchTemplate searchTemplate = this.searchTemplateService.findSearchTemplateByPrincipal();

			searchTemplate.setAge(40);
			searchTemplate.setKeyword("nada");

			searchTemplate.setRelationship(relationship);
			searchTemplate.setGender(gender);

			SearchTemplate savedSearchTemplate = this.searchTemplateService.save(searchTemplate);
			this.searchTemplateService.flush();

			Collection<SearchTemplate> allSearchTemplate = this.searchTemplateService.findAll();

			Assert.isTrue(allSearchTemplate.contains(savedSearchTemplate));

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/*
	 * Use case: An actor who is authenticated as a chorbi must be able to:
	 * Use searchTemplate to search chorbies if has a valid credit card
	 * Expected errors:
	 * - A non registered user tries to search by searchTemplate --> IllegalArgumentException
	 * - An user but not a chorbi tries to search by searchTemplate --> IllegalArgumentException
	 * - A chorbi tries to search by searchTemplate with an invalid creditCard --> IllegalArgumentException
	 */

	@Test
	public void searchBySearchTemplateDriver() {
		final Object testingData[][] = {
			{
				null, IllegalArgumentException.class
			}, {
				"admin", IllegalArgumentException.class
			}, {
				"chorbi5", null
			}, {
				"chorbi1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.searchBySearchTemplateTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
		}
	}
	// Ancillary methods ------------------------------------------------------

	protected void searchBySearchTemplateTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			SearchTemplate searchTemplate = this.searchTemplateService.findSearchTemplateByPrincipal();

			this.searchTemplateService.search(searchTemplate);

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}

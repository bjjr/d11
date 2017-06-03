
package services;

import java.util.Collection;
import java.util.HashSet;
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

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Chorbi;
import domain.Coordinates;
import forms.ChorbiForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChorbiServiceTest extends AbstractTest {

	// System under test ----------------------------

	@Autowired
	private ChorbiService	chorbiService;


	// Services -------------------------------------

	// Tests ----------------------------------------

	/*
	 * Use case: A new user tries to create a chorbi account.
	 * Functional Requirement: Register to the system as a chorbi.
	 */

	@Test
	public void registerDriver() {
		final Object testingData[][] = {
			{// User inputs valid data
				null, "20/01/1985", "passwd1", "passwd1", null
			}, {// New user is under legal age
				null, "20/01/2003", "passwd1", "passwd1", IllegalArgumentException.class
			}, {// User did not write the same password in confirmation field
				null, "20/01/1985", "passwd1", "passwd2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/*
	 * Use case: An existing chorbi wants to update his/her profile.
	 * Functional Requirement: An actor who is authenticated as a chorbi must be able to change his or her profile
	 */

	@Test
	public void profileEditionDriver() {
		final Object testingData[][] = {
			{// A chorbi changes his/her profile correctly
				"chorbi1", "This is my new description", "newemail@test.com", null
			}, {// A chorbi makes a mistake in his/her description
				"chorbi2", "", "newemail@test.com", IllegalArgumentException.class
			}, {// A chorbi makes a mistake in his/her email
				"chorbi3", "This is my new description", "email.", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.profileEditionTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	@Test
	public void maskSensibleDataTest() {
		String text, maskedText, expected;

		text = "You can contact me at +34 111 222 333 or someone@somewhere.com";
		maskedText = this.chorbiService.maskSensibleData(text);
		expected = "You can contact me at *** or ***";

		Assert.isTrue(maskedText.equals(expected));
	}

	/*
	 * Test case: An administrator decides to ban a certain chorbi
	 * Functional Requirement: An actor who is authenticated as an administrator must be able to ban a chorbi, that is, to disable his or her account.
	 */

	@Test
	public void banChorbiDriver() {
		final Object testingData[][] = {
			{// The administrator bans an unbanned chorbi (chorbi9 = 1863)
				"admin", 1863, null
			}, {// A chorbi tries to ban another chorbi.
				"chorbi1", 1863, IllegalArgumentException.class
			}, {// A non registered user tries to ban a chorbi
				null, 1863, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.banChorbiTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Test case: An administrator decides to unban a certain chorbi
	 * Functional Requirement: An actor who is authenticated as an administrator must be able to unban a chorbi which means that his or her account is re-enabled.
	 */

	@Test
	public void unbanChorbiDriver() {
		final Object testingData[][] = {
			{// The administrator unbans an unbanned chorbi (chorbi10 = 1864)
				"admin", 1864, null
			}, {// A chorbi tries to unban another chorbi.
				"chorbi1", 1864, IllegalArgumentException.class
			}, {// A non registered user tries to unban a chorbi
				null, 1864, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.unbanChorbiTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void testfindChorbiesSortNumLikes() {
		this.authenticate("admin");

		List<Chorbi> chorbies;
		Boolean success;

		success = false;
		chorbies = (List<Chorbi>) this.chorbiService.findChorbiesSortNumLikes();
		if (chorbies.get(0).getId() == 1858)
			success = true;

		Assert.isTrue(success);

		this.unauthenticate();
	}

	@Test
	public void testfindChorbiesMoreChirpsRec() {
		this.authenticate("admin");

		Collection<Chorbi> chorbies;
		Boolean success;

		success = false;
		chorbies = this.chorbiService.findChorbiesMoreChirpsRec();
		for (final Chorbi chorbi : chorbies)
			if (chorbi.getId() == 1858)
				success = true;

		Assert.isTrue(success);

		this.unauthenticate();
	}

	@Test
	public void testfindChorbiesMoreChirpsSend() {
		this.authenticate("admin");

		Collection<Chorbi> chorbies;
		Boolean success;

		success = false;
		chorbies = this.chorbiService.findChorbiesMoreChirpsSend();
		for (final Chorbi chorbi : chorbies)
			if (chorbi.getId() == 1857)
				success = true;

		Assert.isTrue(success);

		this.unauthenticate();
	}

	@Test
	public void testFindChorbiesSortNumStars() {
		this.authenticate("admin");

		List<Chorbi> res;

		res = new LinkedList<>(this.chorbiService.findChorbiesSortNumStars());

		Assert.isTrue(res.get(0).equals(this.chorbiService.findOne(1858)));
		Assert.isTrue(res.get(1).equals(this.chorbiService.findOne(1861)));
		Assert.isTrue(res.get(2).equals(this.chorbiService.findOne(1862)));

		this.unauthenticate();
	}

	@Test
	public void testFindChorbieSortedByAvgStars() {
		this.authenticate("admin");

		List<Chorbi> res;

		res = new LinkedList<>(this.chorbiService.findChorbieSortedByAvgStars());

		Assert.isTrue(res.get(0).equals(this.chorbiService.findOne(1858)));
		Assert.isTrue(res.get(1).equals(this.chorbiService.findOne(1861)));
		Assert.isTrue(res.get(2).equals(this.chorbiService.findOne(1862)));

		this.unauthenticate();
	}

	// Templates ------------------------------------

	protected void registerTemplate(final String username, final String birthdate, final String passwd1, final String passwd2, final Class<?> expected) {
		Class<?> caught;
		DateTimeFormatter dtf;
		DateTime dt;

		dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
		dt = dtf.parseDateTime(birthdate);

		caught = null;

		try {
			this.authenticate(username);

			ChorbiForm chorbiForm;
			Chorbi reconstructed, saved;
			DataBinder dataBinder;
			BindingResult binding;
			Coordinates coordinates;
			UserAccount userAccount;
			Collection<Authority> auths;
			Authority auth;

			chorbiForm = new ChorbiForm();
			coordinates = new Coordinates();
			userAccount = new UserAccount();
			auths = new HashSet<>();
			auth = new Authority();
			auth.setAuthority("CHORBI");
			auths.add(auth);

			// Creating a binding
			dataBinder = new DataBinder(chorbiForm, "chorbiForm");
			binding = dataBinder.getBindingResult();

			chorbiForm.setName("test");
			chorbiForm.setSurname("testSurname");
			chorbiForm.setEmail("test@test.com");
			chorbiForm.setPhone("+65 215000333");
			chorbiForm.setPicture("https://testpic.com");
			chorbiForm.setDescription("testDescription");
			chorbiForm.setBirthdate(dt.toDate());
			chorbiForm.setGender("MAN");
			coordinates.setCountry("test");
			coordinates.setCity("test");
			chorbiForm.setCoordinates(coordinates);
			chorbiForm.setRelationship("LOVE");
			userAccount.setUsername("testChorbi");
			userAccount.setPassword(passwd1);
			chorbiForm.setUserAccount(userAccount);
			chorbiForm.setPasswdConfirmation(passwd2);

			reconstructed = this.chorbiService.reconstruct(chorbiForm, binding);
			Assert.isTrue(!binding.hasErrors());

			saved = this.chorbiService.save(reconstructed);
			Assert.isTrue(saved.getId() != 0);

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void profileEditionTemplate(final String username, final String description, final String email, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			Chorbi chorbi, reconstructed;
			DataBinder dataBinder;
			BindingResult binding;

			// Simulating a pruned object using the constructor (attributes set to null)
			chorbi = new Chorbi();

			// Creating a binding
			dataBinder = new DataBinder(chorbi, "chorbi");
			binding = dataBinder.getBindingResult();

			// User inputs necessary info
			chorbi.setEmail(email);
			chorbi.setPhone("+34 636 00 33 22");
			chorbi.setPicture("http://acmetest1234.com/img.png");
			chorbi.setDescription(description);
			chorbi.setRelationship("LOVE");

			reconstructed = this.chorbiService.reconstruct(chorbi, binding);
			Assert.isTrue(!binding.hasErrors());

			this.chorbiService.save(reconstructed);

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void banChorbiTemplate(final String username, final Integer chorbiId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			Chorbi chorbi;
			Authority bannedAuth;

			chorbi = this.chorbiService.findOne(chorbiId);
			bannedAuth = new Authority();
			bannedAuth.setAuthority("BANNED");

			this.chorbiService.ban(chorbi);

			Assert.isTrue(chorbi.getUserAccount().getAuthorities().contains(bannedAuth));

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void unbanChorbiTemplate(final String username, final Integer chorbiId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			Chorbi chorbi;
			Authority bannedAuth;

			chorbi = this.chorbiService.findOne(chorbiId);
			bannedAuth = new Authority();
			bannedAuth.setAuthority("CHORBI");

			this.chorbiService.unban(chorbi);

			Assert.isTrue(chorbi.getUserAccount().getAuthorities().contains(bannedAuth));

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}


package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

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
import domain.Manager;
import forms.ManagerForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ManagerServiceTest extends AbstractTest {

	// System under test ----------------------------

	@Autowired
	private ManagerService	managerService;


	// Services -------------------------------------

	// Tests ----------------------------------------

	/*
	 * Use case: A new user tries to create a manager account.
	 * Functional Requirement: Register to the system as a manager.
	 */

	@Test
	public void registerDriver() {
		final Object testingData[][] = {
			{// User inputs valid data
				"admin", "452347", "passwd1", "passwd1", null
			}, {// New manager blank vat
				"admin", "", "passwd1", "passwd1", IllegalArgumentException.class
			}, {// User did not write the same password in confirmation field
				"admin", "452347", "passwd1", "passwd2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/*
	 * Use case: An existing manager wants to update his/her profile.
	 * Functional Requirement: An actor who is authenticated as a manager must be able to change his or her profile
	 */

	@Test
	public void profileEditionDriver() {
		final Object testingData[][] = {
			{// A manager changes his/her profile correctly
				"manager1", "newCompany", "47512", null
			}, {// A manager makes a mistake in his/her company
				"manager2", "", "785423", IllegalArgumentException.class
			}, {// A manager makes a mistake in his/her vat
				"manager3", "newCompany", "", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.profileEditionTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	// Templates ------------------------------------

	protected void registerTemplate(final String username, final String vat, final String passwd1, final String passwd2, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			ManagerForm managerForm;
			Manager reconstructed, saved;
			DataBinder dataBinder;
			BindingResult binding;
			UserAccount userAccount;
			Collection<Authority> auths;
			Authority auth;

			managerForm = new ManagerForm();
			userAccount = new UserAccount();
			auths = new HashSet<>();
			auth = new Authority();
			auth.setAuthority("MANAGER");
			auths.add(auth);

			// Creating a binding
			dataBinder = new DataBinder(managerForm, "managerForm");
			binding = dataBinder.getBindingResult();

			managerForm.setName("test");
			managerForm.setSurname("testSurname");
			managerForm.setEmail("test@test.com");
			managerForm.setPhone("+65 215000333");

			managerForm.setCompany("testCompany");
			managerForm.setVat(vat);

			userAccount.setUsername("testManager");
			userAccount.setPassword(passwd1);
			managerForm.setUserAccount(userAccount);
			managerForm.setPasswdConfirmation(passwd2);

			reconstructed = this.managerService.reconstruct(managerForm, binding);
			Assert.isTrue(!binding.hasErrors());

			saved = this.managerService.save(reconstructed);
			Assert.isTrue(saved.getId() != 0);

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void profileEditionTemplate(final String username, final String company, final String vat, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			ManagerForm managerForm;
			Manager reconstructed, saved;
			DataBinder dataBinder;
			BindingResult binding;

			managerForm = new ManagerForm(this.managerService.findByPrincipal());

			// Creating a binding
			dataBinder = new DataBinder(managerForm, "managerForm");
			binding = dataBinder.getBindingResult();

			managerForm.setCompany(company);
			managerForm.setVat(vat);
			managerForm.setPasswdConfirmation(managerForm.getUserAccount().getPassword());

			reconstructed = this.managerService.reconstruct(managerForm, binding);
			Assert.isTrue(!binding.hasErrors());

			saved = this.managerService.save(reconstructed);
			Assert.isTrue(saved.getId() != 0);

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}

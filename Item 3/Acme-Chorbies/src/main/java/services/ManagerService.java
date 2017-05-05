
package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Manager;
import forms.ManagerForm;

@Service
@Transactional
public class ManagerService {

	// Managed repository ---------------------------

	@Autowired
	private ManagerRepository	managerRepository;

	// Supporting services --------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ChorbiService		chorbiService;

	// Validator ------------------------------------

	@Autowired
	private Validator			validator;


	// Constructor ----------------------------------

	public ManagerService() {
		super();
	}

	// Simple CRUD methods --------------------------

	public Manager create() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"), "Only a admin can use the create method");

		Manager manager;

		manager = new Manager();

		return manager;
	}

	public Manager save(final Manager manager) {
		Assert.isTrue(!this.actorService.checkAuthority("CHORBI"), "Only a admin or manager can use the save method");

		Manager res;

		String initialPasswd, encodedPasswd;

		initialPasswd = manager.getUserAccount().getPassword();
		encodedPasswd = this.chorbiService.hashPassword(initialPasswd);

		manager.getUserAccount().setPassword(encodedPasswd);

		res = this.managerRepository.save(manager);

		return res;
	}

	public Manager reconstruct(final ManagerForm managerForm, final BindingResult binding) {
		Manager res;

		/*
		 * Avoid form modification
		 */

		res = managerForm.getManager();

		this.checkPasswords(managerForm.getUserAccount().getPassword(), managerForm.getPasswdConfirmation(), binding);

		if (this.actorService.checkAuthority("MANAGER")) {
			final Manager principal = this.findByPrincipal();

			res.setId(principal.getId());
			res.setVersion(principal.getVersion());
			res.getUserAccount().setId(principal.getUserAccount().getId());
			res.getUserAccount().setVersion(principal.getUserAccount().getVersion());
		}

		this.validator.validate(res, binding);

		return res;
	}

	public Manager findOne(final int managerId) {
		Manager res;

		res = this.managerRepository.findOne(managerId);

		return res;
	}

	//Other business methods -----------------------

	public Manager findByPrincipal() {
		Manager res;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		res = this.findByUserAccount(userAccount);

		return res;
	}

	private Manager findByUserAccount(final UserAccount userAccount) {
		Manager res;

		res = this.managerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(res);

		return res;
	}

	public Collection<Manager> findManagersSortedByNumberEvents() {
		Collection<Manager> res;

		res = this.managerRepository.findManagersSortedByNumberEvents();

		return res;
	}

	public List<String[]> findManagersWithDebts() {
		List<String[]> res;

		res = this.managerRepository.findManagersWithDebts();

		return res;
	}
	private void checkPasswords(final String passwd1, final String passwd2, final BindingResult binding) {
		if (!passwd1.equals(passwd2) || (passwd1 == null || passwd2 == null))
			binding.rejectValue("userAccount.password", "chorbi.password.invalid");
	}
}

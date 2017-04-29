
package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Manager;

@Service
@Transactional
public class ManagerService {

	// Managed repository ---------------------------

	@Autowired
	private ManagerRepository	managerRepository;


	// Supporting services --------------------------

	// Constructor ----------------------------------

	public ManagerService() {
		super();
	}

	// Simple CRUD methods --------------------------

	// Other business methods -----------------------

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
}

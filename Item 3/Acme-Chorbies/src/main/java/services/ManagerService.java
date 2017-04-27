
package services;

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
	private ManagerRepository	manaagerRepository;


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

		res = this.manaagerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(res);

		return res;
	}
}

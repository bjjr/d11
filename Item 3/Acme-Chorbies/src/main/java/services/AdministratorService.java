
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;

@Service
@Transactional
public class AdministratorService {

	// Managed repository -----------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;


	// Constructors -----------------------------------------

	public AdministratorService() {
		super();
	}

	// Simple CRUD methods ----------------------------------

	public Administrator save(final Administrator admin) {
		Assert.notNull(admin);

		Administrator result;

		result = this.administratorRepository.save(admin);

		return result;
	}

	// Other business methods -------------------------------

	public Administrator findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Administrator result;

		result = this.administratorRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public Administrator findByPrincipal() {
		Administrator result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.administratorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

}

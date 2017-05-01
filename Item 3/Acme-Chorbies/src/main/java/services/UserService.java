
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.UserRepository;
import security.LoginService;
import security.UserAccount;
import domain.User;

@Service
@Transactional
public class UserService {

	// Managed repository ---------------------------

	@Autowired
	private UserRepository	userRepository;


	// Constructor ----------------------------------

	public UserService() {
		super();
	}

	public User save(final User user) {
		User res;

		res = this.userRepository.save(user);

		return res;
	}

	public User findOne(final int userId) {
		User res;

		res = this.userRepository.findOne(userId);

		return res;
	}

	// Other business methods -----------------------

	public User findByPrincipal() {
		User res;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		res = this.findByUserAccount(userAccount);

		return res;
	}

	private User findByUserAccount(final UserAccount userAccount) {
		User res;

		res = this.userRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(res);

		return res;
	}

}

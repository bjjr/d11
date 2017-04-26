
package security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import services.ActorService;
import services.ChorbiService;
import forms.UserAccountForm;

@Service
@Transactional
public class UserAccountService {

	// Managed repository --------------------
	@Autowired
	private UserAccountRepository	userAccountRepository;

	// Supporting services -------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ChorbiService			chorbiService;

	// Validator	--------------------------

	@Autowired
	private Validator				validator;


	// Constructors --------------------------

	public UserAccountService() {
		super();
	}

	// Simple CRUD methods -------------------

	public UserAccount create(final String authority) {
		Assert.notNull(authority);
		UserAccount res;
		Authority a;
		final Collection<Authority> authorities = new ArrayList<Authority>();

		a = new Authority();
		a.setAuthority(authority);
		authorities.add(a);

		res = new UserAccount();
		res.setAuthorities(authorities);

		return res;
	}

	public UserAccount save(final UserAccount ua) {
		Assert.notNull(ua);

		UserAccount res;
		res = this.userAccountRepository.save(ua);

		return res;
	}

	public UserAccount findByUsername(final String username) {
		Assert.notNull(username);
		UserAccount res;

		res = this.userAccountRepository.findByUsername(username);
		Assert.notNull(res);

		return res;
	}

	// Other business methods ----------------

	public UserAccount reconstruct(final UserAccountForm userAccountForm, final BindingResult binding) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));

		UserAccount principalua, res;
		String previousPasswd, hashedPasswd;

		principalua = this.actorService.findByPrincipal().getUserAccount();
		res = userAccountForm.getUserAccount();

		res.setId(principalua.getId());
		res.setVersion(principalua.getVersion());
		res.setUsername(principalua.getUsername());
		res.setAuthorities(new HashSet<>(principalua.getAuthorities()));

		previousPasswd = principalua.getPassword();
		hashedPasswd = this.chorbiService.hashPassword(userAccountForm.getPreviousPassword());

		if (!userAccountForm.getPassword().equals(userAccountForm.getPasswdConf()))
			binding.rejectValue("password", "chorbi.password.invalid");
		else if (!hashedPasswd.equals(previousPasswd))
			binding.rejectValue("previousPassword", "chorbi.password.invalid.previous");

		this.validator.validate(res, binding);

		return res;
	}
}

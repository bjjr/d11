
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed repository --------------------------------------

	@Autowired
	private ActorRepository	actorRepository;


	// Supporting services -------------------------------------

	// Constructor ---------------------------------------------

	public ActorService() {
		super();
	}

	// Simple CRUD methods -------------------------------------

	public Collection<Actor> findAll() {
		Collection<Actor> res;

		res = this.actorRepository.findAll();
		Assert.notNull(res, "The actors does not exist");

		return res;
	}

	public Actor findOne(final int actorId) {
		Actor res;

		res = this.actorRepository.findOne(actorId);
		Assert.notNull(res, "The actor does not exist");

		return res;
	}

	// Other business methods ----------------------------------

	public Actor findByPrincipal() {
		Actor result = null;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);

		return result;
	}

	public boolean checkAuthority(final String authority) {
		boolean result;
		Actor actor;
		Collection<Authority> authorities;

		result = false;

		try {
			actor = this.findByPrincipal();
			authorities = actor.getUserAccount().getAuthorities();

			for (final Authority a : authorities)
				if (a.getAuthority().equals(authority.toUpperCase())) {
					result = true;
					break;
				}
		} catch (final IllegalArgumentException e) {
			result = false;
		}

		return result;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Actor res;

		res = this.actorRepository.findByUserAccountId(userAccount.getId());

		Assert.notNull(res);

		return res;
	}

}

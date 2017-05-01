
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ChargeRepository;
import domain.Actor;
import domain.Charge;

@Service
@Transactional
public class ChargeService {

	// Managed repository -----------------------------------

	@Autowired
	private ChargeRepository	chargeRepository;

	// Supporting services ----------------------------------

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------

	public ChargeService() {
		super();
	}

	// Simple CRUD methods ----------------------------------

	public Charge create() {
		Charge result;

		result = new Charge();
		result.setPaid(false);

		return result;
	}

	public Charge save(final Charge charge) {
		Assert.notNull(charge);

		Charge result;

		result = this.chargeRepository.save(charge);

		return result;
	}

	public Charge findOne(final int chargeId) {
		Charge result;

		result = this.chargeRepository.findOne(chargeId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Charge> findAll() {
		Collection<Charge> result;

		result = this.chargeRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Charge> findChargesByUser() {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		final Collection<Charge> result;
		final Actor actor;

		actor = this.actorService.findByPrincipal();
		result = this.chargeRepository.findChargesByUser(actor.getId());

		return result;
	}

	public Charge pay(final Charge charge) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Assert.notNull(charge);

		final Charge result;

		charge.setPaid(true);
		result = this.chargeRepository.save(charge);

		return result;
	}

	// Other business methods -------------------------------

	public Double totalBenefit() {
		Double result;

		result = this.chargeRepository.totalBenefit();

		return result;
	}

	public Double totalDue() {
		Double result;

		result = this.chargeRepository.totalDue();

		return result;
	}

	public Double theoreticalBenefit() {
		Double result;

		result = this.chargeRepository.theoreticalBenefit();

		return result;
	}

}

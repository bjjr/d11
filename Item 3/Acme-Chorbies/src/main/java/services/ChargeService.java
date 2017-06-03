
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ChargeRepository;
import domain.Actor;
import domain.Charge;
import domain.Chorbi;
import domain.Manager;
import domain.User;

@Service
@Transactional
public class ChargeService {

	// Managed repository -----------------------------------

	@Autowired
	private ChargeRepository	chargeRepository;

	// Supporting services ----------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private FeeService			feeService;

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private CreditCardService	creditCardService;


	// Constructors -----------------------------------------

	public ChargeService() {
		super();
	}

	// Simple CRUD methods ----------------------------------

	public Charge create(final User user) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("MANAGER"));

		Charge result;
		Double amount;

		amount = 0.0;
		if (user instanceof Chorbi)
			amount = this.feeService.getFeeChorbies();
		else if (user instanceof Manager)
			amount = this.feeService.getFeeManagers();
		result = new Charge();
		result.setUser(user);
		result.setPaid(false);
		result.setAmount(amount);

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

	public Charge save(final Charge charge) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("MANAGER") || this.actorService.checkAuthority("CHORBI"));

		Assert.notNull(charge);

		Charge result;

		result = this.chargeRepository.save(charge);

		return result;
	}

	public Charge pay(final Charge charge) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Assert.notNull(charge);

		Assert.isTrue(!charge.isPaid(), "Cannot pay a charge already paid");

		final Charge result;
		int userId;

		userId = this.actorService.findByPrincipal().getId();
		Assert.isTrue(charge.getUser().getId() == userId, "Cannot pay a charge of another chorbi or a manager");
		Assert.isTrue(charge.getUser().getCreditCard() != null, "You should have a valid credit card");
		Assert.isTrue(this.creditCardService.isCreditCardDateValid(charge.getUser().getCreditCard()), "You should have a valid credit card");

		charge.setPaid(true);
		result = this.save(charge);

		return result;
	}

	public void flush() {
		this.chargeRepository.flush();
	}

	// Other business methods -------------------------------

	public void generateChargesToChorbies() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Charge created;
		Collection<Chorbi> chorbies;

		chorbies = this.chorbiService.findAll();

		for (final Chorbi c : chorbies) {
			created = this.create(c);
			this.save(created);
		}

	}
	public Double totalBenefit() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.chargeRepository.totalBenefit();

		return result;
	}

	public Double totalDue() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.chargeRepository.totalDue();

		return result;
	}

	public Double theoreticalBenefit() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.chargeRepository.theoreticalBenefit();

		return result;
	}

}


package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FeeRepository;
import domain.Fee;

@Service
@Transactional
public class FeeService {

	// Managed repository -----------------------------------

	@Autowired
	private FeeRepository	feeRepository;

	// Supporting services ----------------------------------

	@Autowired
	private ActorService	actorService;

	// Validator --------------------------------------------

	@Autowired
	private Validator		validator;


	// Constructors -----------------------------------------

	public FeeService() {
		super();
	}

	// Simple CRUD methods ----------------------------------

	public Fee findOne(final int feeId) {
		Fee result;

		result = this.feeRepository.findOne(feeId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Fee> findAll() {
		Collection<Fee> result;

		result = this.feeRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Fee save(final Fee fee) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Assert.notNull(fee);

		Fee result;

		result = this.feeRepository.save(fee);

		return result;
	}

	public void flush() {
		this.feeRepository.flush();
	}

	// Other business methods -------------------------------

	public Double getFeeManagers() {
		Double result;

		result = this.feeRepository.getFeeManagers();

		return result;
	}

	public Double getFeeChorbies() {
		Double result;

		result = this.feeRepository.getFeeChorbies();

		return result;
	}

	public Fee reconstruct(final Fee fee, final BindingResult bindingResult) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Fee aux, result;

		aux = this.findOne(fee.getId());
		result = fee;
		result.setFeeType(aux.getFeeType());

		this.validator.validate(result, bindingResult);

		return result;
	}

}

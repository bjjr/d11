
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChorbiRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Chorbi;
import domain.SearchTemplate;
import forms.ChorbiForm;

@Service
@Transactional
public class ChorbiService {

	// Managed repository ---------------------------

	@Autowired
	private ChorbiRepository		chorbiRepository;

	// Supporting services --------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SearchTemplateService	searchTemplateService;

	// Validator ------------------------------------

	@Autowired
	private Validator				validator;


	// Constructor ----------------------------------

	public ChorbiService() {
		super();
	}

	// Simple CRUD methods --------------------------

	public Chorbi create() {
		Assert.isTrue(!(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("ADMIN")), "Only a non registered user can use the create method");

		Chorbi res;

		res = new Chorbi();

		res.setSearchTemplate(this.searchTemplateService.create());

		return res;
	}

	public Chorbi save(final Chorbi chorbi) {
		Chorbi res;
		SearchTemplate savedSt;
		String initialPasswd, encodedPasswd;

		if (chorbi.getId() == 0) {
			initialPasswd = chorbi.getUserAccount().getPassword();
			encodedPasswd = this.hashPassword(initialPasswd);

			chorbi.getUserAccount().setPassword(encodedPasswd);

			savedSt = this.searchTemplateService.save(chorbi.getSearchTemplate());
			chorbi.setSearchTemplate(savedSt);
		}

		res = this.chorbiRepository.save(chorbi);

		return res;
	}

	public Chorbi findOne(final int chorbiId) {
		Assert.isTrue(chorbiId != 0);

		Chorbi res;

		res = this.chorbiRepository.findOne(chorbiId);

		return res;
	}

	public Chorbi findOneToEdit(final int chorbiId) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));
		Assert.isTrue(chorbiId != 0);
		Assert.isTrue(this.actorService.findByPrincipal().getId() == chorbiId);

		Chorbi res;

		res = this.chorbiRepository.findOne(chorbiId);

		return res;
	}

	public Collection<Chorbi> findAll() {
		Collection<Chorbi> res;

		res = this.chorbiRepository.findAll();

		return res;
	}

	public void flush() {
		this.chorbiRepository.flush();
	}

	// Other business methods -----------------------

	public Chorbi findByPrincipal() {
		Chorbi res;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		res = this.findByUserAccount(userAccount);

		return res;
	}

	private Chorbi findByUserAccount(final UserAccount userAccount) {
		Chorbi res;

		res = this.chorbiRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(res);

		return res;
	}

	public String hashPassword(final String password) {
		String res;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		res = encoder.encodePassword(password, null);

		return res;
	}

	/*
	 * Reconstruct for pruned object. Used in profile edition.
	 * A chorbi can only edit his/her email, phone, picture, description, and/or relationship.
	 */

	public Chorbi reconstruct(final Chorbi chorbi, final BindingResult binding) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));
		final Chorbi res;
		Chorbi principal;
		String relationship;

		/*
		 * Avoid form modification
		 */

		relationship = chorbi.getRelationship();
		Assert.isTrue(relationship.equals("LOVE") || relationship.equals("ACTIVITIES") || relationship.equals("FRIENDSHIP") || relationship.equals(""), "Invalid value detected");

		res = chorbi;
		principal = this.findByPrincipal();

		// Retrieve principal's attributes

		res.setId(principal.getId());
		res.setVersion(principal.getVersion());
		res.setName(principal.getName());
		res.setSurname(principal.getSurname());
		res.setBirthdate(principal.getBirthdate());
		res.setGender(principal.getGender());
		res.setCoordinates(principal.getCoordinates());
		res.setCreditCard(principal.getCreditCard());
		res.setSearchTemplate(principal.getSearchTemplate());
		res.setUserAccount(principal.getUserAccount());

		this.validator.validate(chorbi, binding);

		return res;
	}

	/*
	 * Reconstruct method. Used in registration forms.
	 */

	public Chorbi reconstruct(final ChorbiForm chorbiForm, final BindingResult binding) {
		Chorbi res;
		String gender, relationship;

		/*
		 * Avoid form modification
		 */

		gender = chorbiForm.getGender();
		relationship = chorbiForm.getRelationship();

		Assert.isTrue(gender.equals("MAN") || gender.equals("WOMAN") || gender.equals(""), "Invalid value detected");
		Assert.isTrue(relationship.equals("LOVE") || relationship.equals("ACTIVITIES") || relationship.equals("FRIENDSHIP") || relationship.equals(""), "Invalid value detected");

		res = chorbiForm.getChorbi();

		this.checkAge(res.getBirthdate(), binding);
		this.checkPasswords(chorbiForm.getUserAccount().getPassword(), chorbiForm.getPasswdConfirmation(), binding);

		this.validator.validate(res, binding);

		return res;
	}

	private void checkAge(final Date birthdate, final BindingResult binding) {
		DateTime now, birth;
		Integer years;

		if (birthdate == null)
			binding.rejectValue("birthdate", "javax.validation.constraints.NotNull.message");
		else {
			birth = new DateTime(birthdate.getTime());
			now = new DateTime();

			years = Years.yearsBetween(birth, now).getYears();

			if (years < 18)
				binding.rejectValue("birthdate", "chorbi.birthdate.invalid");
		}
	}

	private void checkPasswords(final String passwd1, final String passwd2, final BindingResult binding) {
		if (!passwd1.equals(passwd2) || (passwd1 == null || passwd2 == null))
			binding.rejectValue("userAccount.password", "chorbi.password.invalid");
	}

	/**
	 * Given a text this method masks sensible data in order to not display it to other users.
	 * 
	 * @param text
	 *            The text to analyse
	 * @return The same text with sensible data masked with asterisks
	 */

	public String maskSensibleData(final String text) {
		final String phoneRegex = "(\\+\\d{1,4})?[\\(\\)\\-\\d\\sA-Z]+\\s";
		final String emailRegex = "([\\w\\.]+)@([\\w\\.]+)\\.(\\w+)";

		String res;

		res = text.replaceAll(phoneRegex, "*** ");
		res = res.replaceAll(emailRegex, "***");

		return res;
	}

	public Double findAvgAgeChorbies() {
		Double result;

		result = this.chorbiRepository.findAvgAgeChorbies();

		return result;
	}

	public Double findMaxAgeChorbies() {
		Double result;

		result = this.chorbiRepository.findMaxAgeChorbies();

		return result;
	}

	public Double findMinAgeChorbies() {
		Double result;

		result = this.chorbiRepository.findMinAgeChorbies();

		return result;
	}

	public Double findRatioChorbiesNoCCInvCC() {
		Double result;

		result = this.chorbiRepository.findRatioChorbiesNoCCInvCC();

		return result;
	}

	public Double findRatioChorbiesSearchAct() {
		Double result;

		result = this.chorbiRepository.findRatioChorbiesSearchAct();

		return result;
	}

	public Double findRatioChorbiesSearchFriend() {
		Double result;

		result = this.chorbiRepository.findRatioChorbiesSearchFriend();

		return result;
	}

	public Double findRatioChorbiesSearchLove() {
		Double result;

		result = this.chorbiRepository.findRatioChorbiesSearchLove();

		return result;
	}

	public Collection<Chorbi> findChorbiesSortNumLikes() {
		Collection<Chorbi> result;

		result = this.chorbiRepository.findChorbiesSortNumLikes();

		return result;
	}

	public Collection<Chorbi> findChorbiesMoreChirpsRec() {
		Collection<Chorbi> result;

		result = this.chorbiRepository.findChorbiesMoreChirpsRec();

		return result;
	}

	public Collection<Chorbi> findChorbiesMoreChirpsSend() {
		Collection<Chorbi> result;

		result = this.chorbiRepository.findChorbiesMoreChirpsSend();

		return result;
	}

	public void ban(final Chorbi chorbi) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Collection<Authority> authorities;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("BANNED");
		authorities = new HashSet<>();
		authorities.add(authority);

		chorbi.getUserAccount().setAuthorities(authorities);
		this.save(chorbi);
	}

	public void unban(final Chorbi chorbi) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Collection<Authority> authorities;
		Authority authority;

		authority = new Authority();
		authority.setAuthority("CHORBI");
		authorities = new HashSet<>();
		authorities.add(authority);

		chorbi.getUserAccount().setAuthorities(authorities);
		this.save(chorbi);
	}

	public Collection<Chorbi> findNonBannedChorbies() {
		Collection<Chorbi> res;

		res = this.chorbiRepository.findNonBannedChorbies(this.findByPrincipal().getId());

		return res;
	}

	public List<String[]> findNumberOfChorbiesPerCountry() {
		List<String[]> res;

		res = this.chorbiRepository.findNumberOfChorbiesPerCountry();

		return res;
	}

	public List<String[]> findNumberOfChorbiesPerCity() {
		List<String[]> res;

		res = this.chorbiRepository.findNumberOfChorbiesPerCity();

		return res;
	}

	public List<Integer> findAllId() {
		List<Integer> res;

		res = this.chorbiRepository.findAllId();

		return res;
	}

	public Collection<Chorbi> findChorbiesSortedByNumberEvents() {
		Collection<Chorbi> res;

		res = this.chorbiRepository.findChorbiesSortedByNumberEvents();

		return res;
	}

	public List<String[]> findChorbiesWithDebts() {
		List<String[]> res;

		res = this.chorbiRepository.findChorbiesWithDebts();

		return res;
	}

	public Collection<Chorbi> findChorbieSortedByAvgStars() {
		Assert.isTrue(this.actorService.checkAuthority(Authority.ADMIN));

		Collection<Chorbi> res;

		res = this.chorbiRepository.findChorbieSortedByAvgStars();

		return res;
	}

	public Collection<Chorbi> findChorbiesSortNumStars() {
		Collection<Chorbi> result;

		result = this.chorbiRepository.findChorbiesSortNumStars();

		return result;
	}

}

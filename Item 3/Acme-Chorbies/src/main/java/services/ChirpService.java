
package services;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.Payload;
import javax.validation.constraints.Pattern.Flag;

import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.internal.constraintvalidators.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChirpRepository;
import security.Authority;
import domain.Chirp;
import domain.User;

@Service
@Transactional
public class ChirpService {

	// Managed repository -----------------------------------

	@Autowired
	private ChirpRepository	chirpRepository;

	// Supporting services ----------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private UserService		userService;

	// Validator --------------------------------------------

	@Autowired
	private Validator		validator;


	// Constructors -----------------------------------------

	public ChirpService() {
		super();
	}

	// Simple CRUD methods ----------------------------------

	public Chirp create() {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Chirp result;
		Date moment;
		Collection<String> attachments;

		attachments = new ArrayList<>();
		moment = new Date(System.currentTimeMillis() - 1000);

		result = new Chirp();

		result.setAttachments(attachments);
		result.setMoment(moment);
		result.setCopy(false);

		return result;
	}

	public Chirp create(final int recipientId) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Chirp result;
		Date moment;
		User sender;
		User recipient;
		Collection<String> attachments;
		Authority authority;

		authority = new Authority();
		authority.setAuthority(Authority.CHORBI);
		result = new Chirp();
		attachments = new ArrayList<String>();
		sender = this.userService.findByPrincipal();
		recipient = this.userService.findOne(recipientId);
		Assert.isTrue(!sender.equals(recipient), "Cannot send a chirp to you");
		Assert.isTrue(recipient.getUserAccount().getAuthorities().contains(authority), "You only can send chirps to chorbies");
		moment = new Date(System.currentTimeMillis() - 1000);
		result.setAttachments(attachments);
		result.setCopy(false);
		result.setMoment(moment);
		result.setRecipient(recipient);
		result.setSender(sender);
		result.setSubject("");
		result.setText("");

		return result;
	}

	public Chirp findOne(final int chirpId) {
		Chirp result;

		result = this.chirpRepository.findOne(chirpId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Chirp> findAll() {
		Collection<Chirp> result;

		result = this.chirpRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	// This method finds, using a query, the chirps sent by an user

	public Collection<Chirp> findChirpsSent() {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Collection<Chirp> chirpsSent;
		User user;

		user = this.userService.findByPrincipal();
		chirpsSent = this.chirpRepository.findChirpsSent(user.getId());

		return chirpsSent;
	}

	// This method finds, using a query, the chirps received by an user

	public Collection<Chirp> findChirpsReceived() {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Collection<Chirp> chirpsReceived;
		User user;

		user = this.userService.findByPrincipal();
		chirpsReceived = this.chirpRepository.findChirpsReceived(user.getId());

		return chirpsReceived;
	}

	public Chirp send(final Chirp chirp) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Assert.notNull(chirp);

		Chirp result;
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1000);
		chirp.setMoment(moment);
		chirp.setCopy(false);

		result = this.chirpRepository.save(chirp);

		return result;
	}

	// This method save a copy of a chirp for the recipient of this chirp

	public Chirp saveCopy(final Chirp originalChirp) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Chirp aux;
		Chirp result;

		aux = new Chirp();

		aux.setCopy(true);
		aux.setAttachments(new ArrayList<>(originalChirp.getAttachments()));
		aux.setMoment(originalChirp.getMoment());
		aux.setRecipient(originalChirp.getRecipient());
		aux.setSender(originalChirp.getSender());
		aux.setSubject(originalChirp.getSubject());
		aux.setText(originalChirp.getText());
		result = this.chirpRepository.save(aux);

		return result;
	}

	public void delete(final Chirp chirp) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Assert.notNull(chirp);
		Assert.isTrue(chirp.getId() != 0);

		Assert.isTrue(this.chirpRepository.exists(chirp.getId()));

		User principal;

		principal = this.userService.findByPrincipal();

		Assert.isTrue((chirp.getSender().equals(principal) && chirp.getCopy() == false) || (chirp.getRecipient().equals(principal) && chirp.getCopy() == true));

		this.chirpRepository.delete(chirp);
	}

	public void flush() {
		this.chirpRepository.flush();
	}

	// Other business methods -------------------------------

	public Chirp reply(final int chirpId) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Chirp originalChirp;
		Chirp result;

		originalChirp = this.findOne(chirpId);
		Assert.isTrue(originalChirp.getCopy(), "Cannot reply a chirp send by you");
		Assert.isTrue(this.userService.findByPrincipal().equals(originalChirp.getRecipient()), "Cannot reply a chirp of other chorbies");

		result = this.create(originalChirp.getSender().getId());
		if (!originalChirp.getSubject().contains("RE:"))
			result.setSubject("RE: " + originalChirp.getSubject());
		else
			result.setSubject(originalChirp.getSubject());

		return result;
	}

	public Chirp resend(final int chirpId) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));

		Chirp originalChirp;
		Chirp result;
		User sender;
		Date moment;

		originalChirp = this.findOne(chirpId);
		sender = this.userService.findByPrincipal();
		moment = new Date(System.currentTimeMillis() - 1000);
		Assert.isTrue(!originalChirp.getCopy(), "Cannot resend a chirp received by you");

		result = new Chirp();
		result.setSender(sender);
		result.setMoment(moment);
		result.setCopy(false);
		result.setAttachments(new ArrayList<>(originalChirp.getAttachments()));
		if (!originalChirp.getSubject().contains("FW:"))
			result.setSubject("FW: " + originalChirp.getSubject());
		else
			result.setSubject(originalChirp.getSubject());
		result.setText(originalChirp.getText());

		return result;
	}

	public String getNameRecipient(final User recipient) {
		String result;

		result = recipient.getName() + " " + recipient.getSurname() + " (" + recipient.getUserAccount().getUsername() + ")";

		return result;
	}

	public Chirp reconstruct(final Chirp chirp, final BindingResult bindingResult, final int recipientId) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));
		Chirp result;
		User sender;
		User recipient;

		result = chirp;
		sender = this.userService.findByPrincipal();
		recipient = this.userService.findOne(recipientId);
		result.setCopy(false);
		result.setSender(sender);
		result.setRecipient(recipient);
		result.setMoment(new Date(System.currentTimeMillis() - 1000));

		this.validateURLs(result.getAttachments(), bindingResult);
		this.validator.validate(result, bindingResult);

		return result;
	}

	public Chirp reconstructToResend(final Chirp chirp, final BindingResult bindingResult) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI") || this.actorService.checkAuthority("MANAGER"));
		Chirp result;
		User sender;

		result = chirp;
		sender = this.userService.findByPrincipal();
		result.setCopy(false);
		result.setSender(sender);
		result.setMoment(new Date(System.currentTimeMillis() - 1000));

		this.validateURLs(result.getAttachments(), bindingResult);
		this.validator.validate(result, bindingResult);

		return result;
	}

	private void validateURLs(final Collection<String> attachments, final BindingResult binding) {
		URLValidator validator;

		validator = new URLValidator();

		validator.initialize(new URL() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String regexp() {
				return ".*";
			}

			@Override
			public String protocol() {
				return "";
			}

			@Override
			public int port() {
				return -1;
			}

			@Override
			public Class<? extends Payload>[] payload() {
				return null;
			}

			@Override
			public String message() {
				return "org.hibernate.validator.constraints.URL.message";
			}

			@Override
			public String host() {
				return "";
			}

			@Override
			public Class<?>[] groups() {
				return null;
			}

			@Override
			public Flag[] flags() {
				return null;
			}
		});

		for (final String s : attachments)
			if (!validator.isValid(s, null)) {
				binding.rejectValue("attachments", "org.hibernate.validator.constraints.URL.message");
				break;
			}
	}

	public Double findAvgChirpsRecPerChorbi() {
		Double result;

		result = this.chirpRepository.findAvgChirpsRecPerChorbi();

		return result;
	}

	public Long findMaxChirpsRecPerChorbi() {
		Long result;
		List<Long> cmaxrec;

		result = 0L;
		cmaxrec = (List<Long>) this.chirpRepository.findMaxChirpsRecPerChorbi();

		if (!cmaxrec.isEmpty())
			result = cmaxrec.get(0);

		return result;
	}

	public Long findMinChirpsRecPerChorbi() {
		Long result;
		List<Integer> allChorbiesId, allChorbiesRecieveId;
		List<Long> cminrec;

		allChorbiesId = this.chorbiService.findAllId();
		allChorbiesRecieveId = this.chirpRepository.findAllChorbiesWhoReceiveChirp();

		if (!allChorbiesRecieveId.containsAll(allChorbiesId))
			return 0L;

		result = 0L;
		cminrec = (List<Long>) this.chirpRepository.findMinChirpsRecPerChorbi();

		if (!cminrec.isEmpty())
			result = cminrec.get(0);

		return result;
	}

	public Double findAvgChirpsSendPerChorbi() {
		Double result;

		result = this.chirpRepository.findAvgChirpsSendPerChorbi();

		return result;
	}

	public Long findMaxChirpsSendPerChorbi() {
		Long result;
		List<Long> cmaxsend;

		result = 0L;
		cmaxsend = (List<Long>) this.chirpRepository.findMaxChirpsSendPerChorbi();

		if (!cmaxsend.isEmpty())
			result = cmaxsend.get(0);

		return result;
	}

	public Long findMinChirpsSendPerChorbi() {
		Long result;
		List<Integer> allChorbiesId, allChorbiesSendersId;
		List<Long> cminsend;

		allChorbiesId = this.chorbiService.findAllId();
		allChorbiesSendersId = this.chirpRepository.findAllChorbiesWhoSendChirp();

		if (!allChorbiesSendersId.containsAll(allChorbiesId))
			return 0L;

		result = 0L;
		cminsend = (List<Long>) this.chirpRepository.findMinChirpsSendPerChorbi();

		if (!cminsend.isEmpty())
			result = cminsend.get(0);

		return result;
	}
}

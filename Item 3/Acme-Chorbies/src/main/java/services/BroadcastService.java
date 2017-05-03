
package services;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BroadcastRepository;
import security.Authority;
import domain.Broadcast;
import domain.Chirp;
import domain.Chorbi;
import domain.Manager;

@Service
@Transactional
public class BroadcastService {

	// Managed repository ---------------------------

	@Autowired
	private BroadcastRepository	broadcastRepository;

	// Supporting services --------------------------

	@Autowired
	private ChirpService		chirpService;

	@Autowired
	private UserService			userService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private EventService		eventService;

	@Autowired
	private Validator			validator;


	// Constructor ----------------------------------

	public BroadcastService() {
		super();
	}

	// Simple CRUD methods --------------------------

	public Broadcast create() {
		Broadcast res;

		res = new Broadcast();

		return res;
	}

	public Broadcast save(final Broadcast broadcast) {
		final Authority managerAuthority = new Authority();
		managerAuthority.setAuthority(Authority.MANAGER);

		Assert.isTrue(this.userService.findByPrincipal().getUserAccount().getAuthorities().contains(managerAuthority), "BroadcastService.save: You need to be manager in order to save a broadcast");
		Assert.isTrue(broadcast.getUninformedChorbies().containsAll(this.eventService.findChorbiesByManagerEvents(broadcast.getManager())), "BroadcastService.save: All the Chorbies must be contained in the chorbies that are going to events");

		return this.broadcastRepository.save(broadcast);
	}

	public Broadcast reconstruct(final Broadcast broadcast, final BindingResult bindingResult) {
		final Manager manager = this.managerService.findByPrincipal();
		final Collection<Chorbi> allChorbiesInManagerEvents = this.eventService.findChorbiesByManagerEvents(manager);

		final Broadcast broadcastRes = this.create();

		broadcastRes.setSubject(broadcast.getSubject());
		broadcastRes.setText(broadcast.getText());

		broadcastRes.setManager(manager);
		broadcastRes.setUninformedChorbies(allChorbiesInManagerEvents);

		this.validator.validate(broadcastRes, bindingResult);
		return broadcastRes;
	}
	/*
	 * If all chorbies have been notified about a modification
	 * in an event, then the broadcast is deleted.
	 */

	public Broadcast update(final Broadcast broadcast) {
		Broadcast res;

		res = null;

		if (broadcast.getUninformedChorbies().isEmpty())
			this.broadcastRepository.delete(broadcast);
		else
			res = this.broadcastRepository.save(broadcast);

		return res;
	}

	// Other business methods -----------------------

	/**
	 * Creates a chirp using the data stored in a broadcast ready to be sent as
	 * a copy.
	 * 
	 * @param broadcast
	 *            The broadcast with data
	 * @param chorbi
	 *            The recipient of the new chirp
	 * @return
	 */

	private Chirp getChirp(final Broadcast broadcast, final Chorbi chorbi) {
		Chirp res;

		res = this.chirpService.create();

		res.setSubject(broadcast.getSubject());
		res.setText(broadcast.getText());
		res.setMoment(new Date(System.currentTimeMillis() - 1000));
		res.setSender(broadcast.getManager());
		res.setRecipient(chorbi);

		return res;
	}

	/**
	 * Checks if the chorbi needs to receive any new broadcasts and
	 * sends the notifications as chirps to his/her inbox.
	 * 
	 * @param chorbi
	 *            The chorbi who needs to be notified.
	 */

	public void findNewBroadcasts(final Chorbi chorbi) {
		Collection<Broadcast> broadcasts;
		Collection<Chirp> chirps;

		broadcasts = this.findNonReceivedBroadcasts(chorbi);
		chirps = new LinkedList<>();

		for (final Broadcast b : broadcasts) {
			Chirp chirp;

			chirp = this.getChirp(b, chorbi);
			chirps.add(chirp);

			b.getUninformedChorbies().remove(chorbi);
			this.update(b);
			this.userService.save(b.getManager());
		}

		for (final Chirp c : chirps)
			this.chirpService.saveCopy(c);
	}

	public Collection<Broadcast> findNonReceivedBroadcasts(final Chorbi chorbi) {
		Collection<Broadcast> res;

		res = this.broadcastRepository.findNonReceivedBroadcasts(chorbi);

		return res;
	}

}

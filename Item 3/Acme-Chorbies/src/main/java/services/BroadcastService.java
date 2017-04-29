
package services;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.BroadcastRepository;
import domain.Broadcast;
import domain.Chirp;
import domain.Chorbi;

@Service
@Transactional
public class BroadcastService {

	// Managed repository ---------------------------

	@Autowired
	private BroadcastRepository	broadcastRepository;

	// Supporting services --------------------------

	@Autowired
	private ChirpService		chirpService;


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

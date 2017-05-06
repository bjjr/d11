
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BroadcastService;
import services.ChirpService;
import services.ChorbiService;
import domain.Chirp;
import domain.Chorbi;

@Controller
@RequestMapping("/chirp")
public class ChirpController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private ChirpService		chirpService;

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private BroadcastService	broadcastService;

	@Autowired
	private ActorService		actorService;

	private boolean				isReply;

	private boolean				isResend;

	private int					recipientId;


	// Constructors -------------------------------------------

	public ChirpController() {
		super();
	}

	// Creating -----------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int recipientId) {
		ModelAndView result;
		Chirp chirp;
		Chorbi recipient;
		String infoRecipient;

		this.isReply = false;
		this.isResend = false;

		chirp = this.chirpService.create(recipientId);
		this.setRecipientId(recipientId);
		recipient = this.chorbiService.findOne(this.getRecipientId());
		infoRecipient = this.chirpService.getNameRecipient(recipient);
		result = this.createEditModelAndView(chirp);
		result.addObject("infoRecipient", infoRecipient);

		return result;
	}

	// Replying -----------------------------------------------

	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public ModelAndView reply(@RequestParam final int chirpId) {
		ModelAndView result;
		Chirp chirp;
		Chorbi recipient;
		String infoRecipient;

		this.isReply = true;
		this.isResend = false;

		chirp = this.chirpService.reply(chirpId);
		result = this.createEditModelAndView(chirp);
		this.setRecipientId(chirp.getRecipient().getId());
		recipient = this.chorbiService.findOne(this.getRecipientId());
		infoRecipient = this.chirpService.getNameRecipient(recipient);
		result.addObject("infoRecipient", infoRecipient);

		return result;
	}

	// Resending ----------------------------------------------

	@RequestMapping(value = "/resend", method = RequestMethod.GET)
	public ModelAndView resend(@RequestParam final int chirpId) {
		ModelAndView result;
		Chirp chirp;

		this.isReply = false;
		this.isResend = true;

		chirp = this.chirpService.resend(chirpId);
		result = this.createEditModelAndView(chirp);

		return result;
	}

	// Listing ------------------------------------------------

	@RequestMapping(value = "/listSent", method = RequestMethod.GET)
	public ModelAndView listSent() {
		final ModelAndView result;
		Collection<Chirp> chirps;
		Boolean isSent;

		chirps = this.chirpService.findChirpsSent();
		isSent = true;

		result = new ModelAndView("chirp/list");
		result.addObject("chirps", chirps);
		result.addObject("isSent", isSent);
		result.addObject("requestURI", "chirp/listSent.do");

		return result;
	}

	@RequestMapping(value = "/listReceived", method = RequestMethod.GET)
	public ModelAndView listReceived() {
		final ModelAndView result;
		Collection<Chirp> chirps;
		Boolean isSent;

		// Check for new broadcasts

		if (this.actorService.checkAuthority("CHORBI"))
			this.broadcastService.findNewBroadcasts(this.chorbiService.findByPrincipal());

		chirps = this.chirpService.findChirpsReceived();
		isSent = false;

		result = new ModelAndView("chirp/list");
		result.addObject("chirps", chirps);
		result.addObject("isSent", isSent);
		result.addObject("requestURI", "chirp/listReceived.do");

		return result;
	}

	// Sending ------------------------------------------------

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "send")
	public ModelAndView send(final Chirp chirp, final BindingResult binding) {
		ModelAndView result;
		Chirp reconstructed, saved;
		this.isReply = false;
		this.isResend = false;

		reconstructed = this.chirpService.reconstruct(chirp, binding, this.getRecipientId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(chirp);
		else
			try {
				saved = this.chirpService.send(reconstructed);
				this.chirpService.saveCopy(saved);
				result = new ModelAndView("redirect:/chirp/listSent.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chirp, "misc.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "reply")
	public ModelAndView reply(final Chirp chirp, final BindingResult binding) {
		ModelAndView result;
		Chirp reconstructed, saved;
		this.isReply = true;
		this.isResend = false;

		reconstructed = this.chirpService.reconstruct(chirp, binding, this.getRecipientId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(chirp);
		else
			try {
				saved = this.chirpService.send(reconstructed);
				this.chirpService.saveCopy(saved);
				result = new ModelAndView("redirect:/chirp/listSent.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chirp, "misc.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "resend")
	public ModelAndView resend(final Chirp chirp, final BindingResult binding) {
		ModelAndView result;
		Chirp reconstructed, saved;
		this.isReply = false;
		this.isResend = true;

		reconstructed = this.chirpService.reconstructToResend(chirp, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(chirp);
		else
			try {
				saved = this.chirpService.send(reconstructed);
				this.chirpService.saveCopy(saved);
				result = new ModelAndView("redirect:/chirp/listSent.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chirp, "misc.commit.error");
			}

		return result;
	}

	// Deleting -----------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int chirpId) {
		ModelAndView result;
		Chirp chirp;
		Boolean isChirpSent;

		isChirpSent = false;
		chirp = this.chirpService.findOne(chirpId);
		if (!chirp.getCopy())
			isChirpSent = true;

		try {
			this.chirpService.delete(chirp);
			if (isChirpSent)
				result = new ModelAndView("redirect:/chirp/listSent.do");
			else
				result = new ModelAndView("redirect:/chirp/listReceived.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(chirp, "misc.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------

	protected ModelAndView createEditModelAndView(final Chirp chirp) {
		ModelAndView result;

		result = this.createEditModelAndView(chirp, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Chirp chirp, final String message) {
		ModelAndView result;

		result = new ModelAndView("chirp/create");
		result.addObject("chirp", chirp);
		result.addObject("message", message);
		result.addObject("isReply", this.isReply);
		result.addObject("isResend", this.isResend);

		if (this.isResend) {
			Collection<Chorbi> chorbies;
			Chorbi principal;

			chorbies = this.chorbiService.findAll();
			principal = this.chorbiService.findByPrincipal();
			chorbies.remove(principal);
			result.addObject("chorbies", chorbies);
		}

		return result;
	}

	public int getRecipientId() {
		return this.recipientId;
	}

	public void setRecipientId(final int recipientId) {
		this.recipientId = recipientId;
	}

}

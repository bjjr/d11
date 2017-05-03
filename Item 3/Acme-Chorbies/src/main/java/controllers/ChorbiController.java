
package controllers;

import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.ChorbiLikeService;
import services.ChorbiService;
import domain.Actor;
import domain.Chorbi;
import forms.ChorbiForm;

@Controller
@RequestMapping("/chorbi")
public class ChorbiController extends AbstractController {

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ChorbiLikeService	chorbiLikeService;


	// Constructors ---------------------------------

	public ChorbiController() {
		super();
	}

	// List -----------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Chorbi> chorbies;
		Collection<Chorbi> liked;
		Actor principal;
		Authority bannedAuth;
		Authority adminAuth;

		principal = this.actorService.findByPrincipal();
		liked = null;
		bannedAuth = new Authority();
		bannedAuth.setAuthority("BANNED");
		adminAuth = new Authority();
		adminAuth.setAuthority("ADMIN");

		// Only the administrator can list banned and non-banned chorbies

		if (principal.getUserAccount().getAuthorities().contains(adminAuth))
			chorbies = this.chorbiService.findAll();
		else {
			chorbies = this.chorbiService.findNonBannedChorbies();
			liked = this.chorbiLikeService.findLiked(this.chorbiService.findByPrincipal());
		}

		res = new ModelAndView("chorbi/list");

		res.addObject("chorbies", chorbies);
		res.addObject("requestURI", "chorbi/list.do");
		res.addObject("liked", liked);
		res.addObject("bannedAuth", bannedAuth);

		return res;
	}

	// List chorbis who liked me --------------------

	@RequestMapping(value = "/listChorbiesLikedMe", method = RequestMethod.GET)
	public ModelAndView listChorbiesLikedMe() {
		ModelAndView res;
		final Collection<Chorbi> likers;
		Collection<Chorbi> liked;
		Chorbi principal;
		Authority bannedAuth;

		principal = this.chorbiService.findByPrincipal();

		if (principal.getCreditCard() == null) {
			res = new ModelAndView("redirect:/creditCard/display.do?showWarning=true");
			return res;
		}

		bannedAuth = new Authority();
		bannedAuth.setAuthority("BANNED");

		likers = this.chorbiLikeService.findChorbisByLiked(principal.getId());
		liked = this.chorbiLikeService.findLiked(this.chorbiService.findByPrincipal());

		res = new ModelAndView("chorbi/list");

		res.addObject("chorbies", likers);
		res.addObject("requestURI", "chorbi/listChorbiesLikedMe.do");
		res.addObject("liked", liked);
		res.addObject("bannedAuth", bannedAuth);

		return res;
	}

	// Display --------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int chorbiId) {
		ModelAndView res;
		Chorbi chorbi;
		String maskedDesc;
		final DateTime now, birthdate;
		int years;

		chorbi = this.chorbiService.findOne(chorbiId);
		res = new ModelAndView("chorbi/display");
		maskedDesc = this.chorbiService.maskSensibleData(chorbi.getDescription());
		now = DateTime.now();
		birthdate = new DateTime(chorbi.getBirthdate());
		years = Years.yearsBetween(birthdate, now).getYears();

		res.addObject("chorbi", chorbi);
		res.addObject("maskedDesc", maskedDesc);
		res.addObject("age", years);

		return res;
	}

	// Registration ---------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		ChorbiForm chorbiForm;

		chorbiForm = new ChorbiForm();
		res = this.createEditModelAndView(chorbiForm);

		return res;
	}

	// Edit -----------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView res;
		Chorbi principal;

		principal = this.chorbiService.findByPrincipal();
		res = this.createEditModelAndView(principal);

		return res;
	}

	// Save -----------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ChorbiForm chorbiForm, final BindingResult binding) {
		ModelAndView res;
		Chorbi reconstructed;

		reconstructed = this.chorbiService.reconstruct(chorbiForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(chorbiForm);
		else
			try {
				this.chorbiService.save(reconstructed);
				res = new ModelAndView("redirect:/");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(chorbiForm, "misc.commit.error");
			}

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Chorbi chorbi, final BindingResult binding) {
		ModelAndView res;
		Chorbi reconstructed;

		reconstructed = this.chorbiService.reconstruct(chorbi, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(chorbi);
		else
			try {
				this.chorbiService.save(reconstructed);
				res = new ModelAndView("redirect:display.do?chorbiId=" + reconstructed.getId());
			} catch (final Throwable th) {
				res = this.createEditModelAndView(chorbi, "misc.commit.error");
			}

		return res;
	}

	// Ancillary methods ----------------------------

	protected ModelAndView createEditModelAndView(final ChorbiForm chorbiForm) {
		ModelAndView result;

		result = this.createEditModelAndView(chorbiForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ChorbiForm chorbiForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("chorbi/edit");

		result.addObject("action", "chorbi/register.do");
		result.addObject("modelAttribute", "chorbiForm");
		result.addObject("chorbiForm", chorbiForm);
		result.addObject("message", message);
		result.addObject("isEdit", false);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Chorbi chorbi) {
		ModelAndView result;

		result = this.createEditModelAndView(chorbi, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Chorbi chorbi, final String message) {
		ModelAndView result;

		result = new ModelAndView("chorbi/edit");

		result.addObject("action", "chorbi/edit.do");
		result.addObject("modelAttribute", "chorbi");
		result.addObject("chorbi", chorbi);
		result.addObject("message", message);
		result.addObject("isEdit", true);

		return result;
	}

}


package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import domain.Manager;
import forms.ManagerForm;

@Controller
@RequestMapping("/acmemanager")
public class ManagerController extends AbstractController {

	@Autowired
	private ManagerService	managerService;


	// Constructors ---------------------------------

	public ManagerController() {
		super();
	}

	// Registration ---------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		ManagerForm managerForm;

		managerForm = new ManagerForm();
		res = this.createEditModelAndView(managerForm);

		return res;
	}

	// Edit -----------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView res;
		ManagerForm principal;

		principal = new ManagerForm(this.managerService.findByPrincipal());
		res = this.createEditModelAndView(principal);

		return res;
	}

	// Save -----------------------------------------

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ManagerForm managerForm, final BindingResult binding) {
		ModelAndView res;
		Manager reconstructed;

		reconstructed = this.managerService.reconstruct(managerForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(managerForm);
		else
			try {
				this.managerService.save(reconstructed);
				res = new ModelAndView("redirect:/");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(managerForm, "misc.commit.error");
			}

		return res;
	}

	// Ancillary methods ----------------------------

	protected ModelAndView createEditModelAndView(final ManagerForm managerForm) {
		ModelAndView result;

		result = this.createEditModelAndView(managerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ManagerForm managerForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("manager/edit");

		result.addObject("action", "acmemanager/save.do");
		result.addObject("modelAttribute", "managerForm");
		result.addObject("managerForm", managerForm);
		result.addObject("message", message);
		result.addObject("isEdit", false);

		return result;
	}

}

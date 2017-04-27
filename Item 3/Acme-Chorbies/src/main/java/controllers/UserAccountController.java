
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.UserAccount;
import security.UserAccountService;
import services.ChorbiService;
import forms.UserAccountForm;

@Controller
@RequestMapping("/userAccount")
public class UserAccountController extends AbstractController {

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ChorbiService		chorbiService;


	// Constructors ---------------------------------

	public UserAccountController() {
		super();
	}

	// Edit	-----------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView res;
		final UserAccountForm userAccountForm;

		userAccountForm = new UserAccountForm();
		res = this.createEditModelAndView(userAccountForm);

		return res;
	}

	// Save -----------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final UserAccountForm userAccountForm, final BindingResult binding) {
		ModelAndView res;
		UserAccount reconstructed;
		String pass, hashedpass;

		reconstructed = this.userAccountService.reconstruct(userAccountForm, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(userAccountForm);
		else
			try {
				pass = reconstructed.getPassword();
				hashedpass = this.chorbiService.hashPassword(pass); // Hash new password (previously checked in reconstruct method)
				reconstructed.setPassword(hashedpass);
				this.userAccountService.save(reconstructed);
				res = new ModelAndView("redirect:/");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(userAccountForm, "misc.commit.error");
			}

		return res;
	}

	protected ModelAndView createEditModelAndView(final UserAccountForm userAccountForm) {
		ModelAndView result;

		result = this.createEditModelAndView(userAccountForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserAccountForm userAccountForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("userAccount/edit");

		result.addObject("userAccountForm", userAccountForm);
		result.addObject("message", message);

		return result;
	}
}

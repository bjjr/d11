
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CreditCardService;
import domain.CreditCard;

@Controller
@RequestMapping("/creditCard")
public class CreditCardController extends AbstractController {

	// Services ----------------------------------------------

	@Autowired
	private CreditCardService	creditCardService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final boolean showWarning) {
		ModelAndView res;
		CreditCard creditCard;
		String maskedNumber;

		try {
			creditCard = this.creditCardService.findUserCreditCard();
			maskedNumber = this.creditCardService.getMaskedNumber(creditCard);
		} catch (final IllegalArgumentException e) {
			creditCard = null;
			maskedNumber = null;
		}

		res = new ModelAndView("creditCard/display");
		res.addObject("creditCard", creditCard);
		res.addObject("maskedNumber", maskedNumber);
		res.addObject("showWarningLikers", showWarning);

		return res;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		CreditCard creditCard;

		creditCard = this.creditCardService.create();

		res = this.createEditModelAndView(creditCard);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int creditCardId) {
		ModelAndView res;
		CreditCard creditCard;

		creditCard = this.creditCardService.findCreditCardToEdit(creditCardId);

		res = this.createEditModelAndView(creditCard);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(CreditCard creditCard, final BindingResult binding) {
		ModelAndView res;

		creditCard = this.creditCardService.reconstruct(creditCard, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(creditCard);
		else
			try {
				this.creditCardService.save(creditCard);
				res = new ModelAndView("redirect:display.do");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(creditCard, "misc.commit.error");
			}

		return res;
	}

	protected ModelAndView createEditModelAndView(final CreditCard creditCard) {
		ModelAndView res;

		res = this.createEditModelAndView(creditCard, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final CreditCard creditCard, final String message) {
		ModelAndView res;

		res = new ModelAndView("creditCard/edit");

		res.addObject("creditCard", creditCard);
		res.addObject("message", message);

		return res;
	}

}


package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChargeService;
import services.CreditCardService;
import controllers.AbstractController;
import domain.Charge;
import domain.CreditCard;
import domain.User;

@Controller
@RequestMapping("/charge/user")
public class ChargeUserController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private ChargeService		chargeService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CreditCardService	creditCardService;


	// Constructors -------------------------------------------

	public ChargeUserController() {
		super();
	}

	// Listing ------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Charge> charges;

		charges = this.chargeService.findChargesByUser();

		result = new ModelAndView("charge/list");
		result.addObject("charges", charges);
		result.addObject("requestURI", "charge/user/list.do");

		return result;
	}

	// Paying -------------------------------------------------

	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public ModelAndView pay(@RequestParam final int chargeId) {
		ModelAndView result;
		Charge charge;
		User user;
		CreditCard creditCard;

		charge = this.chargeService.findOne(chargeId);
		user = (User) this.actorService.findByPrincipal();
		creditCard = user.getCreditCard();
		result = new ModelAndView();

		try {
			if (creditCard == null)
				result = new ModelAndView("redirect:/creditCard/create.do");
			if (creditCard != null && !this.creditCardService.isCreditCardDateValid(creditCard))
				result = new ModelAndView("redirect:/creditCard/edit.do?creditCardId=" + creditCard.getId());
			if (creditCard != null && this.creditCardService.isCreditCardDateValid(creditCard)) {
				this.chargeService.pay(charge);
				result = new ModelAndView("redirect:/charge/user/list.do");
			}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/charge/user/list.do");
			result.addObject("misc.commit.error");
		}

		return result;
	}

}

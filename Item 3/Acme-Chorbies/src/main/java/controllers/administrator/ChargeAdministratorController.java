
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChargeService;
import controllers.AbstractController;

@Controller
@RequestMapping("/charge/administrator")
public class ChargeAdministratorController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private ChargeService	chargeService;


	// Constructors -------------------------------------------

	public ChargeAdministratorController() {
		super();
	}

	// Generate charges ---------------------------------------

	@RequestMapping(value = "/generateCharges", method = RequestMethod.GET)
	public ModelAndView generateCharges() {
		final ModelAndView result;

		result = new ModelAndView("charge/generateCharges");

		return result;
	}

	@RequestMapping(value = "/generateCharges", method = RequestMethod.POST, params = "generate")
	public ModelAndView generate() {
		ModelAndView result;

		try {
			this.chargeService.generateChargesToChorbies();
			result = new ModelAndView("redirect:/charge/administrator/generateCharges.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/charge/administrator/generateCharges.do");
			result.addObject("misc.commit.error");
		}

		return result;
	}

	// Economic statistics ------------------------------------

	@RequestMapping(value = "/economicStatistics", method = RequestMethod.GET)
	public ModelAndView economicStatistics() {
		final ModelAndView result;

		final Double totalBenefit;
		final Double totalDue;
		final Double theoreticalBenefit;

		totalBenefit = this.chargeService.totalBenefit();
		totalDue = this.chargeService.totalDue();
		theoreticalBenefit = this.chargeService.theoreticalBenefit();

		result = new ModelAndView("charge/economicStatistics");

		result.addObject("totalBenefit", totalBenefit);
		result.addObject("totalDue", totalDue);
		result.addObject("theoreticalBenefit", theoreticalBenefit);

		return result;
	}

}

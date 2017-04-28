
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

	// Benefit ------------------------------------------------

	@RequestMapping(value = "/economicStatistics", method = RequestMethod.GET)
	public ModelAndView economicStatistics() {
		final ModelAndView result;

		final Double totalBenefit;
		final Double totalDue;
		final Double theoreticalBenefit;

		totalBenefit = this.chargeService.totalBenefit();
		totalDue = this.chargeService.totalDue();
		theoreticalBenefit = this.chargeService.theoreticalBenefit();

		result = new ModelAndView("administrator/economicStatistics");

		result.addObject("totalBenefit", totalBenefit);
		result.addObject("totalDue", totalDue);
		result.addObject("theoreticalBenefit", theoreticalBenefit);

		return result;
	}

}

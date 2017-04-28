
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.FeeService;
import controllers.AbstractController;
import domain.Fee;

@Controller
@RequestMapping("/fee/administrator")
public class FeeAdministratorController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private FeeService	feeService;


	// Constructors -------------------------------------------

	public FeeAdministratorController() {
		super();
	}

	// Listing ------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Fee> fees;

		fees = this.feeService.findAll();

		result = new ModelAndView("fee/list");
		result.addObject("fees", fees);
		result.addObject("requestURI", "fee/administrator/list.do");

		return result;
	}

	// Edition -----------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int feeId) {
		ModelAndView result;
		Fee fee;

		fee = this.feeService.findOne(feeId);

		result = this.createEditModelAndView(fee);

		return result;
	}

	// Saving -------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Fee fee, final BindingResult binding) {
		ModelAndView result;
		Fee reconstructed;

		reconstructed = this.feeService.reconstruct(fee, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(fee);
		else
			try {
				this.feeService.save(reconstructed);
				result = new ModelAndView("redirect:/fee/administrator/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(fee, "misc.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------

	protected ModelAndView createEditModelAndView(final Fee fee) {
		ModelAndView result;
		int feeType;

		feeType = fee.getFeeType();
		result = this.createEditModelAndView(fee, null);
		result.addObject("feeType", feeType);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Fee fee, final String message) {
		ModelAndView result;

		result = new ModelAndView("fee/edit");
		result.addObject("fee", fee);
		result.addObject("message", message);

		return result;
	}

}

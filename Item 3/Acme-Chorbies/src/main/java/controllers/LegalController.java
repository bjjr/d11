
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/legal")
public class LegalController extends AbstractController {

	// Constructors -------------------------------------

	public LegalController() {
		super();
	}

	// Terms and conditions -----------------------------

	@RequestMapping(value = "/terms", method = RequestMethod.GET)
	public ModelAndView terms() {
		ModelAndView res;

		res = new ModelAndView("legal/terms");

		return res;
	}

	// Cookies ------------------------------------------

	@RequestMapping(value = "/cookies", method = RequestMethod.GET)
	public ModelAndView cookies() {
		ModelAndView res;

		res = new ModelAndView("legal/cookies");

		return res;
	}

	// About us -----------------------------------------

	@RequestMapping(value = "/aboutus", method = RequestMethod.GET)
	public ModelAndView aboutUs() {
		ModelAndView res;

		res = new ModelAndView("legal/aboutus");

		return res;
	}

}

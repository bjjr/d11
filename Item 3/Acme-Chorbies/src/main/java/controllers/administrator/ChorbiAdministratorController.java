
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiService;
import controllers.AbstractController;
import domain.Chorbi;

@Controller
@RequestMapping("/chorbi/administrator")
public class ChorbiAdministratorController extends AbstractController {

	// Services -------------------------------------

	@Autowired
	private ChorbiService	chorbiService;


	// Constructors ---------------------------------

	public ChorbiAdministratorController() {
		super();
	}

	// Ban ------------------------------------------

	@RequestMapping(value = "/ban")
	public ModelAndView ban(@RequestParam final int chorbiId) {
		ModelAndView res;

		Chorbi chorbi;

		chorbi = this.chorbiService.findOne(chorbiId);
		this.chorbiService.ban(chorbi);

		res = new ModelAndView("redirect:/chorbi/list.do");

		return res;
	}

	// Unban ----------------------------------------

	@RequestMapping(value = "/unban")
	public ModelAndView unban(@RequestParam final int chorbiId) {
		ModelAndView res;

		Chorbi chorbi;

		chorbi = this.chorbiService.findOne(chorbiId);
		this.chorbiService.unban(chorbi);

		res = new ModelAndView("redirect:/chorbi/list.do");

		return res;
	}
}

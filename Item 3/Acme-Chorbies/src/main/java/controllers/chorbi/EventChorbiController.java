
package controllers.chorbi;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiService;
import controllers.AbstractController;
import domain.Chorbi;
import domain.Event;

@Controller
@RequestMapping("/event/chorbi")
public class EventChorbiController extends AbstractController {

	// Services -----------------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;


	// Constructors -----------------------------------------------------------

	public EventChorbiController() {
		super();
	}

	// Listing -----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Event> events;
		Chorbi principal;

		principal = this.chorbiService.findByPrincipal();
		events = principal.getEvents();

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("requestURI", "event/chorbi/list.do");

		return res;

	}

}

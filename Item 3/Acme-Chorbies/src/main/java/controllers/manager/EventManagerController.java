
package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EventService;
import controllers.AbstractController;
import domain.Event;

@Controller
@RequestMapping("/event/manager")
public class EventManagerController extends AbstractController {

	// Services -----------------------------------------------------------

	@Autowired
	private EventService	eventService;


	// Constructors -----------------------------------------------------------

	public EventManagerController() {
		super();
	}

	// Listing -----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Event> events;

		events = this.eventService.findAll();

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("requestURI", "event/manager/list.do");

		return res;

	}

}

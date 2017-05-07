
package controllers.chorbi;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiService;
import services.EventService;
import controllers.AbstractController;
import domain.Chorbi;
import domain.Event;

@Controller
@RequestMapping("/event/chorbi")
public class EventChorbiController extends AbstractController {

	// Services -----------------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private EventService	eventService;


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
		Date currentDate;

		principal = this.chorbiService.findByPrincipal();
		events = principal.getEvents();
		currentDate = new Date(System.currentTimeMillis());

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("requestURI", "event/chorbi/list.do");
		res.addObject("current", currentDate);

		return res;
	}

	// Register -------------------------------------

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public ModelAndView register(@RequestParam final int eventId) {
		ModelAndView res;
		Event event;

		event = this.eventService.findOne(eventId);
		this.eventService.registerChorbi(event);

		res = new ModelAndView("redirect:/event/list.do");
		return res;
	}

	@RequestMapping(value = "unregister", method = RequestMethod.GET)
	public ModelAndView unregister(@RequestParam final int eventId) {
		ModelAndView res;
		Event event;

		event = this.eventService.findOne(eventId);
		this.eventService.unregisterChorbi(event);

		res = new ModelAndView("redirect:/event/list.do");
		return res;
	}

}

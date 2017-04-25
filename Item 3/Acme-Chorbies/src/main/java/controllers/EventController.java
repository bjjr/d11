
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EventService;
import domain.Event;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController {

	//Services --------------------------------------

	@Autowired
	private EventService	eventService;


	// Constructors ---------------------------------

	public EventController() {
		super();
	}

	// Listing -----------------------------------------

	@RequestMapping(value = "/listAvSts", method = RequestMethod.GET)
	public ModelAndView listAvSts() {
		ModelAndView res;
		Collection<Event> events;

		events = this.eventService.findEventsLessOneMonthSeatsAvailables();

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("requestURI", "event/listAvSts.do");

		return res;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Event> events;

		events = this.eventService.findAll();

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("requestURI", "event/list.do");

		return res;

	}

}

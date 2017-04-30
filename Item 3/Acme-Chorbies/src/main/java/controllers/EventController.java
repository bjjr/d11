
package controllers;

import java.util.Collection;
import java.util.Date;

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

	// Listing --------------------------------------

	@RequestMapping(value = "/listAvSts", method = RequestMethod.GET)
	public ModelAndView listAvSts() {
		ModelAndView res;
		Collection<Event> events;
		Boolean all;

		events = this.eventService.findEventsLessOneMonthSeatsAvailables();
		all = false;

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("all", all);
		res.addObject("requestURI", "event/listAvSts.do");

		return res;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Event> events;
		Date current;
		Boolean all;

		events = this.eventService.findAll();
		current = new Date();
		all = true;

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("current", current);
		res.addObject("all", all);
		res.addObject("requestURI", "event/list.do");

		return res;
	}

}

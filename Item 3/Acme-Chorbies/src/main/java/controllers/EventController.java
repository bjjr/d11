
package controllers;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChorbiService;
import services.EventService;
import domain.Chorbi;
import domain.Event;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController {

	//Services --------------------------------------

	@Autowired
	private EventService	eventService;

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private ActorService	actorService;


	// Constructors ---------------------------------

	public EventController() {
		super();
	}

	// Listing --------------------------------------

	@RequestMapping(value = "/listAvSts", method = RequestMethod.GET)
	public ModelAndView listAvSts() {
		ModelAndView res;
		Collection<Event> events, chorbiEvents;
		Boolean all;
		Date current;

		events = this.eventService.findEventsLessOneMonthSeatsAvailables();
		chorbiEvents = null;
		current = new Date(System.currentTimeMillis());

		if (this.actorService.checkAuthority("CHORBI")) {
			Chorbi principal;

			principal = this.chorbiService.findByPrincipal();
			chorbiEvents = principal.getEvents();
		}

		all = false;

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("chorbiEvents", chorbiEvents);
		res.addObject("current", current);
		res.addObject("all", all);
		res.addObject("requestURI", "event/listAvSts.do");

		return res;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Event> events, chorbiEvents;
		Date current;
		Boolean all;

		events = this.eventService.findAll();
		chorbiEvents = null;
		current = new Date();
		all = true;

		if (this.actorService.checkAuthority("CHORBI")) {
			Chorbi principal;

			principal = this.chorbiService.findByPrincipal();
			chorbiEvents = principal.getEvents();
		}

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("chorbiEvents", chorbiEvents);
		res.addObject("current", current);
		res.addObject("all", all);
		res.addObject("requestURI", "event/list.do");

		return res;
	}

}

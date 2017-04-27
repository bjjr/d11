
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	// Create ---------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Event event;

		event = this.eventService.create();

		res = this.createEditModelAndView(event, false);

		return res;
	}

	// Edition --------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int eventId) {
		ModelAndView res;
		Event event;

		event = this.eventService.findOneToEdit(eventId);

		res = this.createEditModelAndView(event, true);

		return res;
	}

	// Save -----------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Event event, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(event, false);
		else
			try {
				this.eventService.save(event);
				res = new ModelAndView("redirect:manager/list.do");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(event, "misc.commit.error", false);
			}

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdition(final Event event, final BindingResult binding) {
		ModelAndView res;
		Event reconstructed;

		reconstructed = this.eventService.reconstruct(event, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(reconstructed, true);
		else
			try {
				this.eventService.save(reconstructed);
				res = new ModelAndView("redirect:manager/list.do");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(event, "misc.commit.error", true);
			}

		return res;
	}

	// Delete ---------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Event event, final BindingResult binding) {
		ModelAndView res;

		this.eventService.delete(event);

		res = new ModelAndView("redirect:manager/list.do");

		return res;
	}

	// Ancillary methods ----------------------------

	protected ModelAndView createEditModelAndView(final Event event, final Boolean isEdit) {
		ModelAndView result;

		result = this.createEditModelAndView(event, null, isEdit);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Event event, final String message, final Boolean isEdit) {
		ModelAndView result;

		result = new ModelAndView("event/edit");

		if (isEdit)
			result.addObject("action", "event/edit.do");
		else
			result.addObject("action", "event/create.do");

		result.addObject("event", event);
		result.addObject("message", message);

		return result;
	}
}

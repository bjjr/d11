
package controllers.manager;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CreditCardService;
import services.EventService;
import services.ManagerService;
import controllers.AbstractController;
import domain.Event;
import domain.Manager;

@Controller
@RequestMapping("/event/manager")
public class EventManagerController extends AbstractController {

	// Services -----------------------------------------------------------

	@Autowired
	private EventService		eventService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private CreditCardService	creditCardService;


	// Constructors -----------------------------------------------------------

	public EventManagerController() {
		super();
	}

	// Listing -----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Event> events;
		Manager principal;
		Date currentDate;

		principal = this.managerService.findByPrincipal();
		events = this.eventService.findManagerEvents(principal.getId());
		currentDate = new Date(System.currentTimeMillis());

		res = new ModelAndView("event/list");
		res.addObject("events", events);
		res.addObject("requestURI", "event/manager/list.do");
		res.addObject("isManagerView", true);
		res.addObject("current", currentDate);

		return res;

	}

	// Create ---------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Event event;
		Manager principal;

		principal = this.managerService.findByPrincipal();

		if (principal.getCreditCard() == null)
			res = new ModelAndView("redirect:/creditCard/display.do?showWarning=true");
		else if (!this.creditCardService.isCreditCardDateValid(principal.getCreditCard()))
			res = new ModelAndView("redirect:/creditCard/display.do?showWarning=true");
		else {
			event = this.eventService.create();
			res = this.createEditModelAndView(event, false);
		}

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
	public ModelAndView save(final Event event, final BindingResult binding) {
		ModelAndView res;
		Event reconstructed;

		reconstructed = this.eventService.reconstruct(event, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(reconstructed, false);
		else
			try {
				this.eventService.save(reconstructed);
				res = new ModelAndView("redirect:list.do");
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
				res = new ModelAndView("redirect:list.do");
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

		res = new ModelAndView("redirect:list.do");

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
			result.addObject("action", "event/manager/edit.do");
		else
			result.addObject("action", "event/manager/create.do");

		result.addObject("event", event);
		result.addObject("message", message);

		return result;
	}

}

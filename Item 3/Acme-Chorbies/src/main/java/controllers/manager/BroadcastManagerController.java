
package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BroadcastService;
import controllers.AbstractController;
import domain.Broadcast;

@Controller
@RequestMapping("/broadcast/manager")
public class BroadcastManagerController extends AbstractController {

	// Services -----------------------------------------------------------

	@Autowired
	private BroadcastService	broadcastService;


	// Constructors -----------------------------------------------------------

	public BroadcastManagerController() {
		super();
	}

	// Create ---------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final Broadcast broadcast = this.broadcastService.create();

		final ModelAndView view = this.createEditModelAndView(broadcast);

		return view;
	}
	// Save -----------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Broadcast broadcast, final BindingResult binding) {
		ModelAndView res;

		final Broadcast broadcastReconstructed = this.broadcastService.reconstruct(broadcast, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(broadcastReconstructed);
		else
			try {
				this.broadcastService.update(broadcastReconstructed);
				res = new ModelAndView("redirect:/");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(broadcast, "misc.commit.error");
			}

		return res;
	}
	// Ancillary methods ----------------------------

	protected ModelAndView createEditModelAndView(final Broadcast broadcast) {
		ModelAndView result;

		result = this.createEditModelAndView(broadcast, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Broadcast broadcast, final String message) {
		ModelAndView result;

		result = new ModelAndView("broadcast/create");

		result.addObject("broadcast", broadcast);
		result.addObject("message", message);

		return result;
	}

}

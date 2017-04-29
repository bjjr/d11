
package controllers.chorbi;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiLikeService;
import services.ChorbiService;
import controllers.AbstractController;
import domain.Chorbi;
import domain.ChorbiLike;

@Controller
@RequestMapping("/chorbiLike/chorbi")
public class ChorbiLikeChorbiController extends AbstractController {

	// Services -----------------------------------------------------------

	@Autowired
	private ChorbiLikeService	chorbiLikeService;

	@Autowired
	private ChorbiService		chorbiService;


	// Constructors -----------------------------------------------------------

	public ChorbiLikeChorbiController() {
		super();
	}

	// Creating -----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int chorbiId) {
		ModelAndView result;
		Chorbi chorbi;
		ChorbiLike chorbiLike;

		chorbi = this.chorbiService.findOne(chorbiId);
		chorbiLike = this.chorbiLikeService.create(chorbi);
		result = new ModelAndView("chorbiLike/create");
		result.addObject("chorbiLike", chorbiLike);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ChorbiLike chorbiLike, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("chorbiLike/create");
			result.addObject("chorbiLike", chorbiLike);
		} else
			try {
				this.chorbiLikeService.save(chorbiLike);
				result = new ModelAndView("redirect:/chorbi/list.do");
				result.addObject("messageStatus", "chorbiLike.commit.ok");
			} catch (final Throwable oops) {
				result = new ModelAndView("chorbiLike/create");
				result.addObject("chorbiLike", chorbiLike);
				result.addObject("messageStatus", "chorbiLike.commit.error");
			}

		return result;
	}

	// Cancelling -----------------------------------------------------------

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int chorbiId) {
		ModelAndView result;
		Chorbi chorbi;

		chorbi = this.chorbiService.findOne(chorbiId);

		try {
			this.chorbiLikeService.cancelChorbiLike(chorbi);
			result = new ModelAndView("redirect:/chorbi/list.do");
			result.addObject("messageStatus", "chorbiLike.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/chorbi/list.do");
			result.addObject("messageStatus", "chorbiLike.commit.error");
		}

		return result;
	}

}


package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BannerService;
import domain.Banner;

@Controller
@RequestMapping("/banner")
public class BannerController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private BannerService	bannerService;


	// Constructors -------------------------------------------

	public BannerController() {
		super();
	}

	// Listing ------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Banner> banners;

		banners = this.bannerService.findAll();

		result = new ModelAndView("banner/list");
		result.addObject("banners", banners);
		result.addObject("requestURI", "banner/list.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int BannerId) {
		ModelAndView result;
		Banner banner;

		banner = this.bannerService.findOne(BannerId);
		Assert.notNull(banner);
		result = this.createEditModelAndView(banner);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Banner banner, final BindingResult binding) {
		ModelAndView result;
		Banner save;

		save = this.bannerService.reconstruct(banner, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(banner);
		else
			try {
				this.bannerService.save(save);
				result = new ModelAndView("redirect:/banner/list.do");
				result.addObject("message", "banner.commit.ok");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(banner, "misc.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Banner banner) {
		ModelAndView result;

		result = this.createEditModelAndView(banner, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Banner banner, final String message) {
		ModelAndView result;

		result = new ModelAndView("banner/edit");
		result.addObject("banner", banner);
		result.addObject("message", message);

		return result;
	}

}

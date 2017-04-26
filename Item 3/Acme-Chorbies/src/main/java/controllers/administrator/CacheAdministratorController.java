
package controllers.administrator;

import javax.validation.Valid;

import net.sf.ehcache.config.CacheConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CacheService;
import controllers.AbstractController;
import forms.CacheForm;

@Controller
@RequestMapping("/cache/administrator")
public class CacheAdministratorController extends AbstractController {

	// Services -------------------------------------

	@Autowired
	private CacheService	cacheService;


	// Display --------------------------------------

	@RequestMapping(value = "/display")
	public ModelAndView display() {
		ModelAndView res;
		CacheConfiguration cacheConfig;
		CacheForm cacheForm;

		cacheConfig = this.cacheService.getCacheConfig();
		cacheForm = new CacheForm(cacheConfig.getTimeToIdleSeconds());
		res = new ModelAndView("cache/display");
		res.addObject("cacheForm", cacheForm);

		return res;
	}

	@RequestMapping(value = "/edit")
	public ModelAndView create() {
		ModelAndView res;
		CacheForm cacheForm;
		CacheConfiguration cacheConfig;

		cacheConfig = this.cacheService.getCacheConfig();
		cacheForm = new CacheForm(cacheConfig.getTimeToIdleSeconds());
		res = this.createEditModelAndView(cacheForm);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final @Valid CacheForm cacheForm, final BindingResult binding) {
		ModelAndView res;

		if (cacheForm.getTtl(cacheForm) == 0)
			res = this.createEditModelAndView(cacheForm, "cache.invalid");
		else if (binding.hasErrors())
			res = this.createEditModelAndView(cacheForm);
		else
			try {
				this.cacheService.applyConfig(cacheForm);
				res = new ModelAndView("redirect:display.do");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(cacheForm, "misc.commit.error");
			}

		return res;
	}

	protected ModelAndView createEditModelAndView(final CacheForm cacheForm) {
		ModelAndView result;

		result = this.createEditModelAndView(cacheForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CacheForm cacheForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("cache/edit");

		result.addObject("cacheForm", cacheForm);
		result.addObject("message", message);

		return result;
	}

}

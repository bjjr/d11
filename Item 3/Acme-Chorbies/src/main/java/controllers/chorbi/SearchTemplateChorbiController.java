
package controllers.chorbi;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiLikeService;
import services.ChorbiService;
import services.CreditCardService;
import services.SearchTemplateService;
import controllers.AbstractController;
import domain.Chorbi;
import domain.SearchTemplate;

@Controller
@RequestMapping("/searchTemplate/chorbi")
public class SearchTemplateChorbiController extends AbstractController {

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}


	// Services ----------------------------------------------

	@Autowired
	private SearchTemplateService	searchTemplateService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ChorbiService			chorbiService;

	@Autowired
	private ChorbiLikeService		chorbiLikeService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		final SearchTemplate searchTemplate = this.searchTemplateService.findSearchTemplateByPrincipal();

		final ModelAndView res = this.createEditModelAndView(searchTemplate);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SearchTemplate searchTemplate, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(searchTemplate);
		else
			try {
				this.searchTemplateService.save(searchTemplate);
				res = new ModelAndView("redirect:search.do");
			} catch (final Throwable th) {
				res = this.createEditModelAndView(searchTemplate, "misc.commit.error");
			}

		return res;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView res;
		Chorbi principal;

		principal = this.chorbiService.findByPrincipal();

		if (principal.getCreditCard() == null)
			res = new ModelAndView("redirect:/creditCard/display.do?showWarning=true");
		else if (!this.creditCardService.isCreditCardDateValid(principal.getCreditCard()))
			res = new ModelAndView("redirect:/creditCard/display.do?showWarning=true");
		else {
			res = new ModelAndView("chorbi/list");
			final SearchTemplate searchTemplateCurrentUser = this.searchTemplateService.findSearchTemplateByPrincipal();

			Collection<Chorbi> liked;

			liked = this.chorbiLikeService.findLiked(this.chorbiService.findByPrincipal());

			final Collection<Chorbi> chorbiesMatched = this.searchTemplateService.search(searchTemplateCurrentUser);

			res.addObject("chorbies", chorbiesMatched);
			res.addObject("liked", liked);
			res.addObject("requestURI", "searchTemplate/chorbi/search.do");
		}

		return res;
	}

	protected ModelAndView createEditModelAndView(final SearchTemplate searchTemplate) {
		ModelAndView res;

		res = this.createEditModelAndView(searchTemplate, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final SearchTemplate searchTemplate, final String message) {
		ModelAndView res;

		res = new ModelAndView("searchTemplate/edit");

		res.addObject("searchTemplate", searchTemplate);
		res.addObject("message", message);

		return res;
	}

}

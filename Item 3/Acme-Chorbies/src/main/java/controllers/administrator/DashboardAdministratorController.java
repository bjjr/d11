
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import services.ChorbiLikeService;
import services.ChorbiService;
import controllers.AbstractController;
import domain.Chorbi;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Supporting services -----------------------------------------------------------

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private ChirpService		chirpService;

	@Autowired
	private ChorbiLikeService	chorbiLikeService;


	// Constructors -----------------------------------------------------------

	public DashboardAdministratorController() {
		super();
	}

	// Dashboard -----------------------------------------------------------

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() {

		final ModelAndView result;
		final Double avgAC;
		final Double maxAC;
		final Double minAC;
		final Double ratioChorbiesNoCCInvCC;
		final Double ratioChorbiesSearchAct;
		final Double ratioChorbiesSearchFriend;
		final Double ratioChorbiesSearchLove;
		final Collection<Chorbi> chorbiesOrderByCL;
		final Double avgLikesPChorbi;
		final Long maxLikesPChorbi;
		final Long minLikesPChorbi;
		final Double avgChirpsRecChorbi;
		final Long maxChirpsRecChorbi;
		final Long minChirpsRecChorbi;
		final Double avgChirpsSendChorbi;
		final Long maxChirpsSendChorbi;
		final Long minChirpsSendChorbi;
		final Collection<Chorbi> chorbiesMCR;
		final Collection<Chorbi> chorbiesMCS;
		final List<String[]> numberOfChorbiesPerCountry;
		final List<String[]> numberOfChorbiesPerCity;

		numberOfChorbiesPerCountry = this.chorbiService.findNumberOfChorbiesPerCountry();
		numberOfChorbiesPerCity = this.chorbiService.findNumberOfChorbiesPerCity();
		avgAC = this.chorbiService.findAvgAgeChorbies();
		maxAC = this.chorbiService.findMaxAgeChorbies();
		minAC = this.chorbiService.findMinAgeChorbies();
		ratioChorbiesNoCCInvCC = this.chorbiService.findRatioChorbiesNoCCInvCC();
		ratioChorbiesSearchAct = this.chorbiService.findRatioChorbiesSearchAct();
		ratioChorbiesSearchFriend = this.chorbiService.findRatioChorbiesSearchFriend();
		ratioChorbiesSearchLove = this.chorbiService.findRatioChorbiesSearchLove();
		chorbiesOrderByCL = this.chorbies(this.chorbiService.findChorbiesSortNumLikes());
		avgLikesPChorbi = this.chorbiLikeService.findAvgLikesPerChorbi();
		maxLikesPChorbi = this.chorbiLikeService.findMaxLikesPerChorbi();
		minLikesPChorbi = this.chorbiLikeService.findMinLikesPerChorbi();
		avgChirpsRecChorbi = this.chirpService.findAvgChirpsRecPerChorbi();
		maxChirpsRecChorbi = this.chirpService.findMaxChirpsRecPerChorbi();
		minChirpsRecChorbi = this.chirpService.findMinChirpsRecPerChorbi();
		avgChirpsSendChorbi = this.chirpService.findAvgChirpsSendPerChorbi();
		maxChirpsSendChorbi = this.chirpService.findMaxChirpsSendPerChorbi();
		minChirpsSendChorbi = this.chirpService.findMinChirpsSendPerChorbi();
		chorbiesMCR = this.chorbies(this.chorbiService.findChorbiesMoreChirpsRec());
		chorbiesMCS = this.chorbies(this.chorbiService.findChorbiesMoreChirpsSend());

		result = new ModelAndView("administrator/dashboard");
		result.addObject("requestURI", "dashboard/administrator/dashboard.do");
		result.addObject("numberOfChorbiesPerCountry", numberOfChorbiesPerCountry);
		result.addObject("numberOfChorbiesPerCity", numberOfChorbiesPerCity);
		result.addObject("avgAC", avgAC);
		result.addObject("maxAC", maxAC);
		result.addObject("minAC", minAC);
		result.addObject("ratioChorbiesNoCCInvCC", ratioChorbiesNoCCInvCC);
		result.addObject("ratioChorbiesSearchAct", ratioChorbiesSearchAct);
		result.addObject("ratioChorbiesSearchFriend", ratioChorbiesSearchFriend);
		result.addObject("ratioChorbiesSearchLove", ratioChorbiesSearchLove);
		result.addObject("chorbiesOrderByCL", chorbiesOrderByCL);
		result.addObject("avgLikesPChorbi", avgLikesPChorbi);
		result.addObject("maxLikesPChorbi", maxLikesPChorbi);
		result.addObject("minLikesPChorbi", minLikesPChorbi);
		result.addObject("avgChirpsRecChorbi", avgChirpsRecChorbi);
		result.addObject("maxChirpsRecChorbi", maxChirpsRecChorbi);
		result.addObject("minChirpsRecChorbi", minChirpsRecChorbi);
		result.addObject("avgChirpsSendChorbi", avgChirpsSendChorbi);
		result.addObject("maxChirpsSendChorbi", maxChirpsSendChorbi);
		result.addObject("minChirpsSendChorbi", minChirpsSendChorbi);
		result.addObject("chorbiesMCR", chorbiesMCR);
		result.addObject("chorbiesMCS", chorbiesMCS);

		return result;

	}

	//Ancillary methods -----------------------------------
	public Collection<Chorbi> chorbies(final Collection<Chorbi> chorbies) {
		Collection<Chorbi> result;

		result = new ArrayList<Chorbi>();

		if (chorbies != null)
			result.addAll(chorbies);

		return result;
	}

}

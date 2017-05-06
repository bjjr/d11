
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SearchTemplateRepository;
import domain.Chorbi;
import domain.CoordinatesTemplate;
import domain.SearchTemplate;

@Service
@Transactional
public class SearchTemplateService {

	@Autowired
	private SearchTemplateRepository	searchTemplateRepository;

	@Autowired
	private ChorbiService				chorbiService;

	@Autowired
	private CreditCardService			creditCardService;


	public SearchTemplate create() {
		final SearchTemplate searchTemplateResult = new SearchTemplate();

		final CoordinatesTemplate coordinatesTemplate = new CoordinatesTemplate();
		coordinatesTemplate.setCity(null);
		coordinatesTemplate.setCountry(null);
		coordinatesTemplate.setProvince(null);
		coordinatesTemplate.setState(null);

		searchTemplateResult.setAge(null);
		searchTemplateResult.setCoordinatesTemplate(coordinatesTemplate);
		searchTemplateResult.setGender(null);
		searchTemplateResult.setKeyword(null);
		searchTemplateResult.setRelationship(null);

		return searchTemplateResult;
	}

	@CacheEvict(value = "chorbiesPerSearchTemplate", key = "#searchTemplate.id")
	public SearchTemplate save(final SearchTemplate searchTemplate) {

		Assert.notNull(searchTemplate, "SearchTemplateService.save: The search template cannot be null");

		Assert.isTrue(this.isAValidSearchTemplate(searchTemplate), "SearchTemplateService.save: The search template needs to be valid");

		final SearchTemplate searchTemplateSaved = this.searchTemplateRepository.save(searchTemplate);

		return searchTemplateSaved;
	}

	public Collection<SearchTemplate> findAll() {
		return this.searchTemplateRepository.findAll();
	}

	public SearchTemplate findOne(final Integer id) {
		return this.searchTemplateRepository.findOne(id);
	}

	public void flush() {
		this.searchTemplateRepository.flush();
	}

	private Boolean isAValidSearchTemplate(final SearchTemplate searchTemplate) {
		Boolean res = true;

		if (searchTemplate.getGender() != null && (!searchTemplate.getGender().toUpperCase().equals("MAN") && !searchTemplate.getGender().toUpperCase().equals("WOMAN")))
			res = false;

		if (searchTemplate.getRelationship() != null
			&& (!searchTemplate.getRelationship().toUpperCase().equals("ACTIVITIES") && !searchTemplate.getRelationship().toUpperCase().equals("FRIENDSHIP") && !searchTemplate.getRelationship().toUpperCase().equals("LOVE")))
			res = false;

		return res;
	}

	public SearchTemplate findSearchTemplateByPrincipal() {
		final Chorbi chorbi = this.chorbiService.findByPrincipal();

		return chorbi.getSearchTemplate();
	}

	// Other business methods

	@Cacheable(value = "chorbiesPerSearchTemplate", key = "#searchTemplate.id")
	public Collection<Chorbi> search(final SearchTemplate searchTemplate) {

		final Chorbi loggedChorbi = this.chorbiService.findByPrincipal();
		Assert.notNull(loggedChorbi, "SearchTemplateService.search: The logged user cannot be null");

		Assert.notNull(loggedChorbi.getCreditCard(), "SearchTemplateService.search: You need a valid creditcard in order to perform a search");
		Assert.isTrue(this.creditCardService.isCreditCardDateValid(loggedChorbi.getCreditCard()));

		return this.findChorbiesBySearchTemplate(searchTemplate);
	}

	private Collection<Chorbi> findChorbiesBySearchTemplate(final SearchTemplate searchTemplate) {
		final Collection<Chorbi> res = new ArrayList<>();

		if (searchTemplate.getCoordinatesTemplate() != null)
			res.addAll(this.searchTemplateRepository.findChorbiesBySearchTemplate(searchTemplate.getGender(), searchTemplate.getRelationship(), searchTemplate.getCoordinatesTemplate().getCountry(), searchTemplate.getCoordinatesTemplate().getState(),
				searchTemplate.getCoordinatesTemplate().getProvince(), searchTemplate.getCoordinatesTemplate().getCity(), searchTemplate.getKeyword()));
		else
			res.addAll(this.searchTemplateRepository.findChorbiesBySearchTemplateWithoutCoordinates(searchTemplate.getGender(), searchTemplate.getRelationship(), searchTemplate.getKeyword()));

		final Collection<Chorbi> aux = new ArrayList<>();   //I need to do this regarding ConcurrentModificationException
		if (searchTemplate.getAge() != null)
			for (final Chorbi c : res) {
				final Integer age = Years.yearsBetween(LocalDate.fromDateFields(c.getBirthdate()), LocalDate.now()).getYears();
				if (!(age > (searchTemplate.getAge() - 5) && age < (searchTemplate.getAge() + 5)))
					aux.add(c);
			}
		res.removeAll(aux);

		return res;
	}
}

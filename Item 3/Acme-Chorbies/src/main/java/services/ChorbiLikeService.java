
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ChorbiLikeRepository;
import security.Authority;
import domain.Chorbi;
import domain.ChorbiLike;

@Service
@Transactional
public class ChorbiLikeService {

	//Managed repository
	@Autowired
	private ChorbiLikeRepository	chorbiLikeRepository;

	// Supporting services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ChorbiService			chorbiService;


	//Constructors
	public ChorbiLikeService() {
		super();
	}

	// Simple CRUD methods
	public ChorbiLike create(final Chorbi chorbi) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));
		Assert.notNull(chorbi);
		Assert.isTrue(chorbi.getId() != 0);

		ChorbiLike result;
		Chorbi principal;
		Date moment;

		principal = this.chorbiService.findByPrincipal();
		moment = new Date(System.currentTimeMillis() - 1000);

		Assert.isTrue(!principal.equals(chorbi));

		for (final ChorbiLike c : this.findChorbiLikesByLiker(principal.getId()))
			Assert.isTrue(c.getLiked() != chorbi);

		result = new ChorbiLike();
		result.setLiker(principal);
		result.setLiked(chorbi);
		result.setMoment(moment);

		return result;
	}
	public ChorbiLike save(final ChorbiLike chorbiLike) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));
		Assert.notNull(chorbiLike);

		ChorbiLike result;
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1000);
		chorbiLike.setMoment(moment);

		result = this.chorbiLikeRepository.save(chorbiLike);

		return result;
	}

	public void flush() {
		this.chorbiLikeRepository.flush();
	}

	public ChorbiLike findOne(final int id) {
		Assert.notNull(id);
		Assert.isTrue(id != 0);

		ChorbiLike result;

		result = this.chorbiLikeRepository.findOne(id);
		Assert.notNull(result);

		return result;
	}

	public Collection<ChorbiLike> findAll() {
		Collection<ChorbiLike> result;

		result = this.chorbiLikeRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public void delete(final ChorbiLike chorbiLike) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));
		Assert.notNull(chorbiLike);
		Assert.isTrue(chorbiLike.getId() != 0);
		Assert.isTrue(this.chorbiLikeRepository.exists(chorbiLike.getId()));

		this.chorbiLikeRepository.delete(chorbiLike);
	}

	//Other business methods

	public Collection<ChorbiLike> findChorbiLikesByLiker(final int chorbiId) {
		Assert.notNull(chorbiId);
		Assert.isTrue(chorbiId != 0);

		Collection<ChorbiLike> result;

		result = this.chorbiLikeRepository.findChorbiLikesByLiker(chorbiId);

		return result;
	}

	public Collection<ChorbiLike> findChorbiLikesByLiked(final int chorbiId) {
		Assert.notNull(chorbiId);
		Assert.isTrue(chorbiId != 0);

		Collection<ChorbiLike> result;

		result = this.chorbiLikeRepository.findChorbiLikesByLiked(chorbiId);

		return result;
	}

	public void cancelChorbiLike(final Chorbi chorbi) {
		Assert.isTrue(this.actorService.checkAuthority("CHORBI"));
		Assert.notNull(chorbi);

		Chorbi principal;
		ChorbiLike chorbiLike;

		principal = this.chorbiService.findByPrincipal();
		chorbiLike = null;

		chorbiLike = this.chorbiLikeRepository.findLike(principal.getId(), chorbi.getId());

		this.delete(chorbiLike);
	}

	public Collection<Chorbi> findLiked(final Chorbi principal) {
		Collection<Chorbi> result;
		Collection<ChorbiLike> cbl;

		cbl = this.findChorbiLikesByLiker(principal.getId());
		result = new ArrayList<Chorbi>();

		for (final ChorbiLike c : cbl)
			result.add(c.getLiked());

		return result;

	}

	public Collection<Chorbi> findChorbisByLiked(final int chorbiId) {
		Assert.isTrue(this.actorService.checkAuthority(Authority.CHORBI));
		final Chorbi chorbi = this.chorbiService.findByPrincipal();
		Assert.isTrue(chorbi.getCreditCard() != null);

		return this.chorbiLikeRepository.findChorbisByLiked(chorbiId);
	}

	public Double findAvgLikesPerChorbi() {
		Double result;

		result = this.chorbiLikeRepository.findAvgLikesPerChorbi();

		return result;
	}

	public Long findMaxLikesPerChorbi() {
		Long result;
		List<Long> maxLikes;

		result = 0L;
		maxLikes = (List<Long>) this.chorbiLikeRepository.findMaxLikesPerChorbi();

		if (!maxLikes.isEmpty())
			result = maxLikes.get(0);

		return result;
	}

	public Long findMinLikesPerChorbi() {
		Long result;
		List<Long> minLikes;
		List<Integer> allChorbiesId, allChorbiesRecieveId;

		allChorbiesId = this.chorbiService.findAllId();
		allChorbiesRecieveId = this.chorbiLikeRepository.findAllChorbiesWhoLike();

		if (!allChorbiesRecieveId.containsAll(allChorbiesId))
			return 0L;

		result = 0L;
		minLikes = (List<Long>) this.chorbiLikeRepository.findMinLikesPerChorbi();

		if (!minLikes.isEmpty())
			result = minLikes.get(0);

		return result;
	}

	public Double findAvgStarsPerChorbi() {
		Double res;

		res = this.chorbiLikeRepository.findAvgStarsPerChorbi();

		return res;
	}

	public Long findMaxStarsPerChorbi() {
		Long res;
		List<Long> maxStars;

		res = 0L;
		maxStars = (List<Long>) this.chorbiLikeRepository.findMaxStarsPerChorbi();

		if (!maxStars.isEmpty())
			res = maxStars.get(0);

		return res;
	}

	public Long findMinStarsPerChorbi() {
		Long res;
		List<Long> minStars;

		res = 0L;
		minStars = (List<Long>) this.chorbiLikeRepository.findMinStarsPerChorbi();

		if (!minStars.isEmpty())
			res = minStars.get(0);

		return res;
	}

}

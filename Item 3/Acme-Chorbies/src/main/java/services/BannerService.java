
package services;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.BannerRepository;
import domain.Banner;

@Service
@Transactional
public class BannerService {

	// Managed repository --------------------------------------

	@Autowired
	private BannerRepository	bannerRepository;

	// Supporting services -------------------------------------

	@Autowired
	private ActorService		actorService;


	// Constructor ---------------------------------------------

	public BannerService() {
		super();
	}

	// Simple CRUD methods -------------------------------------

	public Banner save(final Banner banner) {
		Assert.notNull(banner);
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"), "Only an admin must edit a banner");

		Banner res;

		res = this.bannerRepository.save(banner);

		return res;
	}

	public void flush() {
		this.bannerRepository.flush();
	}

	public Collection<Banner> findAll() {
		Collection<Banner> res;

		res = this.bannerRepository.findAll();
		Assert.notNull(res, "The banners does not exist");

		return res;
	}

	public Banner findOne(final int bannerId) {
		Banner res;

		res = this.bannerRepository.findOne(bannerId);
		Assert.notNull(res, "The banner does not exist");

		return res;
	}

	// Other business methods ----------------------------------

	/**
	 * @return Random Banner
	 */
	public Banner findRandomBanner() {
		Banner result;
		List<Banner> banners;
		final Random random = new Random();

		banners = (List<Banner>) this.findAll();

		result = banners.get(random.nextInt(banners.size()));

		return result;
	}

	public Banner reconstruct(final Banner banner, final BindingResult binding) {

		banner.setService(this.findOne(banner.getId()).getService());

		return banner;
	}
}

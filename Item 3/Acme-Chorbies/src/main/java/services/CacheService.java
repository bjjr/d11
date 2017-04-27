
package services;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import forms.CacheForm;

@Service
@Transactional
public class CacheService {

	// Supporting services --------------------------

	@Autowired
	private ActorService	actorService;


	// Methods --------------------------------------

	public CacheConfiguration getCacheConfig() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));
		CacheManager cacheManager;
		Cache cache;
		CacheConfiguration config;

		cacheManager = CacheManager.getInstance();
		cache = cacheManager.getCache("chorbiesPerSearchTemplate");
		config = cache.getCacheConfiguration();

		return config;
	}
	@CacheEvict(value = "chorbiesPerSearchTemplate", allEntries = true)
	public void applyConfig(final CacheForm cacheForm) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		CacheConfiguration config;
		Long ttl;

		config = this.getCacheConfig();
		ttl = cacheForm.getTtl(cacheForm);

		config.setTimeToIdleSeconds(ttl);
		config.setTimeToLiveSeconds(ttl);
	}

}

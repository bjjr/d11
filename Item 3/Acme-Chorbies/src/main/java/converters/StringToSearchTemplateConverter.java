
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.SearchTemplateRepository;
import domain.SearchTemplate;

@Component
@Transactional
public class StringToSearchTemplateConverter implements Converter<String, SearchTemplate> {

	@Autowired
	SearchTemplateRepository	searchTemplateRepository;


	@Override
	public SearchTemplate convert(final String text) {
		SearchTemplate res;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				res = null;
			else {
				id = Integer.valueOf(text);
				res = this.searchTemplateRepository.findOne(id);
			}
		} catch (final Throwable th) {
			throw new IllegalArgumentException(th);
		}

		return res;
	}

}

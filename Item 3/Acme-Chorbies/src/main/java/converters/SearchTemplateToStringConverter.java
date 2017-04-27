
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.SearchTemplate;

@Component
@Transactional
public class SearchTemplateToStringConverter implements Converter<SearchTemplate, String> {

	@Override
	public String convert(final SearchTemplate searchTemplate) {
		String res;

		if (searchTemplate == null)
			res = null;
		else
			res = String.valueOf(searchTemplate.getId());

		return res;
	}

}


package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Chorbi;

@Component
@Transactional
public class ChorbiToStringConverter implements Converter<Chorbi, String> {

	@Override
	public String convert(final Chorbi chorbie) {
		String res;

		if (chorbie == null)
			res = null;
		else
			res = String.valueOf(chorbie.getId());

		return res;
	}

}


package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ChorbiLike;

@Component
@Transactional
public class ChorbiLikeToStringConverter implements Converter<ChorbiLike, String> {

	@Override
	public String convert(final ChorbiLike chorbieLike) {
		String res;

		if (chorbieLike == null)
			res = null;
		else
			res = String.valueOf(chorbieLike.getId());

		return res;
	}

}

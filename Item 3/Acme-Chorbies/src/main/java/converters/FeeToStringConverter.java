
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Fee;

@Component
@Transactional
public class FeeToStringConverter implements Converter<Fee, String> {

	@Override
	public String convert(final Fee fee) {
		String res;

		if (fee == null)
			res = null;
		else
			res = String.valueOf(fee.getId());

		return res;
	}

}

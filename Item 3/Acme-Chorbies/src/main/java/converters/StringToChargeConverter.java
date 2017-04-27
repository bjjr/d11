
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ChargeRepository;
import domain.Charge;

@Component
@Transactional
public class StringToChargeConverter implements Converter<String, Charge> {

	@Autowired
	ChargeRepository	chargeRepository;


	@Override
	public Charge convert(final String text) {
		Charge res;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				res = null;
			else {
				id = Integer.valueOf(text);
				res = this.chargeRepository.findOne(id);
			}
		} catch (final Throwable th) {
			throw new IllegalArgumentException(th);
		}

		return res;
	}

}

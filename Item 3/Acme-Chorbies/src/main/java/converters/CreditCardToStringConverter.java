
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;

@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(CreditCard creditCard) {
		String res;

		if (creditCard == null) {
			res = null;
		} else {
			res = String.valueOf(creditCard.getId());
		}
		return res;
	}
}


package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.User;

@Component
@Transactional
public class UserToStringConverter implements Converter<User, String> {

	@Override
	public String convert(final User user) {
		String res;

		if (user == null)
			res = null;
		else
			res = String.valueOf(user.getId());

		return res;
	}

}

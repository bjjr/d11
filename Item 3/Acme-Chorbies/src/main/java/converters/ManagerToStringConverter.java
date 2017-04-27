
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Manager;

@Component
@Transactional
public class ManagerToStringConverter implements Converter<Manager, String> {

	@Override
	public String convert(final Manager manager) {
		String res;

		if (manager == null)
			res = null;
		else
			res = String.valueOf(manager.getId());

		return res;
	}

}


package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Event;

@Component
@Transactional
public class EventToStringConverter implements Converter<Event, String> {

	@Override
	public String convert(final Event event) {
		String res;

		if (event == null)
			res = null;
		else
			res = String.valueOf(event.getId());

		return res;
	}

}

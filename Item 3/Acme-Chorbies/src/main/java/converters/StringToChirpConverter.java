
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ChirpRepository;
import domain.Chirp;

@Component
@Transactional
public class StringToChirpConverter implements Converter<String, Chirp> {

	@Autowired
	ChirpRepository	chirpRepository;


	@Override
	public Chirp convert(final String text) {
		Chirp res;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				res = null;
			else {
				id = Integer.valueOf(text);
				res = this.chirpRepository.findOne(id);
			}
		} catch (final Throwable th) {
			throw new IllegalArgumentException(th);
		}

		return res;
	}

}

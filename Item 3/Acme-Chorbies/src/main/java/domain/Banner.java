
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Banner extends DomainEntity {

	// Attributes 

	private String	service;
	private String	path;


	@NotBlank
	@NotNull
	public String getService() {
		return this.service;
	}

	public void setService(final String service) {
		this.service = service;
	}

	@NotBlank
	@NotNull
	@URL
	public String getPath() {
		return this.path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

}

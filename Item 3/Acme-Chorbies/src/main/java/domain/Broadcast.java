
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Broadcast extends DomainEntity {

	// Attributes -----------------------------------

	private String	subject;
	private String	text;


	@NotBlank
	@NotNull
	@SafeHtml
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	@NotNull
	@SafeHtml
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}


	// Relationships --------------------------------

	private Collection<Chorbi>	uninformedChorbies;
	private Manager				manager;


	@ManyToMany
	@NotNull
	public Collection<Chorbi> getUninformedChorbies() {
		return this.uninformedChorbies;
	}

	public void setUninformedChorbies(final Collection<Chorbi> uninformedChorbies) {
		this.uninformedChorbies = uninformedChorbies;
	}

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

}

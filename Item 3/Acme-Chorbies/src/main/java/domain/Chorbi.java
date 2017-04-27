
package domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"description", "gender", "city", "country", "province", "state"
	})
})
public class Chorbi extends User implements Serializable {

	//Attributes

	private static final long	serialVersionUID	= -9116541658355470130L;
	private String				picture;
	private String				description;
	private Date				birthdate;
	private String				gender;
	private Coordinates			coordinates;
	private String				relationship;
	private Date				lastPaymentDate;


	@NotBlank
	@NotNull
	@URL
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	@NotBlank
	@NotNull
	@Size(min = 1, max = 1500)
	@SafeHtml
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Past
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(final Date birthdate) {
		this.birthdate = birthdate;
	}

	@NotNull
	@NotBlank
	public String getGender() {
		return this.gender;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	@NotNull
	@Valid
	public Coordinates getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(final Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	@NotNull
	@NotBlank
	public String getRelationship() {
		return this.relationship;
	}

	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastPaymentDate() {
		return this.lastPaymentDate;
	}

	public void setLastPaymentDate(final Date lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}


	// Relationships

	private SearchTemplate		searchTemplate;
	private Collection<Event>	events;


	@NotNull
	@Valid
	@OneToOne(optional = false)
	public SearchTemplate getSearchTemplate() {
		return this.searchTemplate;
	}

	public void setSearchTemplate(final SearchTemplate searchTemplate) {
		this.searchTemplate = searchTemplate;
	}

	@ManyToMany
	public Collection<Event> getEvents() {
		return this.events;
	}

	public void setEvents(final Collection<Event> events) {
		this.events = events;
	}

}

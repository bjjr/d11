
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"moment", "availableSeats"
	})
})
public class Event extends DomainEntity {

	// Relationships --------------------------------

	private String	title;
	private Date	moment;
	private String	description;
	private String	picture;
	private int		seats;
	private int		availableSeats;


	@NotNull
	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotNull
	@NotBlank
	@SafeHtml
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@NotBlank
	@URL
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	@Min(1)
	public int getSeats() {
		return this.seats;
	}

	public void setSeats(final int seats) {
		this.seats = seats;
	}

	@Min(0)
	public int getAvailableSeats() {
		return this.availableSeats;
	}

	public void setAvailableSeats(final int availableSeats) {
		this.availableSeats = availableSeats;
	}


	// Relationships --------------------------------

	private Manager	manager;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}
	public void setManager(final Manager manager) {
		this.manager = manager;
	}

}

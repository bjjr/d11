
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "copy")
})
public class Chirp extends DomainEntity {

	// Attributes 

	private Date				moment;
	private String				subject;
	private String				text;
	private Collection<String>	attachments;
	private Boolean				copy;


	@Past
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

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

	@NotNull
	@ElementCollection
	public Collection<String> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final Collection<String> attachments) {
		this.attachments = attachments;
	}

	@NotNull
	public Boolean getCopy() {
		return this.copy;
	}

	public void setCopy(final Boolean copy) {
		this.copy = copy;
	}


	// Relationships

	private User	sender;
	private User	recipient;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getSender() {
		return this.sender;
	}

	public void setSender(final User sender) {
		this.sender = sender;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getRecipient() {
		return this.recipient;
	}

	public void setRecipient(final User recipient) {
		this.recipient = recipient;
	}

}

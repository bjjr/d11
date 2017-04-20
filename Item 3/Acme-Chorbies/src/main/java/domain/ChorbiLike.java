
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class ChorbiLike extends DomainEntity {

	// Attributes

	private Date	moment;
	private String	comment;
	private int		stars;


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

	@SafeHtml
	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@Range(min = 0, max = 3)
	public int getStars() {
		return this.stars;
	}

	public void setStars(final Integer stars) {
		this.stars = stars;
	}


	// Relationships 

	private Chorbi	liker;
	private Chorbi	liked;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Chorbi getLiker() {
		return this.liker;
	}

	public void setLiker(final Chorbi liker) {
		this.liker = liker;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Chorbi getLiked() {
		return this.liked;
	}

	public void setLiked(final Chorbi liked) {
		this.liked = liked;
	}

}

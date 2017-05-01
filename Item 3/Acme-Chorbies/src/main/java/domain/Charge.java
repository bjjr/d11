
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "paid")
})
public class Charge extends DomainEntity {

	// Attributes -----------------------------------

	private double	amount;
	private boolean	paid;


	@Min(value = 0)
	public double getAmount() {
		return this.amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

	public boolean isPaid() {
		return this.paid;
	}

	public void setPaid(final boolean paid) {
		this.paid = paid;
	}


	// Relationships --------------------------------

	private User	user;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

}

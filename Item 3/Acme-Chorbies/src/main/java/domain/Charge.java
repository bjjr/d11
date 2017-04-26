
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Charge extends DomainEntity {

	// Attributes -----------------------------------

	private double	ammount;


	@Min(value = 0)
	public double getAmmount() {
		return this.ammount;
	}

	public void setAmmount(final double ammount) {
		this.ammount = ammount;
	}


	// Relationships --------------------------------

	private Chorbi	chorbi;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Chorbi getChorbi() {
		return this.chorbi;
	}

	public void setChorbi(final Chorbi chorbi) {
		this.chorbi = chorbi;
	}

}

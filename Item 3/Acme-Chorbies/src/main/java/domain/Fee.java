
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "feeType")
})
public class Fee extends DomainEntity {

	// Attributes -----------------------------------

	private int		feeType;
	private double	value;


	public int getFeeType() {
		return this.feeType;
	}

	public void setFeeType(final int feeType) {
		this.feeType = feeType;
	}

	@Min(value = 0)
	public double getValue() {
		return this.value;
	}

	public void setValue(final double value) {
		this.value = value;
	}

}

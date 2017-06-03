
package domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class CreditCard extends DomainEntity implements Serializable {

	// Attributes 

	private static final long	serialVersionUID	= -5419734414589046716L;
	private String				holder;
	private String				brand;
	private String				number;
	private int					year;
	private int					month;
	private int					cvv;


	@NotBlank
	@NotNull
	@SafeHtml
	public String getHolder() {
		return this.holder;
	}

	public void setHolder(final String holder) {
		this.holder = holder;
	}

	@NotBlank
	@NotNull
	public String getBrand() {
		return this.brand;
	}

	public void setBrand(final String brand) {
		this.brand = brand;
	}

	@NotNull
	@NotBlank
	@CreditCardNumber
	@Pattern(regexp = "\\d{14,16}")
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	@Range(min = 0)
	public int getYear() {
		return this.year;
	}

	public void setYear(final int year) {
		this.year = year;
	}

	@Range(min = 1, max = 12)
	public int getMonth() {
		return this.month;
	}

	public void setMonth(final int month) {
		this.month = month;
	}

	@Range(min = 100, max = 999)
	public int getCvv() {
		return this.cvv;
	}

	public void setCvv(final int cvv) {
		this.cvv = cvv;
	}

}

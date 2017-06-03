
package domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "relationship")
})
public class SearchTemplate extends DomainEntity implements Serializable {

	// Attributes 

	private static final long	serialVersionUID	= 9104266230847358385L;
	private Integer				age;
	private String				gender;
	private CoordinatesTemplate	coordinatesTemplate;
	private String				keyword;
	private String				relationship;


	@Range(min = 0)
	public Integer getAge() {
		return this.age;
	}

	public void setAge(final Integer age) {
		this.age = age;
	}

	@SafeHtml
	public String getGender() {
		return this.gender;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}

	@Valid
	public CoordinatesTemplate getCoordinatesTemplate() {
		return this.coordinatesTemplate;
	}

	public void setCoordinatesTemplate(final CoordinatesTemplate coordinatesTemplate) {
		this.coordinatesTemplate = coordinatesTemplate;
	}

	@SafeHtml
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}

	@SafeHtml
	public String getRelationship() {
		return this.relationship;
	}

	public void setRelationship(final String relationship) {
		this.relationship = relationship;
	}

	@Override
	public String toString() {
		return "SearchTemplate [age=" + this.age + ", gender=" + this.gender + ", coordinatesTemplate=" + this.coordinatesTemplate + ", keyword=" + this.keyword + ", relationship=" + this.relationship + "]";
	}

}

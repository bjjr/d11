
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chorbi;
import domain.SearchTemplate;

@Repository
public interface SearchTemplateRepository extends JpaRepository<SearchTemplate, Integer> {

	@Query("select c from Chorbi c where (?1 IS NULL OR c.gender = UPPER(?1)) and (?2 IS NULL OR c.relationship = UPPER(?2)) and (?3 IS NULL OR c.coordinates.country = ?3) and (?4 IS NULL OR c.coordinates.state = ?4) and (?5 IS NULL OR c.coordinates.province = ?5) and (?6 IS NULL OR c.coordinates.city = ?6) and (?7 IS NULL OR c.description LIKE CONCAT('%',?7,'%'))")
	Collection<Chorbi> findChorbiesBySearchTemplate(String gender, String relationship, String country, String state, String province, String city, String keyword);

	@Query("select c from Chorbi c where (?1 IS NULL OR c.gender = UPPER(?1)) and (?2 IS NULL OR c.relationship = UPPER(?2)) and (?3 IS NULL OR c.description LIKE CONCAT('%',?3,'%'))")
	Collection<Chorbi> findChorbiesBySearchTemplateWithoutCoordinates(String gender, String relationship, String keyword);

}

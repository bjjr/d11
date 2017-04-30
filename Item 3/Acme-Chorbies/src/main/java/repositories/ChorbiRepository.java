
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chorbi;

@Repository
public interface ChorbiRepository extends JpaRepository<Chorbi, Integer> {

	@Query("select c from Chorbi c where c.userAccount.id = ?1")
	Chorbi findByUserAccountId(int userAccountId);

	@Query("select avg(SUBSTRING(CURRENT_DATE, 1, 4) - SUBSTRING(c.birthdate, 1, 4)) from Chorbi c")
	Double findAvgAgeChorbies();

	@Query("select max(SUBSTRING(CURRENT_DATE, 1, 4) - SUBSTRING(c.birthdate, 1, 4)) from Chorbi c")
	Double findMaxAgeChorbies();

	@Query("select min(SUBSTRING(CURRENT_DATE, 1, 4) - SUBSTRING(c.birthdate, 1, 4)) from Chorbi c")
	Double findMinAgeChorbies();

	@Query("select (count(c) + (select count(c) from Chorbi c where c.creditCard.year < SUBSTRING(CURRENT_DATE, 1, 4) or (c.creditCard.year = SUBSTRING(CURRENT_DATE, 1, 4) and c.creditCard.month < SUBSTRING(CURRENT_DATE, 6, 2)))) *1. / (select count(c) from Chorbi c) from Chorbi c where c.creditCard is null")
	Double findRatioChorbiesNoCCInvCC();

	@Query("select count(c)*1./(select count(c) from Chorbi c) from Chorbi c where c.searchTemplate.relationship like 'ACTIVITIES'")
	Double findRatioChorbiesSearchAct();

	@Query("select count(c)*1./(select count(c) from Chorbi c) from Chorbi c where c.searchTemplate.relationship like 'FRIENDSHIP'")
	Double findRatioChorbiesSearchFriend();

	@Query("select count(c)*1./(select count(c) from Chorbi c) from Chorbi c where c.searchTemplate.relationship like 'LOVE'")
	Double findRatioChorbiesSearchLove();

	@Query("select cl.liked from ChorbiLike cl group by cl.liked order by count(cl) desc")
	Collection<Chorbi> findChorbiesSortNumLikes();

	@Query("select ch.recipient from Chirp ch group by ch.recipient having count(ch) >= ALL(select count(ch) from Chirp ch group by ch.recipient)")
	Collection<Chorbi> findChorbiesMoreChirpsRec();

	@Query("select ch.sender from Chirp ch group by ch.sender having count(ch) >= ALL(select count(ch) from Chirp ch group by ch.sender)")
	Collection<Chorbi> findChorbiesMoreChirpsSend();

	@Query("select c from Chorbi c where c.id != ?1 and 'BANNED' not member of c.userAccount.authorities")
	Collection<Chorbi> findNonBannedChorbies(int principalChorbiId);

	//--A listing with the number of chorbies per country and city.
	@Query("select c.coordinates.country,count(c) from Chorbi c group by c.coordinates.country")
	List<String[]> findNumberOfChorbiesPerCountry();

	@Query("select c.coordinates.city,count(c) from Chorbi c group by c.coordinates.city")
	List<String[]> findNumberOfChorbiesPerCity();

	@Query("select c.id from Chorbi c")
	List<Integer> findAllId();

	@Query("select c from Chorbi c group by c order by c.events.size desc")
	Collection<Chorbi> findChorbiesSortedByNumberEvents();

	@Query("select c.user.name, sum(c.amount) from Charge c where c.paid = false and type(c.user)=domain.Chorbi group by c.user")
	List<String[]> findChorbiesWithDebts();

	@Query("select cl.liked from ChorbiLike cl group by cl.liked order by avg(cl.stars) desc")
	Collection<Chorbi> findChorbieSortedByAvgStars();
	@Query("select cl.liked from ChorbiLike cl group by cl.liked order by avg(cl.stars) desc")
	Collection<Chorbi> findChorbiesSortNumStars();

}

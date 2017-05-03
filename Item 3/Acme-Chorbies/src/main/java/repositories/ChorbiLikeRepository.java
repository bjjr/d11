
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chorbi;
import domain.ChorbiLike;

@Repository
public interface ChorbiLikeRepository extends JpaRepository<ChorbiLike, Integer> {

	@Query("select c from ChorbiLike c where c.liker.id = ?1")
	Collection<ChorbiLike> findChorbiLikesByLiker(int chorbiId);

	@Query("select c from ChorbiLike c where c.liked.id = ?1")
	Collection<ChorbiLike> findChorbiLikesByLiked(int chorbiId);

	@Query("select c.liker from ChorbiLike c where c.liked.id = ?1")
	Collection<Chorbi> findChorbisByLiked(int chorbiId);

	@Query("select count(cl)*1.0/(select count(c) from Chorbi c) from ChorbiLike cl")
	Double findAvgLikesPerChorbi();

	@Query("select count(cl) from ChorbiLike cl group by cl.liker order by count(cl) desc")
	Collection<Long> findMaxLikesPerChorbi();

	@Query("select count(cl) from ChorbiLike cl group by cl.liker order by count(cl) asc")
	Collection<Long> findMinLikesPerChorbi();

	@Query("select sum(cl.stars)*1./(select count(c) from Chorbi c) from ChorbiLike cl")
	Double findAvgStarsPerChorbi();

	@Query("select sum(cl.stars) from ChorbiLike cl group by cl.liked order by sum(cl.stars) desc")
	Collection<Long> findMaxStarsPerChorbi();

	@Query("select sum(cl.stars) from ChorbiLike cl group by cl.liked order by sum(cl.stars) asc")
	Collection<Long> findMinStarsPerChorbi();

	@Query("select cl.liker.id from ChorbiLike cl group by cl.liker.id")
	List<Integer> findAllChorbiesWhoLike();

	@Query("select l from ChorbiLike l where l.liker.id = ?1 and l.liked.id = ?2")
	ChorbiLike findLike(int likerId, int likedId);

}

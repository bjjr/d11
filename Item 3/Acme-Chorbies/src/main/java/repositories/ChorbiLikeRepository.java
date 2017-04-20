
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ChorbiLike;

@Repository
public interface ChorbiLikeRepository extends JpaRepository<ChorbiLike, Integer> {

	@Query("select c from ChorbiLike c where c.liker.id = ?1")
	Collection<ChorbiLike> findChorbiLikesByLiker(int chorbiId);

	@Query("select c from ChorbiLike c where c.liked.id = ?1")
	Collection<ChorbiLike> findChorbiLikesByLiked(int chorbiId);

	@Query("select count(cl)*1.0/(select count(c) from Chorbi c) from ChorbiLike cl")
	Double findAvgLikesPerChorbi();

	@Query("select count(cl) from ChorbiLike cl group by cl.liker order by count(cl) desc")
	Collection<Long> findMaxLikesPerChorbi();

	@Query("select count(cl) from ChorbiLike cl group by cl.liker order by count(cl) asc")
	Collection<Long> findMinLikesPerChorbi();

	@Query("select cl.liker.id from ChorbiLike cl group by cl.liker.id")
	List<Integer> findAllChorbiesWhoLike();

	@Query("select l from ChorbiLike l where l.liker.id = ?1 and l.liked.id = ?2")
	ChorbiLike findLike(int likerId, int likedId);
}

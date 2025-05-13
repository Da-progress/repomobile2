package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.PlayerPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository
		extends JpaRepository<PlayerPoint, Long>, JpaSpecificationExecutor<PlayerPoint> {

	@Query("select count(p) from PlayerPoint p where p.points > :myPoints and p.sport.id = :sportId")
	public int countBetterPlayers(@Param("myPoints") int playerPoints, @Param("sportId") long sportId);

	@Query("select sum(p.points) from PlayerPoint p where p.player.id = :playerId")
	public Integer sumPlayerPoints(@Param("playerId") long playerId);

	@Query(nativeQuery = true, value =
			"select count(*) from "
					+ "(select pp.user_id, sum(pp.points) as points from "
					+ "(select user_id, points from user_point "
					+ "UNION ALL select user_id, points from explorer_point "
					+ "UNION ALL select user_id, points from socializer_point) as pp "
					+ "group by pp.user_id) as pp_group "
					+ "WHERE pp_group.points > ?1")
	public int countBetterPlayersTotal(int playerPoints);
}

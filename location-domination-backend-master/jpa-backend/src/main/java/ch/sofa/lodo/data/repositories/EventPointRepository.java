package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.EventPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventPointRepository
		extends JpaRepository<EventPoint, Long>, JpaSpecificationExecutor<EventPoint> {

	List<EventPoint> findAllByPlayerId(Long playerId);

	@Query(nativeQuery = true, value = "select count(*) from  (select user_id, sum(points) as points from event_point where event_sport_id = ?1 group by user_id) as pp where pp.points > ?2")
	int countBetterPlayers(long eventId, int myPoints);
}

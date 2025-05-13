package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.LocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationPointRepository
		extends JpaRepository<LocationPoint, Long>, JpaSpecificationExecutor<LocationPoint> {

	List<LocationPoint> findAllByPlayerId(Long playerId);

	@Query(nativeQuery = true, value = "select count(*) from  (select user_id, sum(points) as points from location_point where location_id = ?1 group by user_id) as pp where pp.points > ?2")
	int countBetterPlayers(long id, int myPoints);
}

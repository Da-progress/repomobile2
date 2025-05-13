package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.ExplorerPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExplorerPointRepository
		extends JpaRepository<ExplorerPoint, Long>, JpaSpecificationExecutor<ExplorerPoint> {

	List<ExplorerPoint> findAllByPlayerId(Long playerId);

	@Query("select count(p) from ExplorerPoint p where p.points > :myPoints")
	public int countAllBetterPlayers(@Param("myPoints") int playerPoints);
}

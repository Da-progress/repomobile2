package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.SocializerPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocializerPointRepository
		extends JpaRepository<SocializerPoint, Long>, JpaSpecificationExecutor<SocializerPoint> {

	List<SocializerPoint> findAllByPlayerId(Long playerId);

	@Query("select count(p) from SocializerPoint p where p.points > :myPoints")
	public int countAllBetterPlayers(@Param("myPoints") int playerPoints);
}

package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.constants.GameState;
import ch.sofa.lodo.data.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

	long countByIdAndGameState(Long id, GameState gameState);
}

package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.models.ExplorerPoint;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.SuperService;

import java.util.List;
import java.util.Optional;

public interface ExplorerPointService extends SuperService<ExplorerPoint> {

	List<ExplorerPoint> filterByPlayerId(Long playerId);

	List<ExplorerPoint> findAllByFilter(String searchText);

	ExplorerPoint findByPlayerId(Long playerId);

	int getRank(int playerPoints);

	Optional<ExplorerPoint> getByPlayer(List<ExplorerPoint> source, User player);
}

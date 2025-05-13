package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.models.SocializerPoint;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.SuperService;

import java.util.List;
import java.util.Optional;

public interface SocializerPointService extends SuperService<SocializerPoint> {

	List<SocializerPoint> filterByPlayerId(Long playerId);

	List<SocializerPoint> findAllByFilter(String searchText);

	SocializerPoint findByPlayerId(Long id);

	int getRank(int playerPoints);

	Optional<SocializerPoint> getByPlayer(List<SocializerPoint> source, User player);
}

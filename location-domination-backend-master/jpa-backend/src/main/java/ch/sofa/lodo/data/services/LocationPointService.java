package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.LocationPoint;
import ch.sofa.lodo.data.models.LocationPointInfo;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

public interface LocationPointService extends SuperService<LocationPoint> {

	List<LocationPoint> filterByLocationId(Long locationId);

	List<LocationPoint> filterByLocationId(Long locationId, Integer limit);

	List<LocationPoint> findAll(List<Order> orders);

	List<LocationPoint> filterByLocationIdAndPlayerIdAndOpponentId(Long locationId, Long playerId, Long opponentId);

	List<LocationPoint> filterByPlayerIdAndOpponentId(Long playerId, Long opponentId);

	long countByLocationIdAndPlayerId(Long locationId, Long playerId);

	List<LocationPointInfo> filterByLocationId2(Long locationId, Integer limit);

	//List<LocationPoint> filterByPlayerId(Long playerId);

	List<LocationPoint> filterByHostPlayerId(List<Long> playersId);

	int countBetterPlayers(long locationId, int myPoints);

	List<LocationPoint> filterByLocationIdAndHostId(Long locationId, Long hostId);
}

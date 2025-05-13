package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.EventPoint;
import ch.sofa.lodo.data.models.EventPointInfo;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

public interface EventPointService extends SuperService<EventPoint> {

	List<EventPoint> filterByEventId(Long eventId, List<Long> sportIds, int limit);

	List<EventPoint> findAll(List<Order> orders);

	List<EventPoint> filterByEventsportIdAndPlayerIDAndOpponentId(Long eventsportId, Long playerId, Long opponentId);

	List<EventPoint> filterByPlayerIdAndOpponentId(Long playerId, Long opponentId);

	long countByEventsportIdAndPlayerId(Long eventsportId, Long playerId);

	List<EventPoint> filterByEventId(Long eventId, List<Long> sportIds);

	List<EventPointInfo> filterByEventId2(Long eventId, List<Long> sportIds, int limit);

	//List<EventPoint> filterByPlayerId(Long playerId);

	List<EventPoint> filterByHostPlayerId(List<Long> playersIds);

	int countBetterPlayers(long eventId, int myPoints);
}

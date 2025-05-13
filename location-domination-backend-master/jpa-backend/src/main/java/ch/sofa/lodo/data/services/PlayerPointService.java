package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.models.User;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

public interface PlayerPointService extends SuperService<PlayerPoint> {

	List<PlayerPoint> filterBy(Long UserId);

	List<PlayerPoint> findAll(List<Order> orders);

	List<PlayerPoint> filterBySportId(List<Long> sportIds, String searchText);

	int getPlayerRank(int playerPoints, long sportId);

	Integer sumPlayerPoints(long playerId);

	int countBetterPlayersTotal(int playerTotalPoints);

	public List<PlayerPoint> findAllByFilter(String searchText);

	List<PlayerPoint> getByPlayer(List<PlayerPoint> source, User player);
}

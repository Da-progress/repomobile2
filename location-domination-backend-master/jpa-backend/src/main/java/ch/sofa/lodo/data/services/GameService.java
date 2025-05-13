package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.constants.GameState;
import ch.sofa.lodo.data.models.Game;
import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.services.dtos.GameDominationDto;
import ch.sofa.lodo.data.services.dtos.MyStatsDto;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

public interface GameService extends SuperService<Game> {

	List<Game> filterBy(List<GameState> statuses, List<Long> locationsIds, Long playerId);

	List<Game> filterBy(List<GameState> statuses, List<Long> eventsIds, List<Long> sportsIds);

	List<Game> filterBy(List<GameState> statuses);

	List<Game> findAll(List<Order> orders);

	List<Game> filterByPlayers(List<GameState> statuses, List<Long> playersIds, List<Long> sportsIds);

	List<GameDominationDto> findPlayerDominations(List<Long> playersIds);

	List<MyStatsDto> findSocialStats(List<Long> playersIds);

	List<MyStatsDto> findPlayerSportsStats(List<Long> playersIds);

	Game update(Game entity, boolean sendNotifications);

	Game persist(Game entity, boolean sendNotifycation);

	List<MyStatsDto> findPlayerTotalStats(List<Long> playersIds);

	boolean hasOpenGamesForGuest(Location location, long guestId);
}

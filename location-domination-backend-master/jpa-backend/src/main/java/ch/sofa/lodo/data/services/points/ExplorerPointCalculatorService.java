package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.constants.ExplorerPoints;
import ch.sofa.lodo.data.models.ExplorerPoint;
import ch.sofa.lodo.data.models.Game;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.EventPointService;
import ch.sofa.lodo.data.services.LocationPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExplorerPointCalculatorService {

	@Autowired
	private LocationPointService locationPointService;

	@Autowired
	private EventPointService eventPointService;

	@Autowired
	private ExplorerPointService explorerPointService;

	public void savePoints(Game game) {

		if (game.getLocation() != null) {
			long locHostResult = locationPointService.countByLocationIdAndPlayerId(game.getLocation()
							.getId(),
					game.getHostPlayer()
							.getId());
			if (locHostResult == 0) {
				calculatePointsForPlayer(game.getHostPlayer());
			}

			long locGuestResult = locationPointService.countByLocationIdAndPlayerId(game.getLocation()
							.getId(),
					game.getGuestPlayer()
							.getId());
			if (locGuestResult == 0) {
				calculatePointsForPlayer(game.getGuestPlayer());
			}
		} else if (game.getEventSport() != null) {
			long eventSportHostResult = eventPointService.countByEventsportIdAndPlayerId(game.getEventSport()
							.getId(),
					game.getHostPlayer()
							.getId());
			if (eventSportHostResult == 0) {
				calculatePointsForPlayer(game.getHostPlayer());
			}

			long eventSportGuestResult = eventPointService.countByEventsportIdAndPlayerId(game.getEventSport()
							.getId(),
					game.getGuestPlayer()
							.getId());
			if (eventSportGuestResult == 0) {
				calculatePointsForPlayer(game.getGuestPlayer());
			}
		} else {
			System.out.println("Error in saving points to a game");
		}
	}

	private void calculatePointsForPlayer(User player) {

		List<ExplorerPoint> rows = explorerPointService.filterByPlayerId(player.getId());

		if (rows.isEmpty()) {
			ExplorerPoint points = new ExplorerPoint();
			points.setPlayer(player);
			points.setPoints(ExplorerPoints.getPointObject(1).getPoints());
			points.setDifferentLocationCount(1);

			explorerPointService.persist(points);
		} else if (rows.size() == 1) {
			ExplorerPoint row = rows.get(0);
			row.setDifferentLocationCount(row.getDifferentLocationCount() + 1);
			row.setPoints(row.getPoints() + ExplorerPoints.getPointObject(row.getDifferentLocationCount())
					.getPoints());

			explorerPointService.update(row);
		}
	}
}

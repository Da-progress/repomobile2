package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.constants.PerLocationPerOpponentPoints;
import ch.sofa.lodo.data.models.EventPoint;
import ch.sofa.lodo.data.models.Game;
import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.models.LocationPoint;
import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.models.Sport;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.EventPointService;
import ch.sofa.lodo.data.services.LocationPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointCalculatorService {

	private static final int CREATE_LOCATION_POINTS = 10;

	@Autowired
	private LocationPointService locationPointService;

	@Autowired
	private EventPointService eventPointService;

	@Autowired
	private SportPointService sportPointService;

	public void addCreateLocationPoints(Location location) {
		List<LocationPoint> locationPoints = locationPointService.filterByLocationIdAndHostId(
				location.getId(), location.getCreator().getId());

		if (locationPoints.isEmpty()) {
			LocationPoint locationPoint = new LocationPoint(location.getCreator(), null, location);
			locationPoint.setPoints(CREATE_LOCATION_POINTS);
			locationPointService.persist(locationPoint);

			List<PlayerPoint> playerPoints = sportPointService.filterBySportIdAndPlayerId(
					location.getSport().getId(), location.getCreator().getId());

			if (playerPoints.isEmpty()) {
				PlayerPoint pp = new PlayerPoint();
				pp.setPlayer(location.getCreator());
				pp.setSport(location.getSport());
				pp.setPoints(CREATE_LOCATION_POINTS);
				sportPointService.persist(pp);
			} else if (playerPoints.size() == 1) {
				PlayerPoint pp = playerPoints.get(0);
				pp.setPoints(pp.getPoints() + CREATE_LOCATION_POINTS);
				sportPointService.update(pp);
			}
		}
	}

	public void savePoints(Game game) {
		if (game.getLocation() == null && game.getEventSport() != null) {
			saveEventSportPoints(game);
		} else if (game.getLocation() != null && game.getEventSport() == null) {
			saveLocationPoints(game);
		} else {
			throw new IllegalArgumentException("Game can not have two relations, location and eventSport");
		}
	}

	private void saveEventSportPoints(Game game) {

		int pointsAdded = 0;

		// test WINNER user-location-opponent entry
		List<EventPoint> ownerEventSportPoints = eventPointService.filterByEventsportIdAndPlayerIDAndOpponentId(
				game.getEventSport()
						.getId(),
				game.getHostPlayer()
						.getId(),
				game.getGuestPlayer()
						.getId());

		if (ownerEventSportPoints.isEmpty()) {
			EventPoint points = new EventPoint(game.getHostPlayer(), game.getGuestPlayer(), game.getEventSport());
			if (game.getHostScore() > game.getGuestScore()) {
				points.setWinGamesCount(1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(1)
						.getWinPoints();
				points.setPoints(pointsAdded);
				points.setLostGamesCount(0);
			} else if (game.getHostScore() < game.getGuestScore()) {
				points.setWinGamesCount(0);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(1)
						.getLossPoints();
				points.setPoints(pointsAdded);
				points.setLostGamesCount(1);
			} else {
				// TODO - equal ??
			}
			eventPointService.persist(points);
		} else if (ownerEventSportPoints.size() == 1) {
			EventPoint existing = ownerEventSportPoints.get(0);
			if (game.getHostScore() > game.getGuestScore()) {
				existing.setWinGamesCount(existing.getWinGamesCount() + 1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(existing.getWinGamesCount())
						.getWinPoints();
				int newPoints = existing.getPoints() + pointsAdded;
				existing.setPoints(newPoints);
			} else if (game.getHostScore() < game.getGuestScore()) {
				existing.setLostGamesCount(existing.getLostGamesCount() + 1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(existing.getLostGamesCount())
						.getLossPoints();
				int newPoints = existing.getPoints() + pointsAdded;
				existing.setPoints(newPoints);
			} else {
				// TODO - equal ??
			}
			eventPointService.update(existing);
		} else {
			throw new IllegalArgumentException("Number of location points entries bigger then 1");
		}

		addSportPoints(game.getEventSport()
						.getSport(),
				game.getHostPlayer(), pointsAdded);
		// RESET
		pointsAdded = 0;

		// test LOSER opponent-location-host entry
		List<EventPoint> loserEventPoints = eventPointService.filterByEventsportIdAndPlayerIDAndOpponentId(
				game.getEventSport()
						.getId(),
				game.getGuestPlayer()
						.getId(),
				game.getHostPlayer()
						.getId());

		if (loserEventPoints.isEmpty()) {
			EventPoint points = new EventPoint(game.getGuestPlayer(), game.getHostPlayer(), game.getEventSport());
			if (game.getGuestScore() > game.getHostScore()) {
				points.setWinGamesCount(1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(1)
						.getWinPoints();
				points.setPoints(pointsAdded);
				points.setLostGamesCount(0);
			} else if (game.getGuestScore() < game.getHostScore()) {
				points.setWinGamesCount(0);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(1)
						.getLossPoints();
				points.setPoints(pointsAdded);
				points.setLostGamesCount(1);
			} else {
				// TODO - equal ??
			}
			eventPointService.persist(points);
		} else if (loserEventPoints.size() == 1) {
			EventPoint existing = loserEventPoints.get(0);
			if (game.getGuestScore() > game.getHostScore()) {
				existing.setWinGamesCount(existing.getWinGamesCount() + 1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(existing.getWinGamesCount())
						.getWinPoints();
				int newPoints = existing.getPoints() + pointsAdded;
				existing.setPoints(newPoints);
			} else if (game.getGuestScore() < game.getHostScore()) {
				existing.setLostGamesCount(existing.getLostGamesCount() + 1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(existing.getLostGamesCount())
						.getLossPoints();
				int newPoints = existing.getPoints() + pointsAdded;
				existing.setPoints(newPoints);
			} else {
				// TODO - equal ??
			}
			eventPointService.update(existing);
		} else {
			throw new IllegalArgumentException("Number of location points entries bigger then 1");
		}

		addSportPoints(game.getEventSport()
						.getSport(),
				game.getGuestPlayer(), pointsAdded);
	}

	private void addSportPoints(Sport sport, User player, int pointsAdded) {
		List<PlayerPoint> playerPoints = sportPointService.filterBySportIdAndPlayerId(sport.getId(), player.getId());

		if (playerPoints.isEmpty()) {
			PlayerPoint row = new PlayerPoint();
			row.setPlayer(player);
			row.setSport(sport);
			row.setPoints(pointsAdded);

			sportPointService.persist(row);
		} else if (playerPoints.size() == 1) {
			PlayerPoint row = playerPoints.get(0);
			row.setPoints(row.getPoints() + pointsAdded);

			sportPointService.update(row);
		} else {
			throw new IllegalArgumentException(
					" Only one entry for super-sport-point permited. Found: " + playerPoints.size());
		}
	}

	private void saveLocationPoints(Game game) {

		int pointsAdded = 0;

		// test WINNER user-location-opponent entry
		List<LocationPoint> ownerLocPoints = locationPointService.filterByLocationIdAndPlayerIdAndOpponentId(
				game.getLocation()
						.getId(),
				game.getHostPlayer()
						.getId(),
				game.getGuestPlayer()
						.getId());

		if (ownerLocPoints.isEmpty()) {
			LocationPoint points = new LocationPoint(game.getHostPlayer(), game.getGuestPlayer(), game.getLocation());
			if (game.getHostScore() > game.getGuestScore()) {
				points.setWinGamesCount(1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(1)
						.getWinPoints();
				points.setPoints(pointsAdded);
				points.setLostGamesCount(0);
			} else if (game.getHostScore() < game.getGuestScore()) {
				points.setWinGamesCount(0);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(1)
						.getLossPoints();
				points.setPoints(pointsAdded);
				points.setLostGamesCount(1);
			} else {
				// TODO - equal ??
			}
			locationPointService.persist(points);
		} else if (ownerLocPoints.size() == 1) {
			LocationPoint existing = ownerLocPoints.get(0);
			if (game.getHostScore() > game.getGuestScore()) {
				existing.setWinGamesCount(existing.getWinGamesCount() + 1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(existing.getWinGamesCount())
						.getWinPoints();
				int newPoints = existing.getPoints() + pointsAdded;
				existing.setPoints(newPoints);
			} else if (game.getHostScore() < game.getGuestScore()) {
				existing.setLostGamesCount(existing.getLostGamesCount() + 1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(existing.getLostGamesCount())
						.getLossPoints();
				int newPoints = existing.getPoints() + pointsAdded;
				existing.setPoints(newPoints);
			} else {
				// TODO - equal ??
			}
			locationPointService.update(existing);
		} else {
			throw new IllegalArgumentException("Number of location points entries bigger then 1");
		}

		addSportPoints(game.getLocation()
						.getSport(),
				game.getHostPlayer(), pointsAdded);
		// RESET
		pointsAdded = 0;

		// test LOSER opponent-location-host entry
		List<LocationPoint> loserLocPoints = locationPointService.filterByLocationIdAndPlayerIdAndOpponentId(
				game.getLocation()
						.getId(),
				game.getGuestPlayer()
						.getId(),
				game.getHostPlayer()
						.getId());

		if (loserLocPoints.isEmpty()) {
			LocationPoint points = new LocationPoint(game.getGuestPlayer(), game.getHostPlayer(), game.getLocation());
			if (game.getGuestScore() > game.getHostScore()) {
				points.setWinGamesCount(1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(1)
						.getWinPoints();
				points.setPoints(pointsAdded);
				points.setLostGamesCount(0);
			} else if (game.getGuestScore() < game.getHostScore()) {
				points.setWinGamesCount(0);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(1)
						.getLossPoints();
				points.setPoints(PerLocationPerOpponentPoints.getPointObject(1)
						.getLossPoints());
				points.setLostGamesCount(1);
			} else {
				// TODO - equal ??
			}
			locationPointService.persist(points);
		} else if (loserLocPoints.size() == 1) {
			LocationPoint existing = loserLocPoints.get(0);
			if (game.getGuestScore() > game.getHostScore()) {
				existing.setWinGamesCount(existing.getWinGamesCount() + 1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(existing.getWinGamesCount())
						.getWinPoints();
				int newPoints = existing.getPoints() + pointsAdded;
				existing.setPoints(newPoints);
			} else if (game.getGuestScore() < game.getHostScore()) {
				existing.setLostGamesCount(existing.getLostGamesCount() + 1);
				pointsAdded = PerLocationPerOpponentPoints.getPointObject(existing.getLostGamesCount())
						.getLossPoints();
				int newPoints = existing.getPoints() + pointsAdded;
				existing.setPoints(newPoints);
			} else {
				// TODO - equal ??
			}
			locationPointService.update(existing);
		} else {
			throw new IllegalArgumentException("Number of location points entries bigger then 1");
		}
		addSportPoints(game.getLocation()
						.getSport(),
				game.getGuestPlayer(), pointsAdded);
	}
}

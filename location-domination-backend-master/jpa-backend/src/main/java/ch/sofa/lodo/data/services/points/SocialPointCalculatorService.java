package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.constants.SocializerPoints;
import ch.sofa.lodo.data.models.EventPoint;
import ch.sofa.lodo.data.models.Game;
import ch.sofa.lodo.data.models.LocationPoint;
import ch.sofa.lodo.data.models.SocializerPoint;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.EventPointService;
import ch.sofa.lodo.data.services.LocationPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialPointCalculatorService {

	@Autowired
	private LocationPointService locationPointService;

	@Autowired
	private EventPointService eventPointService;

	@Autowired
	private SocializerPointService socializerPointService;

	public void savePoints(Game game) {

		Long hostId = game.getHostPlayer().getId();
		Long guestId = game.getGuestPlayer().getId();

		List<LocationPoint> locResult = locationPointService.filterByPlayerIdAndOpponentId(hostId, guestId);
		if (!locResult.isEmpty())
			return;

		List<LocationPoint> locResultReverse = locationPointService.filterByPlayerIdAndOpponentId(guestId, hostId);

		if (!locResultReverse.isEmpty())
			return;

		List<EventPoint> eventSportResult = eventPointService.filterByPlayerIdAndOpponentId(hostId, guestId);
		if (!eventSportResult.isEmpty())
			return;

		List<EventPoint> eventSportResultReverse = eventPointService.filterByPlayerIdAndOpponentId(guestId, hostId);
		if (!eventSportResultReverse.isEmpty())
			return;

		calculatePoints(game);
	}

	private void calculatePoints(Game game) {
		forPlayer(game.getHostPlayer());
		forPlayer(game.getGuestPlayer());
	}

	private void forPlayer(User player) {

		List<SocializerPoint> rows = socializerPointService.filterByPlayerId(player.getId());

		if (rows.isEmpty()) {
			SocializerPoint points = new SocializerPoint();
			points.setPlayer(player);
			points.setPoints(SocializerPoints.getPointObject(1).getPoints());
			points.setDifferentOpponentCount(1);

			socializerPointService.persist(points);
		} else if (rows.size() == 1) {
			SocializerPoint row = rows.get(0);
			row.setDifferentOpponentCount(row.getDifferentOpponentCount() + 1);
			row.setPoints(row.getPoints() + SocializerPoints.getPointObject(row.getDifferentOpponentCount())
					.getPoints());

			socializerPointService.update(row);
		}
	}
}

package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.ExplorerPoint;
import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.models.SocializerPoint;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.dtos.PlayerWithPoints;
import ch.sofa.lodo.data.services.points.ExplorerPointService;
import ch.sofa.lodo.data.services.points.SocializerPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventPlayerWithPointServiceImpl implements EventPlayerWithPointService {

	@Autowired
	private EventUserService eventUserService;

	@Autowired
	private ExplorerPointService explorerPointService;

	@Autowired
	private SocializerPointService socializerPointService;

	@Autowired
	private PlayerPointService playerPointService;

	@Override
	public List<PlayerWithPoints> findAllByEventId(Long id, String searchText, List<Long> exclude) {

		List<User> players = eventUserService.findAllByEventId(id, searchText, exclude);

		List<SocializerPoint> socializerPoints = socializerPointService.findAllByFilter(searchText);

		List<ExplorerPoint> explorerPoints = explorerPointService.findAllByFilter(searchText);

		List<PlayerPoint> playerPoints = playerPointService.findAllByFilter(searchText);

		List<PlayerWithPoints> playerWithPoints =
				players.stream().map(player -> new PlayerWithPoints(player)).collect(Collectors.toList());

		playerWithPoints.forEach(plw -> sumPoints(plw, socializerPoints, explorerPoints, playerPoints));

		return playerWithPoints;
	}

	private void sumPoints(PlayerWithPoints playerWithPoints, List<SocializerPoint> socializerPoints,
						   List<ExplorerPoint> explorerPoints, List<PlayerPoint> playerPoints) {

		Optional<SocializerPoint> optionalSocializer =
				socializerPointService.getByPlayer(socializerPoints, playerWithPoints.getPlayer());
		optionalSocializer.ifPresent(e -> playerWithPoints.setPoints(playerWithPoints.getPoints() + e.getPoints()));

		Optional<ExplorerPoint> optionalExplorer =
				explorerPointService.getByPlayer(explorerPoints, playerWithPoints.getPlayer());
		optionalExplorer.ifPresent(e -> playerWithPoints.setPoints(playerWithPoints.getPoints() + e.getPoints()));

		List<PlayerPoint> playerPointsList = playerPointService.getByPlayer(playerPoints, playerWithPoints.getPlayer());
		playerPointsList.forEach(e -> playerWithPoints.setPoints(playerWithPoints.getPoints() + e.getPoints()));
	}
}

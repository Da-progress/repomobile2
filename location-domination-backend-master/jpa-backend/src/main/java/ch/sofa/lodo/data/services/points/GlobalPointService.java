package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.models.ExplorerPoint;
import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.models.SocializerPoint;
import ch.sofa.lodo.data.models.Sport;
import ch.sofa.lodo.data.models.SuperEntity;
import ch.sofa.lodo.data.services.PlayerPointService;
import ch.sofa.lodo.data.services.SportService;
import ch.sofa.lodo.data.services.dtos.GlobalPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GlobalPointService {

	@Autowired
	private PlayerPointService playerPointService;

	@Autowired
	private ExplorerPointService explorerPointService;

	@Autowired
	private SocializerPointService socializerPointService;

	@Autowired
	private SportService sportService;

	public List<GlobalPoint> findAllBy(String searchText, List<Long> sportIds, boolean socializer, boolean explorer, boolean totalPoints) {

		List<GlobalPoint> globalPoints = null;
		List<GlobalPoint> filtered = null;


		if (totalPoints) {
			// all sport by default. Do not use passed sportIds selection here

			List<SocializerPoint> socializerPoints = socializerPointService.findAll();
			List<ExplorerPoint> explorerPoints = explorerPointService.findAll();

			globalPoints = socializerPoints.stream()
					.filter(e -> !e.getPlayer().isBlocked())
					.map(soc -> new GlobalPoint(soc.getPlayer().getId(), soc.getPlayer().getUsername(), soc.getPoints()))
					.collect(Collectors.toList());

			for (ExplorerPoint expo : explorerPoints) {
				if (expo.getPlayer().isBlocked()) continue;
				addExplorerPoints(expo, globalPoints);
			}

			List<Sport> sports = sportService.findAll();
			List<Long> allSportIds = sports.stream().map(SuperEntity::getId).collect(Collectors.toList());
			List<PlayerPoint> userPoints = playerPointService.filterBySportId(allSportIds, null);
			for (PlayerPoint expo : userPoints) {
				if (expo.getPlayer().isBlocked()) continue;
				addUserPoints(expo, globalPoints);
			}

			// sort desc
			Collections.sort(globalPoints, new Comparator<GlobalPoint>() {

				@Override
				public int compare(GlobalPoint o1, GlobalPoint o2) {
					return Integer.compare(o1.getPoints(), o2.getPoints());
				}
			}.reversed());
		}

		if ((sportIds == null || sportIds.isEmpty()) && (socializer != explorer)) {
			if (socializer) {
				List<SocializerPoint> points = socializerPointService.findAll();
				globalPoints = points.stream()
						.filter(e -> !e.getPlayer().isBlocked())
						.map(e -> new GlobalPoint(e.getPlayer().getId(), e.getPlayer().getUsername(), e.getPoints()))
						.collect(Collectors.toList());
			} else {
				List<ExplorerPoint> points = explorerPointService.findAllByFilter(searchText);
				globalPoints = points.stream()
						.filter(e -> !e.getPlayer().isBlocked())
						.map(e -> new GlobalPoint(e.getPlayer().getId(), e.getPlayer().getUsername(), e.getPoints()))
						.collect(Collectors.toList());
			}
		} else if ((sportIds != null && !sportIds.isEmpty()) && !socializer && !explorer) {

			List<PlayerPoint> points = playerPointService.filterBySportId(sportIds, null);
			globalPoints = points.stream()
					.filter(e -> !e.getPlayer().isBlocked())
					.map(e -> new GlobalPoint(e.getPlayer().getId(), e.getPlayer().getUsername(), e.getPoints()))
					.collect(Collectors.toList());
		}

		// set rank
		for (int i = 0; i < globalPoints.size(); i++) {
			globalPoints.get(i).setRank(i + 1);
		}

		// filter by %username%
		if (searchText == null || searchText.trim().isEmpty()) {
			filtered = globalPoints;
		} else {
			filtered = globalPoints.stream().filter(
					e -> e.getUsername().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
		}

		return filtered;
	}

	private void addExplorerPoints(ExplorerPoint expo, List<GlobalPoint> playerWithPoints) {

		Optional<GlobalPoint> result =
				playerWithPoints.stream().filter(e -> e.getUserId().equals(expo.getPlayer().getId())).findFirst();

		if (result.isPresent()) {
			result.ifPresent(e -> e.setPoints(e.getPoints() + expo.getPoints()));
		} else {
			playerWithPoints.add(new GlobalPoint(expo.getPlayer().getId(), expo.getPlayer().getUsername(), expo.getPoints()));
		}
	}

	private void addUserPoints(PlayerPoint playerPoint, List<GlobalPoint> playerWithPoints) {

		Optional<GlobalPoint> result =
				playerWithPoints.stream().filter(e -> e.getUserId().equals(playerPoint.getPlayer().getId())).findFirst();

		if (result.isPresent()) {
			result.ifPresent(e -> e.setPoints(e.getPoints() + playerPoint.getPoints()));
		} else {
			playerWithPoints.add(new GlobalPoint(playerPoint.getPlayer().getId(), playerPoint.getPlayer().getUsername(), playerPoint.getPoints()));
		}
	}
}

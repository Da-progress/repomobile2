package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.services.SuperService;

import java.util.List;

public interface SportPointService extends SuperService<PlayerPoint> {

	public List<PlayerPoint> filterBySportIdAndPlayerId(Long sportId, Long playerId);
}

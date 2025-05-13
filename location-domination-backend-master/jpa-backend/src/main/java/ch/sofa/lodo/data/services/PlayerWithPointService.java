package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.services.dtos.PlayerWithPoints;

import java.util.List;

public interface PlayerWithPointService {

	List<PlayerWithPoints> filterByName(String searchText, List<String> excludeUsernames, List<Long> exclude);
}

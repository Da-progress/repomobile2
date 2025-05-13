package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.services.dtos.PlayerWithPoints;

import java.util.List;

public interface EventPlayerWithPointService {

	List<PlayerWithPoints> findAllByEventId(Long id, String searchText, List<Long> exclude);
}

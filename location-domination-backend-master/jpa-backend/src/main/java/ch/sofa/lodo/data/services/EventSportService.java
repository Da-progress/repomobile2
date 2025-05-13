package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventSport;

import java.util.List;

public interface EventSportService extends SuperService<EventSport> {

	List<EventSport> findAllByEvent(Event event);

	List<EventSport> filterByEventIdAndSportId(Long id, List<Long> sportsIds);

	void deleteAllByEvent(Event event);

	Event findEventByEventSportId(Long id);
}

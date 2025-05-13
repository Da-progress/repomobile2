package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventService extends SuperService<Event> {

	List<Event> filterBy(String searchText);

	List<Event> findAllInDateFrame(LocalDate startDate, LocalDate endDate);

	List<Event> findAllInDateFrame(List<Long> sports, String nameFilter, List<Double> near, String sortList,
								   LocalDate startDate1, LocalDate endDate1);
}

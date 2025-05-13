package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventSport;
import ch.sofa.lodo.data.repositories.EventSportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventSportServiceImpl implements EventSportService {

	@Autowired
	private EventSportRepository repository;

	@Override
	public EventSport persist(EventSport entity) {
		return repository.save(entity);
	}

	@Override
	public EventSport update(EventSport entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(EventSport entity) {
		repository.delete(entity);
	}

	@Override
	public List<EventSport> findAll() {
		return repository.findAll();
	}

	@Override
	public EventSport findById(Long id) {
		return repository.findById(id)
				.orElse(null);
	}

	@Override
	public List<EventSport> findAllByEvent(Event event) {
		return repository.findAllByEvent(event);
	}

	@Override
	public void deleteAllByEvent(Event event) {
		repository.deleteAllByEvent(event);
	}

	@Override
	public Event findEventByEventSportId(Long id) {
		EventSport es = findById(id);
		return es == null ? null : es.getEvent();
	}

	@Override
	public List<EventSport> filterByEventIdAndSportId(Long id, List<Long> sportsIds) {
		Specification<EventSport> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("event")
								.get("id"),
						id);
		Specification<EventSport> eventSportsList = (root, criteriaQuery, criteriaBuilder) -> {
			if (sportsIds == null || sportsIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("sport")
						.get("id")
						.in(sportsIds);
			}
		};

		return repository.findAll(spec.and(eventSportsList));
	}
}

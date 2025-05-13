package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventSport;
import ch.sofa.lodo.data.repositories.EventRepository;
import ch.sofa.lodo.data.repositories.EventSportRepository;
import ch.sofa.lodo.data.services.geo.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository repository;

	@Autowired
	private EventSportService eventSportService;

	@Autowired
	private EventSportRepository eventSportRepository;

	@Autowired
	private DistanceCalculator distanceCalculator;

	@Override
	public Event persist(Event entity) {
		return repository.save(entity);
	}

	@Override
	public Event update(Event entity) {
		return repository.save(entity);
	}

	@Transactional
	@Override
	public void delete(Event entity) {
		eventSportService.deleteAllByEvent(entity);
		repository.delete(entity);
	}

	@Override
	public List<Event> findAll() {
		return repository.findAll();
	}

	@Override
	public Event findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public List<Event> filterBy(String searchText) {

		Specification<Event> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim().isEmpty()) {
				return criteriaBuilder.like(root.get("name"), "%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};

		Specification<Event> specAddressStreet = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim().isEmpty()) {
				return criteriaBuilder.like(root.get("address").get("street"), "%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};

		return repository.findAll(specName.or(specAddressStreet));
	}

	@Override
	public List<Event> findAllInDateFrame(LocalDate startDate1, LocalDate endDate1) {
		Objects.requireNonNull(startDate1);
		Objects.requireNonNull(endDate1);

		LocalDateTime startDate = LocalDateTime.of(startDate1, LocalTime.of(0, 0));
		LocalDateTime endDate = LocalDateTime.of(endDate1, LocalTime.of(23, 59, 59));

		Specification<Event> dateIsAfterStartDate =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
						root.<LocalDateTime>get("startDateTime"), criteriaBuilder.literal(startDate));

		Specification<Event> dateIsBeforeEndDate =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
						root.<LocalDateTime>get("endDateTime"), criteriaBuilder.literal(endDate));

		return repository.findAll(dateIsAfterStartDate.and(dateIsBeforeEndDate));
	}

	@Override
	public List<Event> findAllInDateFrame(List<Long> sports, String nameFilter, List<Double> near, String sortList,
										  LocalDate startDate1, LocalDate endDate1) {
		Objects.requireNonNull(startDate1);
		Objects.requireNonNull(endDate1);

		if (near != null && near.size() != 3) {
			throw new IllegalArgumentException("Must provide all info, longitude, latitude, radius in km");
		}

		String[] sortBy = null;
		if (sortList == null || sortList.trim().isEmpty()) {
			sortBy = new String[1];
			sortBy[0] = "";
		} else {
			sortBy = sortList.split(",");
			for (int i = 0; i < sortBy.length; i++) {
				if (sortBy[i].equals("MOSTFREQ")) {
					// replace
					sortBy[i] = "name-ASC";
				}
			}
		}

		List<Order> orders = new ArrayList<Sort.Order>();
		for (String orderPair : sortBy) {
			if (orderPair.trim().isEmpty()) {
				break;
			}
			String[] orderDirectionAndPropertyNameList = orderPair.split("-");
			if (orderDirectionAndPropertyNameList[1].toUpperCase().equals("ASC")) {
				orders.add(new Order(Direction.ASC, orderDirectionAndPropertyNameList[0]));
			} else {
				orders.add(new Order(Direction.DESC, orderDirectionAndPropertyNameList[0]));
			}
		}
		// default order
		if (orders.isEmpty()) {
			orders.add(new Order(Direction.ASC, "name"));
		}

		// start - end
		LocalDateTime startDate = LocalDateTime.of(startDate1, LocalTime.of(0, 0));
		LocalDateTime endDate = LocalDateTime.of(endDate1, LocalTime.of(23, 59, 59));

		Specification<Event> dateIsAfterStartDate =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
						root.<LocalDateTime>get("startDateTime"), criteriaBuilder.literal(startDate));

		Specification<Event> dateIsBeforeEndDate =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
						root.<LocalDateTime>get("endDateTime"), criteriaBuilder.literal(endDate));

		Specification<EventSport> dateIsAfterStartDate2 =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
						root.join("event").<LocalDateTime>get("startDateTime"), criteriaBuilder.literal(startDate));

		Specification<EventSport> dateIsBeforeEndDate2 =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
						root.join("event").<LocalDateTime>get("endDateTime"), criteriaBuilder.literal(endDate));
		// start - end

		if (near == null) {
			if (sports == null && nameFilter == null) {
				return eventSportRepository.findAll(dateIsAfterStartDate2.and(dateIsBeforeEndDate2)).stream().collect(
						Collectors.groupingBy(EventSport::getEvent)).keySet().stream().collect(Collectors.toList());
			} else if (sports != null && nameFilter == null) {

				// TODO event SPort
				Specification<EventSport> sportsSpec = (root, criteriaQuery, criteriaBuilder) -> {
					if (sports.isEmpty()) {
						return criteriaBuilder.conjunction();
					} else {
						return root.get("sport").get("id").in(sports);
					}
				};
				return eventSportRepository.findAll(
						sportsSpec.and(dateIsAfterStartDate2).and(dateIsBeforeEndDate2)).stream().collect(
						Collectors.groupingBy(EventSport::getEvent)).keySet().stream().collect(
						Collectors.toList());
			} else if (sports == null && nameFilter != null) {

				Specification<EventSport> specName = (root, criteriaQuery, criteriaBuilder) -> {
					if (nameFilter != null && !nameFilter.trim().isEmpty()) {
						return criteriaBuilder.like(criteriaBuilder.lower(root.join("event").get("name")),
								"%" + nameFilter.toLowerCase() + "%");
					}
					return criteriaBuilder.conjunction();
				};
				return eventSportRepository.findAll(specName.and(dateIsAfterStartDate2).and(dateIsBeforeEndDate2)).stream().collect(
						Collectors.groupingBy(EventSport::getEvent)).keySet().stream().collect(
						Collectors.toList());
			} else {
				Specification<EventSport> sportsSpec = (root, criteriaQuery, criteriaBuilder) -> {
					if (sports.isEmpty()) {
						return criteriaBuilder.conjunction();
					} else {
						return root.get("sport").get("id").in(sports);
					}
				};

				Specification<EventSport> specName = (root, criteriaQuery, criteriaBuilder) -> {
					if (nameFilter != null && !nameFilter.trim().isEmpty()) {
						return criteriaBuilder.like(criteriaBuilder.lower(root.join("event").get("name")),
								"%" + nameFilter.toLowerCase() + "%");
					}
					return criteriaBuilder.conjunction();
				};
				return eventSportRepository.findAll(
						sportsSpec.and(specName).and(dateIsAfterStartDate2).and(dateIsBeforeEndDate2)).stream().collect(
						Collectors.groupingBy(EventSport::getEvent)).keySet().stream().collect(
						Collectors.toList());
			}
		} else {
			double radiusInMeters = near.get(2) * 1000;
			double latitude = near.get(0);
			double longitude = near.get(1);

			if (sports == null && nameFilter == null) {
				return whenSportNullAndNameFilterNull(radiusInMeters, longitude, latitude, orders, dateIsAfterStartDate,
						dateIsBeforeEndDate);
			} else if (sports == null && nameFilter != null) {
				return whenSportNullAndNameFilterNotNull(nameFilter, radiusInMeters, longitude, latitude, orders,
						dateIsAfterStartDate, dateIsBeforeEndDate);
			} else if (sports != null && nameFilter == null) {
				return whenSportNotNullAndNameFilterNull(sports, radiusInMeters, longitude, latitude, orders, startDate,
						endDate);
			} else {
				return whenSportNotNullAndNameFilterNotNull(sports, nameFilter, radiusInMeters, longitude, latitude,
						orders, startDate, endDate);
			}
		}
	}

	private List<Event> whenSportNotNullAndNameFilterNotNull(List<Long> sports, String nameFilter,
															 double radiusInMeters, double longitude, double latitude, List<Order> orders, LocalDateTime startDate,
															 LocalDateTime endDate) {
		Specification<EventSport> sportSpec = (root, criteriaQuery, criteriaBuilder) -> {
			if (sports.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("sport").get("id").in(sports);
			}
		};
		Specification<EventSport> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (nameFilter != null && !nameFilter.trim().isEmpty()) {
				return criteriaBuilder.like(root.join("event").get("name"), "%" + nameFilter + "%");
			}
			return criteriaBuilder.conjunction();
		};

		Specification<EventSport> dateIsAfterStartDate =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
						root.join("event").<LocalDateTime>get("startDateTime"), criteriaBuilder.literal(startDate));

		Specification<EventSport> dateIsBeforeEndDate =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
						root.join("event").<LocalDateTime>get("endDateTime"), criteriaBuilder.literal(endDate));

		List<EventSport> events = eventSportRepository.findAll(
				specName.and(sportSpec).and(dateIsAfterStartDate).and(dateIsBeforeEndDate));


		Map<Event, List<EventSport>> res = distanceCalculator.filterEventSportsInRadius(events, latitude, longitude,
				radiusInMeters).stream().collect(Collectors.groupingBy(EventSport::getEvent));

		return res.keySet().stream().collect(Collectors.toList());
	}

	private List<Event> whenSportNotNullAndNameFilterNull(List<Long> sports, double radiusInMeters, double longitude,
														  double latitude, List<Order> orders, LocalDateTime startDate, LocalDateTime endDate) {

		Specification<EventSport> sportSpec = (root, criteriaQuery, criteriaBuilder) -> {
			if (sports.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("sport").get("id").in(sports);
			}
		};

		Specification<EventSport> dateIsAfterStartDate =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
						root.join("event").<LocalDateTime>get("startDateTime"), criteriaBuilder.literal(startDate));

		Specification<EventSport> dateIsBeforeEndDate =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
						root.join("event").<LocalDateTime>get("endDateTime"), criteriaBuilder.literal(endDate));

		List<EventSport> events = eventSportRepository.findAll(
				sportSpec.and(dateIsAfterStartDate).and(dateIsBeforeEndDate));


		Map<Event, List<EventSport>> res = distanceCalculator.filterEventSportsInRadius(events, latitude, longitude,
				radiusInMeters).stream().collect(Collectors.groupingBy(EventSport::getEvent));

		return res.keySet().stream().collect(Collectors.toList());
	}

	private List<Event> whenSportNullAndNameFilterNotNull(String nameFilter, double radiusInMeters, double longitude,
														  double latitude, List<Order> orders, Specification<Event> dateIsAfterStartDate,
														  Specification<Event> dateIsBeforeEndDate) {


		Specification<Event> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (nameFilter != null && !nameFilter.trim().isEmpty()) {
				return criteriaBuilder.like(root.get("name"), "%" + nameFilter + "%");
			}
			return criteriaBuilder.conjunction();
		};

		List<Event> events =
				repository.findAll(specName.and(dateIsAfterStartDate).and(dateIsBeforeEndDate), Sort.by(orders));

		List<Long> eventsFiltered =
				distanceCalculator.filterEventsInRadius(events, latitude, longitude, radiusInMeters).stream().map(
						e -> e.getId()).collect(Collectors.toList());

		Specification<EventSport> eventSportsList = (root, criteriaQuery, criteriaBuilder) -> {
			if (eventsFiltered.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("event").get("id").in(eventsFiltered);
			}
		};

		return eventSportRepository.findAll(eventSportsList).stream().collect(
				Collectors.groupingBy(EventSport::getEvent)).keySet().stream().collect(Collectors.toList());
	}

	private List<Event> whenSportNullAndNameFilterNull(double radiusInMeters, double longitude, double latitude,
													   List<Order> orders, Specification<Event> dateIsAfterStartDate, Specification<Event> dateIsBeforeEndDate) {

		List<Event> events = repository.findAll(dateIsAfterStartDate.and(dateIsBeforeEndDate), Sort.by(orders));

		List<Long> eventsFiltered =
				distanceCalculator.filterEventsInRadius(events, latitude, longitude, radiusInMeters).stream().map(
						e -> e.getId()).collect(Collectors.toList());

		Specification<EventSport> eventSportsList = (root, criteriaQuery, criteriaBuilder) -> {
			if (eventsFiltered.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("event").get("id").in(eventsFiltered);
			}
		};

		return eventSportRepository.findAll(eventSportsList).stream().collect(
				Collectors.groupingBy(EventSport::getEvent)).keySet().stream().collect(Collectors.toList());
	}
}

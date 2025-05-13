package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.EventPoint;
import ch.sofa.lodo.data.models.EventPointInfo;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.EventPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventPointServiceImpl implements EventPointService {

	@Autowired
	private EventPointRepository repository;

	@Override
	public EventPoint persist(EventPoint entity) {
		entity.setRecordVersion(1);
		return repository.save(entity);
	}

	@Override
	public EventPoint update(EventPoint entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(EventPoint entity) {
		repository.delete(entity);
	}

	@Override
	public List<EventPoint> findAll() {
		return repository.findAll();
	}

	@Override
	public EventPoint findById(Long id) {
		return repository.findById(id)
				.orElse(null);
	}

	@Override
	public List<EventPoint> filterByPlayerIdAndOpponentId(Long playerId, Long opponentId) {

		Specification<EventPoint> specPlayer =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("player")
								.get("id"),
						playerId);
		Specification<EventPoint> specOpponent =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("opponent")
								.get("id"),
						opponentId);

		return repository.findAll(specPlayer.and(specOpponent));
	}

	@Override
	public List<EventPoint> filterByEventsportIdAndPlayerIDAndOpponentId(Long eventsportId, Long playerId,
																		 Long opponentId) {
		Specification<EventPoint> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("eventSport")
								.get("id"),
						eventsportId);
		Specification<EventPoint> specPlayer =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("player")
								.get("id"),
						playerId);
		Specification<EventPoint> specOpponent =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("opponent")
								.get("id"),
						opponentId);

		return repository.findAll(spec.and(specPlayer)
				.and(specOpponent));
	}

	@Override
	public long countByEventsportIdAndPlayerId(Long eventsportId, Long playerId) {
		Specification<EventPoint> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("eventSport")
								.get("id"),
						eventsportId);
		Specification<EventPoint> specPlayer =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("player")
								.get("id"),
						playerId);

		return repository.count(spec.and(specPlayer));
	}

	@Override
	public List<EventPoint> filterByEventId(Long eventId, List<Long> sportIds) {
		Specification<EventPoint> spec = (root, criteriaQuery,
										  criteriaBuilder) -> criteriaBuilder.equal(root.join("eventSport")
						.join("event")
						.get("id"),
				eventId);

		Specification<EventPoint> specSport = (root, criteriaQuery, criteriaBuilder) -> {
			if (sportIds == null || sportIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("eventSport")
						.join("sport")
						.get("id")
						.in(sportIds);
			}
		};

		return repository.findAll(spec.and(specSport));
	}

	@Override
	public List<EventPoint> filterByEventId(Long eventId, List<Long> sportIds, int limit) {
		Specification<EventPoint> spec = (root, criteriaQuery,
										  criteriaBuilder) -> criteriaBuilder.equal(root.join("eventSport")
						.join("event")
						.get("id"),
				eventId);

		Specification<EventPoint> specSport = (root, criteriaQuery, criteriaBuilder) -> {
			if (sportIds == null || sportIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("eventSport")
						.join("sport")
						.get("id")
						.in(sportIds);
			}
		};

		Pageable paging = PageRequest.of(0, limit, Sort.by(new Order(Direction.DESC, "points")));

		return repository.findAll(spec.and(specSport), paging)
				.toList();
	}

	@Override
	public List<EventPoint> filterByHostPlayerId(List<Long> playersIds) {

		System.out.println("... " + playersIds);
		Specification<EventPoint> hostPlayerList = (root, criteriaQuery, criteriaBuilder) -> {
			if (playersIds == null || playersIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("player")
						.get("id")
						.in(playersIds);
			}
		};

		return repository.findAll(hostPlayerList);
	}

	@Override
	public List<EventPointInfo> filterByEventId2(Long eventId, List<Long> sportIds, int limit) {

		List<EventPoint> result = filterByEventId(eventId, sportIds/* , limit */); // no limit on base data

		Map<User, List<EventPoint>> result2 = result.stream()
				.collect(Collectors.groupingBy(EventPoint::getPlayer));

		List<EventPointInfo> result3 = result2.entrySet()
				.stream()
				.filter(x -> !x.getKey().isBlocked())
				.map(this::sumEventPointsByHostPlayer)
				.sorted(Comparator.comparing(EventPointInfo::getPoints)
						.reversed())
				.collect(Collectors.toList());

		if (limit >= result3.size()) {
			return result3;
		}
		return result3.subList(0, limit);
	}

	private EventPointInfo sumEventPointsByHostPlayer(Map.Entry<User, List<EventPoint>> entrySet) {

		EventPointInfo result = new EventPointInfo();

		result.setPlayer(entrySet.getKey());
		result.setEventSport(entrySet.getValue()
				.get(0)
				.getEventSport());
		result.setPoints(entrySet.getValue()
				.stream()
				.mapToInt(EventPoint::getPoints)
				.reduce(0, Integer::sum));
		result.setWinGamesCount(entrySet.getValue()
				.stream()
				.mapToInt(EventPoint::getWinGamesCount)
				.reduce(0, Integer::sum));
		result.setLostGamesCount(entrySet.getValue()
				.stream()
				.mapToInt(EventPoint::getLostGamesCount)
				.reduce(0, Integer::sum));

		return result;
	}

	@Override
	public List<EventPoint> findAll(List<Order> orders) {
		return repository.findAll(Sort.by(orders));
	}

	@Override
	public int countBetterPlayers(long eventId, int myPoints) {
		return repository.countBetterPlayers(eventId, myPoints) + 1;
	}
}

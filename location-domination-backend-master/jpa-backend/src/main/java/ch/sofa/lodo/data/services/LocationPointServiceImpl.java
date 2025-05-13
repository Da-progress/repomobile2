package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.LocationPoint;
import ch.sofa.lodo.data.models.LocationPointInfo;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.LocationPointRepository;
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
public class LocationPointServiceImpl implements LocationPointService {

	@Autowired
	private LocationPointRepository repository;

	@Override
	public LocationPoint persist(LocationPoint entity) {
		return repository.save(entity);
	}

	@Override
	public LocationPoint update(LocationPoint entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(LocationPoint entity) {
		repository.delete(entity);
	}

	@Override
	public List<LocationPoint> findAll() {
		return repository.findAll();
	}

	@Override
	public LocationPoint findById(Long id) {
		return repository.findById(id)
				.orElse(null);
	}

	@Override
	public List<LocationPoint> filterByHostPlayerId(List<Long> playersIds) {

		Specification<LocationPoint> hostPlayerList = (root, criteriaQuery, criteriaBuilder) -> {
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
	public List<LocationPoint> filterByPlayerIdAndOpponentId(Long playerId, Long opponentId) {

		Specification<LocationPoint> specPlayer =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("player")
								.get("id"),
						playerId);
		Specification<LocationPoint> specOpponent =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("opponent")
								.get("id"),
						opponentId);
		return repository.findAll(specPlayer.and(specOpponent));
	}

	@Override
	public List<LocationPoint> filterByLocationIdAndPlayerIdAndOpponentId(Long locationId, Long playerId,
																		  Long opponentId) {
		Specification<LocationPoint> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("location")
								.get("id"),
						locationId);
		Specification<LocationPoint> specPlayer =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("player")
								.get("id"),
						playerId);
		Specification<LocationPoint> specOpponent =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("opponent")
								.get("id"),
						opponentId);
		return repository.findAll(spec.and(specPlayer)
				.and(specOpponent));
	}

	@Override
	public long countByLocationIdAndPlayerId(Long locationId, Long playerId) {
		Specification<LocationPoint> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("location")
								.get("id"),
						locationId);
		Specification<LocationPoint> specPlayer =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("player")
								.get("id"),
						playerId);
		return repository.count(spec.and(specPlayer));
	}

	@Override
	public List<LocationPoint> filterByLocationId(Long locationId) {
		Specification<LocationPoint> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("location")
								.get("id"),
						locationId);
		return repository.findAll(spec, Sort.by("points")
				.descending());
	}

	@Override
	public List<LocationPoint> filterByLocationIdAndHostId(Long locationId, Long playerId) {
		Specification<LocationPoint> spec = (root, criteriaQuery, criteriaBuilder)
				-> criteriaBuilder.equal(root.join("location").get("id"), locationId);

		Specification<LocationPoint> specPlayer = (root, criteriaQuery, criteriaBuilder)
				-> criteriaBuilder.equal(root.join("player").get("id"), playerId);

		return repository.findAll(spec.and(specPlayer));
	}

	@Override
	public List<LocationPoint> filterByLocationId(Long locationId, Integer limit) {
		Specification<LocationPoint> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("location")
								.get("id"),
						locationId);
		// default sorting on points.
		// MUST for generated transient rank id
		Pageable paging = PageRequest.of(0, limit, Sort.by(new Order(Direction.DESC, "points")));
		return repository.findAll(spec, paging)
				.toList();
	}

	@Override
	public List<LocationPointInfo> filterByLocationId2(Long locationId, Integer limit) {

		List<LocationPoint> result = filterByLocationId(locationId/* , limit */); // do not limit on base data

		System.out.println(".. result loc count ? " + result.size());

		Map<User, List<LocationPoint>> result2 = result.stream()
				.collect(Collectors.groupingBy(LocationPoint::getPlayer));

		result2.forEach((k, v) -> System.out.println(".. user: " + k.getUsername()));

		List<LocationPointInfo> result3 = result2.entrySet()
				.stream()
				.filter(x -> !x.getKey().isBlocked())
				.map(this::sumLocationPointsByHostPlayer)
				.sorted(Comparator.comparing(LocationPointInfo::getPoints)
						.reversed())
				.collect(Collectors.toList());

		if (limit >= result3.size()) {
			return result3;
		}
		return result3.subList(0, limit);
	}

	private LocationPointInfo sumLocationPointsByHostPlayer(Map.Entry<User, List<LocationPoint>> entrySet) {

		LocationPointInfo result = new LocationPointInfo();

		result.setPlayer(entrySet.getKey());
		result.setLocation(entrySet.getValue()
				.get(0)
				.getLocation());
		result.setPoints(entrySet.getValue()
				.stream()
				.mapToInt(e -> e.getPoints())
				.reduce(0, Integer::sum));
		result.setWinGamesCount(entrySet.getValue()
				.stream()
				.mapToInt(e -> e.getWinGamesCount())
				.reduce(0, Integer::sum));
		result.setLostGamesCount(entrySet.getValue()
				.stream()
				.mapToInt(e -> e.getLostGamesCount())
				.reduce(0, Integer::sum));

		return result;
	}

	@Override
	public List<LocationPoint> findAll(List<Order> orders) {
		return repository.findAll(Sort.by(orders));
	}

	@Override
	public int countBetterPlayers(long locationId, int myPoints) {
		return repository.countBetterPlayers(locationId, myPoints) + 1;
	}
}

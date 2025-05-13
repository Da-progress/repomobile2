package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.models.LocationState;
import ch.sofa.lodo.data.repositories.LocationRepository;
import ch.sofa.lodo.data.services.geo.DistanceCalculator;
import ch.sofa.lodo.data.services.points.PointCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository repository;

	@Autowired
	private PointCalculatorService pointCalculatorService;

	@Autowired
	private LocationStateService locationStateService;

	@Autowired
	private DistanceCalculator distanceCalculator;

	@Autowired
	private GameService gameService;

	@Transactional
	@Override
	public Location persist(Location entity) {
		if (entity.getLocationState() == null) {
			entity.setLocationState(locationStateService.findById(1L));
		}
		if (entity.getCreationDateTime() == null) {
			entity.setCreationDateTime(LocalDateTime.now());
		}
		Location location = repository.save(entity);
		pointCalculatorService.addCreateLocationPoints(location);
		return location;
	}

	@Override
	public Location update(Location entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(Location entity) {
		repository.delete(entity);
	}

	@Override
	public List<Location> findAll(List<Order> orders) {
		return repository.findAll(Sort.by(orders));
	}

	@Override
	public Location findById(Long id) {
		return repository.findById(id)
				.orElse(null);
	}

	@Override
	public List<Location> filterByLocationState(List<LocationState> statuses, String searchText) {
		Specification<Location> spec1 = (root, criteriaQuery, criteriaBuilder) -> {
			if (statuses.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("locationState")
						.in(statuses);
			}
		};

		Specification<Location> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim()
					.isEmpty()) {
				return criteriaBuilder.like(root.get("name"), "%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};

		Specification<Location> specAddressStreet = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim()
					.isEmpty()) {
				return criteriaBuilder.like(root.get("address").get("street"), "%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};

		return repository.findAll(spec1.and(specName.or(specAddressStreet)));
	}

	@Override
	public List<Location> filterBy(List<Long> sports, String nameFilter, List<Double> near, String sortList) {
		if (near != null && near.size() != 3) {
			throw new IllegalArgumentException("Must provide all info, longitude, latitude, radius in km");
		}

		String[] sortBy = null;
		if (sortList == null || sortList.trim()
				.isEmpty()) {
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
			if (orderPair.trim()
					.isEmpty()) {
				break;
			}
			String[] orderDirectionAndPropertyNameList = orderPair.split("-");
			if (orderDirectionAndPropertyNameList[1].toUpperCase()
					.equals("ASC")) {
				orders.add(new Order(Direction.ASC, orderDirectionAndPropertyNameList[0]));
			} else {
				orders.add(new Order(Direction.DESC, orderDirectionAndPropertyNameList[0]));
			}
		}
		// default order
		if (orders.isEmpty()) {
			orders.add(new Order(Direction.ASC, "name"));
		}

		if (near == null) {
			if (sports == null && nameFilter == null) {
				return this.findAll(orders);
			} else if (sports != null && nameFilter == null) {
				return repository.findBySportIds(sports, Sort.by(orders));
			} else if (sports == null && nameFilter != null) {
				return repository.findByNameIgnoreCaseContaining(nameFilter, Sort.by(orders));
			} else {
				return repository.findBySportIdsAndName(sports, nameFilter, Sort.by(orders));
			}
		} else {
			double radiusInMeters = near.get(2) * 1000;
			double latitude = near.get(0);
			double longitude = near.get(1);

			if (sports == null && nameFilter == null) {
				return whenSportNullAndNameFilterNull(radiusInMeters, longitude, latitude, orders);
			} else if (sports == null && nameFilter != null) {
				return whenSportNullAndNameFilterNotNull(nameFilter, radiusInMeters, longitude, latitude, orders);
			} else if (sports != null && nameFilter == null) {
				return whenSportNotNullAndNameFilterNull(sports, radiusInMeters, longitude, latitude, orders);
			} else {
				return whenSportNotNullAndNameFilterNotNull(sports, nameFilter, radiusInMeters, longitude, latitude, orders);
			}
		}
	}

	private List<Location> whenSportNotNullAndNameFilterNotNull(List<Long> sports, String nameFilter, double radiusInMeters, double longitude, double latitude,
																List<Order> orders) {
		List<Location> locations = repository.findBySportIdsAndName(sports, nameFilter, Sort.by(orders));
		return distanceCalculator.filterLocationsInRadius(locations, latitude, longitude, radiusInMeters);
	}

	private List<Location> whenSportNotNullAndNameFilterNull(List<Long> sports, double radiusInMeters, double longitude, double latitude, List<Order> orders) {
		List<Location> locations = repository.findBySportIds(sports, Sort.by(orders));
		return distanceCalculator.filterLocationsInRadius(locations, latitude, longitude, radiusInMeters);
	}

	private List<Location> whenSportNullAndNameFilterNotNull(String nameFilter, double radiusInMeters, double longitude, double latitude, List<Order> orders) {
		List<Location> locations = repository.findByNameIgnoreCaseContaining(nameFilter, Sort.by(orders));
		return distanceCalculator.filterLocationsInRadius(locations, latitude, longitude, radiusInMeters);
	}

	private List<Location> whenSportNullAndNameFilterNull(double radiusInMeters, double longitude, double latitude, List<Order> orders) {
		List<Location> locations = repository.findAll(Sort.by(orders));
		return distanceCalculator.filterLocationsInRadius(locations, latitude, longitude, radiusInMeters);
	}

	@Override
	public List<Location> findAll() {
		return repository.findAll();
	}

	private String convertSortToCsv(List<Order> orders) {
		String csv = orders.stream()
				.map(e -> e.getDirection()
						.name())
				.reduce("", (t, u) -> t + "," + u);
		return csv.isEmpty() ? "" : csv;
	}

	@Override
	public String addImage(Long id, byte[] file) {

		Location loc = findById(id);
		loc.setLoc_image(file);
		update(loc);

		return "OK";
	}

	@Override
	public List<Location> filterBy(List<Long> sports, String nameFilter, List<Double> near, String sortList,
								   boolean onlyOpenGames, long guestId) {

		List<Location> all = filterBy(sports, nameFilter, near, sortList);

		if (onlyOpenGames) {
			List<Location> all2 =
					all.stream().filter(loc -> gameService.hasOpenGamesForGuest(loc, guestId)).collect(
							Collectors.toList());
			return all2;
		}

		return all;
	}
}

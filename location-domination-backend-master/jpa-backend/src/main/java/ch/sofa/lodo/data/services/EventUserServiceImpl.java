package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventUser;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.EventUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventUserServiceImpl implements EventUserService {

	@Autowired
	private EventUserRepository repository;

	@Autowired
	private EventService eventService;

	@Autowired
	private PlayerService playerService;

	@Override
	public EventUser persist(EventUser entity) {
		return repository.save(entity);
	}

	@Override
	public EventUser update(EventUser entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(EventUser entity) {
		repository.delete(entity);
	}

	@Override
	public List<EventUser> findAll() {
		return repository.findAll();
	}

	@Override
	public List<User> findAllByEventId(Long id, String searchText, List<Long> exclude) {

		Specification<EventUser> eventSpec = (root, criteriaQuery, criteriaBuilder) -> {

			return criteriaBuilder.equal(root.get("event")
							.get("id"),
					id);
		};

		Specification<EventUser> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim()
					.isEmpty()) {
				return criteriaBuilder.like(root.get("player")
								.get("username"),
						"%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};

		Specification<EventUser> playerRule = (root, criteriaQuery, criteriaBuilder) -> {
			if (exclude == null || exclude.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("player")
						.get("id")
						.in(exclude)
						.not();
			}
		};
		List<EventUser> result = repository.findAll(eventSpec.and(specName)
				.and(playerRule));
		return result.stream()
				.map(e -> e.getPlayer())
				.collect(Collectors.toList());
	}

	@Override
	public EventUser findById(Long id) {
		return repository.findById(id)
				.orElse(null);
	}

	@Override
	public boolean isPlayerRegistered(Long eventId, Long playerId) {

		User player = playerService.findById(playerId);
		if (player == null)
			return false;

		Event event = eventService.findById(eventId);
		if (event == null)
			return false;

		return isPlayerRegistered(event, player);
	}

	@Override
	public boolean isPlayerRegistered(Event event, User player) {
		List<EventUser> existingRelations = repository.findAllByEventAndPlayer(event, player);
		return existingRelations.size() == 1;
	}

	@Override
	public EventUser registerPlayer(Long eventId, Long playerId, String registrationCode) {

		User player = playerService.findById(playerId);
		if (player == null)
			return null;

		Event event = eventService.findById(eventId);
		if (event == null)
			return null;

		return registerPlayer(event, player, registrationCode);
	}

	@Override
	public EventUser registerPlayer(Event event, User player, String registrationCode) {

		if (testRegistrationKey(event, registrationCode)) {
			List<EventUser> existingRelations = repository.findAllByEventAndPlayer(event, player);
			if (existingRelations.isEmpty()) {
				EventUser instance = new EventUser(event, player);
				instance.setRegistrationDateTime(LocalDateTime.now());
				return repository.save(instance);
			} else if (existingRelations.size() == 1) {
				return existingRelations.get(0);
			}
		}

		return null;
	}

	private boolean testRegistrationKey(Event event, String registrationCode) {
		Event eventRefreshed = eventService.findById(event.getId());
		if (eventRefreshed == null || registrationCode == null || registrationCode.trim()
				.isEmpty())
			return false;

		return registrationCode.equals(eventRefreshed.getRegistrationCode());
	}

	@Override
	public boolean unregisterPlayer(Event event, User player) {
		List<EventUser> existingRelations = repository.findAllByEventAndPlayer(event, player);
		if (existingRelations.isEmpty()) {
			return true;
		} else if (existingRelations.size() == 1) {
			repository.delete(existingRelations.get(0));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean unregisterPlayer(Long eventId, Long playerId) {
		User player = playerService.findById(playerId);
		if (player == null)
			return false;

		Event event = eventService.findById(eventId);
		if (event == null)
			return false;

		return unregisterPlayer(event, player);
	}
}

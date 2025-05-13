package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.UserPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerPointServiceImpl implements PlayerPointService {

	@Autowired
	private UserPointRepository repository;

	@Override
	public PlayerPoint persist(PlayerPoint entity) {
		entity.setRecordVersion(1);
		return repository.save(entity);
	}

	@Override
	public PlayerPoint update(PlayerPoint entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(PlayerPoint entity) {
		repository.delete(entity);
	}

	@Override
	public List<PlayerPoint> findAll() {
		return repository.findAll();
	}

	@Override
	public PlayerPoint findById(Long id) {
		return repository.findById(id)
				.orElse(null);
	}

	@Override
	public List<PlayerPoint> filterBy(Long userId) {
		Specification<PlayerPoint> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("player")
								.get("id"),
						userId);
		return repository.findAll(spec);
	}

	@Override
	public List<PlayerPoint> filterBySportId(List<Long> sportIds, String searchText) {
		Specification<PlayerPoint> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim()
					.isEmpty()) {
				return criteriaBuilder.like(root.join("player")
								.get("username"),
						"%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};

		Specification<PlayerPoint> spec = (root, criteriaQuery, criteriaBuilder) -> {
			if (sportIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("sport")
						.get("id")
						.in(sportIds);
			}
		};

		return repository.findAll(spec.and(specName), Sort.by(Direction.DESC, "points"));
	}

	@Override
	public List<PlayerPoint> findAllByFilter(String searchText) {
		Specification<PlayerPoint> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim().isEmpty()) {
				return criteriaBuilder.like(root.join("player").get("username"), "%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};
		return repository.findAll(specName, Sort.by(Direction.DESC, "points"));
	}

	@Override
	public List<PlayerPoint> findAll(List<Order> orders) {
		return repository.findAll(Sort.by(orders));
	}

	@Override
	public int getPlayerRank(int playerPoints, long sportId) {
		return repository.countBetterPlayers(playerPoints, sportId) + 1;
	}

	@Override
	public Integer sumPlayerPoints(long playerId) {
		return repository.sumPlayerPoints(playerId);
	}

	@Override
	public int countBetterPlayersTotal(int playerTotalPoints) {
		return repository.countBetterPlayersTotal(playerTotalPoints) + 1;
	}

	@Override
	public List<PlayerPoint> getByPlayer(List<PlayerPoint> source, User player) {

		return source.stream().filter(e -> e.getPlayer().equals(player)).collect(Collectors.toList());
	}
}

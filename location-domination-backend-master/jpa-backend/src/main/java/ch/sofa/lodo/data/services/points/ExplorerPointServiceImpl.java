package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.models.ExplorerPoint;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.ExplorerPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExplorerPointServiceImpl implements ExplorerPointService {

	@Autowired
	private ExplorerPointRepository repository;

	@Override
	public ExplorerPoint persist(ExplorerPoint entity) {
		return repository.save(entity);
	}

	@Override
	public ExplorerPoint update(ExplorerPoint entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(ExplorerPoint entity) {
		repository.delete(entity);
	}

	@Override
	public List<ExplorerPoint> findAll() {
		return repository.findAll(Sort.by(Direction.DESC, "points"));
	}

	@Override
	public List<ExplorerPoint> findAllByFilter(String searchText) {
		Specification<ExplorerPoint> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim().isEmpty()) {
				return criteriaBuilder.like(root.join("player").get("username"), "%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};
		return repository.findAll(specName, Sort.by(Direction.DESC, "points"));
	}

	@Override
	public ExplorerPoint findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public List<ExplorerPoint> filterByPlayerId(Long playerId) {

		return repository.findAllByPlayerId(playerId);
	}

	@Override
	public ExplorerPoint findByPlayerId(Long playerId) {
		List<ExplorerPoint> res = repository.findAllByPlayerId(playerId);
		if (res.isEmpty()) {
			return null;
		} else if (res.size() == 1) {
			return res.get(0);
		} else {
			return null;
		}
	}

	@Override
	public int getRank(int playerPoints) {
		return repository.countAllBetterPlayers(playerPoints) + 1;
	}

	@Override
	public Optional<ExplorerPoint> getByPlayer(List<ExplorerPoint> source, User player) {

		return source.stream().filter(e -> e.getPlayer().equals(player)).findFirst();
	}
}

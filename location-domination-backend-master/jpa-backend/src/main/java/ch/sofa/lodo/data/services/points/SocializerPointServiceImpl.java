package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.models.SocializerPoint;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.SocializerPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocializerPointServiceImpl implements SocializerPointService {

	@Autowired
	private SocializerPointRepository repository;

	@Override
	public SocializerPoint persist(SocializerPoint entity) {
		return repository.save(entity);
	}

	@Override
	public SocializerPoint update(SocializerPoint entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(SocializerPoint entity) {
		repository.delete(entity);
	}

	@Override
	public List<SocializerPoint> findAll() {
		return repository.findAll(Sort.by(Direction.DESC, "points"));
	}

	@Override
	public List<SocializerPoint> findAllByFilter(String searchText) {
		Specification<SocializerPoint> specName = (root, criteriaQuery, criteriaBuilder) -> {
			if (searchText != null && !searchText.trim().isEmpty()) {
				return criteriaBuilder.like(root.join("player").get("username"), "%" + searchText + "%");
			}
			return criteriaBuilder.conjunction();
		};
		return repository.findAll(specName, Sort.by(Direction.DESC, "points"));
	}

	@Override
	public SocializerPoint findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public SocializerPoint findByPlayerId(Long playerId) {
		List<SocializerPoint> res = repository.findAllByPlayerId(playerId);
		if (res.isEmpty()) {
			return null;
		} else if (res.size() == 1) {
			return res.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<SocializerPoint> filterByPlayerId(Long playerId) {
		return repository.findAllByPlayerId(playerId);
	}

	@Override
	public int getRank(int playerPoints) {
		return repository.countAllBetterPlayers(playerPoints) + 1;
	}

	public Optional<SocializerPoint> getByPlayer(List<SocializerPoint> source, User player) {

		return source.stream().filter(e -> e.getPlayer().equals(player)).findFirst();
	}
}

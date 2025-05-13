package ch.sofa.lodo.data.services.points;

import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.repositories.UserPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportPointServiceImpl implements SportPointService {

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
	public List<PlayerPoint> filterBySportIdAndPlayerId(Long sportId, Long playerId) {
		Specification<PlayerPoint> spec =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("sport")
								.get("id"),
						sportId);
		Specification<PlayerPoint> specPlayer =
				(root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("player")
								.get("id"),
						playerId);

		return repository.findAll(spec.and(specPlayer));
	}
}

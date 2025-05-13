package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Sport;
import ch.sofa.lodo.data.repositories.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportServiceImpl implements SportService {

	@Autowired
	private SportRepository repository;

	@Override
	public Sport persist(Sport entity) {
		return repository.save(entity);
	}

	@Override
	public Sport update(Sport entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(Sport entity) {
		repository.delete(entity);
	}

	@Override
	public List<Sport> findAll() {
		return repository.findAll();
	}

	@Override
	public Sport findById(Long id) {
		return repository.findById(id)
				.orElse(null);
	}
}

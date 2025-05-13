package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.LocationState;
import ch.sofa.lodo.data.repositories.LocationStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationStateServiceImpl implements LocationStateService {

	@Autowired
	private LocationStateRepository repository;

	@Override
	public LocationState persist(LocationState entity) {
		return repository.save(entity);
	}

	@Override
	public LocationState update(LocationState entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(LocationState entity) {
		repository.delete(entity);
	}

	@Override
	public List<LocationState> findAll() {
		return repository.findAll();
	}

	@Override
	public LocationState findById(Long id) {
		return repository.findById(id)
				.orElse(null);
	}
}

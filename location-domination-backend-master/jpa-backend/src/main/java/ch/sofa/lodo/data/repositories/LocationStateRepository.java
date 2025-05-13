package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.LocationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationStateRepository extends JpaRepository<LocationState, Long> {

}

package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventSport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventSportRepository extends JpaRepository<EventSport, Long>, JpaSpecificationExecutor<EventSport> {

	List<EventSport> findAllByEvent(Event event);

	void deleteAllByEvent(Event event);
}

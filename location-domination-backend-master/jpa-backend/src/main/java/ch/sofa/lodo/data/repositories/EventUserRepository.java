package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventUser;
import ch.sofa.lodo.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, Long>, JpaSpecificationExecutor<EventUser> {

	List<EventUser> findAllByEventAndPlayer(Event event, User player);

	List<EventUser> findAllByEventId(Long id);
}

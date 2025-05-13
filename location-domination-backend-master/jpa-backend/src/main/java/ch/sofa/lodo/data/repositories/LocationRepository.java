package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.models.Location;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

	@Query("select l from Location l, Sport s where l.sport.id = s.id and s.id IN :ids")
	List<Location> findBySportIds(@Param("ids") List<Long> sports, Sort sort);

	@Query("select l from Location l, Sport s where l.sport.id = s.id and s.id IN :ids and LOWER(l.name) LIKE LOWER(concat('%',concat(:name, '%')))")
	List<Location> findBySportIdsAndName(@Param("ids") List<Long> sports, @Param("name") String nameFilter, Sort sort);

	List<Location> findByNameIgnoreCaseContaining(String nameFilter, Sort sort);
}

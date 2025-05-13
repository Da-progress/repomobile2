package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.models.LocationState;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

public interface LocationService extends SuperService<Location> {

	List<Location> filterBy(List<Long> sports, String nameFilter, List<Double> near, String sortList);

	List<Location> findAll(List<Order> orders);

	List<Location> filterByLocationState(List<LocationState> statuses, String searchText);

	String addImage(Long id, byte[] file);

	/**
	 * @param sports
	 * @param nameFilter
	 * @param near
	 * @param sortList
	 * @param onlyOpenGames
	 * @param guestId       set 0 if user is not set, >0 for wanted user
	 * @return return all locations if user note set (guestId == 0), returns filtered locations by open games for set user if guestId > 0
	 */
	List<Location> filterBy(List<Long> sports, String nameFilter, List<Double> near, String sortList,
							boolean onlyOpenGames, long guestId);
}

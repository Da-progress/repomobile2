package ch.sofa.lodo.controllers;

import ch.sofa.lodo.controllers.response.entities.LocationLight;
import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/loc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationLightController {

	private LocationService locationService;

	@Autowired
	public LocationLightController(LocationService locationService) {
		this.locationService = locationService;
	}

	@GetMapping(consumes = MediaType.ALL_VALUE)
	public List<LocationLight> getAll(@RequestParam(required = false) List<Long> sports, @RequestParam(required = false, name = "filter") String nameFilter,
									  @RequestParam(required = false, name = "sort") String sort, @RequestParam(required = false) List<Double> near,
									  @RequestParam(required = false, name = "onlyopengames") boolean onlyOpenGames,
									  @RequestParam(required = false, name = "guest") Long guestId) {
		try {
			List<Location> locations = locationService.filterBy(sports, nameFilter, near, sort, onlyOpenGames, guestId == null ? 0 : guestId);

			return locations.stream()
					.map(loc -> new LocationLight(loc.getId(), loc.getName(), loc.getLongitude(), loc.getLatitude(),
							loc.getAddress(), loc.getSport()))
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching locations");
		}
	}

	@GetMapping(path = "{id}", consumes = MediaType.ALL_VALUE)
	public LocationLight getOne(@PathVariable long id) {
		try {
			Location loc = locationService.findById(id);
			return loc == null ? null :
					new LocationLight(loc.getId(), loc.getName(), loc.getLongitude(), loc.getLatitude(),
							loc.getAddress(), loc.getSport());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching locations");
		}
	}
}

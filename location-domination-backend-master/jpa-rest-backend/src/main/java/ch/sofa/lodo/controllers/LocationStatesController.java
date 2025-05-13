package ch.sofa.lodo.controllers;

import ch.sofa.lodo.data.models.LocationState;
import ch.sofa.lodo.data.services.LocationStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/locationstates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationStatesController {

	private LocationStateService locationStateService;

	@Autowired
	public LocationStatesController(LocationStateService locationStateService) {
		this.locationStateService = locationStateService;
	}

	@GetMapping(consumes = MediaType.ALL_VALUE)
	public List<LocationState> getAll() {
		try {
			return locationStateService.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching location states");
		}
	}
}

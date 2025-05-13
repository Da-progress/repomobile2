package ch.sofa.lodo.controllers;

import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.models.LocationPointInfo;
import ch.sofa.lodo.data.services.LocationPointService;
import ch.sofa.lodo.data.services.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping(value = "/locations", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationController {

	private LocationService locationService;
	private LocationPointService locationPointService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	public LocationController(LocationService locationService, LocationPointService locationPointService) {
		this.locationService = locationService;
		this.locationPointService = locationPointService;
	}

	@PostConstruct
	public void init() {
		mapper.getDateFormat().setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@GetMapping(consumes = MediaType.ALL_VALUE)
	public List<Location> getAll(@RequestParam(required = false) List<Long> sports,
								 @RequestParam(required = false, name = "filter") String nameFilter,
								 @RequestParam(required = false, name = "sort") String sort,
								 @RequestParam(required = false) List<Double> near) {
		try {
			return locationService.filterBy(sports, nameFilter, near, sort);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching locations");
		}
	}

	@GetMapping(path = "{id}", consumes = MediaType.ALL_VALUE)
	public Location getOne(@PathVariable long id) {
		try {
			return locationService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching locations");
		}
	}

	@GetMapping(path = "{id}/players/points", consumes = MediaType.ALL_VALUE)
	public List<LocationPointInfo> getOne(@PathVariable long id, @RequestParam(required = false) Integer limit) {
		try {
			return locationPointService.filterByLocationId2(id, limit);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching location points");
		}
	}

	@PostMapping
	public Location createLocation(@RequestBody Location location) {
		try {
			return locationService.persist(location);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error when creating location");
		}
	}

	@PostMapping(path = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadFile(@RequestPart("file") MultipartFile file, @PathVariable long id) {

		try {
			return locationService.addImage(id, file.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error create location ");
		}
	}
}

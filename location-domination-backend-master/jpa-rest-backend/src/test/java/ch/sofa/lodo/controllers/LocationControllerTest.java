package ch.sofa.lodo.controllers;

import ch.sofa.lodo.configurations.LocationDominationApp;
import ch.sofa.lodo.configurations.SecurityConfiguration;
import ch.sofa.lodo.controllers.response.entities.LocationLight;
import ch.sofa.lodo.data.configurations.JpaConfiguration;
import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.services.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {LocationDominationApp.class, JpaConfiguration.class, SecurityConfiguration.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@RunWith(SpringRunner.class)
public class LocationControllerTest {

	private static final String LOCATIONS_PATH = "/locations";

	private static final String URL_DOMAIN = "http://localhost:";

	private static final String LOC_PATH = "/loc";
	@LocalServerPort
	int port;
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private LocationService locationService;

	@Test
	public void locationsCount() throws Exception {
		ResponseEntity<Location[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOCATIONS_PATH, Location[].class);

		assertEquals(7, ((Location[]) response.getBody()).length);
	}

	@Test
	public void locationLightCount() throws Exception {

		ResponseEntity<LocationLight[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOC_PATH, LocationLight[].class);

		assertEquals(7, ((LocationLight[]) response.getBody()).length);
	}

	@Test
	public void locationSingleEqual() throws Exception {

		Long id = 1L;

		Location instanceFromDb = locationService.findById(id);

		ResponseEntity<Location> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + "/locations/" + id, Location.class);

		Location instanceFromResponse = ((Location) response.getBody());

		assertTrue("location Creation_datetime", isEqual(instanceFromDb.getCreationDateTime(), instanceFromResponse.getCreationDateTime()));
		assertTrue("location Creator", isEqual(instanceFromDb.getCreator(), instanceFromResponse.getCreator()));
		assertTrue("location latitude", isEqual(instanceFromDb.getLatitude(), instanceFromResponse.getLatitude()));
		assertTrue("location longitude", isEqual(instanceFromDb.getLongitude(), instanceFromResponse.getLongitude()));
		assertTrue("location locationstate", isEqual(instanceFromDb.getLocationState(), instanceFromResponse.getLocationState()));
		assertTrue("location name", isEqual(instanceFromDb.getName(), instanceFromResponse.getName()));
		assertTrue("location recordVersion", isEqual(instanceFromDb.getRecordVersion(), instanceFromResponse.getRecordVersion()));
		assertTrue("location sport", isEqual(instanceFromDb.getSport(), instanceFromResponse.getSport()));
		assertTrue("location verification_datetime", isEqual(instanceFromDb.getVerificationDateTime(), instanceFromResponse.getVerificationDateTime()));
	}

	@Test
	public void locationLightSingleEqual() throws Exception {

		Long id = 1L;

		Location instanceFromDb = locationService.findById(id);

		ResponseEntity<LocationLight> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + "/loc/" + id, LocationLight.class);

		LocationLight instanceFromResponse = ((LocationLight) response.getBody());

		assertTrue("locationState latitude", isEqual(instanceFromDb.getLatitude(), instanceFromResponse.getLatitude()));
		assertTrue("locationState longitude", isEqual(instanceFromDb.getLongitude(), instanceFromResponse.getLongitude()));
		assertTrue("locationState sport", isEqual(instanceFromDb.getSport()
				.getId(), instanceFromResponse.getSport()));
	}

	@Test
	public void locationsBySports() throws Exception {

		String sportsIds = "1,5";

		ResponseEntity<Location[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOCATIONS_PATH + "?sports=" + sportsIds, Location[].class);

		List<Location> locations = Arrays.asList(((Location[]) response.getBody()));
		List<Location> errorLocations = locations.stream()
				.filter(e -> !(e.getSport()
						.getId()
						.equals(1L)
						|| e.getSport()
						.getId()
						.equals(5L)))
				.collect(Collectors.toList());

		assertEquals(0, errorLocations.size());
		assertEquals(4, locations.size());
	}

	@Test
	public void locationStatesBySports() throws Exception {

		String sportsIds = "1,5";

		ResponseEntity<LocationLight[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOC_PATH + "?sports=" + sportsIds, LocationLight[].class);

		List<LocationLight> locations = Arrays.asList(((LocationLight[]) response.getBody()));
		List<LocationLight> errorLocations = locations.stream()
				.filter(e -> !(e.getSport()
						.equals(1L)
						|| e.getSport()
						.equals(5L)))
				.collect(Collectors.toList());

		assertEquals(0, errorLocations.size());
		assertEquals(4, locations.size());
	}

	@Test
	public void locationsNameFilter() throws Exception {

		String searchString = "haus";

		ResponseEntity<Location[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOCATIONS_PATH + "?filter=" + searchString, Location[].class);

		List<Location> locations = Arrays.asList(((Location[]) response.getBody()));
		List<Location> errorLocations = locations.stream()
				.filter(e -> !e.getName()
						.toLowerCase()
						.contains(searchString.toLowerCase()))
				.collect(Collectors.toList());

		assertEquals(0, errorLocations.size());
		assertEquals(3, locations.size());
	}

	@Test
	public void locationLightByNameFilter() throws Exception {

		String searchString = "haus";

		ResponseEntity<LocationLight[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOC_PATH + "?filter=" + searchString, LocationLight[].class);

		List<LocationLight> locations = Arrays.asList(((LocationLight[]) response.getBody()));

		assertEquals(3, locations.size());
	}

	@Test
	public void locationsNear() throws Exception {

		Long id = 3L;

		Location instanceFromDb = locationService.findById(id);

		NumberFormat formatter = new DecimalFormat("#0.000000");

		String searchString = String.join(",", formatter.format(instanceFromDb.getLatitude()), formatter.format(instanceFromDb.getLongitude()), String.valueOf(5));
		ResponseEntity<Location[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOCATIONS_PATH + "?near=" + searchString, Location[].class);

		List<Location> locations = Arrays.asList(((Location[]) response.getBody()));
		assertEquals(5, locations.size());
	}

	@Test
	public void locationLightNear() throws Exception {

		Long id = 3L;

		Location instanceFromDb = locationService.findById(id);

		NumberFormat formatter = new DecimalFormat("#0.000000");

		String searchString = String.join(",", formatter.format(instanceFromDb.getLatitude()), formatter.format(instanceFromDb.getLongitude()), String.valueOf(5));
		ResponseEntity<LocationLight[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOC_PATH + "?near=" + searchString, LocationLight[].class);

		List<LocationLight> locations = Arrays.asList(((LocationLight[]) response.getBody()));
		assertEquals(5, locations.size());
	}

	@Test
	public void locationsBySportsAndNameFilter() throws Exception {

		String searchString = "schac";
		String sportsIds = "2";

		ResponseEntity<Location[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOCATIONS_PATH + "?filter=" + searchString + "&sports=" + sportsIds, Location[].class);

		List<Location> locations = Arrays.asList(((Location[]) response.getBody()));
		List<Location> errorLocations = locations.stream()
				.filter(e -> !e.getName()
						.toLowerCase()
						.contains(searchString.toLowerCase()))
				.collect(Collectors.toList());

		assertEquals(0, errorLocations.size());
		assertEquals(2, locations.size());
	}

	@Test
	public void locationLightBySportsAndNameFilter() throws Exception {

		String searchString = "schac";
		String sportsIds = "2";

		ResponseEntity<LocationLight[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOCATIONS_PATH + "?filter=" + searchString + "&sports=" + sportsIds, LocationLight[].class);

		List<LocationLight> locations = Arrays.asList(((LocationLight[]) response.getBody()));

		assertEquals(2, locations.size());
	}

	private boolean isEqual(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1 == null && o2 != null) {
			return false;
		}
		if (o1 != null && o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		return false;
	}

	@Test
	public void generateBcryptHash() {
		BCryptPasswordEncoder cc = new BCryptPasswordEncoder();
		String pass = "testpass";
		System.out.println("..generated hash:" + cc.encode(pass) + "for pass: " + pass);
	}
}

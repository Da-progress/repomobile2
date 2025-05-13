package ch.sofa.lodo.controllers;

import ch.sofa.lodo.configurations.LocationDominationApp;
import ch.sofa.lodo.configurations.SecurityConfiguration;
import ch.sofa.lodo.data.configurations.JpaConfiguration;
import ch.sofa.lodo.data.models.LocationPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {LocationDominationApp.class, JpaConfiguration.class, SecurityConfiguration.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@RunWith(SpringRunner.class)
public class LocationPointControllerTest {

	private static final String LOCATION_POINTS_PATH = "/location_points";

	private static final String URL_DOMAIN = "http://localhost:";
	@LocalServerPort
	int port;
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void locationsCount() {
		ResponseEntity<LocationPoint[]> response = this.restTemplate.withBasicAuth("testuser", "testpass")
				.getForEntity(URL_DOMAIN + port + LOCATION_POINTS_PATH + "/1", LocationPoint[].class);

		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().length);
	}
}

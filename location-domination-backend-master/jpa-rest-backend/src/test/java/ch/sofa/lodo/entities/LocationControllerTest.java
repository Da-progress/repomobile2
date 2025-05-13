package ch.sofa.lodo.entities;

import ch.sofa.lodo.data.models.SmsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = {SmsResponse.class})
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@RunWith(SpringRunner.class)
public class LocationControllerTest {

	@Test
	public void deserializeTest() throws Exception {
		String response = "{\"StatusCode\":\"1\",\"StatusInfo\":\"OK\"}";

		ObjectMapper objectMapper = new ObjectMapper();
		SmsResponse smsResponse = objectMapper.readValue(response, SmsResponse.class);

		assertEquals(smsResponse.getStatusCode(), 1);
		assertEquals(smsResponse.getStatusInfo(), "OK");
	}
}

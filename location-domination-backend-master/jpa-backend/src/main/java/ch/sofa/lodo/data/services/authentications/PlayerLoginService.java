package ch.sofa.lodo.data.services.authentications;

import ch.sofa.lodo.data.constants.UserType;
import ch.sofa.lodo.data.models.SmsRequest;
import ch.sofa.lodo.data.models.SmsResponse;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.UserRepository;
import ch.sofa.lodo.data.services.dtos.LoginResponseDto;
import ch.sofa.lodo.data.services.dtos.NewPlayerDto;
import ch.sofa.lodo.data.services.dtos.PlayerRegistrationDto;
import ch.sofa.lodo.data.services.generators.RegistrationCodeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlayerLoginService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public LoginResponseDto login(String username, String password, String firebaseDeviceToken) {

		LoginResponseDto dto = new LoginResponseDto();

		List<User> users = userRepository.findAllByUsernameAndDeletedFalse(username);
		if (users.isEmpty()) {
			dto.setErrorMessage("WRONG_Password_or_USERNAME");
			return dto;
		} else if (users.size() == 1) {

			// copy only mandatory data
			User original = users.get(0);

			if (bCryptPasswordEncoder.matches(password, original.getPassword())) {
				User loggedinPlayer = new User();
				loggedinPlayer.setUsername(original.getUsername());
				loggedinPlayer.setId(original.getId());
				loggedinPlayer.setAuthenticated(original.isAuthenticated());
				dto.setPlayer(loggedinPlayer);
				dto.setSuccess(true);
				// update login count
				original.setCountLogin(original.getCountLogin() + 1);
				userRepository.save(original);
				userRepository.save(original);

				// update user with current device firebase token
				original.setFirebaseUserDeviceToken(firebaseDeviceToken);
				userRepository.save(original);

				return dto;
			} else {
				dto.setErrorMessage("WRONG_Password_or_USERNAME");
				return dto;
			}
		} else {
			System.out.println("PlayerLogin service # For user: " + username + ", found multiple entries.");
			dto.setErrorMessage("LOGIN_PROBLEM_MULTIPLE");
			return dto;
		}
	}

	public PlayerRegistrationDto register(NewPlayerDto player) {

		PlayerRegistrationDto dto = new PlayerRegistrationDto();
		try {
			if (!testUsername(player.getUsername()).isDuplicate()) {
				User newPlayer = new User();
				newPlayer.setUserType(UserType.PLAYER);
				newPlayer.setUsername(player.getUsername());
				newPlayer.setRegisterDateTime(LocalDateTime.now());
				newPlayer.setMobileNumber(player.getMobileNr());
				newPlayer.setPasswordHash(bCryptPasswordEncoder.encode(player.getPassword()));
				newPlayer.setFirebaseUserDeviceToken(player.getFirebaseUserDeviceToken());
				dto.setRegisteredPlayer(userRepository.save(newPlayer));
				// code
				sendAuthorizationCode(createAuthorizationCode(dto.getRegisteredPlayer()));
				return dto;
			} else {
				// for duplicate
				dto.setDuplicate(true);
				return dto;
			}
		} catch (Exception ex) {
			// something went wrong - no registration
			return dto;
		}
	}

	private User createAuthorizationCode(User newPlayer) {
		newPlayer.setAuthorizationCode(RegistrationCodeGenerator.generate());
		return userRepository.save(newPlayer);
	}

	private void sendAuthorizationCode(User newPlayer) {

		System.out.println("... auth code: " + newPlayer.getAuthorizationCode());

		String hostname = "https://json.aspsms.com/SendTextSMS\n";

		try {
			RestTemplate rest = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			JSONObject sms = SmsRequest.createSmsRequest("AZAWDLKMUMJD",
					"P0rDJS8B0gC5sla6xBlZkzDo",
					"0041765948680",
					newPlayer.getMobileNumber(),
					newPlayer.getAuthorizationCode()
			);

			HttpEntity<String> request =
					new HttpEntity<String>(sms.toString(), headers);

			String response = rest.postForObject(hostname, request, String.class);

			if (response == null) throw new NullPointerException("sms could not be send");

			ObjectMapper objectMapper = new ObjectMapper();
			SmsResponse smsResponse = objectMapper.readValue(response, SmsResponse.class);

			System.out.println("... sms respond: " + smsResponse.getStatusCode() + " " + smsResponse.getStatusInfo());

			if (smsResponse.getStatusCode() != 1) {
				throw new Exception(smsResponse.getStatusInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PlayerRegistrationDto testUsername(String username) {

		User player = userRepository.findByUsernameAndUserType(username, UserType.PLAYER);
		if (player == null) {
			PlayerRegistrationDto dto = new PlayerRegistrationDto();
			dto.setDuplicate(false);
			return dto;
		}

		PlayerRegistrationDto dto = new PlayerRegistrationDto();
		dto.setDuplicate(true);
		dto.setMessage("username-duplicate");
		return dto;
	}
}

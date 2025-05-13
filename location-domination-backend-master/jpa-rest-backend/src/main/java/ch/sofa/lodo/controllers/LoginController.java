package ch.sofa.lodo.controllers;

import ch.sofa.lodo.data.services.authentications.PlayerLoginService;
import ch.sofa.lodo.data.services.dtos.LoginResponseDto;
import ch.sofa.lodo.data.services.dtos.NewPlayerDto;
import ch.sofa.lodo.data.services.dtos.PlayerRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

	private PlayerLoginService playerLoginService;

	@Autowired
	public LoginController(PlayerLoginService playerLoginService) {

		this.playerLoginService = playerLoginService;
	}

	@GetMapping(path = "testUsername", consumes = MediaType.ALL_VALUE)
	public PlayerRegistrationDto getOne(@RequestParam(required = true) String username) {
		try {
			return playerLoginService.testUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error testing username");
		}
	}

	@PutMapping(path = "register", consumes = MediaType.ALL_VALUE)
	public PlayerRegistrationDto register(@RequestBody NewPlayerDto newPlayer) {
		try {
			return playerLoginService.register(newPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error testing username");
		}
	}

	@PutMapping(consumes = MediaType.ALL_VALUE)
	public LoginResponseDto login(@RequestBody NewPlayerDto newPlayer) {
		try {
			return playerLoginService.login(newPlayer.getUsername(), newPlayer.getPassword(), newPlayer.getFirebaseUserDeviceToken());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error logging in");
		}
	}
}

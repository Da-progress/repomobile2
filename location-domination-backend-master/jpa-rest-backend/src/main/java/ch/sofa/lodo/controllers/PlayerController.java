package ch.sofa.lodo.controllers;

import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.FirebaseMessaging;
import ch.sofa.lodo.data.services.PlayerPointService;
import ch.sofa.lodo.data.services.PlayerService;
import ch.sofa.lodo.data.services.PlayerWithPointService;
import ch.sofa.lodo.data.services.authentications.PlayerLoginService;
import ch.sofa.lodo.data.services.dtos.PlayerWithPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/players", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {

	private PlayerService playerService;
	private PlayerPointService playerPointService;
	private PlayerLoginService playerLoginService;
	private FirebaseMessaging firebaseMessaging;

	@Autowired
	private PlayerWithPointService playerWithPointService;

	@Autowired
	public PlayerController(PlayerService playerService, PlayerPointService userPointService,
							PlayerLoginService playerLoginService, FirebaseMessaging firebaseMessaging) {
		super();
		this.playerService = playerService;
		this.playerPointService = userPointService;
		this.playerLoginService = playerLoginService;
		this.firebaseMessaging = firebaseMessaging;
	}

	@GetMapping(consumes = MediaType.ALL_VALUE)
	public List<User> getAll(@RequestParam(required = false, name = "filter") String nameFilter,
							 @RequestParam(required = false) List<String> excludeUsernames,
							 @RequestParam(required = false) List<Long> exclude) {
		try {
			return playerService.filterByName(nameFilter, excludeUsernames, exclude);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching players");
		}
	}

	@GetMapping(path = "{id}", consumes = MediaType.ALL_VALUE)
	public User getOne(@PathVariable long id) {
		try {
			return playerService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching players");
		}
	}

	@GetMapping(path = "{id}/authenticate", consumes = MediaType.ALL_VALUE)
	public boolean authenticate(@PathVariable long id, @RequestParam(required = false) String authCode) {
		try {
			return playerService.authenticate(id, authCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error authenticating player");
		}
	}

	@GetMapping(path = "findByPhoneNr", consumes = MediaType.ALL_VALUE)
	public User findByPhoneNr(@RequestParam(required = true) String phoneNr) {
		try {
			return playerService.findByPhoneNr(phoneNr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error authenticating player");
		}
	}

	@PostMapping(path = "{id}", consumes = MediaType.ALL_VALUE)
	public User updatePlayer(@PathVariable long id, @RequestBody User player) {
		try {
			User updated = playerService.updateLimited(player);
//			MessageDto dto = new MessageDto(updated.getFirebaseUserDeviceToken(), "player updated", updated.getUsername());
//			firebaseMessaging.sendNotificationMessage3(dto);
			return updated;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching players");
		}
	}

	@PostMapping(path = "{id}/changePassword", consumes = MediaType.ALL_VALUE)
	public User changePassword(@PathVariable long id, @RequestBody User player) {
		try {
			return playerService.changePassword(player);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching players");
		}
	}

	@PostMapping(path = "{id}/delete", consumes = MediaType.ALL_VALUE)
	public void delete(@PathVariable long id) {
		try {
			playerService.markAsDeleted(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error marking player as deleted");
		}
	}

	@GetMapping(path = "{id}/changePasswordOnForgot", consumes = MediaType.ALL_VALUE)
	public User changePasswordOnForgot(@PathVariable long id, @RequestParam(required = true) String code, @RequestParam(required = true) String password) {
		try {
			return playerService.changePassword(id, code, password);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching players");
		}
	}

	@GetMapping(path = "{id}/points", consumes = MediaType.ALL_VALUE)
	public List<PlayerPoint> getPoints(@PathVariable long id) {
		try {
			return playerPointService.filterBy(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching users");
		}
	}

	@GetMapping(path = "points", consumes = MediaType.ALL_VALUE)
	public List<PlayerWithPoints> getPlyersWithPoints(@RequestParam(required = false, name = "filter") String nameFilter,
													  @RequestParam(required = false) List<String> excludeUsernames,
													  @RequestParam(required = false) List<Long> exclude) {
		try {
			return playerWithPointService.filterByName(nameFilter, excludeUsernames, exclude);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching users");
		}
	}

	@GetMapping(path = "count", consumes = MediaType.ALL_VALUE)
	public long count() {
		try {
			return playerService.count();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error counting players");
		}
	}
}

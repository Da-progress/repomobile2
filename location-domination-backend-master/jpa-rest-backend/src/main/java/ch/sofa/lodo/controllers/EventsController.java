package ch.sofa.lodo.controllers;

import ch.sofa.lodo.controllers.response.entities.EventLight;
import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventPointInfo;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.services.EventPlayerWithPointService;
import ch.sofa.lodo.data.services.EventPointService;
import ch.sofa.lodo.data.services.EventService;
import ch.sofa.lodo.data.services.EventUserService;
import ch.sofa.lodo.data.services.dtos.PlayerWithPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/events", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
public class EventsController {

	private EventService eventService;
	private EventUserService eventUserService;
	private EventPointService eventPointService;

	@Autowired
	private EventPlayerWithPointService eventPlayerWithPointService;

	@Autowired
	public EventsController(EventService eventService, EventUserService eventUserService,
							EventPointService eventPointService) {
		this.eventService = eventService;
		this.eventUserService = eventUserService;
		this.eventPointService = eventPointService;
	}

	@GetMapping(consumes = MediaType.ALL_VALUE)
	public List<EventLight> getEvents() {
		// events limited by time length in past (1 month) and if future (6 months)
		// compare with today date

		try {
			LocalDate today = LocalDate.now();
			LocalDate lowerDateLimit = today.minusDays(30);
			LocalDate upperDateLimit = today.plusDays(180);

			List<Event> events = eventService.findAllInDateFrame(lowerDateLimit, upperDateLimit);
			return events.stream().map(EventLight::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException("Error fetching events");
		}
	}

	@GetMapping(path = "map", consumes = MediaType.ALL_VALUE)
	public List<Event> getEventSports(@RequestParam(required = false) List<Long> sports,
									  @RequestParam(required = false, name = "filter") String nameFilter,
									  @RequestParam(required = false, name = "sort") String sort,
									  @RequestParam(required = false) List<Double> near) {
		// events limited by time length in past (1 month) and if future (6 months)
		// compare with today date

		try {
			LocalDate today = LocalDate.now();
			LocalDate lowerDateLimit = today.minusDays(30);
			LocalDate upperDateLimit = today.plusDays(180);

			return eventService.findAllInDateFrame(sports, nameFilter, near, sort, lowerDateLimit, upperDateLimit);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching event sports for map");
		}
	}

	@GetMapping(path = "{id}", consumes = MediaType.ALL_VALUE)
	public Event getOne(@PathVariable long id) {
		try {
			return eventService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching event");
		}
	}

	@GetMapping(path = "{id}/players/register", consumes = MediaType.ALL_VALUE)
	public boolean register(@PathVariable long id, @RequestParam(required = true) long player,
							@RequestParam(required = true) String registrationCode) {
		try {
			return eventUserService.registerPlayer(id, player, registrationCode) != null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error registering player");
		}
	}

	@GetMapping(path = "{id}/players/unregister", consumes = MediaType.ALL_VALUE)
	public boolean unregister(@PathVariable long id, @RequestParam(required = true) long player) {
		try {
			return eventUserService.unregisterPlayer(id, player);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error unregistering player");
		}
	}

	@GetMapping(path = "{id}/players/{playerId}/isRegistered", consumes = MediaType.ALL_VALUE)
	public boolean isRegistered(@PathVariable long id, @PathVariable long playerId) {
		try {
			return eventUserService.isPlayerRegistered(id, playerId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error testing registered player");
		}
	}

	@GetMapping(path = "{id}/players/points", consumes = MediaType.ALL_VALUE)
	public List<EventPointInfo> getPoints(@PathVariable long id, @RequestParam(required = false) List<Long> sport,
										  @RequestParam(required = false) int limit) {
		try {
			if (sport != null)
				sport.forEach(e -> System.out.println(".... event sport" + e));

			List<EventPointInfo> result = eventPointService.filterByEventId2(id, sport, limit);
			System.out.println("... result size ? " + result.size());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching event points");
		}
	}

	@GetMapping(path = "{id}/players", consumes = MediaType.ALL_VALUE)
	public List<User> getRegisteredPlayers(@PathVariable long id,
										   @RequestParam(required = false, name = "filter") String nameFilter,
										   @RequestParam(required = false) List<Long> exclude) {
		try {
			return eventUserService.findAllByEventId(id, nameFilter, exclude);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching event registered players");
		}
	}

	@GetMapping(path = "{id}/playerswithpoints", consumes = MediaType.ALL_VALUE)
	public List<PlayerWithPoints> getRegisteredPlayersWithPoints(@PathVariable long id,
																 @RequestParam(required = false, name = "filter") String nameFilter,
																 @RequestParam(required = false) List<Long> exclude) {
		try {
			return eventPlayerWithPointService.findAllByEventId(id, nameFilter, exclude);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching event registered players");
		}
	}
}

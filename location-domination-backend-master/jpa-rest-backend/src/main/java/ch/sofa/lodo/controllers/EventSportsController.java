package ch.sofa.lodo.controllers;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventSport;
import ch.sofa.lodo.data.services.EventSportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/eventSports", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
public class EventSportsController {

	private EventSportService eventSportService;

	@Autowired
	public EventSportsController(EventSportService eventSportService) {
		this.eventSportService = eventSportService;
	}

	@GetMapping(consumes = MediaType.ALL_VALUE)
	public EventSport getEventSport(@RequestParam(required = false) Long event,
									@RequestParam(required = false) Long sport) {

		try {
			List<Long> sportIds = new ArrayList<>();
			sportIds.add(sport);
			List<EventSport> result = eventSportService.filterByEventIdAndSportId(event, sportIds);
			if (result.isEmpty()) {
				return null;
			} else if (result.size() == 1) {
				return result.get(0);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error registering player");
		}
	}

	@GetMapping(path = "event", consumes = MediaType.ALL_VALUE)
	public Event getEventromEventSPort(@RequestParam(required = true) Long id) {

		try {
			return eventSportService.findEventByEventSportId(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error registering player");
		}
	}
}

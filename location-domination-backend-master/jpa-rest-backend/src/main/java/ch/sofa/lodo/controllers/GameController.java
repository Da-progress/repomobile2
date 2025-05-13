package ch.sofa.lodo.controllers;

import ch.sofa.lodo.data.constants.GameState;
import ch.sofa.lodo.data.models.Game;
import ch.sofa.lodo.data.services.GameService;
import ch.sofa.lodo.data.services.dtos.GameDominationDto;
import ch.sofa.lodo.data.services.dtos.MyStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/games", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

	private GameService gameService;

	@Autowired
	public GameController(GameService gameService) {
		super();
		this.gameService = gameService;
	}

	@GetMapping(consumes = MediaType.ALL_VALUE)
	public List<Game> getAll(@RequestParam(required = false) List<Long> event,
							 @RequestParam(required = false) List<Long> playersIds, @RequestParam(required = false) List<Long> location,
							 @RequestParam(required = false) List<Integer> sport, @RequestParam(required = false) List<GameState> state,
							 @RequestParam(required = false) Long player) {

		if (event == null && location != null && playersIds == null) {
			try {
				return gameService.filterBy(state, location, player);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error fetching games from event");
			}
		} else if (event != null && location == null && playersIds == null) {
			try {
				return gameService.filterBy(state, event, sport.stream()
						.map(Long::new)
						.collect(Collectors.toList()));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error fetching games from location");
			}
		} else if (event == null && location == null && playersIds != null) {
			try {
				return gameService.filterByPlayers(state, playersIds, sport.stream()
						.map(Long::new)
						.collect(Collectors.toList()));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error fetching games from location");
			}
		} else {
			throw new RuntimeException("Error fetching games");
		}
	}

	@GetMapping(path = "{id}", consumes = MediaType.ALL_VALUE)
	public Game getOne(@PathVariable long id) {
		try {
			return gameService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error getting game with id " + id);
		}
	}

	@GetMapping(path = "dominations", consumes = MediaType.ALL_VALUE)
	public List<GameDominationDto> getPlayerDominations(@RequestParam(required = false) List<Long> playersIds) {
		try {
			if (playersIds != null && playersIds.size() == 1) {
				return gameService.findPlayerDominations(playersIds);
			}

			return new ArrayList<GameDominationDto>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error getting player dominations");
		}
	}

	@GetMapping(path = "stats/socializer", consumes = MediaType.ALL_VALUE)
	public List<MyStatsDto> getPlayerStats(@RequestParam(required = false) List<Long> playersIds) {
		try {
			if (playersIds != null && playersIds.size() == 1) {
				return gameService.findSocialStats(playersIds);
			}

			return new ArrayList<MyStatsDto>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error getting stats socializer");
		}
	}

	@GetMapping(path = "stats/total", consumes = MediaType.ALL_VALUE)
	public List<MyStatsDto> getPlayerTotalStats(@RequestParam(required = false) List<Long> playersIds) {
		try {
			if (playersIds != null && playersIds.size() == 1) {
				return gameService.findPlayerTotalStats(playersIds);
			}

			return new ArrayList<MyStatsDto>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error getting stats total");
		}
	}

	@GetMapping(path = "stats/sports", consumes = MediaType.ALL_VALUE)
	public List<MyStatsDto> getPlayerSportsStats(@RequestParam(required = false) List<Long> playersIds) {
		try {
			if (playersIds != null && playersIds.size() == 1) {
				return gameService.findPlayerSportsStats(playersIds);
			}

			return new ArrayList<MyStatsDto>();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error getting stats sports");
		}
	}

	@PostMapping
	public Game createGame(@RequestBody Game game) {
		try {
			return gameService.persist(game, true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating game");
		}
	}

	@PutMapping(path = "{id}")
	public Game updateGame(@RequestBody Game game, @PathVariable long id) {
		if (gameService.findById(id) == null) {
			throw new RuntimeException("No update. Game not exist: " + id);
		}
		try {
			return gameService.update(game, true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error updating game");
		}
	}
}

package ch.sofa.lodo.controllers;

import ch.sofa.lodo.data.services.dtos.GlobalPoint;
import ch.sofa.lodo.data.services.points.GlobalPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/leaderboard", consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
public class LeaderboardController {

	private GlobalPointService globalPointService;

	@Autowired
	public LeaderboardController(GlobalPointService globalPointService) {
		this.globalPointService = globalPointService;
	}

	@GetMapping(consumes = MediaType.ALL_VALUE)
	public List<GlobalPoint> getList(@RequestParam(required = false) List<Long> sports,
									 @RequestParam(required = false) String filter,
									 @RequestParam(required = false) boolean socialPoints,
									 @RequestParam(required = false) boolean explorerPoints,
									 @RequestParam(required = false) boolean totalPoints) {

		try {
			return globalPointService.findAllBy(filter, sports, socialPoints, explorerPoints, totalPoints);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error locading leaderboard");
		}
	}
}

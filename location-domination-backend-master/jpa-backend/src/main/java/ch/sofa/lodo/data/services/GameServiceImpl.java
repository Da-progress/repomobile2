package ch.sofa.lodo.data.services;

import ch.sofa.lodo.data.constants.GameState;
import ch.sofa.lodo.data.models.EventPoint;
import ch.sofa.lodo.data.models.EventSport;
import ch.sofa.lodo.data.models.ExplorerPoint;
import ch.sofa.lodo.data.models.Game;
import ch.sofa.lodo.data.models.Location;
import ch.sofa.lodo.data.models.LocationPoint;
import ch.sofa.lodo.data.models.PlayerPoint;
import ch.sofa.lodo.data.models.SocializerPoint;
import ch.sofa.lodo.data.models.Sport;
import ch.sofa.lodo.data.models.User;
import ch.sofa.lodo.data.repositories.GameRepository;
import ch.sofa.lodo.data.services.FirebaseMessaging.MessageDto;
import ch.sofa.lodo.data.services.dtos.GameDominationDto;
import ch.sofa.lodo.data.services.dtos.MyStatsDto;
import ch.sofa.lodo.data.services.points.ExplorerPointCalculatorService;
import ch.sofa.lodo.data.services.points.ExplorerPointService;
import ch.sofa.lodo.data.services.points.PointCalculatorService;
import ch.sofa.lodo.data.services.points.SocialPointCalculatorService;
import ch.sofa.lodo.data.services.points.SocializerPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository repository;
	@Autowired
	private EventSportService eventSportService;
	@Autowired
	private PointCalculatorService pointService;
	@Autowired
	private SocialPointCalculatorService socialPointCalculatorService;
	@Autowired
	private ExplorerPointCalculatorService explorerPointCalculatorService;
	@Autowired
	private LocationPointService locationPointService;
	@Autowired
	private EventPointService eventPointService;
	@Autowired
	private ExplorerPointService explorerPointService;
	@Autowired
	private SocializerPointService socializerPointService;
	@Autowired
	private FirebaseMessaging firebaseMessaging;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private PlayerPointService playerPointService;

	@Override
	public List<MyStatsDto> findSocialStats(List<Long> playersIds) {

		if (playersIds != null && playersIds.size() == 1) {

			SocializerPoint socPoints = socializerPointService.findByPlayerId(playersIds.get(0));

			ExplorerPoint explorerPoints = explorerPointService.findByPlayerId(playersIds.get(0));

			int socPointsNumber = socPoints == null ? 0 : socPoints.getPoints();
			int explorerPointsNumber = explorerPoints == null ? 0 : explorerPoints.getPoints();

			int socializerRanking = socializerPointService.getRank(socPointsNumber);
			int explorerRanking = explorerPointService.getRank(explorerPointsNumber);
			MyStatsDto socDto = new MyStatsDto("fas-user-friends", socPointsNumber, "Socializer-Punkte -> Je mehr unterschiedliche Gegner, je mehr Punkte.", socializerRanking);
			MyStatsDto exploPoints = new MyStatsDto("fas-compass", explorerPointsNumber, "Explorer-Punkte -> Je mehr unterschiedliche Locations bespielt werden, je mehr Punkte.", explorerRanking);

			List<MyStatsDto> myStatsDtos = new ArrayList<>();
			myStatsDtos.add(socDto);
			myStatsDtos.add(exploPoints);

			return myStatsDtos;
		} else {
			//
			return new ArrayList<>();
		}
	}

	@Override
	public List<MyStatsDto> findPlayerTotalStats(List<Long> playersIds) {

		if (playersIds != null && playersIds.size() == 1) {

			SocializerPoint socPoints = socializerPointService.findByPlayerId(playersIds.get(0));

			ExplorerPoint explorerPoints = explorerPointService.findByPlayerId(playersIds.get(0));

			int socPointsNumber = socPoints == null ? 0 : socPoints.getPoints();
			int explorerPointsNumber = explorerPoints == null ? 0 : explorerPoints.getPoints();

			Integer playerSportsPointsNullable = playerPointService.sumPlayerPoints(playersIds.get(0));
			int playersPortsPoints = playerSportsPointsNullable == null ? 0 : playerSportsPointsNullable;

			int totalPlayerPoints = socPointsNumber + explorerPointsNumber + playersPortsPoints;

			int rankTotal = playerPointService.countBetterPlayersTotal(totalPlayerPoints);

			MyStatsDto totalDto = new MyStatsDto("fas-globe-europe", totalPlayerPoints, "Total", rankTotal);

			List<MyStatsDto> myStatsDtos = new ArrayList<>();
			myStatsDtos.add(totalDto);

			return myStatsDtos;
		} else {
			//
			return new ArrayList<>();
		}
	}

	@Override
	public List<MyStatsDto> findPlayerSportsStats(List<Long> playersIds) {

		if (playersIds != null && playersIds.size() == 1) {

			Map<Sport, MyStatsDto> statsBySports = new HashMap<>();
			List<PlayerPoint> sportPoints = playerPointService.filterBy(playersIds.get(0));

			for (PlayerPoint point : sportPoints) {
				int rank = playerPointService.getPlayerRank(point.getPoints(), point.getSport().getId());
				statsBySports.put(point.getSport(), new MyStatsDto(point.getSport().getIconName(), point.getPoints(), point.getSport().getName(), rank));
			}

			return new ArrayList<>(statsBySports.values());
		} else {
			//
			return new ArrayList<>();
		}
	}

	@Override
	public Game persist(Game entity, boolean sendNotifycation) {
		if (entity.getGameState() == null) {
			entity.setGameState(GameState.OPEN);
		}
		entity.setCreationDateTime(LocalDateTime.now());
		entity.setRecordVersion(1); // TODO duplicate remove ?

		Game game = repository.save(entity);
		User guest = game.getGuestPlayer() == null ? null : playerService.findById(game.getGuestPlayer().getId());

		if (sendNotifycation) {
			if (GameState.OPEN.equals(game.getGameState())) {
				if (game.getAppointment1() != null || game.getAppointment2() != null
						|| game.getAppointment3() != null) {
					// notify only when guest player is set
					if (guest != null) {
						// host made appointments
						String title = "Player " + game.getHostPlayer().getUsername() + " hat ein Game vorgeschlagen!";
						sendGameNotification(guest, game, title);
					}
				}
			}

			if (GameState.RECORDED.equals(game.getGameState())) {

				String title = "Player " + game.getHostPlayer().getUsername() + " hat ein Game erfasst.";
				sendGameNotification(guest, game, title);
			}
		}
		return game;
	}

	@Override
	public Game persist(Game entity) {
		if (entity.getGameState() == null) {
			entity.setGameState(GameState.OPEN);
		}
		entity.setCreationDateTime(LocalDateTime.now());
		entity.setRecordVersion(1); // TODO duplicate remove ?

		return repository.save(entity);
	}

	@Transactional
	@Override
	public Game update(Game entity, boolean sendNotifications) {
		Game oldGameState = findById(entity.getId());
		GameState oldState = oldGameState.getGameState();
		boolean isGuestSuggesting = oldGameState.isHasHostMadeAppointments() && !entity.isHasHostMadeAppointments();
		boolean isHostSuggesting = !oldGameState.isHasHostMadeAppointments() && entity.isHasHostMadeAppointments();

		boolean pointCandidate = testAddPointsCandidate(entity);

		boolean isResultChanged = oldGameState.isHasHostSuggestedResult() != entity.isHasHostSuggestedResult();

		if (isResultChanged && entity.getEventSport().getEvent().getEndDateTime() != null && LocalDateTime.now()
				.isAfter(entity.getEventSport().getEvent().getEndDateTime().plusMinutes(15))) {
			throw new RuntimeException("Can't record results 15 minutes passed end date");
		}
		
		Game game = repository.save(entity);

		if (pointCandidate) {
			socialPointCalculatorService.savePoints(entity);
			explorerPointCalculatorService.savePoints(entity);
			pointService.savePoints(entity);
		}

		if (sendNotifications) {
			if (isResultChanged) {
				User playerToNotify = null;
				User playerWhoChangedResult = null;
				if (game.isHasHostSuggestedResult()) {
					playerToNotify = playerService.findById(game.getGuestPlayer().getId());
					playerWhoChangedResult = game.getHostPlayer();
				} else {
					playerToNotify = playerService.findById(game.getHostPlayer().getId());
					playerWhoChangedResult = game.getGuestPlayer();
				}

				String title = "Player " + playerWhoChangedResult.getUsername() + " korrigierte Game Resultat";
				sendGameNotification(playerToNotify, game, title);
			} else {
				sendNotification(oldState, isGuestSuggesting, isHostSuggesting, game);
			}
		}

		return game;
	}

	@Override
	public Game update(Game entity) {

		boolean pointCandidate = testAddPointsCandidate(entity);

		Game game = repository.save(entity);

		if (pointCandidate) {
			socialPointCalculatorService.savePoints(entity);
			explorerPointCalculatorService.savePoints(entity);
			pointService.savePoints(entity);
		}

		return game;
	}

	private void sendGameNotification(User recipient, Game game, String title) {
		MessageDto dto;
		if (game.getLocation() != null) {
			dto = new MessageDto(recipient.getFirebaseUserDeviceToken(), title,
					"Game bei Location " + game.getLocation().getName());
		} else if (game.getEventSport() != null) {
			dto = new MessageDto(recipient.getFirebaseUserDeviceToken(), title,
					"Game bei Event " + game.getEventSport().getEvent().getName());
		} else {
			System.err.println("Can not send notification for invalid game");
			return;
		}
		dto.getData().put(MessageDto.GAME_STATE_KEY_MAP, game.getGameState().getName());
		dto.getData().put(MessageDto.GAME_ID_KEY, String.valueOf(game.getId()));

		try {
			firebaseMessaging.sendNotificationMessage3(dto);
		} catch (IOException e) {
			System.out.println("Firebase can not send message" + dto.toString());
			e.printStackTrace();
		}
	}

	private void sendNotification(GameState oldState, boolean isGuestSuggesting, boolean isHostSuggesting, Game game) {
		if (GameState.OPEN.equals(oldState) && GameState.OPEN.equals(game.getGameState())) {
			if (game.getAppointment1() != null || game.getAppointment2() != null || game.getAppointment3() != null) {
				// notify only when guest player is set
				if (isGuestSuggesting) {
					User host = playerService.findById(game.getHostPlayer().getId());

					if (game.getGuestPlayer() != null) {
						// host made appointments
						String title = "Player " + game.getGuestPlayer().getUsername() + " hat ein Game vorgeschlagen";
						sendGameNotification(host, game, title);
					}
				} else if (isHostSuggesting) {
					User guest = playerService.findById(game.getGuestPlayer().getId());

					if (game.getGuestPlayer() != null) {
						// guest made appointments
						String title = "Player " + game.getHostPlayer().getUsername() + " hat ein Game vorgeschlagen";
						sendGameNotification(guest, game, title);
					}
				}
			}
		} else if (GameState.OPEN.equals(oldState) && GameState.PLANNED.equals(game.getGameState())) {

			if (game.isHasHostMadeAppointments()) {
				User host = playerService.findById(game.getHostPlayer().getId());
				// host made appointments
				String title = "Player " + game.getGuestPlayer().getUsername() + " akzeptierte Game";
				sendGameNotification(host, game, title);
			} else {
				User guest = playerService.findById(game.getGuestPlayer().getId());
				// guest made appointments
				String title = "Player " + game.getHostPlayer().getUsername() + " akzeptierte Game";
				sendGameNotification(guest, game, title);
			}
		} else if (GameState.PLANNED.equals(oldState) && GameState.RECORDED.equals(game.getGameState())) {

			String title = "Player " + game.getHostPlayer().getUsername() + " hat ein Game Resultat erfasst";
			User guest = playerService.findById(game.getGuestPlayer().getId());
			sendGameNotification(guest, game, title);
		} else if (GameState.RECORDED.equals(oldState) && GameState.PLAYED.equals(game.getGameState())) {

			if (game.isHasHostSuggestedResult()) {
				String title = "Player " + game.getGuestPlayer().getUsername() + " bestätigte Game";
				User host = playerService.findById(game.getHostPlayer().getId());
				sendGameNotification(host, game, title);
			} else {
				String title = "Player " + game.getHostPlayer().getUsername() + " bestätigte Game";
				User guest = playerService.findById(game.getGuestPlayer().getId());
				sendGameNotification(guest, game, title);
			}
		} else if (GameState.RECORDED.equals(oldState) && GameState.INVALID.equals(game.getGameState())) {

			if (game.isHasHostSuggestedResult()) {
				String title = "Player " + game.getGuestPlayer().getUsername() + " hat Game Resultat abgelehnt";
				User host = playerService.findById(game.getHostPlayer().getId());
				sendGameNotification(host, game, title);
			} else {
				String title = "Player " + game.getHostPlayer().getUsername() + " hat Game Resultat abgelehnt";
				User guest = playerService.findById(game.getGuestPlayer().getId());
				sendGameNotification(guest, game, title);
			}
		} else if (GameState.OPEN.equals(oldState) && GameState.INVALID.equals(game.getGameState())) {
			if (game.getGuestPlayer() != null) {
				String title = "Player " + game.getHostPlayer().getUsername() + " hat Spiel gelöscht";
				User guest = playerService.findById(game.getGuestPlayer().getId());
				sendGameNotification(guest, game, title);
			}
		}
	}

	private boolean testAddPointsCandidate(Game entity) {
		if (GameState.PLAYED.equals(entity.getGameState())) {
			// test state transition
			long countLastRecordedState = repository.countByIdAndGameState(entity.getId(), GameState.RECORDED);

			return countLastRecordedState == 1;
		}
		return false;
	}

	@Override
	public void delete(Game entity) {
		repository.delete(entity);
	}

	@Override
	public List<Game> findAll() {
		return repository.findAll();
	}

	@Override
	public Game findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public List<Game> filterBy(List<GameState> statuses) {
		Specification<Game> spec = (root, criteriaQuery, criteriaBuilder) -> {
			if (statuses.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("gameState").in(statuses);
			}
		};
		return repository.findAll(spec);
	}

	@Override
	public List<Game> filterBy(List<GameState> statuses, List<Long> locationsIds, Long playerId) {
		Specification<Game> spec = (root, criteriaQuery, criteriaBuilder) -> {
			if (statuses.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("gameState").in(statuses);
			}
		};

		Specification<Game> specEventNull = (root, criteriaQuery, criteriaBuilder) -> {
			return root.get("eventSport").isNull();
		};
		Specification<Game> locList = (root, criteriaQuery, criteriaBuilder) -> {
			if (locationsIds == null || locationsIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("location").get("id").in(locationsIds);
			}
		};

		Specification<Game> playerRule = (root, criteriaQuery, criteriaBuilder) -> {
			if (playerId == null) {
				return criteriaBuilder.conjunction();
			} else {
				return criteriaBuilder.equal(root.join("hostPlayer").get("id"), playerId);
			}
		};
		return repository.findAll(Objects.requireNonNull(spec.and(locList).and(specEventNull)).and(playerRule));
	}

	@Override
	public List<Game> filterByPlayers(List<GameState> statuses, List<Long> playersIds, List<Long> sportsIds) {

		Specification<Game> gameState = (root, criteriaQuery, criteriaBuilder) -> {
			if (statuses.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("gameState").in(statuses);
			}
		};

		Specification<Game> hostPlayerList = (root, criteriaQuery, criteriaBuilder) -> {
			if (playersIds == null || playersIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("hostPlayer").get("id").in(playersIds);
			}
		};

		Specification<Game> guestPlayerList = (root, criteriaQuery, criteriaBuilder) -> {
			if (playersIds == null || playersIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("guestPlayer").get("id").in(playersIds);
			}
		};

		Specification<Game> locNotNullRule = (root, criteriaQuery, criteriaBuilder) -> {
			return root.get("location").isNotNull();
		};

		Specification<Game> eventSportNotNullRule = (root, criteriaQuery, criteriaBuilder) -> {
			return root.get("eventSport").isNotNull();
		};

		Specification<Game> eventSportSportRule = (root, criteriaQuery, criteriaBuilder) -> {
			if (sportsIds == null || sportsIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("eventSport").join("sport").get("id").in(sportsIds);
			}
		};

		Specification<Game> locSportRule = (root, criteriaQuery, criteriaBuilder) -> {
			if (sportsIds == null || sportsIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("location").join("sport").get("id").in(sportsIds);
			}
		};

		List<Game> res4 = repository.findAll(
				gameState.and(hostPlayerList).and(locNotNullRule.and(locSportRule)));

		List<Game> res5 = repository.findAll(
				gameState.and(hostPlayerList).and(eventSportNotNullRule.and(eventSportSportRule)));

		List<Game> res6 = repository.findAll(
				gameState.and(guestPlayerList).and(locNotNullRule.and(locSportRule)));

		List<Game> res7 = repository.findAll(
				gameState.and(guestPlayerList).and(eventSportNotNullRule.and(eventSportSportRule)));

		List<Game> res100 = new ArrayList<Game>();
		res100.addAll(res4);
		res100.addAll(res5);
		res100.addAll(res6);
		res100.addAll(res7);
		return res100;
	}

	@Override
	public List<Game> filterBy(List<GameState> statuses, List<Long> eventsIds, List<Long> sportsIds) {
		if (eventsIds.size() != 1) {
			throw new IllegalArgumentException("Exactly one eventId required. Count is " + eventsIds.size());
		}
		List<EventSport> eventSports = eventSportService.filterByEventIdAndSportId(eventsIds.get(0), sportsIds);

		Specification<Game> specLocationNull = (root, criteriaQuery, criteriaBuilder) -> {
			return root.get("location").isNull();
		};

		Specification<Game> spec = (root, criteriaQuery, criteriaBuilder) -> {
			if (statuses.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.get("gameState").in(statuses);
			}
		};
		Specification<Game> eventSportsList = (root, criteriaQuery, criteriaBuilder) -> {
			if (eventsIds.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return root.join("eventSport").in(eventSports);
			}
		};

		return repository.findAll(spec.and(eventSportsList).and(specLocationNull));
	}

	@Override
	public List<Game> findAll(List<Order> orders) {
		return repository.findAll(Sort.by(orders));
	}

	@Override
	public List<GameDominationDto> findPlayerDominations(List<Long> playersIds) {

		List<GameDominationDto> sumsByPointsLocations = getLocationDominationData(playersIds);

		List<GameDominationDto> sumsByPointsEvents = getEventSportDominationData(playersIds);

		List<GameDominationDto> res = new ArrayList<>();
		res.addAll(sumsByPointsLocations);
		res.addAll(sumsByPointsEvents);

		res.sort(new GameDominationDtoPointsComparator());

		return res;
	}

	private List<GameDominationDto> getEventSportDominationData(List<Long> playersIds) {
		List<EventPoint> resEvent = eventPointService.filterByHostPlayerId(playersIds);

		List<GameDominationDto> resEventDom =
				resEvent.stream().map(e -> new GameDominationDto(e.getEventSport().getId(), false, true,
						e.getEventSport().getSport(), e.getPoints(), e.getEventSport().getEvent().getName())).collect(
						Collectors.toList());

		List<GameDominationDto> sumsByPointsEvents = sumForLocations(resEventDom);

		sumsByPointsEvents.forEach(this::addEventSportRankdata);
		return sumsByPointsEvents;
	}

	private void addLocationRankdata(GameDominationDto dto) {

		dto.setRank(locationPointService.countBetterPlayers(dto.getId(), dto.getPoints()));
	}

	private void addEventSportRankdata(GameDominationDto dto) {

		dto.setRank(eventPointService.countBetterPlayers(dto.getId(), dto.getPoints()));
	}

	private List<GameDominationDto> getLocationDominationData(List<Long> playersIds) {
		List<LocationPoint> resLoc = locationPointService.filterByHostPlayerId(playersIds);

		List<GameDominationDto> resLocDom = resLoc.stream().map(e -> new GameDominationDto(e.getLocation().getId(),
				true, false, e.getLocation().getSport(), e.getPoints(), e.getLocation().getName())).collect(
				Collectors.toList());

		List<GameDominationDto> sumsByPointsLocations = sumForLocations(resLocDom);
		sumsByPointsLocations.forEach(this::addLocationRankdata);

		return sumsByPointsLocations;
	}

	private List<GameDominationDto> sumForLocations(List<GameDominationDto> originalList) {

		Map<Long, List<GameDominationDto>> groupByLocOrEvent =
				originalList.stream().collect(Collectors.groupingBy(GameDominationDto::getId, Collectors.toList()));

		List<GameDominationDto> sumsByPoints = new ArrayList<>();

		for (Entry<Long, List<GameDominationDto>> entry : groupByLocOrEvent.entrySet()) {
			GameDominationDto value = entry.getValue().get(0);

			GameDominationDto sum = new GameDominationDto(entry.getKey(), value.isOnLocation(), value.isOnEvent(),
					value.getSport(), 0, value.getName());

			int sumPoints = entry.getValue().stream().mapToInt(GameDominationDto::getPoints).sum();

			sum.setPoints(sumPoints);
			sumsByPoints.add(sum);
		}
		return sumsByPoints;
	}

	@Override
	public boolean hasOpenGamesForGuest(Location location, long guestId) {
		Objects.requireNonNull(location);

		Specification<Game> locationSpec =
				(root, criteriaQuery, criteriaBuilder)
						-> criteriaBuilder.equal(root.join("location").get("id"), location.getId());

		Specification<Game> openLocationSpec =
				(root, criteriaQuery, criteriaBuilder)
						-> criteriaBuilder.equal(root.get("gameState"), GameState.OPEN);

		Specification<Game> guestSpec =
				(root, criteriaQuery, criteriaBuilder)
						-> criteriaBuilder.equal(root.join("guestPlayer").get("id"), guestId);
		Specification<Game> guestNullSpec =
				(root, criteriaQuery, criteriaBuilder)
						-> criteriaBuilder.isNull(root.get("guestPlayer"));

		if (guestId == 0) {
			return repository.count(locationSpec.and(openLocationSpec)) > 0L;
		}
		if (repository.count(locationSpec.and(openLocationSpec).and(guestSpec)) > 0L) return true;
		if (repository.count(locationSpec.and(openLocationSpec).and(guestNullSpec)) > 0L) return true;

		return false;
	}

	public static class GameDominationDtoPointsComparator implements Comparator<GameDominationDto> {

		@Override
		public int compare(GameDominationDto o1, GameDominationDto o2) {
			if (o1.getPoints() < o2.getPoints()) {
				return 1;
			} else if (o1.getPoints() > o2.getPoints()) {
				return -1;
			}
			return 0;
		}
	}
}

package ch.sofa.lodo.data.models;

public class EventPointInfo {

	private User player;

	private EventSport eventSport;

	private int points;

	private int lostGamesCount;

	private int winGamesCount;

	public User getPlayer() {
		return player;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public EventSport getEventSport() {
		return eventSport;
	}

	public void setEventSport(EventSport eventSport) {
		this.eventSport = eventSport;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLostGamesCount() {
		return lostGamesCount;
	}

	public void setLostGamesCount(int lostGamesCount) {
		this.lostGamesCount = lostGamesCount;
	}

	public int getWinGamesCount() {
		return winGamesCount;
	}

	public void setWinGamesCount(int winGamesCount) {
		this.winGamesCount = winGamesCount;
	}
}

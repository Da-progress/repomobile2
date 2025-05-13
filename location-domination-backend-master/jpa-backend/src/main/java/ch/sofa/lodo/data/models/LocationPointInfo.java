package ch.sofa.lodo.data.models;

/**
 * Sum points over opponents from {@link LocationPoint}
 */
public class LocationPointInfo {

	private User player;

	private Location location;

	private int points;

	private int lostGamesCount;

	private int winGamesCount;

	public User getPlayer() {
		return player;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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

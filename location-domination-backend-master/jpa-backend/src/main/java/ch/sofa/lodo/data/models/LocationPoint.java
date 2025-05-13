package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Entity
@Table(name = "location_point")
public class LocationPoint extends SuperEntity {

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "user_id")
	private User player;

	@ManyToOne(cascade = {})
	// NUll is possible for case of adding 10 points when location is created
	//@NotNull
	@JoinColumn(name = "opponent_id")
	private User opponent;

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "location_id")
	private Location location;

	@Column(name = "points")
	private int points;

	@Column(name = "lost_games_count")
	private int lostGamesCount = 0;

	@Column(name = "win_games_count")
	private int winGamesCount = 0;

	public LocationPoint() {

	}

	public LocationPoint(@NotNull User player, @NotNull User opponent, @NotNull Location location) {
		this.player = player;
		this.opponent = opponent;
		this.location = location;
	}

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

	public User getOpponent() {
		return opponent;
	}

	public void setOpponent(User opponent) {
		this.opponent = opponent;
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

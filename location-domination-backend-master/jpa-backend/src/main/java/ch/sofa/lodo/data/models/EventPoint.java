package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Entity
@Table(name = "event_point")
public class EventPoint extends SuperEntity {

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "user_id")
	private User player;

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "opponent_id")
	private User opponent;

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "event_sport_id")
	private EventSport eventSport;

	@Column(name = "points")
	private int points;

	@Column(name = "lost_games_count")
	private int lostGamesCount;

	@Column(name = "win_games_count")
	private int winGamesCount;

	public EventPoint() {

	}

	public EventPoint(@NotNull User player, @NotNull User opponent, @NotNull EventSport eventSport) {
		super();
		this.player = player;
		this.opponent = opponent;
		this.eventSport = eventSport;
	}

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

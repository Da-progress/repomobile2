package ch.sofa.lodo.data.services.dtos;

import ch.sofa.lodo.data.models.User;

public class PlayerWithPoints {

	private User player;
	private int points;

	public PlayerWithPoints(User player) {
		super();
		this.player = player;
	}

	public PlayerWithPoints(User player, int points) {
		super();
		this.player = player;
		this.points = points;
	}

	public User getPlayer() {
		return player;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}

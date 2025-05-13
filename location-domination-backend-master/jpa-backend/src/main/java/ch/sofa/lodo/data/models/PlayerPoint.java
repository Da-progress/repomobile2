package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Entity
@Table(name = "user_point")
public class PlayerPoint extends SuperEntity {

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "user_id")
	private User player;

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "sport_id")
	private Sport sport;

	@Column(name = "points")
	private int points;

	public User getPlayer() {
		return player;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}

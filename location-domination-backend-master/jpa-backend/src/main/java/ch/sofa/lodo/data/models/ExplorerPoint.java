package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Entity
@Table(name = "explorer_point")
public class ExplorerPoint extends SuperEntity {

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "user_id")
	private User player;

	@Column(name = "points")
	private int points;

	@Column(name = "different_location_count")
	private int differentLocationCount;

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

	public int getDifferentLocationCount() {
		return differentLocationCount;
	}

	public void setDifferentLocationCount(int differentLocationCount) {
		this.differentLocationCount = differentLocationCount;
	}
}

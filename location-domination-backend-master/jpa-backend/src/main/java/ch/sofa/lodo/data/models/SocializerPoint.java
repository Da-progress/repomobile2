package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Entity
@Table(name = "socializer_point")
public class SocializerPoint extends SuperEntity {

	@ManyToOne(cascade = {})
	@NotNull
	@JoinColumn(name = "user_id")
	private User player;

	@Column(name = "points")
	private int points;

	@Column(name = "different_opponent_count")
	private int differentOpponentCount;

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

	public int getDifferentOpponentCount() {
		return differentOpponentCount;
	}

	public void setDifferentOpponentCount(int differentOpponentCount) {
		this.differentOpponentCount = differentOpponentCount;
	}
}

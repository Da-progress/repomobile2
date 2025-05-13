package ch.sofa.lodo.data.services.dtos;

@SuppressWarnings("unused")
public class GlobalPoint {

	private Long userId;

	private String username;

	private int points;

	private int rank;

	public GlobalPoint(Long userId, String username, int points) {
		super();
		this.userId = userId;
		this.username = username;
		this.points = points;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}

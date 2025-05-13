package ch.sofa.lodo.data.services.dtos;

public class MyStatsDto {

	private String iconName;
	private Integer points;
	private String sportName;
	private Integer ranking;

	public MyStatsDto(String iconName, Integer points, String sportName, Integer ranking) {
		super();
		this.iconName = iconName;
		this.points = points;
		this.sportName = sportName;
		this.ranking = ranking;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
}

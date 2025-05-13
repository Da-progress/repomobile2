package ch.sofa.lodo.data.services.dtos;

import ch.sofa.lodo.data.models.Sport;

public class GameDominationDto {

	/**
	 * Location or eveSport id
	 */
	private Long id;
	private boolean onLocation;
	private boolean onEvent;
	private Sport sport;
	private int points;
	private String name;
	private int rank;

	public GameDominationDto(Long id, boolean onLocation, boolean onEvent, Sport sport, int points, String name) {
		super();
		this.id = id;
		this.onLocation = onLocation;
		this.onEvent = onEvent;
		this.sport = sport;
		this.points = points;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isOnLocation() {
		return onLocation;
	}

	public void setOnLocation(boolean onLocation) {
		this.onLocation = onLocation;
	}

	public boolean isOnEvent() {
		return onEvent;
	}

	public void setOnEvent(boolean onEvent) {
		this.onEvent = onEvent;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}

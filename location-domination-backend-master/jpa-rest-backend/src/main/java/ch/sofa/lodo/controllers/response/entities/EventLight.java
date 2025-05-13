package ch.sofa.lodo.controllers.response.entities;

import ch.sofa.lodo.data.models.Event;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class EventLight {

	private Long id;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String name;

	public EventLight(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime, String name) {
		super();
		this.id = id;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.name = name;
	}

	public EventLight(Event event) {
		super();
		this.id = event.getId();
		this.startDateTime = event.getStartDateTime();
		this.endDateTime = event.getEndDateTime();
		this.name = event.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

package ch.sofa.lodo.data.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "event_sport")
public class EventSport extends SuperEntity {

	@JsonBackReference
	@ManyToOne
	@NotNull
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "sport_id")
	private Sport sport;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}
}

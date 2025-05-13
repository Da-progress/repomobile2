package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
@Table(name = "event_user", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"}))
public class EventUser extends SuperEntity {

	@ManyToOne
	@NotNull
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "user_id")
	private User player;

	@Column(name = "registration_datetime", nullable = false)
	private LocalDateTime registrationDateTime;

	public EventUser() {
	}

	public EventUser(@NotNull Event event, @NotNull User player) {
		super();
		this.event = event;
		this.player = player;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getPlayer() {
		return player;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public LocalDateTime getRegistrationDateTime() {
		return registrationDateTime;
	}

	public void setRegistrationDateTime(LocalDateTime registrationDateTime) {
		this.registrationDateTime = registrationDateTime;
	}
}

package ch.sofa.lodo.data.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table(name = "event")
public class Event extends SuperEntity {

	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "creation_datetime")
	private LocalDateTime creationDateTime;

	@Column(name = "start_datetime")
	private LocalDateTime startDateTime;

	@Column(name = "end_datetime")
	private LocalDateTime endDateTime;

	@OneToOne(cascade = {CascadeType.ALL})
	@NotNull
	@JoinColumn(name = "address_id")
	private Address address = new Address();

	@Column(name = "longitude")
	private double longitude;

	@Column(name = "latitude")
	private double latitude;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "creator_id")
	private User creator;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "responsible_user_id")
	private User responsibleUser;

	@Column(name = "registration_code", length = 20)
	private String registrationCode;

	@Column(name = "sponsor_text", length = 250)
	private String sponsorText;

	@JsonManagedReference
	@OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
	private List<EventSport> eventSports;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(User responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public String getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}

	public String getSponsorText() {
		return sponsorText;
	}

	public void setSponsorText(String sponsorText) {
		this.sponsorText = sponsorText;
	}

	public List<EventSport> getEventSports() {
		return eventSports;
	}

	public void setEventSports(List<EventSport> eventSports) {
		this.eventSports = eventSports;
	}
}

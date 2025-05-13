package ch.sofa.lodo.data.models;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "location", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE-locName-Sport", columnNames = {"name", "sport_id"})
})
@Entity
public class Location extends SuperEntity {

	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "longitude")
	private double longitude;

	@Column(name = "latitude")
	private double latitude;

	@Column(name = "creation_datetime")
	private LocalDateTime creationDateTime;

	@Column(name = "verification_datetime")
	private LocalDateTime verificationDateTime;

	@OneToOne(cascade = {CascadeType.ALL})
	@NotNull
	@JoinColumn(name = "address_id")
	private Address address = new Address();

	@ManyToOne
	@NotNull
	@JoinColumn(name = "sport_id")
	private Sport sport;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "player_id")
	private User creator;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "location_state_id")
	private LocationState locationState;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "loc_image", nullable = true)
	private byte[] loc_image;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creation_time) {
		this.creationDateTime = creation_time;
	}

	public LocalDateTime getVerificationDateTime() {
		return verificationDateTime;
	}

	public void setVerificationDateTime(LocalDateTime verificationDateTime) {
		this.verificationDateTime = verificationDateTime;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public LocationState getLocationState() {
		return locationState;
	}

	public void setLocationState(LocationState locationState) {
		this.locationState = locationState;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public byte[] getLoc_image() {
		return loc_image;
	}

	public void setLoc_image(byte[] loc_image) {
		this.loc_image = loc_image;
	}
}

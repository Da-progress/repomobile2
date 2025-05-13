package ch.sofa.lodo.controllers.response.entities;

import ch.sofa.lodo.data.models.Address;
import ch.sofa.lodo.data.models.Sport;

@SuppressWarnings("unused")
public class LocationLight {

	private Long id;
	private String name;
	private double longitude;
	private double latitude;
	private Address address;
	private Sport sport;

	public LocationLight(Long id, String name, double longitude, double latitude, Address address, Sport sport) {
		super();
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
		this.sport = sport;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}
}

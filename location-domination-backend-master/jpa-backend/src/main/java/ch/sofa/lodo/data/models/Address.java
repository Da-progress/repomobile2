package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address extends SuperEntity {

	@Column(name = "street", length = 50)
	private String street = "";

	@Column(name = "street_number", length = 10)
	private String streetNumber = "";

	@Column(name = "postcode", length = 10)
	private String postcode = "";

	@Column(name = "city", length = 50)
	private String city = "";

	@Column(name = "country", length = 4)
	private String country = "";

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}

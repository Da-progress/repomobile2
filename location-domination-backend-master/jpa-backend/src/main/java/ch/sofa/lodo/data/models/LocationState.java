package ch.sofa.lodo.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "location_state")
public class LocationState extends SuperEntity {

	@Column(name = "name", length = 20)
	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

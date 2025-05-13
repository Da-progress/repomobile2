package ch.sofa.lodo.admin.views.locations.events;

import ch.sofa.lodo.data.models.Location;

public class LocationGridOnSelectedEvent {

	private Location location;

	public LocationGridOnSelectedEvent(Location location) {
		super();
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}
}

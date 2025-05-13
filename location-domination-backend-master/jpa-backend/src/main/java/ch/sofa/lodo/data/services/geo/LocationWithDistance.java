package ch.sofa.lodo.data.services.geo;

import ch.sofa.lodo.data.models.Location;

public class LocationWithDistance {

	private Location location;
	private double distance;

	public LocationWithDistance(Location location, double distance) {
		this.location = location;
		this.distance = distance;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}

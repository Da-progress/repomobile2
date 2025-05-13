package ch.sofa.lodo.data.services.geo;

import ch.sofa.lodo.data.models.EventSport;

public class EventSportWithDistance {

	private EventSport eventsport;
	private double distance;

	public EventSportWithDistance(EventSport eventsport, double distance) {
		this.eventsport = eventsport;
		this.distance = distance;
	}

	public EventSport getEventSport() {
		return eventsport;
	}

	public void setEventSport(EventSport event) {
		this.eventsport = event;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}

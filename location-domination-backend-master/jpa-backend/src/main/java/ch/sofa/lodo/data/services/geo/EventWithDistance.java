package ch.sofa.lodo.data.services.geo;

import ch.sofa.lodo.data.models.Event;

public class EventWithDistance {

	private Event event;
	private double distance;

	public EventWithDistance(Event event, double distance) {
		this.event = event;
		this.distance = distance;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}

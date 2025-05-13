package ch.sofa.lodo.data.services.geo;

import ch.sofa.lodo.data.models.Event;
import ch.sofa.lodo.data.models.EventSport;
import ch.sofa.lodo.data.models.Location;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistanceCalculator {

	public List<Location> filterLocationsInRadius(List<Location> locations, double refLat, double refLon,
												  double radius) {
		return calculateDistance(refLat, refLon, locations).stream().filter(lwd -> lwd.getDistance() <= radius).map(
				LocationWithDistance::getLocation).collect(Collectors.toList());
	}

	public List<Event> filterEventsInRadius(List<Event> eventSports, double refLat, double refLon, double radius) {
		return calculateEventDistance(refLat, refLon, eventSports).stream().filter(
				lwd -> lwd.getDistance() <= radius).map(EventWithDistance::getEvent).collect(Collectors.toList());
	}

	public List<EventSport> filterEventSportsInRadius(List<EventSport> events, double refLat, double refLon, double radius) {
		return calculateEventSportDistance(refLat, refLon, events).stream().filter(lwd -> lwd.getDistance() <= radius).map(
				EventSportWithDistance::getEventSport).collect(Collectors.toList());
	}

	private List<LocationWithDistance> calculateDistance(double refLat, double refLon, List<Location> locations) {
		return locations.stream().map(loc -> calculateDistance(refLat, refLon, loc)).collect(Collectors.toList());
	}

	private List<EventWithDistance> calculateEventDistance(double refLat, double refLon, List<Event> events) {
		return events.stream().map(eve -> calculateDistance(refLat, refLon, eve)).collect(Collectors.toList());
	}

	private List<EventSportWithDistance> calculateEventSportDistance(double refLat, double refLon,
																	 List<EventSport> events) {
		return events.stream().map(eve -> calculateDistance(refLat, refLon, eve)).collect(Collectors.toList());
	}

	private LocationWithDistance calculateDistance(double refLat, double refLon, Location location) {
		return new LocationWithDistance(location,
				distanceMath(refLat, refLon, location.getLatitude(), location.getLongitude()));
	}

	private EventWithDistance calculateDistance(double refLat, double refLon, Event event) {
		return new EventWithDistance(event, distanceMath(refLat, refLon, event.getLatitude(), event.getLongitude()));
	}

	private EventSportWithDistance calculateDistance(double refLat, double refLon, EventSport event) {
		return new EventSportWithDistance(event,
				distanceMath(refLat, refLon, event.getEvent().getLatitude(), event.getEvent().getLongitude()));
	}

	public double distanceMath(double lat1, double lon1, double lat2, double lon2) {
		final int earthRadius = 6371; // meters

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double sinLatDistance2 = Math.sin(latDistance / 2);
		double sinLonDistance2 = Math.sin(lonDistance / 2);
		double chordLength2 = sinLatDistance2 * sinLatDistance2
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * sinLonDistance2 * sinLonDistance2;
		double angularDistance = 2 * Math.atan2(Math.sqrt(chordLength2), Math.sqrt(1 - chordLength2)); // radians
		return Math.abs(earthRadius * angularDistance * 1000); // meters
	}
}

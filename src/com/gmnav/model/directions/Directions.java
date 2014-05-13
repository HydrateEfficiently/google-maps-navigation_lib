package com.gmnav.model.directions;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gmnav.model.util.GoogleUtil;
import com.gmnav.model.util.LatLngUtil;
import com.google.android.gms.maps.model.LatLng;

public class Directions {
	
	private ArrayList<Direction> directions;
	private ArrayList<Point> path;
	private ArrayList<LatLng> latLngPath;
	private LatLng origin;
	private LatLng destination;
	
	public Directions(LatLng origin, LatLng destination, String jsonString) throws JSONException {
		this.origin = origin;
		this.destination = destination;
		JSONObject route = new JSONObject(jsonString).getJSONArray("routes").getJSONObject(0); // Only one route supported
		JSONObject leg = route.getJSONArray("legs").getJSONObject(0); // Only one leg supported
		createDirections(leg.getJSONArray("steps"));
		createPath();
		createLatLngPath();
	}
	
	private void createDirections(JSONArray steps) throws JSONException {
		directions = new ArrayList<Direction>();
		List<LatLng> nextDirectionPath = new ArrayList<LatLng>();
		nextDirectionPath.add(origin);
		for (int i = 0; i < steps.length(); i++) {
			JSONObject googleStep = steps.getJSONObject(i);
			directions.add(new Direction(nextDirectionPath, googleStep));
			nextDirectionPath = GoogleUtil.decodePolyline(googleStep.getJSONObject("polyline").getString("points"));
		}
		nextDirectionPath.add(destination);
		directions.add(Direction.createArrivalDirection(nextDirectionPath));
	}
	
	private void createPath() {
		Direction currentDirection;
		Point currentPoint;
		Point prevPoint = createLastPoint();
		
		path = new ArrayList<Point>();
		path.add(prevPoint);
		
		for (int i = directions.size() - 1; i >= 0; i--) {
			currentDirection = directions.get(i);
			List<LatLng> currentDirectionPoints = currentDirection.getPath();
			for (int j = currentDirectionPoints.size() - 1; j >= 0; j--) {
				currentPoint = createPoint(currentDirectionPoints.get(j), prevPoint, currentDirection);
				path.add(0, currentPoint);
				prevPoint = currentPoint;
			}
		}
	}
	
	private Point createLastPoint() {
		return new Point() {{
			location = destination;
			distanceToNextPoint = 0;
			timeToNextPoint = 0;
			distanceToCurrentDirectionMeters = 0;
			timeToCurrentDirectionMinutes = 0;
			distanceToNextDirectionMeters = 0;
			timeToNextDirectionMinutes = 0;
			distanceToArrivalMeters = 0;
			timeToArrivalMinutes = 0;
			direction = directions.get(directions.size() - 1);
			nextDirection = null;
			nextPoint = null;
		}};
	}
	
	private Point createPoint(final LatLng loc, final Point next, final Direction dir) {
		final double distanceToNext = LatLngUtil.distanceInMeters(loc, next.location);
		final boolean isNewDirection = next.direction != dir;
		return new Point() {{
			location = loc;
			distanceToNextPoint = distanceToNext;
			distanceToCurrentDirectionMeters = isNewDirection ? 0 : next.distanceToCurrentDirectionMeters + distanceToNext;
			distanceToNextDirectionMeters = isNewDirection ? next.distanceToCurrentDirectionMeters + distanceToNext : next.distanceToNextDirectionMeters + distanceToNext;
			distanceToArrivalMeters = next.distanceToArrivalMeters + distanceToNext;
			direction = dir;
			nextDirection = isNewDirection ? next.direction : next.nextDirection;
			nextPoint = next;
		}};
	}
	
	private void createLatLngPath() {
		latLngPath = new ArrayList<LatLng>();
		LatLng lastLocation = path.get(0).location;
		for (int i = 1; i < path.size(); i++) {
			LatLng currentLocation = path.get(i).location;
			if (!lastLocation.equals(currentLocation)) {
				latLngPath.add(currentLocation);
			}
			lastLocation = currentLocation;
		}
	}
	
	public List<Direction> getDirectionsList() {
		return directions;
	}
	
	public List<Point> getPath() {
		return path;
	}
	
	public List<LatLng> getLatLngPath() {
		return latLngPath;
	}
}

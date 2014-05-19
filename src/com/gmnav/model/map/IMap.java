package com.gmnav.model.map;

import android.graphics.Point;

import com.gmnav.model.LatLng;
import com.gmnav.model.PointD;

public interface IMap {
	
	public interface OnTouchEventHandler {
		void invoke();
	}
	
	public interface OnUpdate {
		void invoke();
	}
	
	public void setLocation(LatLng location);
	
	public void setBearing(double bearing);
	
	public void setTilt(double tilt);
	
	public void setZoom(double zoom);
	
	public void setAnchor(PointD anchor);
	
	public void setOnTouchEventHandler(OnTouchEventHandler handler);
	
	public void setOnUpdateEventHandler(OnUpdate handler);
	
	public LatLng getLocation();
	
	public double getBearing();
	
	public double getTilt();
	
	public double getZoom();
	
	public PointD getAnchor();
	
	public Point getSize();
	
	public void invalidate();
	
	public void invalidate(int animationTime);
	
	public void addPolyline(PolylineOptions options);
	
	public void removePolyline();

}

package com.gmnav.model.vehicle;

import android.graphics.Bitmap;

import com.gmnav.Defaults;
import com.gmnav.model.util.PointD;
import com.google.android.gms.maps.model.LatLng;

public class VehicleOptions {
	private LatLng location = Defaults.LOCATION;
	private Bitmap image;
	private PointD imageAnchor = new PointD(0.5, 0.5); // TODO: Implement me.
	private PointD screenAnchor = new PointD(0.5d, 0.75d);
	
	public VehicleOptions location(LatLng location) {
		this.location = location;
		return this;
	}
	
	public LatLng location() {
		return location;
	}
	
	public VehicleOptions image(Bitmap image) {
		this.image = image;
		return this;
	}
	
	public Bitmap image() {
		return image;
	}
	
	public VehicleOptions screenAnchor(PointD screenAnchor) {
		this.screenAnchor = screenAnchor;
		return this;
	}
	
	public PointD screenAnchor() {
		return screenAnchor;
	}
}
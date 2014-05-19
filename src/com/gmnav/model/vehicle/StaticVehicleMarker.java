package com.gmnav.model.vehicle;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gmnav.NavigationFragment;
import com.gmnav.R;
import com.gmnav.model.PointD;
import com.gmnav.model.map.IMap;
import com.gmnav.model.map.IMap.OnUpdate;
import com.gmnav.model.map.NavigationMap;
import com.gmnav.model.util.LayoutUtil;

public class StaticVehicleMarker implements IVehicleMarker {
	
	private Vehicle vehicle;
	private IMap map;
	private ImageView markerImageView;
	private Bitmap image;
	private boolean isVisible;
	private double currentTilt = -1.0f;
	
	public StaticVehicleMarker(NavigationFragment navigationFragment, Vehicle vehicle, NavigationMap navigationMap) {
		this.vehicle = vehicle;
		map = navigationMap.getMap();

		ViewGroup view = (ViewGroup)navigationFragment.getView();
		LinearLayout container = (LinearLayout)LayoutUtil.getChildViewById(view, R.id.static_vehicle_marker_container);
		markerImageView = (ImageView)LayoutUtil.getChildViewById(container, R.id.static_vehicle_marker);
		image = vehicle.getImage();
		isVisible = markerImageView.getVisibility() == View.VISIBLE;
		
		map.setOnUpdateEventHandler(new OnUpdate() {
			@Override
			public void invoke() {
				setLayoutParams(map.getTilt());
			}
		});
		updateLayoutParams();
	}
	
	private void updateLayoutParams() {
		setLayoutParams(map.getTilt());
	}

	@Override
	public void show() {
		if (!isVisible) {
			isVisible = true;
			updateLayoutParams();
			markerImageView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hide() {
		if (isVisible) {
			isVisible = false;
			markerImageView.setVisibility(View.INVISIBLE);
		}
	}

	private void setLayoutParams(double tilt) {
		if (isVisible && tilt != currentTilt) {
			final int imageHeight = (int)(image.getHeight() * Math.cos(Math.toRadians(tilt)));
			final int imageWidth = image.getWidth();
			final Bitmap flattenedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
			markerImageView.setImageBitmap(flattenedImage);
			
			final Point mapSize = map.getSize();
			final PointD anchor = vehicle.getScreenAnchor();
			LayoutParams layout = new LayoutParams(imageWidth, imageHeight) {{
				leftMargin = (int)((mapSize.x * anchor.x) - width / 2);
				topMargin = (int)((mapSize.y * anchor.y) - height / 2);
			}};
			markerImageView.setLayoutParams(layout);
			currentTilt = tilt;
		}
	}
}

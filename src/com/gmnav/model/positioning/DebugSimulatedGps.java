package com.gmnav.model.positioning;

import com.gmnav.model.LatLng;

public class DebugSimulatedGps extends AbstractSimulatedGps {
	
	public DebugSimulatedGps(GpsOptions options, LatLng location) {
		super(options, location);
	}

	@Override
	public void doFollowPath() {
		currentPosition = new Position(currentPosition.location, 0, System.currentTimeMillis());
		onTickHandler.invoke(currentPosition);
		whileHasCurrentPath(new WhileHasCurrentPathAction() {
			@Override
			public void invoke() {
				advancePosition(currentPath);
				onTickHandler.invoke(currentPosition);	
			}
		});
	}
}

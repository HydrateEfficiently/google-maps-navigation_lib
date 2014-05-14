package com.gmnav;

import java.util.ArrayList;
import java.util.List;

import com.gmnav.R;
import com.gmnav.model.directions.Direction;
import com.gmnav.model.directions.DistanceFormatter;
import com.gmnav.model.directions.ImageFactory;
import com.gmnav.model.directions.Movement;
import com.gmnav.model.util.LayoutUtil;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DirectionFragment extends Fragment {
	
	private Direction direction;
	private GridLayout view;
	
	private static List<Direction> directionsById = new ArrayList<Direction>();
	
	public static final DirectionFragment newInstance(Direction direction) {
		DirectionFragment fragment = new DirectionFragment();
		int id = directionsById.size();
		directionsById.add(id, direction);
		Bundle args = new Bundle();
		args.putInt("index", id);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		int id = getArguments().getInt("index");
		direction = directionsById.get(id);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = (GridLayout)inflater.inflate(R.layout.direction_fragment, container, false);
		}
		
		setDirectionDescription(generateDirectionDescription());
		setDirectionImage();
		return view;
	}
	
	private String generateDirectionDescription() {
		String directionDescription;
		switch (direction.getMovement()) {
			case DEPARTURE:
				directionDescription = direction.getCurrent() + " toward " + direction.getTarget();
				break;
			case TURN_LEFT:
			case TURN_RIGHT:
			case CONTINUE:
			case ARRIVAL:
				directionDescription = direction.getTarget();
				break;
			default:
				directionDescription = "Unknown";
		}
		return directionDescription;
	}
	
	public void setDirectionImage() {
		if (view != null) {
			ImageView directionImage = (ImageView)LayoutUtil.getChildViewById(view, R.id.direction_image);
			directionImage.setImageResource(ImageFactory.getImageResource(directionImage.getContext(), direction, "87ceeb"));
		}
	}
	
	public void setDirectionDescription(String text) {
		if (view != null) {
			TextView directionDescription = (TextView)LayoutUtil.getChildViewById(view, R.id.direction_description_text); 
			directionDescription.setText(text);
		}
	}
	
	public void setDirectionDistance(double distanceMeters) {
		if (view != null) {
			TextView directionDistance = (TextView)LayoutUtil.getChildViewById(view, R.id.distance_to_direction);
			String distance = DistanceFormatter.formatMeters(distanceMeters);
			directionDistance.setText(distance);
		}
	}
}

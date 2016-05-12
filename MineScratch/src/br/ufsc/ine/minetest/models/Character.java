package br.ufsc.ine.minetest.models;

import java.util.List;

public class Character {
	
	private Coordinate currentCoordinate;
	

	public Character() {
		currentCoordinate = new Coordinate();
	}
	
	
	public List<Float> getPosition() {
		return currentCoordinate.getPosition();
	}
	
	public List<Float> getAngle() {
		return currentCoordinate.getAngle();
	}
	
	public List<Float> getSpeed() {
		return currentCoordinate.getSpeed();
	}


	public void setPosition(Float x, Float y, Float z) {
		currentCoordinate.setPosition(x, y, z);
	}
	
	public void setAngle(Float pitch, Float yaw) {
		currentCoordinate.setAngle(pitch, yaw);
	}
	
	public void setSpeed(Float dx, Float dy, Float dz) {
		currentCoordinate.setSpeed(dx, dy, dz);
	}
}

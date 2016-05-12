package br.ufsc.ine.minetest.models;

import java.util.List;

public class Coordinate {
	private Position position;
	private Speed speed;
	private Angle angle;
	

	public Coordinate() {
		this.position = new Position();
		this.speed = new Speed();
		this.angle = new Angle();
		
		Float zero = new Float(0);
		position.setPosition(zero, zero, zero);
		angle.setAngle(zero, zero);
		speed.setSpeed(zero, zero, zero);
	}
	
	
	public List<Float> getPosition() {
		return position.asList();
	}
	
	public List<Float> getAngle() {
		return angle.asList();
	}
	
	public List<Float> getSpeed() {
		return speed.asList();
	}


	public void setPosition(Float x, Float y, Float z) {
		position.setPosition(x, y, z);
	}
	
	public void setAngle(Float pitch, Float yaw) {
		angle.setAngle(pitch, yaw);
	}
	
	public void setSpeed(Float dx, Float dy, Float dz) {
		speed.setSpeed(dx, dy, dz);
	}
}

package br.ufsc.ine.minetest.models;

import java.util.LinkedList;
import java.util.List;

public class Coordinate {

	private List<Float> position;
	private List<Float> speed;
	private List<Float> angle;

	public Coordinate() {
		this.position = new LinkedList<Float>();
		position.add(new Float(0));
		position.add(new Float(0));
		position.add(new Float(0));

		this.speed = new LinkedList<Float>();
		speed.add(new Float(0));
		speed.add(new Float(0));
		speed.add(new Float(0));

		this.angle = new LinkedList<Float>();
		angle.add(new Float(0));
		angle.add(new Float(0));
	}

	public List<Float> getPosition() {
		return position;
	}

	public List<Float> getSpeed() {
		return speed;
	}

	public List<Float> getAngle() {
		return angle;
	}

	public void setPosition(Float x, Float y, Float z) {
		this.position.set(0, x);
		this.position.set(1, y);
		this.position.set(2, z);
	}
	
	public void setPosition(List<Float> position) {
		this.position = position;
	}

	public void setSpeed(Float dx, Float dy, Float dz) {
		this.speed.set(0, dx);
		this.speed.set(1, dy);
		this.speed.set(2, dz);
	}
	
	public void setSpeed(List<Float> speed) {
		this.speed = speed;
	}

	public void setAngle(Float pitch, Float yaw) {
		this.angle.set(0, pitch);
		this.angle.set(1, yaw);
	}

	public void setAngle(List<Float> angle) {
		this.angle = angle;
	}
}

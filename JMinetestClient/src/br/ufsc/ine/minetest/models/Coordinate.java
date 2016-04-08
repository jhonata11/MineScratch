package br.ufsc.ine.minetest.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Coordinate {

	private List<Integer> position;
	private List<Integer> speed;
	private List<Integer> angle;

	public Coordinate() {
		this.position = new LinkedList<Integer>(Collections.nCopies(3, 0));
		this.speed = new LinkedList<Integer>(Collections.nCopies(3, 0));
		this.angle = new LinkedList<Integer>(Collections.nCopies(2, 0));
	}

	public List<Integer> getPosition() {
		return position;
	}

	public List<Integer> getSpeed() {
		return speed;
	}

	public List<Integer> getAngle() {
		return angle;
	}

	public void setPosition(double x, double y, double z) {
		this.position.set(0, (int) x * 1000);
		this.position.set(1, (int) y * 1000);
		this.position.set(2, (int) z * 1000);
	}

	public void setSpeed(int dx, int dy, int dz) {
		this.speed.set(0, dx * 100);
		this.speed.set(1, dy * 100);
		this.speed.set(2, dz * 100);
	}

	public void setAngle(int pitch, int yaw) {
		this.angle.set(0, pitch * 100);
		this.speed.set(1, yaw * 100);
	}


}

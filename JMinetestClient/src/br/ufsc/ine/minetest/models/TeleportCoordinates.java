package br.ufsc.ine.minetest.models;

public class TeleportCoordinates {
	
	private int [] position;
	private int [] speed;
	private int [] angle;
	
	public TeleportCoordinates() {
		this.position = new int[3];
		this.speed = new int[3];
		this.angle = new int[2];
		this.setSpeed(0, 0, 0);
		this.setAngle(0, 0);
		this.setPosition(0, 0, 0);
	}
	
	public void setPosition(int x, int y, int z){
		this.position[0] = x * 1000;
		this.position[1] = y * 1000;
		this.position[2] = z * 1000;
	}
	
	public void setSpeed(int dx, int dy, int dz){
		this.speed[0] = dx * 100;
		this.speed[1] = dy * 100;
		this.speed[2] = dz * 100;
	}
	
	public void setAngle(int pitch, int yaw){
		this.angle[0] = pitch * 100;
		this.angle[1] = yaw * 100;
	}

	public int [] getPosition() {
		return position;
	}

	public int [] getSpeed() {
		return speed;
	}

	public int [] getAngle() {
		return angle;
	}
}

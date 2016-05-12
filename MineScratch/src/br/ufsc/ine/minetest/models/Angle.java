package br.ufsc.ine.minetest.models;

import java.util.LinkedList;
import java.util.List;

public class Angle {
	
	private Float pitch;
	private Float yaw;
	
	public void setAngle(Float pitch, Float yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public List<Float> asList(){
		LinkedList<Float> list = new LinkedList<>();
		list.add(pitch);
		list.add(yaw);
		return list;
	}
}

package br.ufsc.ine.minetest.models;

import java.util.LinkedList;
import java.util.List;

public class Speed {
	
	private Float dx;
	private Float dy;
	private Float dz;
	
	public void setSpeed(Float dx, Float dy, Float dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	
	public List<Float> asList(){
		LinkedList<Float> list = new LinkedList<>();
		list.add(dx);
		list.add(dy);
		list.add(dz);
		return list;
	}
}

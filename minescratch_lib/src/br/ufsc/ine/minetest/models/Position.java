package br.ufsc.ine.minetest.models;

import java.util.LinkedList;
import java.util.List;

public class Position {
	
	private Float x;
	private Float y;
	private Float z;
	
	public void setPosition(Float x, Float y, Float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public List<Float> asList(){
		LinkedList<Float> list = new LinkedList<>();
		list.add(x);
		list.add(y);
		list.add(z);
		return list;
	}
}

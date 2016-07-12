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
	
	@Override
	public boolean equals(Object obj) {
		Position other  = (Position) obj;
		List<Float> asList = other.asList();
		if(this.x.equals(asList.get(0)) && this.y.equals(asList.get(1)) && this.z.equals(asList.get(2))){
			return true;
		} else {
			return super.equals(obj);
		}
	}
	
	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(x);
		sb.append(y);
		sb.append(z);
		return sb.toString().hashCode();
	}
}

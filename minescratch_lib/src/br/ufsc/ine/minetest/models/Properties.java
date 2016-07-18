package br.ufsc.ine.minetest.models;

import java.util.HashMap;
import java.util.List;

public class Properties {
	private List<Position> initials;
	private List<Position> allowed;
	
	public List<Position> getInitials() {
		return initials;
	}
	public void setInitials(List<Position> initials) {
		this.initials = initials;
	}
	public List<Position> getAllowed() {
		return allowed;
	}
	public void setAllowed(List<Position> allowed) {
		this.allowed = allowed;
	}


}

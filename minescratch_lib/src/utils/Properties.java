package utils;

import java.util.HashMap;
import java.util.List;

import br.ufsc.ine.minetest.models.Position;

public class Properties {
	private List<Position> initials;
	private HashMap<Position, List<Position>> allowed;
	
	public List<Position> getInitials() {
		return initials;
	}
	public void setInitials(List<Position> initials) {
		this.initials = initials;
	}
	public HashMap<Position, List<Position>> getAllowed() {
		return allowed;
	}
	public void setAllowed(HashMap<Position, List<Position>> allowed) {
		this.allowed = allowed;
	}

}

package br.ufsc.ine.minetest.models;

public class PlayerInfo {
	
	private Coordinate coordinates;

	public PlayerInfo() {
		this.coordinates = new Coordinate();
	}
	
	
	public Coordinate getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinate coordinate) {
		this.coordinates = coordinate;
	}

}

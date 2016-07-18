package br.ufsc.ine.minetest.models;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PropertiesReader {
	protected String propsPath;
	
	public PropertiesReader(String filePath) {
		propsPath = filePath;
	}
	
	
	public Properties readProperties() throws Exception{
		JsonObject jsonObject = new JsonObject();
		JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(new FileReader(propsPath));
        jsonObject = jsonElement.getAsJsonObject();
        Properties properties = new Properties();
        setInitials(jsonObject, properties);
		setAllowed(jsonObject, properties);
        
        return properties;
	}


	private void setAllowed(JsonObject jsonObject, Properties properties) {
		JsonArray array = jsonObject.get("allowed").getAsJsonArray();
		List<Position> allowed = new ArrayList<Position>();
        for(JsonElement initial : array){
        	Position pos = positionFromElement(initial.getAsJsonArray());
        	allowed.add(pos);
        }
        properties.setAllowed(allowed);
	}


	private void setInitials(JsonObject jsonObject, Properties properties) {
		JsonArray array = jsonObject.get("initial").getAsJsonArray();
		List<Position> initials = new ArrayList<Position>();
        for(JsonElement initial : array){
        	Position pos = positionFromElement(initial.getAsJsonArray());
        	initials.add(pos);
        }
        properties.setInitials(initials);
	}


	private Position positionFromElement(JsonArray array) {
		float x = array.get(0).getAsFloat();
		float y = array.get(1).getAsFloat();
		float z = array.get(2).getAsFloat();
		Position pos = new Position();
		pos.setPosition(x, y, z);
		return pos;
	}

}

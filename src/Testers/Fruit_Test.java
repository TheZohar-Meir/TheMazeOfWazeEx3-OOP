package Testers;

import Server.Game_Server;
import Server.game_service;
import dataStructure.Fruit;
import utils.Point3D;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class Fruit_Test {
	
	private ArrayList<Fruit> FruitsList = new ArrayList<Fruit>();
	private List<String> JsonFruits = new ArrayList<String>(); 
	public boolean Rot = false;  // true if the robot is on the way
	
    @Test
    void FruitInit() throws JSONException 
    {
    	game_service game = Game_Server.getServer(15);
    	JsonFruits = game.getFruits();
		for(String s: JsonFruits){
			Fruit f = new Fruit();
			f.initFruit(s); // init Fruit from Jsom File
			FruitsList.add(f);
		}
    }
    
    
    @Test
    void getValue() throws JSONException 
    {
   	  int value;
        game_service game = Game_Server.getServer(15);
      	JsonFruits = game.getFruits();
  		for(String s: JsonFruits){
  			Fruit f = new Fruit();
  			f.initFruit(s); // init Fruit from Jsom File
  			JSONObject obj = new JSONObject(s);
			JSONObject fruit = (JSONObject) obj.get("Fruit");
			value = fruit.getInt("value");
  			assertEquals(value,f.getValue());
  		}
    }
    

    @Test
    void getType() throws JSONException {
        
    	int type;
        game_service game = Game_Server.getServer(15);
      	JsonFruits = game.getFruits();
  		for(String s: JsonFruits){
  			Fruit f = new Fruit();
  			f.initFruit(s); // init Fruit from Jsom File
  			JSONObject obj = new JSONObject(s);
			JSONObject fruit = (JSONObject) obj.get("Fruit");
			type = fruit.getInt("type");
  			assertEquals(type,f.getType());
  		}
    }

    @Test
    void getLocation() throws JSONException {
    	
    	Point3D tempPos;
        game_service game = Game_Server.getServer(15);
      	JsonFruits = game.getFruits();
  		for(String s: JsonFruits){
  			Fruit f = new Fruit();
  			f.initFruit(s); // init Fruit from Jsom File
  			JSONObject obj = new JSONObject(s);
			JSONObject fruit = (JSONObject) obj.get("Fruit");
			String pos = fruit.getString("pos");
			String[] point = pos.split(",");
			double x = Double.parseDouble(point[0]);
			double y = Double.parseDouble(point[1]);
			double z = Double.parseDouble(point[2]);
			tempPos = new Point3D(x, y, z);
			assertEquals(tempPos.x(),f.getPos().x());
		    assertEquals(tempPos.y(),f.getPos().y());
  		}
    }
    
    
}
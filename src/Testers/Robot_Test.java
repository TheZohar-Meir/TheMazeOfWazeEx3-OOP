package Testers;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import Server.Game_Server;
import Server.game_service;
import dataStructure.Robot;
import utils.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

class Robot_Test {

	private ArrayList<Robot> RobotList = new ArrayList<Robot>();
	private List<String> JsonRobots = new ArrayList<String>(); 
	
	@Test
	void InitRobots() throws JSONException{

		game_service game = Game_Server.getServer(15);
		JsonRobots = game.getRobots();
		for(String s: JsonRobots){

			JSONObject obj = new JSONObject(s);
			JSONObject JsonRobot = (JSONObject) obj.get("Robot");
			Robot r = new Robot();
			r.RobotFromJSON(JsonRobot.toString());
			RobotList.add(r);
		}
	}
	
    @Test
    void getValue() throws JSONException {
    	
    	int value;
        game_service game = Game_Server.getServer(15);
       	JsonRobots = game.getRobots();
   		for(String s: JsonRobots){
   			Robot r = new Robot();
   			r.RobotFromJSON(s); // init Fruit from Jsom File
   			JSONObject obj = new JSONObject(s);
 			JSONObject JsonRobot = (JSONObject) obj.get("Robot");
 			value = JsonRobot.getInt("value");
   			assertEquals(value,r.getValue());
   		}
    }

    @Test
    void getDest() throws JSONException {

    	int Dest;
        game_service game = Game_Server.getServer(15);
       	JsonRobots = game.getRobots();
   		for(String s: JsonRobots){
   			Robot r = new Robot();
   			r.RobotFromJSON(s); // init Fruit from Jsom File
   			JSONObject obj = new JSONObject(s);
 			JSONObject JsonRobot = (JSONObject) obj.get("Robot");
 			Dest = JsonRobot.getInt("Dest");
   			assertEquals(Dest,r.getDest());
   		}
    }

    @Test
    void getSrc() throws JSONException {

    	int Src;
        game_service game = Game_Server.getServer(15);
       	JsonRobots = game.getRobots();
   		for(String s: JsonRobots){
   			Robot r = new Robot();
   			r.RobotFromJSON(s); // init Fruit from Jsom File
   			JSONObject obj = new JSONObject(s);
 			JSONObject JsonRobot = (JSONObject) obj.get("Robot");
 			Src = JsonRobot.getInt("Dest");
   			assertEquals(Src,r.getSrc());
   		}
    }

    @Test
    void getId() throws JSONException {

    	int Id;
        game_service game = Game_Server.getServer(15);
       	JsonRobots = game.getRobots();
   		for(String s: JsonRobots){
   			Robot r = new Robot();
   			r.RobotFromJSON(s); // init Fruit from Jsom File
   			JSONObject obj = new JSONObject(s);
 			JSONObject JsonRobot = (JSONObject) obj.get("Robot");
 			Id = JsonRobot.getInt("Dest");
   			assertEquals(Id,r.getSrcId());
   		}
    }

    @Test
    void getLocation() throws JSONException {

    	Point3D tempPos;
        game_service game = Game_Server.getServer(15);
        JsonRobots = game.getRobots();
  		for(String s: JsonRobots){
  			
  			Robot r = new Robot();
   			r.RobotFromJSON(s); // init Fruit from Jsom File
   			JSONObject obj = new JSONObject(s);
 			JSONObject JsonRobot = (JSONObject) obj.get("Robot");
			String pos = JsonRobot.getString("pos");
			String[] point = pos.split(",");
			double x = Double.parseDouble(point[0]);
			double y = Double.parseDouble(point[1]);
			double z = Double.parseDouble(point[2]);
			tempPos = new Point3D(x, y, z);
			assertEquals(tempPos.x(),r.getPos().x());
		    assertEquals(tempPos.y(),r.getPos().y());
  		}
    }
}

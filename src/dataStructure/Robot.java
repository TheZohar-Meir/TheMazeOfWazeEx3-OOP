package dataStructure;

import java.util.List;

import org.json.JSONObject;
import utils.Point3D;
/**
 * ~~Robot Class~~
 ** This class represents the Robots of the game.
 *	One can control the Robots Manually and collect the fruits of the game
 *	One can also run this game Automatically and watch how the game runs the robots
 * 	and collect the fruits.
 * 
 * @author Zohar & Lidor

 * Class Fields :
 * 
 * 
 * id - an integer to represent the robot id.
 * value - each robot has a value field he gets from the server(mostlyNull)
 * pos - a 3DPoint to hold the robot location.
 * speed - holds the movement speed of the robot.
 * src -   holds the robot source node.
 * SrcId - holds the key of the source node.
 * dest -  holds the destination node of the robot.
 * edge -  holde the edge that the robot is on.
 * moving - determine of the robot is moving now or he is not.
 */
public class Robot {

	private int id;
	private int value;
	private Point3D pos;
	private double speed;
	private node_data src;
	private int SrcId;
	private node_data dest;
	private edge_data edge;
	public boolean moving;
	public Fruit F;
	public int firtTime;
	public int nextVertex;
	public List<node_data> Thrill;

	
	
	public List<node_data> getThrill() {
		return Thrill;
	}

	public void setThrill(List<node_data> thrill) {
		Thrill = thrill;
	}

	/**
	 * Default constructor, sets all values to 0 and nulls.
	 */ 
	public Robot(){
		this.id = 0;
		this.pos = null;
		this.speed = 1;
		this.edge = null;
		this.src = null;
		this.dest=null;
		this.F = null;
		this.moving = false;
		this.firtTime = 0;
		nextVertex = -1;
		
	}
	
	public Fruit getF() {
		return F;
	}
	public void setF(Fruit f) {
		F = f;
	}
	/**
	 * Specific constructor, sets all values to the input ones.
	 * @param id - an int to represent the robot id.
	 * @param pos - a 3DPoint to set the robot location.
	 * @param speed - sets the movement speed of the robot.
	 * @param node - sets the robot src node to the input.
	 */
	public Robot(int id,Point3D pos,double speed,node_data node){
		this.id = id;
		this.pos = pos;
		this.speed = speed;
		this.src = node;
	}
	/**
	 * 
	 * @return the robot id.
	 */
	public int getId(){
		return id;
	}
	/**
	 * 
	 * @param id - sets this robot id to the input integer.
	 */
	public void setId(int id){
		this.id = id;
	}
	/**
	 * 
	 * @return - returns the location of the robot as a 3D Point.
	 */
	public Point3D getPos() {
		return pos;
	}
	/**
	 * 
	 * @param pos - sets the robot location to the input 3D location.
	 */
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
	/**
	 * 
	 * @return the speed of the robot movement.
	 */
	public double getSpeed() {
		return speed;
	}
	/**
	 * 
	 * @param speed - sets the speed of the robot's movement.
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	/**
	 * 
	 * @return the source node of the robot.
	 */
	public node_data getSrc() {
		return src;
	}
	/**
	 * 
	 * @param node - sets the source node of the robot.
	 */
	public void setSrc(node_data node) {
		this.src = node;
	}

	/**
	 * 
	 * @return - returns the destination node of the robot.
	 */
	public node_data getDest() {
		return dest;
	}
	/**
	 * @param node -  sets the destination node of the robot.
	 */
	public void setDest(node_data node) {
		this.dest = node;	
	}
	/**
	 * 
	 * @return - returns the edge that the robot placed on.
	 */
	public edge_data getEdge() {
		return edge;
	}
	/**
	 * 
	 * @param edge - sets the node edge field to the input one.
	 */
	public void setEdge(edge_data edge) {
		this.edge = edge;
	}


	/**
	 * This method gets a String from a json file which will be usually
	 *  sent from the game server and builds a robot with the values it gets from the server.
	 * @param json - a string that holds the robots values. 
	 */
	public void RobotFromJSON(String json)
	{
		if(!json.isEmpty())
		{
			try
			{
				JSONObject obj = new JSONObject(json);
				JSONObject CurrBot = (JSONObject) obj.get("Robot");

				String pos = CurrBot.getString("pos");
				String[] arr = pos.split(",");
				double x = Double.parseDouble(arr[0]);
				double y = Double.parseDouble(arr[1]);
				double z = Double.parseDouble(arr[2]);
				this.pos = new Point3D(x, y, z);
				int id = CurrBot.getInt("id");
				this.id = id;
				int value = CurrBot.getInt("value");
				this.setValue(value);
				int speed = CurrBot.getInt("speed");
				this.speed = speed;
				int src = CurrBot.getInt("src");
				this.setSrcId(src);

			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @return - returns the robot source node key.
	 */
	public int getSrcId() {
		return SrcId;
	}
	/**
	 * 
	 * @param srcId - sets the robots source Id to be as inserted.
	 */
	public void setSrcId(int srcId) {
		SrcId = srcId;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

}

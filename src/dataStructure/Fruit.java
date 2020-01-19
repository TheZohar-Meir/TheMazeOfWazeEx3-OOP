package dataStructure;

import org.json.JSONObject;
import utils.Point3D;
/**
 * ~~Fruit Class~~
 ** This class represents the Fruits of the game.
 *	the idea is to collect those fruits in order to get a better score (grades).
 *	each fruit has two type of values that represents from where you can collect it.
 * 	
 * 
 * @author Zohar & Lidor

 * Class Fields :
 * 
 * 
 * value- the points you recive for collecting this fruit.
 * tpye - each fruit has two type of values(1,-1) that represents from where you can collect it..
 * pos  - a 3DPoint to hold the fruit location.
 * edge -  holde the edge that the fruit is placeed on.
 * ClosestNode - holds the the node that is closest to the fruit.
 * src - holds the fruit source node key.
 * dest -  holds the destination node's key of the fruit.
 * rot - "rot" means robot on the move - a boolean field to mark of a robot is
 * 		 already sent to this specific fruit or not.
 * dist - holds a distance as a double to calculate distance later on.
 */
public class Fruit {

	private int value;
	private int type;
	private Point3D pos;
	private edge_data edge;
	private node_data ClosestNode;
	private int src;
	private int dest;
	public boolean Rot = false;  // Robot on the way
	public double dist;

	/**
	 * Default constructor - sets all values to 0 and null , type to 1.
	 */
	public Fruit(){
		this.value = 0;
		this.type = 1;
		this.pos=null;
		this.edge=null;
	}

	/**
	 *  Specific constructor.
	 * @param val - value of the fruit
	 * @param type - type of the fruit (1 or -1 )
	 * @param pos - 3D point to hold the fruit location.
	 * @param edge - the edge that the fruit is placed on.
	 */
	public Fruit(int val,int type, Point3D pos, edge_data edge){
		this.value = val;
		this.type = type;
		this.pos = pos;
		this.edge = edge;
	}
	/**
	 * 
	 * @return the key of the source node of the fruit.
	 */
	public int getSrc() {
		return src;
	}
	/**
	 * This methods sets the source node id of the fruit.
	 * @param - an integer to represent the key of the source node of the fruit.
	 */
	public void setSrc(int src) {
		this.src = src;
	}
	
	/**
	 * 
	 * @return the key of the destination node of the fruit.
	 */
	public int getDest() {
		return dest;
	}
	
	/**
	 * This methods sets the destination node id of the fruit.
	 * @param - an integer to represent the key of the destination node of the fruit.
	 */
	public void setDest(int dest) {
		this.dest = dest;
	}
	
	/**
	 * 
	 * @return -  a double to represent distance for future calculations.
	 */
	
	public double getDist() {
		return dist;
	}
	
	/**
	 * This method idea is to hold a double that represent distance.
	 * 
	 * @param dist - a double to set the distance.
	 */
	
	public void setDist(double dist) {
		this.dist = dist;
	}
	
	/**
	 * 
	 * @return - the value of the fruit ( the points ones get from picking this fruit).
	 */
	public int getValue(){
		return value;
	}
	
	/**
	 *  Set the value of the fruit.
	 * @param value - an integer to represent the fruit value's.
	 */
	
	public void setValue(int value){
		this.value = value;
	}
	
	/**
	 *  Sets the type of the fruit, -1 of you can pick the fruit only from lower vertex
	 *  to a higher one ( from 8 to 9 ) and 1 if you can only pick the fruit from a higher
	 *  vertex fo a lower one.
	 * @param type -         1 / -1  as written  above.
	 */
	public void setType(int type){
		this.type = type;
	}
	
	/**
	 * 
	 * @return the tpye of the fruit.
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * 
	 * @return - returns a 3D point that represents the fruit location.
	 */
	public Point3D getPos(){
		return pos;
	}
	
	/**
	 * A 3D Point that represents the fruit location.
	 * @param pos -  3D Points to set the location.
	 */
	public void setPos(Point3D pos){
		this.pos = pos;
	}
	
	/**
	 * 
	 * @return - returns the edge that the fruit is placed on.
	 */
	public edge_data getEdge(){
		return edge;
	}
	
	/**
	 *  Sets an edge_data edge that the fruit is placed on her.
	 * @param edge -  the edge that wanted to be set.
	 */
	public void setEdge(edge_data edge) {
		this.edge = edge;
	}

	/**
	 * 
	 * @return - returns the closest node to the fruit.
	 */
	public node_data getClosestNode() {
		return ClosestNode;
	}
	
	/**
	 * This methods sets the closest node to the fruit.
	 * @param closestNode - a node_data that will be set as the closest node to the fruit.
	 */
	public void setClosestNode(node_data closestNode) {
		ClosestNode = closestNode;
	}

	/**
	 * This method gets a string as input and create a new fruit from the values inside the string
	 * using a JSON.
	 * @param g - a string that contains the values of the fruit.
	 */
	public void initFruit(String g){

		if(!g.isEmpty()){
			try{
				JSONObject obj = new JSONObject(g);
				JSONObject fruit = (JSONObject) obj.get("Fruit");
				int value = fruit.getInt("value");
				this.value = value;
				int type = fruit.getInt("type");
				this.type = type;
				String pos = fruit.getString("pos");
				String[] point = pos.split(",");
				double x = Double.parseDouble(point[0]);
				double y = Double.parseDouble(point[1]);
				double z = Double.parseDouble(point[2]);
				this.pos = new Point3D(x, y, z);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}

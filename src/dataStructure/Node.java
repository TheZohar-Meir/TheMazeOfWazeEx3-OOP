package dataStructure;

import java.io.Serializable;
import utils.Point3D;

/**
 * This class goal is to represent a Vertex on the graph.
 * Point3D location - represents the location of the Node (Point3D holds x,y,z values).
 * int key- represents the key of the Node (will be used on the HashMap).
 * int tag - simply int that works the same as boolean but has 3 options (0-White,1-Grey,2-Black) it will be used on the algorithms class.
 * double weight - represents the "time" it takes to get to this Node withing a specific thrill (will be explained on Graph_Algo)
 * String info - simply a string to hold data, also used on algorithm class.
 * @author Zohar & Lidor.
 */
public class Node implements node_data,Serializable{

	private static final long serialVersionUID = 1L;
	private int key;
	private Point3D location;
	private double weight;
	private String info;
	private int tag=0;
	
	/**
	 * Default constructor - sets all values to zeros and null.
	 */
	public Node(){
		
		this.key=0;
		this.location= new Point3D(0,0,0);
		this.weight=0;
		this.info=null;
		this.tag=0;
	}
	
	
	/**
	 * Specific one - sets the input values as the Node values.
	 * @param key - the key of the Node (in order to hold on a HashMap).
	 * @param weight - the weight represents to time to get into this Node on the graph (mainly used on graph algorithm class).
	 * @param point - a 3D Points represent the Node location on R(3) dimension.
	 */
	public Node (int key,double weight, Point3D point){
		
		this.key=key;
		this.weight=weight;
		this.location =new Point3D (point);
	}
	
	
	/**
	 *  return an integer that represents this Node key
	 */
	public int getKey() {
		return this.key;
	}

	
	/**
	 *  return a Point3D that represents this Node location on R(3) dimension. 
	 */
	public Point3D getLocation() {
		return this.location;
	}

	
	/**
	 * sets the given point as this Node location
	 * @param p - 3D Point.
	 */
	public void setLocation(Point3D p) {
		this.location = new Point3D(p);
	}

	
	/**
	 * return a double that represents this Node weight(time to travel).
	 */
	public double getWeight() {
		return this.weight;
	}

	
	/**
	 * sets the weight of a Node.
	 * @param w - the weight to be set as this Node weight.
	 */
	public void setWeight(double w) {
		this.weight=w;
	}

	
	/**
	 * return a string that represents this Node info (will be used at algorithm class).
	 */
	public String getInfo() {
		return this.info;
	}

	
	/**
	 * sets the weight of a Node.
	 * @param w - the weight to be set as this Node weight.
	 */
	public void setInfo(String s) {
		this.info=s;
	}

	
	/**
	 * return an integer that represents this edge Tag.
	 */
	public int getTag() {
		return this.tag;
	}

	
	/**
	 * sets the tag of a Node.
	 * @param t - the tag to be set as this Node weight.
	 */
	public void setTag(int t) {
		this.tag = t;
	}
	
}
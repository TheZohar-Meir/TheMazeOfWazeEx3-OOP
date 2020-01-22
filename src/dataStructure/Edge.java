package dataStructure;

import java.io.Serializable;

/**
 * This class goal is to represent an edge on the graph.
 * each edge has some private fields:
 * 1. int src - represents the source Node(Vertex) of an edge.
 * 2. int dest - represents the destination Node of an edge.
 * 3. int tag - represents a boolean parameter which will be used later on graph_algorithm class.
 * 4. double weight - weight represents the "time" it takes to move on this edge.
 * 5. String info - a string field which will be used later on graph_algorithm class.
 * @author Zohar & Lidor.
 *
 */
public class Edge implements edge_data,Serializable{
	
	private static final long serialVersionUID = 1L;
	private int src;
	private int dest;
	private int tag;
	private double weight;
	private String info;

	
	/**
	 * Default constructor 
	 */
	public Edge(){
		
		this.src=0;
		this.dest=0;
		this.tag=0;
		this.weight=0.0;
		this.info = null;
	}

	
	/**
	 * an edge constructor with specific inputs.
	 * @param src - the source Node (vertex).
	 * @param dest - the destination Node (vertex).
	 * @param weight - the weight of the edge which represents the "time to travel on this edge".
	 */
	public Edge(int src, int dest, double weight){
		
		this.src=src;
		this.dest=dest;
		String str = Double.toString(weight);
		str=String.format("%.5g%n",weight);
		weight= Double.parseDouble(str);
		this.weight=weight;
	}

	
	/**
	 * Getter to an edge source vertex.
	 */
	public int getSrc() {
		return this.src;
	}

	
	/**
	 * Getter to an edge destination vertex.
	 */
	public int getDest() {
		return this.dest;
	}

	
	/**
	 * Getter to an edge's Weight.
	 */
	public double getWeight() {
		return this.weight;
	}


	/**
	 * Getter to an edge's info --> usually null may be changed at graph algo functions.
	 */
	public String getInfo() {
		return this.info;
	}


	/**
	 * Setter to an edge's info witch will be used later on with the graph algorithm.
	 */
	public void setInfo(String s) {
		this.info=s;
	}


	/**
	 * Getter to an edge's tag.
	 */
	public int getTag() {
		return this.tag;
	}

	/**
	 * set the tag of an edge.
	 * @param t - the tag value.
	 */
	public void setTag(int t) {
		this.tag=t;
	}

}
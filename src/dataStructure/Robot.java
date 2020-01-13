package dataStructure;

import java.awt.image.BufferedImage;

import utils.Point3D;

public class Robot {
	private int id;
	private Point3D pos;
	private double speed;
	private node_data node;
	private edge_data edge;
	private BufferedImage image;

	public Robot(){
		this.id = 0;
		this.pos = null;
		this.speed = 1;
		this.edge = null;
		this.node = null;
	}
	
	public Robot(int id,Point3D pos,double speed,node_data node){
		this.id = id;
		this.pos = pos;
		this.speed = speed;
		this.node = node;
	}

	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public Point3D getPos() {
		return pos;
	}
	
	public void setPos(Point3D pos) {
		this.pos = pos;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public node_data getNode() {
		return node;
	}
	
	public void setNode(node_data node) {
		this.node = node;
	}
	
	public edge_data getEdge() {
		return edge;
	}
	
	public void setEdge(edge_data edge) {
		this.edge = edge;
	}
	
	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
//init (?)
}

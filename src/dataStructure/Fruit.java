package dataStructure;


import java.awt.image.BufferedImage;

import org.json.JSONObject;
import utils.Point3D;
public class Fruit {

	private int value;
	private int type;
	private Point3D pos;
	private edge_data edge;
	BufferedImage myImage;

	public Fruit(){
		this.value = 0;
		this.type = 1;
		this.pos=null;
		this.edge=null;
	}
	
	public Fruit(int val,int type, Point3D pos, edge_data edge){
		this.value = val;
		this.type = type;
		this.pos = pos;
		this.edge = edge;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}

	public Point3D getPos(){
		return pos;
	}
	
	public void setPos(Point3D pos){
		this.pos = pos;
	}
	
	public edge_data getEdge(){
		return edge;
	}
	
	public void setEdge(edge_data edge) {
		this.edge = edge;
	}

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


	/**
	 * get the image of the object
	 * @return the myImage
	 */
	public BufferedImage getMyImage() {
		return myImage;
	}

	/**
	 * @param myImage - to set the image of the object
	 */
	public void setMyImage(BufferedImage myImage) {
		this.myImage = myImage;
	}


	
}

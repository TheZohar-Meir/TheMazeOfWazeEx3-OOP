package dataStructure;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Point3D;

public class Robot {
	
	private int id;
	private int value;
	private Point3D pos;
	private double speed;
	private node_data src;
	private int SrcId;
 	private node_data dest;
	private edge_data edge;
	private BufferedImage image;
	public boolean moving = false;
	public List<node_data> moveList = new ArrayList<node_data>();

	public Robot(){
		this.id = 0;
		this.pos = null;
		this.speed = 1;
		this.edge = null;
		this.src = null;
		this.dest=null;
	}
	
	public Robot(int id,Point3D pos,double speed,node_data node){
		this.id = id;
		this.pos = pos;
		this.speed = speed;
		this.src = node;
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
	
	public node_data getSrc() {
		return src;
	}
	
	public void setSrc(node_data node) {
		this.src = node;
	}
	
	
	public node_data getDest() {
		return dest;
	}
	
	public void setDest(node_data node) {
		this.dest = node;
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
				this.value = value;
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

	public int getSrcId() {
		return SrcId;
	}

	public void setSrcId(int srcId) {
		SrcId = srcId;
	}
	
}

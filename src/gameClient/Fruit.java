package gameClient;

import java.awt.image.BufferedImage;

import org.json.JSONObject;

import utils.Point3D;

/**
 * This class represents a Fruit in the game.
 */
public class Fruit {
	
	private boolean life = true;
	private Point3D fruit_point;
	private String type;
	private int idfruit;
	private double speed;
	private double NatoTime;
	private String timeStamp;
	private BufferedImage myImage;

	/**
	 * a constructor for the class and make a full copy of fruit
	 */
	public Fruit() {
		
	}
	
	
	/**
	 * a constructor for the class and make a full copy of fruit
	 * @param f - a diffrent fruit 
	 */
	public Fruit(Fruit f) {
		this.type = f.getType();
		this.idfruit = f.getIdfruit();
		this.fruit_point = new Point3D(f.getPointer_fruit());
		this.speed = f.getSpeed();
		this.timeStamp = f.getTimeStamp();
	}
	

	/**
	 * the method check if the fruit is alive
	 * @return - alive or died
	 */
	public boolean isLife() {
		return life;
	}
	
	
//	public void initFruit(String g){
//		if(!g.isEmpty()){
//			try{
//				JSONObject obj = new JSONObject(g);
//				JSONObject fruit = (JSONObject) obj.get("Fruit");
//				int value = fruit.getInt("value");
//				this.value = value;
//				int type = fruit.getInt("type");
//				this.type = type;
//				String pos = fruit.getString("pos");
//				String[] point = pos.split(",");
//				double x = Double.parseDouble(point[0]);
//				double y = Double.parseDouble(point[1]);
//				double z = Double.parseDouble(point[2]);
//				this.fruit_point = new Point3D(x, y, z);
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	
	/**
	 * the method change alive fruit to dead
	 * @param life = boolean to dicide if alive or not
	 */
	public void setLife(boolean life) {
		this.life = life;
	}
	
	
	/**
	 * the method get the time the fruit was eated
	 * @return timestamp - the time
	 */
	public String getTimeStamp() {
		return timeStamp;
	}
	
	
	/**
	 * the method set the time fruit was eated
	 * @param strDate - the time
	 */
	public void setTimeStamp(String strDate) {
		this.timeStamp = strDate;
	}
	
	
	/**
	 * the method get the coordinats of the fruit
	 * @return pointer_fruit - the coordinates
	 */
	public Point3D getPointer_fruit() {
		return fruit_point;
	}
	
	
	/**
	 * the method set the coordinats of the fruit
	 */
	public void setPointer_fruit(Point3D point) {
		this.fruit_point = point;
	}
	
	
	/**
	 * the method get the the type
	 * @return type - fruit
	 */
	public String getType() {
		return type;
	}
	
	
	/**
	 * the method set the the type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	/**
	 * the method get the the id  of the fruit
	 * @return idfruit - fruit
	 */
	public int getIdfruit() {
		return idfruit;
	}
	
	
	/**
	 * the method set the the id  of the fruit
	 */
	public void setIdfruit(int idfruit) {
		this.idfruit = idfruit;
	}
	
	
	/**
	 * the method get the the speed of the fruit
	 * @return idfruit - fruit
	 */
	public double getSpeed() {
		// TODO Auto-generated method stub
		return speed;
	}
	
	
	/**
	 * the method set the the speed of the fruit
	 */
	public void setSpeed(double d) {
		this.speed = d;
	}
	
	
	/**
	 * the method get the natoTime of fruit
	 * @return NatoTime - the time
	 */
	public double getNatoTime() {
		return NatoTime;
	}
	
	
	/**
	 * the method set the natoTime of fruit
	 * @param natoTime - the time
	 */
	public void setNatoTime(double natoTime) {
		NatoTime = natoTime;
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
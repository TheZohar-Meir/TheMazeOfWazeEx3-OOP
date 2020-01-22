package gameClient;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;






public class AutoGame {

	private static final long serialVersionUID = 1L;
	private final double EPSILON = 0.00000001;
	private ArrayList<Fruit> FruitsList = new ArrayList<Fruit>(); // Always changing 
	private ArrayList<Robot> RobotsList = new ArrayList<Robot>(); // creating once. 
	private DGraph DG = new DGraph();
	private Graph_Algo GA;
	private graph GG;
	private static final double min_x = 35.18725458757062;
	private static final double max_x = 35.21315127845036;
	private static final double min_y = 32.09920263529412;
	private static final double max_y = 32.10943409579832;
	private final int fram = 700;
	public game_service game = null;
	private boolean Auto = false;
	private boolean PaintRobots = false;
	private boolean PaintFruits = false;
	BufferedImage myImage;
	private static boolean IsKMLWorking = false;
	private Thread ourT;
	static int numOfScenerio = 0;
	private static String file_name_KML;
	private static int firtTime = 0;
	KML k;
	
	public MyGameGUI GUI;
	
	
	/**
	 * This method Called when you prees the CreateAuto on the GUi's menu bar.
	 * When it called she asks for a scenario number , sends the input to the Server and gets the values of the graph
	 * robots and fruits.
	 * After setting this data we call to three other methods to initialize the GUI robots and fruits.
	 * 
	 */
	private void CreateAuto()  {

		numOfScenerio = 0;
		clear();
		this.Auto=true;
		String user_input = JOptionPane.showInputDialog(null, "Please enter your scenario number ([0,23]) ");
		int scenario_num = Integer.parseInt(user_input) ;

		while (scenario_num<0||scenario_num>23){
			user_input = JOptionPane.showInputDialog(null,"Please Enter a value between 0-23 ");
			scenario_num = Integer.parseInt(user_input) ;
		}
		numOfScenerio = scenario_num;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		this.game=game;
		System.out.println(this.game);
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		this.DG=gg;
		this.GG = DG;
		this.GA = new Graph_Algo(GG);

		FruitInit(this.game);
		RobotInitAuto(this.game);
		GUI.initGUI(this.DG);
		ourT.start();
	}

	

	/**
	 * This method gets the current running game and simply checks the fruits 
	 * locations and paint them on the GUI while adding them to our fruits list.
	 * 
	 * @param game
	 */
	private void FruitInit(game_service game) {

		if(!FruitsList.isEmpty()){
			FruitsList.clear();
		}
		try {
			List<String> tempFruits = new ArrayList<String>(); 
			tempFruits = game.getFruits();
			for(String s: tempFruits){
				Fruit f = new Fruit();
				f.initFruit(s);
				edge_data ed = findFruitEdge(f);
				f.setEdge(ed);
				f.setSrc(ed.getSrc());
				f.setDest(ed.getDest());
				FruitsList.add(f);
				if(IsKMLWorking)
				{
					String objType = f.getType() == 1 ? "Fruit1" : "Fruit2";
					try 
					{
						KML.Write_Data(file_name_KML, f.getPos().x(), f.getPos().y(), objType, game.timeToEnd());
					} 
					catch (FileNotFoundException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	/**
	 *  This method gets 2 points and returns the distance between of them.
	 * @param p - 3DPoint A
	 * @param p2 - 3DPoint B
	 * @return - the distance between A and B.
	 */
	private double dist(Point3D p, Point3D p2) {
		double ans = Math.sqrt(Math.pow((p.x()-p2.x()),2)+(Math.pow((p.y()-p2.y()),2)));
		return Math.abs(ans);
	}
	
	/**
	 *  This method created in order to find the edge that the fruit is placed on.
	 *  this method based on distance:
	 *  First we calculate the distance between a node to the fruit.
	 *  Then we calculate the distance between this node's dest to the fruit.
	 *  We summary the distances and if both of them togheter equal to the distance 
	 *  between the node to his dest then we know that the fruit is on this specific edge.
	 * @param f - Fruit to check what edge he stands on
	 * @return - the edge that the fruit is on.
	 */
	private edge_data findFruitEdge(Fruit f) {

		edge_data temp = new Edge();

		if(this.DG != null) {
			Collection<node_data> Nodes = DG.getV();

			for(node_data myNode : Nodes) {

				Collection<edge_data> Edeges = DG.getE(myNode.getKey());

				for(edge_data myEdge : Edeges) {

					Point3D srcP = myNode.getLocation();
					Point3D destP = DG.getNode(myEdge.getDest()).getLocation();
					Point3D fruitPos = f.getPos();

					double totald = dist(srcP, destP);
					double f_s = dist(fruitPos,srcP);
					double f_d = dist(fruitPos,destP);
					double dis = f_s+f_d;
					double anss = Math.abs(totald-(dis));
					if( anss <= EPSILON){

						int low = myNode.getKey();

						int high = myEdge.getDest();

						if(myNode.getKey()>myEdge.getDest()) {
							low = myEdge.getDest();
							high = myNode.getKey();
						}
						if(f.getType() == 1) {
							edge_data tempEdge = DG.getEdge(low, high);
							temp = tempEdge;
							if(tempEdge != null) {
								return tempEdge;
							}
						}
						if(f.getType() == -1) {
							edge_data tempEdge = DG.getEdge(high,low);
							temp = tempEdge;
							if(tempEdge!= null) {
								return tempEdge;
							}
						}
					}
				}
			}
		}
		return temp;
	}
	
	private void clear() {

		if (DG!=null) {
			Collection<node_data> v = DG.getV();
			for(node_data n : v) {
				Collection<edge_data> e = DG.getE(n.getKey());
				for(edge_data ed: e) {
					DG.getEdge(ed.getSrc(), ed.getDest()).setTag(0);
				}
			}
			FruitsList.clear();
			RobotsList.clear();
			firtTime = 0;
		}
	}


	/**
	 * This method is responsible to move the Robots automatically on the game.
	 * Here we have to transform our DGraph to an Algo_Graph in order to
	 * use Graph_Algo shortestPath algorithm we simply check for each robot which
	 * fruit is the closest to him and sent him to that fruit location with 
	 * the game.move and chooseNextEdge commands of the server.
	 * @param game - the running game.
	 */
	public void MoveAutoGame (game_service game) {

		Graph_Algo a = new Graph_Algo();
		a.init(DG);
		for (Robot tempRob : RobotsList) {

			if(!tempRob.moving){
				//System.out.println("not moving !!!!");
				Fruit ClosestFruit = FindClosestFruit(tempRob);
				if(tempRob.getF() != null && ClosestFruit != tempRob.getF()) {
					tempRob.getF().setR(null);
					tempRob.setF(ClosestFruit);
					tempRob.getF().setR(tempRob);
				}
				if(tempRob.getF() == null) {
					tempRob.setF(ClosestFruit);
					tempRob.getF().setR(tempRob);
				}
				/////calculate path && move 
				if(ClosestFruit.R == tempRob){
					int arg1 = tempRob.getSrcId();
					int arg2 = ClosestFruit.getDest();
					if(arg1 == arg2) arg2 = ClosestFruit.getSrc();
					System.out.println("arg1    : "+arg1);
					System.out.println("arg2    : "+arg2);

					List<node_data> lis = a.shortestPath(arg1,arg2);
					if(lis.size()>1)tempRob.setDest(lis.get(1));
					else {
						tempRob.setDest(lis.get(0));
					}
					System.out.println("Lis[0]  : "+lis.get(0).getKey());
					System.out.println("Lis[1] location  : "+lis.get(1).getLocation());
					
					
					System.out.println("DEST Private functionnnnnn "+ tempRob.getDest().getKey());
					game.chooseNextEdge(tempRob.getId(), tempRob.getDest().getKey());
					//System.out.println("R_Src : "+tempRob.getSrcId());
					//System.out.println("Next Des "+tempRob.getDest().getKey());
					game.move();
					tempRob.moving = true;
					tempRob.setSrcId(-1);
				}
			}
		}
	}


	/**
	 * This method gets a robot and calculate the distance between that robot to each fruit
	 * after calculating it we simply return the fruit with the shortest distance
	 * in order to send a robot to him and get more points while we play Automatically.
	 * @param TempRobot - a robot we want to check the closest fruit to him.
	 * @return - the closest fruit to that robot.
	 */
	private Fruit FindClosestFruit(Robot TempRobot) {

		Fruit ans = new Fruit();
		int i = 0;
		double shortestDis=0;

		for (Fruit tempFruit : FruitsList ) {

			if(tempFruit.getR() == null) {
				double x = tempFruit.getPos().x();
				double y = tempFruit.getPos().y();
				Point3D FruitPos = new Point3D(scale(x, min_x, max_x , 50 , GUI.getWidth()-50),scale(y,max_y, min_y , 70 , GUI.getHeight()-70));

				if (i==0) {
					shortestDis=dist(FruitPos, TempRobot.getPos());
					i++;
				}
				if (dist(FruitPos, TempRobot.getPos())<=shortestDis) {
					shortestDis=dist(FruitPos,TempRobot.getPos());
					ans=tempFruit;
				}
			}
		}
		return ans;
	}

	/**
	 * 
	 * @param data denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */
	private double scale(double data, double r_min, double r_max,double t_min, double t_max)
	{
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	/**
	 * This method gets an integer represents the source node ID and a point 3d 
	 * This method idea is to return the closest fruit's dest node to the given src
	 * after that we can send a robot to that location in order to get as many points as possible.
	 * @param src - get a robot Source node
	 * @param p - represents the fruit position.
	 * @return - a key that represnets the closest fruit dest node to the src we entered.
	 */
	public int AutoFindClosestDest(int src , Point3D p) {

		int des = FindClosestSrcToFruit(p);
		if(des == src) {
			for(Fruit lol: FruitsList) {
				if(lol.getPos() == p) {
					des = lol.getSrc();
				}
			}	
		}
		int ans = -1 ;
		List<node_data> list = GA.shortestPath(src,des );
		if( list.size()>1) ans = list.get(1).getKey();
		else {
			ans = list.get(0).getKey();
		}
		return ans;
	}



	/**
	 * This Method called if you choose Create Auto game on the menu bar.
	 * The idea behind this method is to initialize the robots at the positions that
	 * we get from the server.
	 * @param game - The current game that is running.
	 */
	private void RobotInitAuto (game_service game) {

		if(!RobotsList.isEmpty())RobotsList.clear();
		String gameInfo = game.toString();
		try {
			JSONObject line = new JSONObject(gameInfo);
			JSONObject mygame = line.getJSONObject("GameServer");
			int R_amount = mygame.getInt("robots");
			int j=0;
			ArrayList<Double> FruitsDist = new ArrayList<Double>(); 

			for(int a = 0;a<R_amount;a++) { //loop to add robots to the server
				NodeCloseToFruit(FruitsList.get(j));
				FruitsDist.add(FruitsList.get(j).getDist());
				j++;
			}

			FruitsDist.sort(null);
			for (double tempD : FruitsDist) {
				int i=0;
				while (FruitsList.get(i).getDist() != tempD) {
					i++;
				}
				game.addRobot(FruitsList.get(i).getClosestNode().getKey());
			}
			List<String> tempRobots = new ArrayList<String>(); 
			tempRobots = game.getRobots();
			for(String s: tempRobots){
				Robot r = new Robot();
				r.RobotFromJSON(s);
				RobotsList.add(r);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}

	
	/**
	 * This method gets a fruit locations and return an integer represents
	 * the closest source node's  key to this fruit based on distance.
	 * @param p - a 3D point
	 * @return and integer represents the closest node's key to the point.
	 */
	public int FindClosestSrcToFruit (Point3D p) {

		int ans=0;
		int i=0;
		double shortestDis=0;
		Point3D Fpoint = new Point3D(scale(p.x(), min_x, max_x , 50 , GUI.getWidth()-50),scale(p.y(),max_y, min_y , 70 , GUI.getHeight()-70));
		Collection<node_data> Nodes = DG.getV();

		for (node_data tempNode : Nodes ) {

			double x = tempNode.getLocation().x();
			double y = tempNode.getLocation().y();
			Point3D Npoint = new Point3D(scale(x, min_x, max_x , 50 , GUI.getWidth()-50),scale(y,max_y, min_y , 70 , GUI.getHeight()-70));
			// Fpoint is the converting of x and y from point to frames.

			if (i==0) {
				shortestDis=dist(Npoint, Fpoint);
				i++;
			}
			if (dist(Npoint, Fpoint)<=shortestDis) {
				shortestDis=dist(Npoint, Fpoint);
				ans = tempNode.getKey();
			}
		}
		return ans;
	}
	

	/**
	 * This method gets a fruit and return an integer represents the closest Node key 
	 * to the fruit that was given.
	 * @param f - The fruit we want to find the closest node to him
	 * @return - an integer represents the closest Node key to the fruit that was given. 
	 */
	public int NodeCloseToFruit (Fruit f) {

		int ans=0;
		int i=0;
		double shortestDis=0;
		Point3D Fpoint = f.getPos();
		Point3D TeMpoint = new Point3D(scale(Fpoint.x(), min_x, max_x , 50 , GUI.getWidth()-50),scale(Fpoint.y(),max_y, min_y , 70 , GUI.getHeight()-70));
		Collection<node_data> Nodes = DG.getV();

		for (node_data tempNode : Nodes ) {

			double x = tempNode.getLocation().x();
			double y = tempNode.getLocation().y();
			Point3D Npoint = new Point3D(scale(x, min_x, max_x , 50 , GUI.getWidth()-50),scale(y,max_y, min_y , 70 , GUI.getHeight()-70));
			// Fpoint is the converting of x and y from point to frames.
			if (i==0) {
				shortestDis = dist(Npoint, TeMpoint);
				i++;
			}	
			if (dist(Npoint, TeMpoint)<=shortestDis) {
				shortestDis=dist(Npoint, TeMpoint);
				ans = tempNode.getKey();
				f.setDist(shortestDis);
				f.setClosestNode(tempNode);
			}
		}
		return ans;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

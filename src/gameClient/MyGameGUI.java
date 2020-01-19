package gameClient;

import utils.Point3D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;
import java.awt.Toolkit;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.Fruit;
import dataStructure.Node;
import dataStructure.Robot;
import gui.GraphRefresher;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo; 
/**
 * # ~~MyGameGUI~~
#### * This class represents the game itself and all the Graphics around.
#### * The class extends JFrame and implements ActionListener , Serializable ,GraphRefresher and MouseListener. 
#### * At this class you have to choose if you want to play the game manually or automatically
#### * The game purpose is to pick up as many fruits as you can.
#### * At a manual game mode you prees once to select a robot and twice in order to send him to a new vertex.
#### * @authors: Zohar and Lidor.




 * ### Class fields : ###
 * 
 * Double EPSILON - simply a tiny number to calculate distances.
 *  ArrayList<Fruit> FruitsList - an array list of Fruits to hold the fruits in the game.
 *  ArrayList<Robot> RobotsList - an array list of Robots to hold the robots in the game.
 * 	DG - a graph of type DGraph to hold and present data.
 *  GA - a graph of type Graph_Algo to calculate algorithms.
 *  GG - a general graph of type graph.
 *  Final doubles : min_x,min_y,max_x,max_y - to hold the sizes of the window as coordinates and use them with scale function.
 *	Final int fram - a final int that represents the window size.
 *  game_services game - the current running game.
 *	boolean Auto - true if the game is auto false if the game is manual.
 *  boolean PaintRobots - true if the robots in a paint process false if not.
 *  boolean PaintFruits - true if the robots in a paint process false if not.
 *  boolean @@@@IsKMLWorking@@@@@ - true if we are exporting the KML atm false if not.
 *  Thread ourT - a thread to run and refresh the GUI all the time.
 *  Static int numOfSenerio - an int to hold the current game scenario.
 *  static String @@@@@@file_name_KML@@@@@ -  the KML saved file name.
 *  KML k - an object of type KML * See class for more info. 
 */
public class MyGameGUI extends JFrame implements ActionListener , Serializable,  GraphRefresher, MouseListener, Runnable{

	private static final long serialVersionUID = 1L;
	private final double EPSILON = 0.00000001;
	private ArrayList<Fruit> FruitsList = new ArrayList<Fruit>();
	private ArrayList<Robot> RobotsList = new ArrayList<Robot>();
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
	static int numOfSenerio = 0;
	private static String file_name_KML;
	KML k;
	
	/**
	 * Default  constructor.
	 */
	public MyGameGUI() {
		DG=null;
		GG = null;
		initGUI(DG);
		this.addMouseListener(this);
	}
	/**
	 * A constructor that builds the GUI with a given graph as input
	 * @param Dgraph - the given graph as input to be displayed as the GUI.
	 */
	public MyGameGUI(DGraph Dgraph) {
		this.DG = Dgraph;
		GG = Dgraph;
		initGUI(DG);
		this.addMouseListener(this);
	}


	/**
	 *  This method is the first method we run in order to set the data we need in order to display the GUI
	 *  We also set the size of the window and creates the menu bar on this function.
	 *  We create here a Thread in order to refresh the display of the GUI as long as the game runs.
	 * @param DGraph -  a DGraph represent the scenario of the game.
	 */
	private void initGUI(DGraph DGraph){

		this.setSize(fram, fram);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);

		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		menuBar.add(menu);
		this.setMenuBar(menuBar);
		MenuItem item1 = new MenuItem("New Custom Game");
		item1.addActionListener(this);
		menu.add(item1);
		MenuItem item2 = new MenuItem("New Auto Game");
		item2.addActionListener(this);
		menu.add(item2);	

		ourT=new Thread(this);
	}

/**
 *  The paint method of the GUI
 *  this method sets the graphics of the GUI.
 *  After that we call to three other functions to paint the objects aswell.
 *  on this method we also set the background of the GUI.
 *  @param g - this GUI graphics **SOULD NOT BE CHANGED**
 *  
 */
	public void paint(Graphics g) {
		super.paint(g);	
		Image img = Toolkit.getDefaultToolkit().getImage(MyGameGUI.class.getResource("/gameClient/BackGround.jpg"));  
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
		paintGraph(g);
		paintRobot(g);
		paintFruit(g);
	}

	/**
	 * This method gets as input this GUI Graphics , After that we check if our list of robots is empty
	 * if the list is not empty we run on the list to gets the positions of the robots.
	 * We scale the location of the robots since we get a locations as coordinates and our GUI is JFrame based
	 * which is working with pixels (frames).
	 * After that we simply attach the image to each robot and paint him at the right position.
	 * @param g - this GUI graphics -- do not change.
	 * 
	 * @return the new graphics that contains the robots.
	 */
	public Graphics paintRobot(Graphics g) {

		if(RobotsList!= null){
			PaintRobots = true;
			for(Robot r : RobotsList) {
				Point3D p = r.getPos();
				try 
				{ 
				BufferedImage Robot_image = ImageIO.read(new File("data/RobotSmall.png"));
				int x = (int) scale(p.x(), min_x, max_x , 50 , this.getWidth()-50);
				int y = (int) scale(p.y(), max_y, min_y , 70 , this.getHeight()-70);
				//	g.drawImage(RobotIMG.getImage(), x, y, null);	
				g.drawImage(Robot_image, x-12, y-8, null);
				
				} catch (IOException e) {}
			}
		}
		PaintRobots = false;
		return g;
	}

	
/**
 *  Very smiler to the paint robots function, the different is that here we paint the Fruits.
 *  We simply run on the fruitsList and paint each fruit on its right position and adding an image according
 *  to the fruit type.
 * @param g - this GUI graphics.
 * @return - the graphics after painting the fruits.
 */
	public Graphics paintFruit(Graphics g) {

		if(!FruitsList.isEmpty()){
			PaintFruits = true;
			for(Fruit f : FruitsList) {

				findFruitEdge(f);
				Point3D p = f.getPos();
				if(f.getType() == 1) g.setColor(Color.magenta);
				else g.setColor(Color.YELLOW);
				int x = (int) scale(p.x(), min_x, max_x , 50, this.getWidth()-50);
				int y = (int) scale(p.y(), max_y, min_y ,70 , this.getHeight()-70);
				try 
				{ 
				if(f.getType() == 1) {
					BufferedImage Robot_image = ImageIO.read(new File("data/Fruit1Small.png"));
					g.drawImage(Robot_image, x-12, y-8, null);
				}
				else {
					BufferedImage Robot_image = ImageIO.read(new File("data/Fruit2Small.png"));
					g.drawImage(Robot_image, x-12, y-8, null);
					}
				
				} catch (IOException e) {}
				
			}
		}
		PaintFruits = false;
		return g;
	}

	
/**
 * This method initialize the GUI's graphics to display the graph given from the server.
 * We run on the vertexes and edges and simply draw them with filloval.
 * @param g - this GUI graphics.
 */
	public void paintGraph(Graphics g) {

		if(this.DG != null) {
			Collection<node_data> Vertexes = DG.getV();
			for(node_data node_data: Vertexes) {

				Point3D TempPoint = node_data.getLocation();
				g.setColor(Color.RED); 
				int x0 = (int) scale(TempPoint.x(), min_x, max_x , 50 , this.getHeight()-50);
				int y0 = (int) scale(TempPoint.y() ,max_y, min_y, 70 , this.getWidth()-70);	

				g.fillOval(x0-6, y0-4, 10, 10);	
				g.drawString(Integer.toString(node_data.getKey()), x0-6, y0+20);
				Collection<edge_data> Edge = DG.getE(node_data.getKey());

				for(edge_data edge_data: Edge) {	
					g.setColor(Color.DARK_GRAY);

					node_data dest = DG.getNode(edge_data.getDest());
					Point3D TempPoint2 = dest.getLocation();
					int x1 = (int) scale(TempPoint2.x(), min_x, max_x , 50 , this.getWidth()-50);
					int y1 = (int) scale(TempPoint2.y(),max_y, min_y , 70 , this.getHeight()-70);

					if (TempPoint2 != null) {
						g.drawLine(x0, y0, x1, y1);
						g.drawString(Double.toString(edge_data.getWeight()),((((x0+x1)/2)+x1)/2) , ((((y0+y1)/2)+y1)/2));
						g.setColor(Color.GREEN);
						int XFrame =((((((x0+x1)/2)+x1)/2)+x1)/2);
						int YFrame = ((((((y0+y1)/2)+y1)/2)+y1)/2);
						g.fillOval(XFrame, YFrame, 7, 7);	
					}
				}
			}
		}
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


	/**
	 *  This method created in order to clean all of the data that was inserted before you start a new game.
	 *  
	 */
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
		}
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
	 *  Our class must inherited functions because we implements other classes.
	 *  this method simply update the graph by sending it to refresh.
	 */
	public void graphUpdate() {
		repaint();
	}


	/**
	 * This method Called when you prees the CreateAuto on the GUi's menu bar.
	 * When it called she asks for a scenario number , sends the input to the Server and gets the values of the graph
	 * robots and fruits.
	 * After setting this data we call to three other methods to initialize the GUI robots and fruits.
	 * 
	 */
	private void CreateAuto()  {
		
		numOfSenerio = 0;
		clear();
		this.Auto=true;
		String user_input = JOptionPane.showInputDialog(null, "Please enter your scenario number ([0,23]) ");
		int scenario_num = Integer.parseInt(user_input) ;

		while (scenario_num<0||scenario_num>23){
			user_input = JOptionPane.showInputDialog(null,"Please Enter a value between 0-23 ");
			scenario_num = Integer.parseInt(user_input) ;
		}
		numOfSenerio = scenario_num;
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
		initGUI(this.DG);
		ourT.start();
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

			Fruit tempf = FindClosestFruit(tempRob);
			if(tempf.Rot == false){

				int arg1 = tempRob.getSrcId();
				int arg2 = tempf.getDest();
				if(arg1 == arg2) {
					arg2 = tempf.getSrc();
				}	
				List<node_data> lis = a.shortestPath(arg1,arg2);
				if(lis.size()>1)tempRob.setDest(lis.get(1));
				else {
					tempRob.setDest(lis.get(0));
				}
				tempf.Rot = true;
				game.chooseNextEdge(tempRob.getId(), tempRob.getDest().getKey());
				game.move();
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

		Fruit ans =new Fruit();
		int i = 0;
		double shortestDis=0;

		for (Fruit tempFruit : FruitsList ) {

			if(!tempFruit.Rot) {
				double x = tempFruit.getPos().x();
				double y = tempFruit.getPos().y();
				Point3D FruitPos = new Point3D(scale(x, min_x, max_x , 50 , this.getWidth()-50),scale(y,max_y, min_y , 70 , this.getHeight()-70));

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
		Point3D TeMpoint = new Point3D(scale(Fpoint.x(), min_x, max_x , 50 , this.getWidth()-50),scale(Fpoint.y(),max_y, min_y , 70 , this.getHeight()-70));
		Collection<node_data> Nodes = DG.getV();

		for (node_data tempNode : Nodes ) {

			double x = tempNode.getLocation().x();
			double y = tempNode.getLocation().y();
			Point3D Npoint = new Point3D(scale(x, min_x, max_x , 50 , this.getWidth()-50),scale(y,max_y, min_y , 70 , this.getHeight()-70));
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



	/**
	 * This function called upon clicking CreateCustom on the menu bar
	 * First we open a dialog box to ask the scenario number ,
	 * We send the number to the server and initialize the graph with the server given values
	 * The difference between Create Auto and Custom is mostly that here we ask the player
	 * Where he wants to place the robots and place them as he desire.
	 */
	private void CreateCustom() {

		numOfSenerio = 0;
		clear();
		this.Auto=false;
		String user_input = JOptionPane.showInputDialog(null, "Please enter your scenario number ([0,23]) ");
		int scenario_num = Integer.parseInt(user_input) ;

		while (scenario_num<0||scenario_num>23){
			user_input = JOptionPane.showInputDialog(null,"Please Enter a value between 0-23 ");
			scenario_num = Integer.parseInt(user_input) ;
		}
		numOfSenerio = scenario_num;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		this.game=game;
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		this.DG=gg;
		this.GG = DG;
		this.GA = new Graph_Algo(GG);;

		initGUI(this.DG);
		FruitInit(game);
		RobotInit(game);
		ourT.start();
	}


	/*
	 * This method called when a player create a custom game,
	 * we simply gets the robots values from the server
	 * and then we ask the player where he wants to insert the robots.
	 *@param - the current running game.
	 */
	private void RobotInit(game_service game) {

		if(!RobotsList.isEmpty())RobotsList.clear();
		String gameInfo = game.toString();
		try 
		{
			JSONObject line = new JSONObject(gameInfo);
			JSONObject mygame = line.getJSONObject("GameServer");
			int R_amount = mygame.getInt("robots");
			int RobotLocation=0;
			
			for(int a = 0;a<R_amount;a++) { //loop to add robots to the server
				String RobotInput = JOptionPane.showInputDialog(null, "Please enter the Node number to initilize robot's location ");
				RobotLocation = Integer.parseInt(RobotInput);
				game.addRobot(RobotLocation);
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
	 * This method check if we are already writing to a KML file.
	 * @return true - already writing
	 * @return false - not writing yet.
	 * */
	public static boolean KMLexporting()
	{
		return IsKMLWorking;
	}

	
	/**
	 * Receives from the current running thread the order to create a new KML file.
	 * @param file_name  - the name u want to call your file.
	 * */
	public static void startKML(String file_name) 
	{
		if(!file_name.endsWith(".kml") && !file_name.endsWith(".KML"))
			file_name += ".kml";
		file_name_KML = KML.CreatNewKMLFile(file_name, numOfSenerio);
		IsKMLWorking = true;
	}

	

	public BufferedImage getMyImage() {
		return myImage;
	}

	public void setMyImage(BufferedImage myImage) {
		this.myImage = myImage;
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
	 * This method finds the closest dest node to a given source node and a point.
	 * @param src - an integer represnets a source Node.
	 * @param p - a points locations.
	 * @return - and integer represents the closest dest of the source node to the point.
	 */
	public int FindClosestDest (int src , Point3D p) {

		int ans=0;
		int i=0;
		double shortestDis=0;
		Collection<edge_data> srcEdges = DG.getE(src);

		if(srcEdges != null) {
			for (edge_data tempEdge : srcEdges ) {

				Point3D PPP = DG.NodeMap.get(tempEdge.getDest()).getLocation();
				double x = PPP.x();
				double y = PPP.y();
				Point3D Fpoint = new Point3D(scale(x, min_x, max_x , 50 , this.getWidth()-50),scale(y,max_y, min_y , 70 , this.getHeight()-70));
				//Fpoint is the converting of x and y from point to frames.

				if (i==0) {
					shortestDis=dist(Fpoint, p);
					i++;
				}
				if (dist(Fpoint, p)<=shortestDis) {

					shortestDis=dist(Fpoint, p);
					ans=tempEdge.getDest();
				}
			}
			return ans;
		}
		return -1;
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
		Point3D Fpoint = new Point3D(scale(p.x(), min_x, max_x , 50 , this.getWidth()-50),scale(p.y(),max_y, min_y , 70 , this.getHeight()-70));
		Collection<node_data> Nodes = DG.getV();

		for (node_data tempNode : Nodes ) {

			double x = tempNode.getLocation().x();
			double y = tempNode.getLocation().y();
			Point3D Npoint = new Point3D(scale(x, min_x, max_x , 50 , this.getWidth()-50),scale(y,max_y, min_y , 70 , this.getHeight()-70));
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
	 * This method gets a locations and return an integer represents
	 * the closest source node's  key to this fruit based on distance.
	 * This method smiler to the above one but used for manual game and the point 
	 * here is where the player prest with the mouse.
	 * @param p - a 3D point where the player prest with the mouse.
	 * @return an integer represents the closest node's key to the point.
	 */
	public int FindClosestSrc (Point3D p) {

		int ans=0;
		int i=0;
		double shortestDis=0;
		Collection<node_data> Nodes = DG.getV();

		for (node_data tempNode : Nodes ) {

			double x = tempNode.getLocation().x();
			double y = tempNode.getLocation().y();
			Point3D Fpoint = new Point3D(scale(x, min_x, max_x , 50 , this.getWidth()-50),scale(y,max_y, min_y , 70 , this.getHeight()-70));
			// Fpoint is the converting of x and y from point to frames.
			if (i==0) {
				shortestDis=dist(Fpoint, p);
				i++;
			}
			if (dist(Fpoint, p)<=shortestDis) {
				shortestDis=dist(Fpoint, p);
				ans = tempNode.getKey();
			}
		}
		return ans;
	}


	/**
	 * This method gets a point and returns the closest robot to that point.
	 * It created to get the Robot when you prees on him with the mouse to use him.
	 * @param p - the point the play clicked with the mouse.
	 * @return - The closest robot to the prest point.
	 */
	public Robot FindClosestRobot (Point3D p) {

		Robot ans = new Robot();
		int i=0;
		double shortestDis=0;

		for (Robot tempRobot : RobotsList ) {

			double x = tempRobot.getPos().x();
			double y = tempRobot.getPos().y();
			Point3D click = new Point3D(scale(x, min_x, max_x , 50 , this.getWidth()-50),scale(y,max_y, min_y , 70 , this.getHeight()-70));

			if (i==0) {
				shortestDis=dist(click, p);
				i++;
			}
			if (dist(click, p)<=shortestDis) {
				shortestDis=dist(click, p);
				ans=tempRobot;
			}
		}
		return ans;
	}


	/**
	 *  The following methods created in order to pass the conflict of
	 *  double clicking and one clicked with the mouse.
	 *  we use a timer to determine if your double clicked was ment to be a double
	 *  and to avoide getting a single clicked aswell when you double click.
	 *  Please KISS.
	 */
	private int src = -1;
	private boolean waiting ;
	private boolean click2 ;
	private boolean isAlreadyOneClick;
	private Robot currentRobot = new Robot();
	public void mouseClicked(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();
		Point3D p = new Point3D(x,y); 

		if(isAlreadyOneClick) 
		{
			click2 = true;
			isAlreadyOneClick = false;
			clickTwice(currentRobot, p ,currentRobot.getSrc().getKey());
		} 
		else
		{
			isAlreadyOneClick = true;
			waiting = true;
			Timer t = new Timer("doubleclickTimer", false);

			t.schedule(new TimerTask() {

				@Override
				public void run() {
					isAlreadyOneClick = false;
					if(!click2)waiting = false;
					click2 = false;
					if(!waiting) currentRobot = clickOnce(currentRobot, p);
				}
			}, 400);
		}
	}


	/**
	 *  This method called upon one clicked, her job is to return the robot you clicked on 
	 *  while you play the game manually.
	 * @param currentRobot - an outside param in order to set a robot each time -> do not change.
	 * @param p - the location of the mouse clicked.
	 * @return -  The robot that the player clicked on.
	 */
	public Robot clickOnce(Robot currentRobot,Point3D p){
		if (!Auto && this.game.isRunning()) {
			currentRobot = FindClosestRobot (p);
			int tempsrc = FindClosestSrc(p);
			node_data temp = new Node();
			temp = DG.NodeMap.get(tempsrc);
			currentRobot.setSrc(temp);
		}
		return currentRobot;
	}


	/**
	 * This method sets the robot you already picked his new destination.
	 * @param currentRobot - the robot that marked when you clicked once.
	 * @param p - the location of the double clicked point.
	 * @param src - the current robot src node key.
	 */
	public void clickTwice(Robot currentRobot,Point3D p,int src){
		if (!Auto && this.game.isRunning()) {
			if(currentRobot != null) {
				int dest = FindClosestDest(src,p);
				currentRobot.setDest(DG.NodeMap.get(dest));
				if(!currentRobot.moving) {
				this.game.chooseNextEdge(currentRobot.getId(),dest);
				this.game.move();
				currentRobot.moving = true;
				}
				if(currentRobot.getPos()==currentRobot.getDest().getLocation())currentRobot.moving = false;
			}
		}
	}
	
	
	
	/**
	 * Auto Generated methods from implementing MouseListener.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}


	/**
	 *  This method determine what you clicked on the menu bar and send you to
	 *  the correct method after clicked.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		String input = e.getActionCommand();
		switch(input) {
		case "New Custom Game":CreateCustom();
		break;
		case "New Auto Game": CreateAuto();
		break;
		default:
			break;
		}
	}


	/**
	 * This function purpose is to update the robots locations and paint all the time.
	 * It also saves them positions in order to export that later to the KML.
	 * @param game  - the current running game.
	 */
	public void UpdateRobots(game_service game) {

		if(!RobotsList.isEmpty()){
			RobotsList.clear();
		}
		try {
			List<String> tempRobots = new ArrayList<String>(); 
			tempRobots = game.getRobots();
			for(String s: tempRobots){
				Robot r = new Robot();
				r.RobotFromJSON(s);
				RobotsList.add(r);
				
				if(IsKMLWorking)
				{
					try 
					{
						KML.Write_Data(file_name_KML, r.getPos().x(), r.getPos().y(), "Robot", game.timeToEnd());
					} 
					catch (FileNotFoundException e) {e.printStackTrace();}
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}


	
	/**
	 * The Thread running functions,
	 * responsible to decide if the game is auto or manual
	 * and keep refreshing the GUI.
	 */
	@Override
	public void run() {
		
		int Manualdt = 70;
		int Autodt = 60;

		if(this.game!=null) {
			
			int answer = JOptionPane.showConfirmDialog(null, "Do you want to creat a KML file??");
			if(answer == 0){
				String input = JOptionPane.showInputDialog("please enter file name");
				if(input != null && input != "")
					startKML(input);
			}
			else {
				
			}
			this.game.startGame();
			while(game.isRunning()) {
				if (this.Auto) {
					try 
					{
					if(!PaintFruits)FruitInit(this.game);
					if(!PaintRobots)UpdateRobots(this.game);
					MoveAutoGame(this.game);
					//game.move();
					repaint();
					Thread.sleep(Autodt);
					}
					catch (InterruptedException e) {e.printStackTrace();}
				}
				else {
					try {
						game.move();
						if(!PaintFruits)FruitInit(this.game);
						if(!PaintRobots)UpdateRobots(this.game);
						repaint();
						Thread.sleep(Manualdt);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(IsKMLWorking)
			{	
				try{KML.close_KML(file_name_KML);}
				catch (IOException e) {e.printStackTrace();}
			}
			System.out.println(game.toString());
		}
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	
	/**
	 * Main.
	 * @param args
	 */
	public static void main(String[] args) {
		MyGameGUI Test =  new MyGameGUI();
		Test.setVisible(true);
	}

	
	
}
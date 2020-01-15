package gameClient;
import java.awt.Toolkit; 
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONException;
import org.json.JSONObject;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.Fruit;
import dataStructure.Node;
import dataStructure.Robot;
import gui.GraphRefresher;
import Server.Game_Server;
import Server.game_service;
import Threads.RunGameT; 

public class MyGameGUI extends JFrame implements ActionListener , Serializable,  GraphRefresher, MouseListener, Runnable{

	private static final long serialVersionUID = 1L;
	private final double EPSILON = 0.00001;
	private ArrayList<Fruit> FruitsList = new ArrayList<Fruit>();
	private ArrayList<Robot> RobotsList = new ArrayList<Robot>();
	ImageIcon RobotIMG = new ImageIcon("/gameClient/RobotImage.jpg");
	ImageIcon FruitIMG = new ImageIcon();
	private DGraph DG = new DGraph();
	private graph GG;
	private static double min_x = 35.18725458757062;
	private static double max_x = 35.21315127845036;
	private static double min_y = 32.09920263529412;
	private static double max_y = 32.10943409579832;
	private final int fram = 700;
	public static MouseEvent e;
	public game_service game=null;
	BufferedImage myImage;


	public MyGameGUI() {
		int scenario_num = 0;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String g = game.getGraph();
		DG.init(g);
		initGUI(DG);
		//MouseLis OurMouse = new MouseLis ();
		this.addMouseListener(this);

	}

	public MyGameGUI(DGraph Dgraph) {
		this.DG = Dgraph;
		initGUI(DG);
		this.addMouseListener(this);
	}


	/**
	 * 
	 * @param DGraph
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
		MenuItem item1 = new MenuItem("Save Graph");
		item1.addActionListener(this);
		menu.add(item1);
		MenuItem item2 = new MenuItem("Load Graph");
		item2.addActionListener(this);
		menu.add(item2);
		MenuItem item3 = new MenuItem("New Custom Game");
		item3.addActionListener(this);
		menu.add(item3);
		MenuItem item4 = new MenuItem("New Auto Game");
		item4.addActionListener(this);
		menu.add(item4);	
	}


	/**
	 * 
	 */
	public void paint(Graphics g) {

		super.paint(g);
		Image img = Toolkit.getDefaultToolkit().getImage(MyGameGUI.class.getResource("/gameClient/game.png"));  
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  

		paintGraph(g);
		g=paintRobot(g);
		g=paintFruit(g);
	}

	public Graphics paintRobot(Graphics g) {

		if(RobotsList!= null){
			for(Robot r : RobotsList) {

				Point3D p = r.getPos();
				g.setColor(Color.BLACK);
				int x = (int) scale(p.x(), min_x, max_x , 50 , this.getWidth()-50);
				int y = (int) scale(p.y(), max_y, min_y , 70 , this.getHeight()-70);
				//	g.drawImage(RobotIMG.getImage(), x, y, null);	
				g.drawRoundRect(x-12, y-8, 20, 20, 150, 150);
			}
		}
		return g;
		//repaint();
	}


	public Graphics paintFruit(Graphics g) {

		if(!FruitsList.isEmpty()){

			for(Fruit f : FruitsList) {

				findFruitEdge(f);
				Point3D p = f.getPos();
				if(f.getType() == 1) g.setColor(Color.magenta);
				else g.setColor(Color.YELLOW);
				int x = (int) scale(p.x(), min_x, max_x , 50, this.getWidth()-50);
				int y = (int) scale(p.y(), max_y, min_y ,70 , this.getHeight()-70);
				g.fillOval(x-8, y-8, 15	, 15);
			}
		}
		return g;
		//repaint();
	}


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
	 * 
	 * @param f
	 */
	private void findFruitEdge(Fruit f) {

		if(this.DG != null) {
			Collection<node_data> Nodes = DG.getV();

			for(node_data myNode : Nodes) {

				Collection<edge_data> Edeges = DG.getE(myNode.getKey());

				for(edge_data myEdge : Edeges) {

					Point3D srcP = myNode.getLocation();
					Point3D destP = DG.getNode(myEdge.getDest()).getLocation();
					Point3D fruitPos = f.getPos();

					if( (dist(srcP, destP)-((dist(fruitPos,srcP) + dist(fruitPos,destP)))) <= EPSILON){

						int low = myNode.getKey();
						int high = myEdge.getDest();
						if(myNode.getKey()>myEdge.getDest()) {
							low = myEdge.getDest();
							high = myNode.getKey();
						}
						if(f.getType() == 1) {
							edge_data tempEdge = DG.getEdge(low, high);
							if(tempEdge != null) f.setEdge(tempEdge);
						}
						if(f.getType() == -1) {
							edge_data tempEdge = DG.getEdge(high,low);
							if(tempEdge!= null)f.setEdge(tempEdge);
						}
					}
				}
			}
		}
	}


	/**
	 * 
	 */
	private void clear() {

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


	/**
	 * 
	 * @param p
	 * @param p2
	 * @return
	 */
	private double dist(Point3D p, Point3D p2) {
		double ans = Math.sqrt(Math.pow((p.x()-p2.x()),2)+(Math.pow((p.y()-p2.y()),2)));
		return ans;
	}


	/**
	 * 
	 */
	public void graphUpdate() {
		repaint();
	}


	/**
	 * 
	 */
	private void CreateAuto() {

		String user_input = JOptionPane.showInputDialog(null, "Please enter your scenario number ([0,23]) ");
		int scenario_num = Integer.parseInt(user_input) ;

		while (scenario_num<0||scenario_num>23){
			user_input = JOptionPane.showInputDialog(null,"Please Enter a value between 0-23 ");
			scenario_num = Integer.parseInt(user_input) ;
		}
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		System.out.println(game);
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		this.DG=gg;
		FruitInit(game);
		RobotInitAuto(game);
		initGUI(this.DG);
	}

	private void RobotInitAuto (game_service game) {
		
		
		if(!RobotsList.isEmpty())RobotsList.clear();
		String gameInfo = game.toString();
		try {

			JSONObject line = new JSONObject(gameInfo);
			JSONObject mygame = line.getJSONObject("GameServer");
			int R_amount = mygame.getInt("robots");

			int j=0;
			int RobotLocation=0;
			for(int a = 0;a<R_amount;a++) { //loop to add robots to the server

			
				RobotLocation = NodeCloseToFruit(FruitsList.get(j));
				System.out.println("X Position : "+FruitsList.get(j).getPos().x());
				System.out.println("Y Position : "+FruitsList.get(j).getPos().y());
				System.out.println(RobotLocation);
				game.addRobot(RobotLocation);
				j++;
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

		repaint();
		
		
		
		
	}
	
	
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
				shortestDis=dist(Npoint, TeMpoint);
				i++;
			}

			if (dist(Npoint, TeMpoint)<=shortestDis) {

				shortestDis=dist(Npoint, TeMpoint);
				ans = tempNode.getKey();
			}
		}
		System.out.println(ans);
		return ans;
	}
	
	

	/**
	 * 
	 */
	private void CreateCustom() {

		this.clear();

		String user_input = JOptionPane.showInputDialog(null, "Please enter your scenario number ([0,23]) ");
		int scenario_num = Integer.parseInt(user_input) ;

		while (scenario_num<0||scenario_num>23){
			user_input = JOptionPane.showInputDialog(null,"Please Enter a value between 0-23 ");
			scenario_num = Integer.parseInt(user_input) ;
		}
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		//String gameInfo = game.toString();
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		this.DG = gg;
		initGUI(this.DG);
		
		FruitInit(game);
		RobotInit(game); // read from JSONfile, build robots and add them to RobotsList. 
		RunGameT PlaySolo = new RunGameT();
		PlaySolo.SetGame(game);
		Thread t = new Thread(PlaySolo);
		t.run();
		
		//RunGameT.StartCustom(game);
		//game.startGame();
		//t.run();
		//t.start();
//		try {
//			Thread.sleep(1000);
//			System.out.println("Im sleeping");
//		} catch (InterruptedException e) {
//		
//			e.printStackTrace();
//		}
		//repaint();
		//Thread  RunGame = new Thread() ;
		//game.startGame();
					//should be a Thread!!!
//		while(game.isRunning()) {
//			ManualPlay(game);
//		}
		String results = game.toString();
		
		this.game=game;
		System.out.println("Game Over: "+results);
		repaint();
	}



	private void RobotInit(game_service game) {

		if(!RobotsList.isEmpty())RobotsList.clear();
		String gameInfo = game.toString();
		try {

			JSONObject line = new JSONObject(gameInfo);
			JSONObject mygame = line.getJSONObject("GameServer");
			int R_amount = mygame.getInt("robots");

			//Iterator<String> f_iter = game.getFruits().iterator();
			//while(f_iter.hasNext()) {System.out.println(f_iter.next());}	
			//this.getContentPane().setLayout(new FlowLayout());
			//ImageIcon RobotIMG = createImageIcon("/gameClient/RobotImage.jpg","This Image represents the robot");
			//Image RobotIMG = Toolkit.getDefaultToolkit().getImage(MyGameGUI.class.getResource("/gameClient/RobotImage.jpg"));
			//JPanel t = new JPanel();

			int RobotLocation=0;
			for(int a = 0;a<R_amount;a++) { //loop to add robots to the server

				String RobotInput = JOptionPane.showInputDialog(null, "Please enter the Node number to initilize robot's location ");
				RobotLocation = Integer.parseInt(RobotInput);
				//				JLabel RobotLable = new JLabel(RobotIMG );
				//				int RobotX = (int)scale(this.DG.NodeMap.get(RobotLocation).getLocation().ix(),min_x, max_x , 50 , this.getHeight()-50);
				//				int RobotY = (int)scale(this.DG.NodeMap.get(RobotLocation).getLocation().iy(),max_y, min_y , 70 , this.getHeight()-70);
				//				RobotLable.setBounds(RobotX,RobotY, 50, 50);
				//				this.getContentPane().add(new JLabel(RobotIMG));
				//				t.add(RobotLable);
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

		repaint();
	}


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
				FruitsList.add(f);
				//System.out.println("F X VALUE IS:" +f.getPos().x());
			}
		}
		catch (Exception e) {e.printStackTrace();}
		repaint();
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
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param log
	 */
	private static void AutoMoveRobots(game_service game, graph gg) {

		List<String> log = game.move();
		if(log!=null) {
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest==-1) {	
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
						System.out.println(ttt);
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}




	/**
	 * 
	 * @param graph
	 */
	private void FindMinMax (graph graph) {

		Collection<node_data> GraphNodes = graph.getV();
		for (node_data MyNode : GraphNodes) {
			Point3D p = MyNode.getLocation();
			if(p.x() < min_x) min_x = p.x();
			if(p.x() > max_x)max_x = p.x();
			if(p.y() > max_y)max_y = p.y();
			if(p.y() < min_y)min_y = p.y();
		}

		//		System.out.println(min_x);
		//		System.out.println(max_x);
		//		System.out.println(max_y);
		//		System.out.println(min_y);
	}


	private void StartCustom(game_service game)
	{
//		game.startGame();
//		//ThreadPaint(game);
//		//ThreadMouse(game);
//		while(game.isRunning()) {
//			//initGUI();
//			ManualPlay(game);
//		}
		String results = game.toString();
		System.out.println("Game Over: "+results);


	}


	public void ManualPlay(game_service game){

		List<String> log = game.move();
		int destMove=-1;
		if(log != null)
		{
			
		
			if(currentRobot.getDest() != null) {
				 destMove = currentRobot.getDest().getKey();
			}
			if(destMove!= -1) {

				if(currentRobot!= null) {
					game.chooseNextEdge(currentRobot.getSrc().getKey(), destMove);
					game.move();
					repaint();
				}
			}
		}
	}



	private int ManuelNext(game_service game){

		int dest=0;
		return dest;
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
	
	
	private int src = -1;
	private int dest = -1;
	private boolean waiting ;
	private boolean click2 ;
	private boolean isAlreadyOneClick;
	private Robot currentRobot = new Robot();

	public void mouseClicked(MouseEvent e) 
	{
		this.e=e;
		
		int x = e.getX();
		int y = e.getY();
		Point3D p = new Point3D(x,y); 


		if(isAlreadyOneClick) 
		{
			click2 = true;
			isAlreadyOneClick = false;
			dest = clickTwice(currentRobot, p,currentRobot.getSrc().getKey());
			//System.out.println("stammm 222222222");
		} 
		else
		{
			isAlreadyOneClick = true;
			//System.out.println("waitingggg   ");
			waiting = true;
			Timer t = new Timer("doubleclickTimer", false);
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					isAlreadyOneClick = false;
					//System.out.println("stopppppp");

					if(!click2)waiting = false;
					click2 = false;
					if(!waiting) currentRobot = clickOnce(currentRobot, p);

				}
			}, 400);
		}
		
		
		//e.notifyAll();
	}


	public Robot clickOnce(Robot currentRobot,Point3D p){

		currentRobot = FindClosestRobot (p);
		int tempsrc = FindClosestSrc(p);
		node_data temp = new Node();
		temp = DG.NodeMap.get(tempsrc);
		currentRobot.setSrc(temp);
		System.out.println();
		System.out.println(" 111111 Current Robot close to Node "+tempsrc);
		System.out.println();
		return currentRobot;
	}


	public int clickTwice(Robot currentRobot,Point3D p,int src){

		int dest = FindClosestDest (src,p);
		currentRobot.setDest(DG.getNode(dest));
		System.out.println(" 222222 Current Robot dest is Node "+dest);
		System.out.println();
		return dest;
	}

	
	
//	public static void StartCustom(game_service game)
//	{
//		game.startGame();
//		//ThreadPaint(game);
//		//ThreadMouse(game);
//		while(game.isRunning()) {
//			//initGUI();
//			System.out.println("Game Is Running");
//			ManualPlay(game);
//		}
//		String results = game.toString();
//		System.out.println("Game Over: "+results);
//
//
//	}
	
	
	
//	public static void ManualPlay(game_service game){
//
//		List<String> log = game.move();
//		int destMove=-1;
//		if(log != null)
//		{
//			
//		
//			if(currentRobot.getDest() != null) {
//				 destMove = currentRobot.getDest().getKey();
//			}
//			if(destMove!= -1) {
//
//				if(currentRobot!= null) {
//					game.chooseNextEdge(currentRobot.getSrc().getKey(), destMove);
//					game.move();
//					//repaint();
//				}
//			}
//		}
//	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {


	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	/**
	 * 
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



	public BufferedImage getMyImage() {
		return myImage;
	}

	public void setMyImage(BufferedImage myImage) {
		this.myImage = myImage;
	}
	
	
	
	
	
	

	public static void main(String[] args) {

		MyGameGUI Test =  new MyGameGUI();
		Test.setVisible(true);
	}

	@Override
	public void run() {
		
		MyGameGUI Test =  new MyGameGUI();
		Test.setVisible(true);
		
		
		
	}

	

}


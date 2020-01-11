package gameClient;



import utils.Point3D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.Fruit;
import dataStructure.Node;
import dataStructure.Robot;
import gui.GraphRefresher;
import javafx.scene.chart.PieChart.Data;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Fruit;
import dataStructure.Robot;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;

public class MyGameGUI extends JFrame implements ActionListener , Serializable,  GraphRefresher, MouseListener, MouseMotionListener{
	private final double EPSILON = 0.00001;
	private ArrayList<Fruit> FruitsList = new ArrayList<Fruit>();
	private ArrayList<Robot> RobotsList = new ArrayList<Robot>();
	private DGraph DG = new DGraph();
	private graph GG;
	private Graph_Algo GA = new Graph_Algo();
	private double min_x=Integer.MAX_VALUE;
	private double max_x=Integer.MIN_VALUE;
	private double min_y=Integer.MAX_VALUE;
	private double max_y=Integer.MIN_VALUE;

	public MyGameGUI() {

//		int scenario_num = 2;
//		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
//		String g = game.getGraph();
//		DG.init(g);
		this.GG = null;
		this.DG=null;
		initGUI(DG);
	}

	public MyGameGUI(DGraph Dgraph) {
		this.DG = Dgraph;
		initGUI(DG);
	}

	private void initGUI(DGraph DGraph){
		this.setSize(700, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		if(DGraph != null) FindMinMax(DGraph);
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
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
	}

	public void paint(Graphics g) {
		super.paint(g);
		
		if(this.DG != null) {
		Collection<node_data> Vertexes = DG.getV();
		for(node_data node_data: Vertexes) {
			
			Point3D TempPoint = node_data.getLocation();
			g.setColor(Color.RED);
			int x0 = (int) scale(TempPoint.x(), min_x, max_x , 50 , this.getHeight()-50);
			int y0 = (int) scale(TempPoint.y(), min_y, max_y , 70 , this.getWidth()-70);
			g.fillOval(x0, y0, 10, 10);	
			// Check here the +1 -2 brother it may affect the string cuz we already transformed to cordinates
			g.drawString(Integer.toString(node_data.getKey()), x0+1, y0-2);
			Collection<edge_data> Edge = DG.getE(node_data.getKey());

			for(edge_data edge_data: Edge) {	
				g.setColor(Color.DARK_GRAY);
				
				node_data dest = DG.getNode(edge_data.getDest());
				Point3D TempPoint2 = dest.getLocation();
				int x1 = (int) scale(TempPoint2.x(), min_x, max_x , 50 , this.getWidth()-50);
				int y1 = (int) scale(TempPoint2.y(), min_y, max_y , 70 , this.getHeight()-70);
				if (TempPoint2 != null) {
				g.drawLine(x0, y0, x1, y1);

				g.drawString(Double.toString(edge_data.getWeight()),((x0+x1)/2) , ((y0+y1)/2));
				g.setColor(Color.GREEN);
				int XFrame =((((((x0+x1)/2)+x1)/2)+x1)/2);
				int YFrame = ((((((y0+y1)/2)+y1)/2)+y1)/2);
				g.fillOval(XFrame, YFrame, 6, 6);	
				
				
				}
			}
		}
		if(!FruitsList.isEmpty()){
			for(Fruit f : FruitsList) {
				findFruitEdge(f);
				Point3D p = f.getPos();
				if(f.getType() == 1) g.setColor(Color.GREEN);
				else g.setColor(Color.YELLOW);
				int x = (int) scale(p.x(), min_x, max_x , 50, this.getWidth()-50);
				int y = (int) scale(p.y(), min_y, max_y ,70 , this.getHeight()-70);
				g.fillOval(x, y, 8	, 8);
			}
		}
		if(RobotsList!= null){
			for(Robot r : RobotsList) {
				Point3D p = r.getPos();
				g.setColor(Color.PINK);
				int x = (int) scale(p.x(), min_x, max_x , 50 , this.getWidth()-50);
				int y = (int) scale(p.y(), min_y, max_y , 70 , this.getHeight()-70);
				g.fillOval(x, y, 3	, 3);			
			}
		}
	 }
	}
	private void clear() {
		Collection<node_data> v = GG.getV();
		for(node_data n : v) {
			Collection<edge_data> e = GG.getE(n.getKey());
			for(edge_data ed: e) {
				GG.getEdge(ed.getSrc(), ed.getDest()).setTag(0);
			}
		}
	}

	private void findFruitEdge(Fruit f) {
		Collection<node_data> v = GG.getV();
		for(node_data n : v) {
			Collection<edge_data> e = GG.getE(n.getKey());
			for(edge_data ed: e) {
				Point3D p =GG.getNode(ed.getSrc()).getLocation();
				Point3D p2 =GG.getNode(ed.getDest()).getLocation();
				//check if the fruit is on the edge
				if((dist(p, p2)-(dist(f.getPos(),p)+dist(f.getPos(), p2)))<= EPSILON){
					int low=n.getKey();
					int high=ed.getDest();
					if(n.getKey()>ed.getDest()) {
						low= ed.getDest();
						high= n.getKey();
					}
					if(f.getType()==1) {
						edge_data edF = GG.getEdge(low, high);
						if(edF!= null) f.setEdge(edF);
					}
					//the reverse edge is the way to eat the fruit
					if(f.getType()==-1) {
						edge_data edF = GG.getEdge(high,low);
						if(edF!= null)f.setEdge(edF);
					}
				}

			}
		}
	}
	private double dist(Point3D p, Point3D p2) {
		double ans = Math.sqrt(Math.pow((p.x()-p2.x()),2)+(Math.pow((p.y()-p2.y()),2)));
		return ans;

	}

	public void graphUpdate() {
		repaint();
	}

	private void CreateAuto() {
		String user_input = JOptionPane.showInputDialog(
				null, "Please enter your scenario number ([0,23]) ");
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
		initGUI(this.DG);
		
	}
		
	

	private void CreateCustom() {
		String user_input = JOptionPane.showInputDialog(
				null, "Please enter your scenario number ([0,23]) ");
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
		initGUI(this.DG);
		
	}
		
	



	private void FindMinMax (graph graph) {


		Collection<node_data> GraphNodes = graph.getV();
		for (node_data MyNode : GraphNodes) {
			Point3D p = MyNode.getLocation();
			if(p.x() < min_x)min_x = p.x();
			if(p.x() > max_x)max_x = p.x();
			if(p.y() > max_y)max_y = p.y();
			if(p.y() < min_y)min_y = p.y();

		}

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
	private double scale(double data, double r_min, double r_max, 
			double t_min, double t_max)
	{
		
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}


	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
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
	
	
	


	public static void main(String[] args) {

		
//		Point3D p1 = new Point3D (72,35,10);
//		Point3D p2 = new Point3D (50,80,9);
//		Point3D p3 = new Point3D (20,60,11);
//		Point3D p4 = new Point3D (50,100,0.4);
//		Point3D p5 = new Point3D (20,68,0.5);
//		Point3D p6 = new Point3D (80,180,0.5);
//		Point3D p7 = new Point3D (40,20,4);
//		Point3D p8 = new Point3D (48,50,0.4);
//		Point3D p9 = new Point3D (75,85,0.5);
//		Point3D p10 = new Point3D (90,70,0.5);
//		Point3D p11 = new Point3D (91,71,4);
//		Point3D p12 = new Point3D (63,95,0.4);
//		Point3D p13 = new Point3D (25,66,0.5);
//		Point3D p14 = new Point3D (71,55,0.5);
//		Point3D p15 = new Point3D (45,26,4);
//		Point3D p16 = new Point3D (81,72,0.4);
//		Point3D p17 = new Point3D (25,65,0.5);
//		Point3D p18 = new Point3D (61,23,0.5);
//		Point3D p19 = new Point3D (28,57,4);
//
//		Node n1 = new Node (1, 9, p1);
//		Node n2 = new Node (2, 5.5, p2);
//		Node n3 = new Node (3, 5.3, p3);
//		Node n4 = new Node (4, 2, p4);
//		Node n5 = new Node (5, 5.7, p5);
//		Node n6 = new Node (6,4,p6);
//		Node n7 = new Node (7,9,p7);
//		Node n8 = new Node (8, 7, p8);
//		Node n9 = new Node (9, 5.2, p9);
//		Node n10 = new Node (10, 1, p10);
//		Node n11 = new Node (11, 3.6, p11);
//		Node n12 = new Node (12, 3, p12);
//		Node n13 = new Node (13,1.5,p13);
//		Node n14 = new Node (14,3,p14);
//		Node n15 = new Node (15, 4, p15);
//		Node n16 = new Node (16, 5.6, p16);
//		Node n17 = new Node (17, 6, p17);
//		Node n18 = new Node (18, 5.8, p18);
//		Node n19 = new Node (19, 8, p19);
//
//		DGraph g = new DGraph();
//
//		g.addNode(n1);
//		g.addNode(n2);
//		g.addNode(n3);
//		g.addNode(n4);
//		g.addNode(n5);
//		g.addNode(n6);
//		g.addNode(n7);
//		g.addNode(n8);
//		g.addNode(n9);
//		g.addNode(n10);
//		g.addNode(n11);
//		g.addNode(n12);
//		g.addNode(n13);
//		g.addNode(n14);
//		g.addNode(n15);
//		g.addNode(n16);
//		g.addNode(n17);
//		g.addNode(n18);
//		g.addNode(n19);
//
//		g.connect(1,2,3);
//		g.connect(2,1,11);
//		g.connect(2,3,5.9);
//		g.connect(3,2,8);
//		g.connect(3,4,5.2);
//		g.connect(4,3,5);
//		g.connect(5,4,6);
//		g.connect(4,5,2.7);
//		g.connect(1,6,5);
//		g.connect(6,1,3.8);
//		g.connect(6,5,8.6);
//		g.connect(5,6,8.6);
//		g.connect(7,8,3);
//		g.connect(8,7,11);
//		g.connect(8,9,5.9);
//		g.connect(9,8,5.9);
//		g.connect(9,10,8);
//		g.connect(10,9,5.9);
//		g.connect(10,8,5.2);
//		g.connect(10,6,5);
//		g.connect(10,11,5.9);
//		g.connect(11,10,5.9);
//		g.connect(11,6,6);
//		g.connect(12,13,2.7);
//		g.connect(6,12,5);
//		g.connect(13,14,3.8);
//		g.connect(14,13,8.6);
//		g.connect(15,2,8.6);
//		g.connect(2,16,3);
//		g.connect(16,17,2);
//		g.connect(16,10,5.9);
//		g.connect(15,3,8);
//		g.connect(17,4,5.2);
//		g.connect(8,18,5);
//		g.connect(18,16,6);
//		g.connect(18,5,2.7);
//		g.connect(18,12,5);
//		g.connect(9,18,3.8);
//		g.connect(19,1,8.6);
//		g.connect(18,19,8.6);
//		g.connect(12,3,2);
//		g.connect(12,2,2);
//		g.connect(3,8,2);
//		g.connect(9,11,2);
//		g.connect(14,12,2);
//		g.connect(16,5,2);
//		g.connect(17,19,2);
//		g.connect(19,17,2);
//		g.connect(5,16,2);
//		g.connect(15,19,2);
//		g.connect(19,15,2);
//		g.connect(16,6,2);
//		g.connect(6,16,2);
//		

		MyGameGUI Test =  new MyGameGUI();
		Test.setVisible(true);

	}

}


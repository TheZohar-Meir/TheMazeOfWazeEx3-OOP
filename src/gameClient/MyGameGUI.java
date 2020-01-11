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
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import dataStructure.Fruit;
import dataStructure.Robot;
import gui.GraphRefresher;
import Server.Game_Server;
import Server.game_service; 
 

public class MyGameGUI extends JFrame implements ActionListener , Serializable,  GraphRefresher, MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	private final double EPSILON = 0.00001;
	private ArrayList<Fruit> FruitsList = new ArrayList<Fruit>();
	private ArrayList<Robot> RobotsList = new ArrayList<Robot>();
	private DGraph DG = new DGraph();
	private graph GG;
	//private Graph_Algo GA = new Graph_Algo();
	private double min_x = Integer.MAX_VALUE;
	private double max_x = Integer.MIN_VALUE;
	private double min_y = Integer.MAX_VALUE;
	private double max_y = Integer.MIN_VALUE;


	public MyGameGUI() {
		int scenario_num = 2;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String g = game.getGraph();
		DG.init(g);
		initGUI(DG);
	}

	public MyGameGUI(DGraph Dgraph) {
		this.DG = Dgraph;
		initGUI(DG);
	}

	
	/**
	 * 
	 * @param DGraph
	 */
	private void initGUI(DGraph DGraph){
		
//		ImageIcon image = new ImageIcon ("data/AA.png"); // Creates the image
//		JLabel label = new JLabel (image); // add the image to the label
//		this.add(label); //add the label to the frame
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		//System.out.println(new File("A3.png").exists());
		
		if(DGraph != null) {FindMinMax(DGraph);}
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

	
	/**
	 * 
	 */
	public void paint(Graphics g) {
		
		super.paint(g);

		Image img = Toolkit.getDefaultToolkit().getImage(MyGameGUI.class.getResource("/gameClient/AA.png"));  
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
				                  
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

	
	
	/**
	 * 
	 * @param f
	 */
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
	
	
	/**
	 * 
	 */
	private void clear() {
		Collection<node_data> v = GG.getV();
		for(node_data n : v) {
			Collection<edge_data> e = GG.getE(n.getKey());
			for(edge_data ed: e) {
				GG.getEdge(ed.getSrc(), ed.getDest()).setTag(0);
			}
		}
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
		initGUI(this.DG);
	}


	/**
	 * 
	 */
	private void CreateCustom() {
		
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
		initGUI(this.DG);
	}


	/**
	 * 
	 * @param graph
	 */
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

	public static void main(String[] args) {

		MyGameGUI Test =  new MyGameGUI();
		Test.setVisible(true);

	}

}


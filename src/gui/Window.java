package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;


/**
 *This class represents a visual display of the Graph.
 *This class extends JFrame and implements ActionListener..
 *Each window has some private fields:
 *graph graph - a field to save the given graph needs to be presented.
 *static final long serialVersionUID - must be written according to eclipse.
 *LinkedList points - a linked list that will be filled with the graph vertexes
 *
 *### IMPORTENT ISSUE : this class represents a visual display of the graph ###
 *### Our graph data structure supports the option of inserting any 3DPoint ###
 *### you wishes BUT ###
 *### there is a visual mess if your   ###
 *### insert points are too close to each others or if the  ###
 *### points are outside of your screen resolution. ###
 *### It is also important to take in count that JFrame works with frames ###
 *### and not coordinates therefore adding a points with the next x and y values won't ### 
 *###be displayed on a the Graph GUI :###
 *### x - Should be between 20 - 1000 ###
 *### y - Should be between 50 - 1000 ###
 *### if you'll enter your 3DPoints coordinates according to the ###
 *### written above you will be able to see them also in our Graph display window.###
 *
 *
 *for further information please visit our repository wiki page:
 *https://github.com/lidorT/OOP---Ex2/issues/1
 * © authors: Zohar and Lidor.
 */



public class Window extends JFrame implements ActionListener
{

	private graph graph;
	private static final long serialVersionUID = 1L;
	LinkedList<Point3D> points = new LinkedList<Point3D>();

	public Window(){
		this.graph =null;
		initGUI();
	}

	public Window(graph g)
	{
		this.graph=g;
		initGUI();
	}

	
	/**
	 * This function purpose is to initialize the GUI.
	 * Here we set the GUI size, we creates the menu bar and adds its options.
	 */
	private void initGUI() 
	{
		this.setSize(700, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		MenuBar menuBar = new MenuBar();
		Menu File = new Menu("File");
		menuBar.add(File);
		Menu Graph_Algorithms = new Menu("Graph Algorithms");
		menuBar.add(Graph_Algorithms);
		this.setMenuBar(menuBar);

		MenuItem save = new MenuItem("Save");
		save.addActionListener(this);

		MenuItem load = new MenuItem("Load");
		load.addActionListener(this);

		File.add(save);
		File.add(load);

		MenuItem Drawgraph = new MenuItem("Refresh graph");
		Drawgraph.addActionListener(this);

		MenuItem isConnected = new MenuItem("Is This Graph Connected");
		isConnected.addActionListener(this);

		MenuItem shortestPathDist = new MenuItem("Shortest Path Distance");
		shortestPathDist.addActionListener(this);

		MenuItem shortestPath = new MenuItem("Shortest Path");
		shortestPath.addActionListener(this);

		MenuItem TSP = new MenuItem("TSP");
		TSP.addActionListener(this);

		Graph_Algorithms.add(Drawgraph);
		Graph_Algorithms.add(isConnected);
		Graph_Algorithms.add(shortestPathDist);
		Graph_Algorithms.add(shortestPath);
		Graph_Algorithms.add(TSP);
	}

	
	/**
	 * paint functions is the basic function that paints the graph as JFrame required.
	 * we set the points(Node) and the edges inside the graph.
	 * we set the size of the points, lines , weights and more.
	 */
	public void paint(Graphics g)
	{
		super.paint(g);

		if(this.graph != null) {
			Collection <node_data> Vertexes = this.graph.getV();
			for (node_data node_data : Vertexes) {
				Point3D TempPoint = node_data.getLocation();
				
				if(node_data.getTag()!=8 && node_data.getTag() != 9) g.setColor(Color.RED);
				if(node_data.getTag() == 8) {
					g.setColor(Color.CYAN);
					node_data.setTag(0);
				}
				if(node_data.getTag() == 9) {
					g.setColor(Color.BLUE);
					node_data.setTag(0);
				}
				g.fillOval(TempPoint.ix(), TempPoint.iy(), 12, 12);
				g.setColor(Color.BLACK);
				g.drawString(Integer.toString(node_data.getKey()), (TempPoint.ix()+1), (TempPoint.iy()-2));
				Collection<edge_data> Edge = this.graph.getE(node_data.getKey());
				for (edge_data edge_data : Edge) {
					if (edge_data.getTag() == 9) {
						edge_data.setTag(0);
						g.setColor(Color.RED);
					}
					else {
						g.setColor(Color.DARK_GRAY);
					}
					node_data dest = graph.getNode(edge_data.getDest());
					Point3D TempPoint2 = dest.getLocation();
					if (TempPoint2 != null) {
						
						Stroke tempo = new BasicStroke(2f);
						((Graphics2D) g).setStroke(tempo);
						((Graphics2D) g).draw(new Line2D.Double(TempPoint.ix(), TempPoint.iy(),
								TempPoint2.ix(), TempPoint2.iy()));
						g.drawString(Double.toString(edge_data.getWeight()),(TempPoint.ix()+5*TempPoint2.ix())/6 , (TempPoint.iy()+5*TempPoint2.iy())/6);
						g.setColor(Color.GREEN);
						int XFrame =((((((TempPoint.ix()+TempPoint2.ix())/2)+TempPoint2.ix())/2)+TempPoint2.ix())/2);
						int YFrame = ((((((TempPoint.iy()+TempPoint2.iy())/2)+TempPoint2.iy())/2)+TempPoint2.iy())/2);
						g.fillOval(XFrame, YFrame, 8, 8);	
					}
				}
			}
		}
	}

	
	/**
	 * this function simply connects the GUI button " Save "  to the Graph_Algo function save.
	 */
	private void Save() {
		
		Graph_Algo graphalgo = new Graph_Algo();
		graphalgo.init(this.graph);
		JFrame Frame = new JFrame();
		JFileChooser Choose = new JFileChooser();
		Choose.setDialogTitle("Save a file");   
		int userSelection = Choose.showSaveDialog(Frame);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File Save = Choose.getSelectedFile();
			String FileName= Save.getAbsolutePath();
			graphalgo.save(FileName);		
			System.out.println("Save as file: " + Save.getAbsolutePath());
		}

	}

	
	/**
	 * this function simply connects the GUI button " Load "  to the Graph_Algo function Load.
	 */
	private void Load() {
		Graph_Algo graphalgo = new Graph_Algo();
		JFrame Frame = new JFrame();
		JFileChooser Choose = new JFileChooser();
		Choose.setDialogTitle("Load a file");   
		int userSelection = Choose.showOpenDialog(Frame);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToLoad = Choose.getSelectedFile();
			String file= fileToLoad.getAbsolutePath();
			graphalgo.init(file);
			repaint();
			System.out.println("Load from file: " + fileToLoad.getAbsolutePath());
		}
	}

	
	/**
	 * This function simply connects the GUI button "shortestPathDist" to the Graph_Algo function shortestPathDist.
	 */
	private void shortestPathDist() {
		
		JFrame Frame = new JFrame();
		String src = JOptionPane.showInputDialog(
				null, "Please enter the source Node key");
		String dest = JOptionPane.showInputDialog(
				null, "Please enter your destination Node key");
		Graph_Algo graphalgo = new Graph_Algo();
		graphalgo.init(this.graph);
		try {
			int s = Integer.parseInt(src);
			int d = Integer.parseInt(dest);
		}
		catch (Exception e) {
			throw new RuntimeException("One of your inputs src/dest is not an Integer");
		}
		double ans = graphalgo.shortestPathDist(Integer.parseInt(src), Integer.parseInt(dest));
		String s = Double.toString(ans);	
		JOptionPane.showMessageDialog(Frame, "The shortest distance you hat to travel is : " + s);
	
	}

	
	/**
	 * This function simply connects the GUI button " shortestPath "  to the Graph_Algo function shortestPath.
	 * after getting the shortest path we also mark the path with red for an easier view.
	 * we also mark the first Node of the path color to Cyan and all the other Nodes
	 * inside the path to Blue.
	 */
	private void shortestPath() {
		
		JFrame Frame = new JFrame();
		String s = "";
		String src = JOptionPane.showInputDialog(
				null, "Please enter the source Node key");
		String dest = JOptionPane.showInputDialog(
				null, "Please enter your destination Node key");
		Graph_Algo graphalgo = new Graph_Algo();
		graphalgo.init(this.graph);
		
		if (!src.equals(dest)) {
			try {
				int src2 = Integer.parseInt(src);
				int dest2 = Integer.parseInt(dest);
			}
			catch (Exception e) {
				throw new RuntimeException("One of your inputs src/dest is not an Integer");
			}
			ArrayList<node_data> shortPath = new ArrayList<node_data>();
			shortPath = (ArrayList<node_data>) graphalgo.shortestPath(Integer.parseInt(src), Integer.parseInt(dest));
			for (int i = 0 ; i+1 < shortPath.size() ; i++) {
				this.graph.getEdge(shortPath.get(i).getKey(), shortPath.get(i+1).getKey()).setTag(9);
				s+= shortPath.get(i).getKey() + "---> ";
				if(i==0) {
					this.graph.getNode(shortPath.get(i).getKey()).setTag(8);
				}
				else this.graph.getNode(shortPath.get(i).getKey()).setTag(9);
			}
			s+= shortPath.get(shortPath.size()-1).getKey();
			this.graph.getNode(shortPath.get(shortPath.size()-1).getKey()).setTag(9);
			repaint();
			JOptionPane.showMessageDialog(Frame, "the shortest path is: " +s);
		}
	}

	
	/**
	 *This function simply connects the GUI button " TSP "  to the Graph_Algo function TSP.
	 *Here you'll have to enter each key of node you want to travel and in order to stop to enter Stop.
	 */
	private void TSP() {
		
		JFrame Frame = new JFrame();
		Graph_Algo graph_algo = new Graph_Algo();
		graph_algo.init(this.graph);
		ArrayList<Integer> targets = new ArrayList<Integer>();
		String tempSrc = "";
		String temp = "";
		do {
			tempSrc = JOptionPane.showInputDialog(null, "Enter a key, to stop this dialog please isert the word Stop");
			if ((!tempSrc.equals("Stop"))) {
				try {
					int parse = Integer.parseInt(tempSrc);
				}
				catch(Exception e) {
					throw new RuntimeException("One of your inputs is not an Integer");
				}
				targets.add(Integer.parseInt(tempSrc));
			}
		}
		while(!tempSrc.equals("Stop"));
		ArrayList<node_data> shortPath = new ArrayList<node_data>();
		shortPath = (ArrayList<node_data>) graph_algo.TSP(targets);
		if (shortPath != null ) 
		{
			for (int i = 0 ; i+1 < shortPath.size() ; i++) {
				this.graph.getEdge(shortPath.get(i).getKey(), shortPath.get(i+1).getKey()).setTag(9);
				temp += shortPath.get(i).getKey() + "---> ";
				this.graph.getNode(shortPath.get(i).getKey()).setTag(9);
			}
			temp += shortPath.get(shortPath.size()-1).getKey();
			this.graph.getNode(shortPath.get(0).getKey()).setTag(8);
			this.graph.getNode(shortPath.get(shortPath.size()-1).getKey()).setTag(9);
			repaint();
			JOptionPane.showMessageDialog(Frame, "the shortest path on this graph is: " +temp);
		}
		if(shortPath==null) {
			JOptionPane.showMessageDialog(Frame, "the shortest path on this graph is: null ");
		}
	}
	
	
	/**
	 * This function checks if the graph is Strongly connected with the use
	 * of IsConnected function from Graph_Algo class.
	 */
	private void isConnected() {
		
		JFrame Frame = new JFrame();
		Graph_Algo graph_algo = new Graph_Algo();
		graph_algo.init(this.graph);
		if (graph_algo.isConnected()) {
			JOptionPane.showMessageDialog(Frame, "this graph is connected");
		}
		else {
			JOptionPane.showMessageDialog(Frame, "the graph is not connected");
		}
	}

	
	/**
	 * This function receives ActionEvent e that represents the button that have been pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String input = e.getActionCommand();

		switch(input) {
		case "Refresh graph" : repaint();
		break;
		case "Save": Save();
		break;
		case "Load": Load();
		break;
		case "Is This Graph Connected": isConnected();
		break;
		case "Shortest Path Distance": shortestPathDist();
		break;
		case "Shortest Path": shortestPath();
		break;
		case "TSP": TSP();
		break;
		default:
			break;
		}
	}


}
package Testers;

import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

public class DGraph_Test {

	private DGraph Dg = null;
	
	@Before
	public void setUp() throws Exception {
		Dg = DGraph_factory();
	}
	
	@Test
	public void DGraph() {
		
		DGraph g = new DGraph();
	}

	@Test
	public void GetNode() {
		
		System.out.println("GetNode: ");
		node_data nd1 = Dg.getNode(1); 
		node_data nd2 = Dg.getNode(2);
		node_data nd3 = Dg.getNode(8); //should be null
		
		System.out.println("should be node: " + nd1);
		System.out.println("should be node: " + nd2);
		System.out.println("should be null: " + nd3);
		System.out.println();
	}

	@Test
	public void GetEdge() {
		
		System.out.println("GetEdge: ");
		edge_data ed1 = Dg.getEdge(1,2); 
		edge_data ed2 = Dg.getEdge(2,3); 
		edge_data ed3 = Dg.getEdge(3,2);  
		edge_data ed4 = Dg.getEdge(3,1);//should be null
		
		System.out.println("should be edge: " + ed1);
		System.out.println("should be edge: " + ed2);
		System.out.println("should be edge: " + ed3);
		System.out.println("should be null: " + ed4);
		System.out.println();
	}

	@Test
	public void AddNode() {
		
		System.out.println("AddNode: ");
		Point3D p = new Point3D (18,5,4);
		Node n = new Node (1, 5.6, p);
		Node n8 = new Node (8, 5.6, p);
		Dg.addNode(n8);
		System.out.println("Adding node: "+n8);
		try {
			Dg.addNode(n);
		}
		catch(Exception e){
			System.err.println("ERROR, there is already node with the same key");
		}
		System.out.println();
	}

	@Test
	public void Connect() {
		
		System.out.println("Connect: ");
		Dg.connect(3,1,10);
		System.out.println("connect "+3+" to "+1);
		System.out.println();
	}

	@Test
	public void GetV() {

		System.out.println("GetV: ");
		Collection<node_data> cn = Dg.getV();
		for(node_data node: cn){
			System.out.print(" --> "+node.getKey());
		}
		System.out.println();
		System.out.println();
	}

	@Test
	public void GetE() {
		
		System.out.println("GetE: ");
		Collection<edge_data> en = Dg.getE(5);
		System.out.println("Edges of node "+5+": ");
		for(edge_data edge: en){
			System.out.print(" ("+edge.getSrc()+","+edge.getDest()+") ");
		}
		System.out.println();
		System.out.println();
	}

	@Test
	public void RemoveNode() {
		
		System.out.println("RemoveNode:");
		node_data node = Dg.removeNode(3);
		System.out.println("should be node: "+node);
		
		node_data node2 = Dg.removeNode(9);
		System.out.println("should be null: "+node2); //should be null
		System.out.println();
	}

	@Test
	public void RemoveEdge() {
		
		System.out.println("RemoveEdge:");
		edge_data edge = Dg.removeEdge(5,6);
		System.out.println("should be edge: "+edge);
		
		edge_data edge2 = Dg.removeEdge(9,3);
		System.out.println("should be null: "+edge2); //should be null
		System.out.println();
	}

	@Test
	public void NodeSize() {

		System.out.print("NodeSize: ");
		System.out.println(Dg.nodeSize());
		System.out.println();
	}

	@Test
	public void EdgeSize() {

		System.out.print("EdgeSize: ");
		System.out.println(Dg.edgeSize());
		System.out.println();
	}

	@Test
	public void GetMC() {

		System.out.print("GetMC: ");
		System.out.println(Dg.getMC());
		System.out.println();
	}
	
	
	public static DGraph DGraph_factory(){
		
		Point3D p1 = new Point3D (7,3,10);
		Point3D p2 = new Point3D (05,8,9);
		Point3D p3 = new Point3D (15,10,11);
		Point3D p4 = new Point3D (12,7,0.4);
		Point3D p5 = new Point3D (2,6,0.5);
		Point3D p6 = new Point3D (8,1,0.5);
		Point3D p7 = new Point3D (4,2,4);
		
		Node n1 = new Node (1, 5.2, p1);
		Node n2 = new Node (2, 5.2, p2);
		Node n3 = new Node (3, 5.2, p3);
		Node n4 = new Node (4, 5.2, p4);
		Node n5 = new Node (5, 5.2, p5);
		Node n6 = new Node (6,5.2,p6);
		Node n7 = new Node (7,0,p7);
		
		DGraph g = new DGraph();
		
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.addNode(n6);
		g.addNode(n7);
		
		g.connect(1,2,3);
		g.connect(2,1,11);
		g.connect(2,3,5.9);
		g.connect(3,2,8);
		g.connect(3,4,5.2);
		g.connect(4,3,5);
		g.connect(5,4,6);
		g.connect(4,5,2.7);
		g.connect(1,6,5);
		g.connect(6,1,3.8);
		g.connect(6,5,8.6);
		g.connect(5,6,8.6);
		
		return g;
	}
	

}

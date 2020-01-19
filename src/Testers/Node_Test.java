package Testers;

import org.junit.Test;
import dataStructure.Node;
import utils.Point3D;

public class Node_Test {

	@Test
	public void Node() { // Node()
		
		Node n = new Node();
	}

	@Test
	public void NodeIntDoublePoint3D() { // Node(int key,double weight,Point3D point){}
		
		int key = 3;
		double weight = 5.3;
		Point3D point = new Point3D (3,5.1,0.5);
		Node n = new Node(key,weight,point);
		Point3D point2 = new Point3D (3.3,7.5,0.5);
		Node n2 = new Node(4,2.5,point2);
		System.out.println();
	}

	@Test
	public void GetKey() {
		
		System.out.println("GetKey: ");
		int key = 3;
		double weight = 5.3;
		Point3D point = new Point3D (3,5.1,0.5);
		Node n = new Node(key,weight,point);
		System.out.println("The key is: "+n.getKey());
		System.out.println();
	}

	@Test
	public void GetLocation() {
		
		System.out.println("GetLocation: ");
		int key = 3;
		double weight = 5.3;
		Point3D point = new Point3D (3,5.1,0.5);
		Node n = new Node(key,weight,point);
		Point3D point2 = n.getLocation();
		System.out.println("The location is: ("+point2+")");
		System.out.println();
	}

	@Test
	public void SetLocation() {
		
		System.out.println("SetLocation: ");
		Node n = new Node();
		System.out.println("Before SetLocation: "+n.getLocation());
		Point3D p = new Point3D (3,5,1);
		n.setLocation(p);
		System.out.println("After SetLocation: "+n.getLocation());
		System.out.println();
	}

	@Test
	public void GetWeight() {
		
		System.out.println("GetWeight: ");
		Point3D point = new Point3D (3,5.8,0.5);
		Node n = new Node(10,105,point);
		System.out.println("The Weight is: "+n.getWeight());
		System.out.println();
	}

	@Test
	public void SetWeight() {
		
		System.out.println("SetWeight: ");
		Point3D point = new Point3D (3,5.8,0.5);
		Node n = new Node(10,105,point);
		System.out.println("Before SetWeight: "+n.getWeight());
		n.setWeight(60);
		System.out.println("After SetWeight: "+n.getWeight());
		System.out.println();
	}

	@Test
	public void GetInfo() {
		
		System.out.println("GetInfo: ");
		Point3D point = new Point3D (3,5.8,0.5);
		Node n = new Node(10,105,point);
		System.out.println("The Info is: "+n.getInfo()); //should be null
		System.out.println();
	}

	@Test
	public void SetInfo() {
		
		System.out.println("SetInfo: ");
		Node n = new Node();
		System.out.println("The Info is: "+n.getInfo()); //should be null
		n.setInfo("5");
		System.out.println("The Info is: "+n.getInfo());
		System.out.println();
	}

	@Test
	public void GetTag() {
		
		System.out.println("GetTag: ");
		Point3D point = new Point3D (3,5.8,0.5);
		Node n = new Node(10,105,point);
		System.out.println("The tag is: "+n.getTag()); //should be 0
		System.out.println();
	}

	@Test
	public void SetTag() {
		
		System.out.println("SetTag: ");
		Node n = new Node();
		System.out.println("Before SetTag: "+n.getTag()); //should be null
		n.setTag(2);
		System.out.println("After SetTag: "+n.getTag());
		System.out.println();
	}

}

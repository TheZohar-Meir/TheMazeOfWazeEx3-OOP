package Testers;

import org.junit.Test;
import dataStructure.*;

public class Edge_Test {

	@Test
	public void Edge() {
		
		System.out.println("Edge: ");
		Edge edge = new Edge();
		System.out.println("build edge: "+edge);
		System.out.println();
	}

	@Test
	public void EdgeIntIntDouble() { // Edge(int src, int dest, double weight){
		
		System.out.println("EdgeIntIntDouble: ");
		Edge edge = new Edge(5,10,3.6);
		System.out.println("build edge: "+edge);
		System.out.println();
	}

	@Test
	public void GetSrc() {

		System.out.println("GetSrc: ");
		Edge edge = new Edge(5,10,3.6);
		System.out.println("edge src: "+edge.getSrc());
		System.out.println();
	}

	@Test
	public void GetDest() {
		
		System.out.println("GetDest: ");
		Edge edge = new Edge(5,10,3.6);
		System.out.println("edge dest: "+edge.getDest());
		System.out.println();
	}

	@Test
	public void GetWeight() {
		
		System.out.println("GetWeight: ");
		Edge edge = new Edge(5,10,3.6);
		System.out.println("edge weight: "+edge.getWeight());	
		System.out.println();
	}

	@Test
	public void GetInfo() {

		System.out.println("GetInfo: ");
		Edge edge = new Edge(5,10,3.6);
		System.out.println("edge info: "+edge.getInfo());	
		System.out.println();
	}

	@Test
	public void SetInfo() {
		
		System.out.println("SetInfo: ");
		Edge edge = new Edge(5,10,3.6);
		edge.setInfo("succsses");
		System.out.println("edge info after set: "+edge.getInfo());	
		System.out.println();
	}

	@Test
	public void GetTag() {
		
		System.out.println("GetTag: ");
		Edge edge = new Edge(5,10,3.6);
		System.out.println("edge tag: "+edge.getTag());	
		System.out.println();
	}

	@Test
	public void SetTag() {
		
		System.out.println("SetTag: ");
		Edge edge = new Edge(5,10,3.6);
		System.out.println("edge tag before set: "+edge.getTag());	
		edge.setTag(1);
		System.out.println("edge tag after set: "+edge.getTag());	
		edge.setTag(2);
		System.out.println("edge tag after set: "+edge.getTag());
		System.out.println();
	}

}

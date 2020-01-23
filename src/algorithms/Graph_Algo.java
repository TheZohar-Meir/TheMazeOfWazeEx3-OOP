package algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Collections;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;

/**
 ** This class represents the set of graph-theory algorithms
 * The class implements graph_algorithm interface and java's Serializable interface.
 * Each Graph has some private fields:
 * Each Graph_Algo has a field of graph named ga(graph algorithm).
 * final long serialVersionUID = 1L - must be written as part of extending Serializable interface.
 * © authors: Zohar and Lidor.
 */
public class Graph_Algo implements graph_algorithms,Serializable{

	private static final long serialVersionUID = 1L;
	private graph ga; 

	/**
	 * Default constructor.
	 * @param g - a graph
	 */
	public Graph_Algo(graph g) {
		this.ga = g;
	}


	/**
	 * Default constructor.
	 * @param g - a graph
	 */
	public Graph_Algo() {
		this.ga = null;
	}


	/**
	 * This function initializes ga field from a given graph.
	 * @param g - an input graph.
	 */
	public void init(graph g) {
		this.ga = g;
	}


	/**
	 * This function initializes ga from a given text file.
	 * @param file_name - the text file name.
	 */
	public void init(String file_name) {

		try {
			FileInputStream fileIn = new FileInputStream(file_name);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			this.ga= (graph) objectIn.readObject();

			System.out.println("The Object has been read from the file");
			objectIn.close();
			fileIn.close();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}



	/**
	 *  saves the current graph into a text file.
	 * 	@param file_name - the text file name.
	 */
	public void save(String file_name) {

		try {
			FileOutputStream f = new FileOutputStream(new File(file_name));
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(this.ga);
			o.close();
			f.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("could not read file");
		}
	}


	/**
	 * this method checks if the current graph is strongly connected or not.
	 */
	public boolean isConnected() {

		if(this.ga.nodeSize()>0){
			boolean flag = true;
			Collection<node_data> vertex_collect = this.ga.getV();
			node_data[] Nodes_arr = vertex_collect.toArray(new node_data[vertex_collect.size()]);
			Queue<node_data> Nq = new LinkedList<node_data>();
			int visitCounter = 0;

			for (int i=0;i<Nodes_arr.length && flag ;i++) {

				ClearTags();
				node_data current = Nodes_arr[i];
				current.setTag(1);
				Nq.add(current);
				visitCounter = 0;

				while(!Nq.isEmpty()) {

					node_data head = Nq.peek();
					Collection<edge_data> edges_collect = this.ga.getE(head.getKey());

					for (edge_data myEdge: edges_collect) {

						if(ga.getNode(myEdge.getDest()).getTag() != 2)
							ga.getNode(myEdge.getDest()).setTag(1);

						if(ga.getNode(myEdge.getDest()).getTag() != 2 && !Nq.contains(ga.getNode(myEdge.getDest()))){
							Nq.add(ga.getNode(myEdge.getDest()));
						}
					}
					head.setTag(2);
					if(Nq.peek().getTag() == 2){
						Nq.poll();
						visitCounter++;
					}
				}
				if(visitCounter != Nodes_arr.length){

					flag = false;
				}
			}	
			return flag;
		}
		return false;
	}


	/**
	 * this function returns the summary of the weight 
	 * of all edges between the shortest path between source node to destination node.
	 * @param src -  the source Node key.
	 * @param dest-	 the destination Node key.
	 */
	public double shortestPathDist(int src, int dest) {

		if(IsExist(src) && IsExist(dest)){

			if (!this.isConnected()){
				if(ga.getNode(dest).getTag() == 0){
					throw new RuntimeException("Error, there is no path between src to dest.");
				}
			}
		}
		else{
			throw new RuntimeException("Error, there is no src or dest vertix.");
		}
		dijkstra(src); 
		return ga.getNode(dest).getWeight();
	}


	/**
	 *  return a list of the nodes you need to travel in order to pass the thrill
	 *  between the given source node to the destination node within the shortest path.
	 * @param src -  the source Node key.
	 * @param dest-	 the destination Node key.
	 */
	public List<node_data> shortestPath(int src, int dest) {

		if(IsExist(src) && IsExist(dest)){
			dijkstra(src);

			List<node_data> ans = new ArrayList<node_data>();
			node_data tempNode = ga.getNode(dest);

			while(tempNode.getKey() != src){

				ans.add(tempNode);
				tempNode = ga.getNode(Integer.parseInt(tempNode.getInfo()));
			}
			ans.add(tempNode);
			Collections.reverse(ans);
			return ans;
		}
		throw new RuntimeException("Error, there is no src or dest vertix.");
	}


	/**
	 * this function returns a List of nodes you have to travel 
	 * at the most efficient way in order to travel all the nodes on the input List.
	 * @param targets - a list of integers represents the Nodes keys.
	 */
	public List<node_data> TSP(List<Integer> targets) {

		List<node_data> ans = new ArrayList<node_data>();
		Integer[] targets_arr = targets.toArray(new Integer[targets.size()]);

		for(int src:targets_arr){
			if(!IsExist(src)){
				throw new RuntimeException("Error, there is no src or dest vertix.");
			}
		}

		if(targets_arr.length>1){

			if (!this.isConnected()){
				for (int i=1;i<targets_arr.length;i++){
					if(ga.getNode(targets_arr[i]).getTag() == 0){
						return null;
					}
				}
			}
		}

		for (int i=0;i<targets_arr.length-1;i++){

			List<node_data> temp = shortestPath(targets_arr[i], targets_arr[i+1]);

			for (node_data myNode : temp){
				if(ans.size()>0){
					if(myNode.getKey() != ans.get(ans.size()-1).getKey()) ans.add(myNode);
				}
				else ans.add(myNode);
			}
		}
		return ans;
	}


	/**
	 * This function return a deep copy graph of this.ga
	 */
	public graph copy() {

		graph temp = new DGraph();
		Collection<node_data> Nodes = ga.getV();

		for (node_data Node : Nodes) {
			temp.addNode(Node);
		}
		for (node_data Node : Nodes) {
			Collection<edge_data> Edges = ga.getE(Node.getKey());

			for (edge_data Edge : Edges) {
				temp.connect(Edge.getSrc(), Edge.getDest(), Edge.getWeight());
			}
		}
		return temp;
	}



	/////////////////////////////////////////////////
	/////////////// Private Methods /////////////////
	/////////////////////////////////////////////////


	/**
	 * This functions checks if src node exist.
	 * @param src - key of a node
	 * @return true/false
	 */
	private boolean IsExist(int src) {

		Collection<node_data> Nodes = ga.getV();
		for(node_data node: Nodes){
			if(node.getKey() == src) return true;
		}
		return false;
	}


	/**
	 * This function changes all Tags to 0
	 */
	private void ClearTags() {

		Collection<node_data> NodesCollection = ga.getV();

		for (node_data temp: NodesCollection) {
			temp.setTag(0);
		}
	}


	/**
	 * This function changes all Nodes Tag to 0 and weight to infinity
	 */
	private void resetNodes() {

		Collection<node_data> NodesCollection = ga.getV();
		for (node_data node: NodesCollection) 
		{
			node.setTag(0);
			node.setWeight(Double.MAX_VALUE);
		}
	}


	/**
	 * dijkstra algorithm for more information : https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm.
	 * @param src - Integer that represent node key
	 */
	private void dijkstra(int src) {

		resetNodes();
		Queue<node_data> Nq = new LinkedList<node_data>();
		ga.getNode(src).setWeight(0);
		Nq.add(ga.getNode(src));

		while(!Nq.isEmpty()){

			node_data temp1 = Nq.poll();
			Collection<edge_data> Edges = ga.getE(temp1.getKey());

			for(edge_data edge: Edges){

				double weightNode = temp1.getWeight();
				node_data temp2 = ga.getNode(edge.getDest());
				double weightEdge = edge.getWeight();

				if(temp2.getTag() != 1)
					if(weightEdge + weightNode < temp2.getWeight()) 
					{
						Nq.remove(temp2);
						temp2.setWeight(weightEdge + weightNode);
						temp2.setInfo("" + temp1.getKey());
						Nq.add(temp2);
					}
			}
		}
	}



}
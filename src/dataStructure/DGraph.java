package dataStructure;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import gui.GraphRefresher;
import utils.Point3D;

import java.io.Serializable;
import java.util.Collection;

/**
 *This class represents the Graph itself.
 *The class implements graph interface and java's Serializable interface.
 *Each Graph has some private fields:
 *Each DGraph has a HashMap field of Nodes which there the key set as the Node Key and the values are the node_data, each DGraph also has another HashMap field which there each key is a node_data and its values are HashMaps of edge_data.
 *int nodeSize- represents the number of Nodes inside this Dgraph.
 *int edgeSize- represents the number of Edges inside this Dgraph.
 *int MC- represents the version of the graph (each time you change a value in the graph MC is increased by 1).
 * © authors: Zohar and Lidor.
 */
public class DGraph implements graph,Serializable{

	private static final long serialVersionUID = 1L;
	private int MC;
	private int nodeSize;
	private int edgeSize;

	public HashMap <Integer,node_data> NodeMap;
	public HashMap <node_data, HashMap<Integer, edge_data>> EdgeMap;
	private GraphRefresher listener;
	/**
	 * Deaffult constructor sets all values to zeros and null.
	 */
	public DGraph(){

		this.nodeSize=0;
		this.edgeSize=0;
		this.MC=0;
		this.NodeMap=new HashMap<Integer,node_data>();
		this.EdgeMap=new HashMap <node_data, HashMap<Integer, edge_data>> ();
	}
	
	
	/**
	 *  return the node with the input key
	 *  with the use of the HashMap within O(1) running time complexity.
	 *  @param key - the Node's key.
	 */
	public node_data getNode(int key) { 

		if(NodeMap.containsKey(key))return this.NodeMap.get(key);
		return null;
	}
	
	
	/**
	 *  return an Edge that has the inputs source and destination values inside the graph.
	 *  @param src - the source Node's key.
	 *  @param dest - the destination Node's key.
	 *  
	 */
	public edge_data getEdge(int src, int dest) {

		if(NodeMap.containsKey(src) && NodeMap.containsKey(dest)){

				node_data temp_node = NodeMap.get(src);
				if(EdgeMap.get(temp_node).containsKey(dest)) {
				edge_data temp_edge = EdgeMap.get(temp_node).get(dest);
				return temp_edge;
			}
		}
		return null;
	}
	
	
	/**
	 * adds the Node n to the Graph , we first checks if its not exist in the graph already and if so 
	 * we add it to the graph while we updating MC and NodeSize fields.
	 * @param n - The node that will be added to the HashMap.
	 * @param dest - the destination Node's key.
	 *  
	 */
	public void addNode(node_data n) {

		if (!this.NodeMap.containsKey(n.getKey()) ){
			this.NodeMap.put(n.getKey(), n);
			this.EdgeMap.put(n,new HashMap<Integer,edge_data>());
			nodeSize++;
			MC++;
		}
		else {
			throw new RuntimeException("A vertex already exists in that key name");
		}
	}

	
	/**
	 * This method checks if the given source node is exist in the graph and if so it creates a temp node that 
	 * will represent dest input and creating an edge between the two of them.
	 *	@param src - the source Node's key.
	 *  @param dest - the destination Node's key.
	 *  @param w - the weight of the edge between the two nodes.
	 */
	public void connect(int src, int dest, double w) {

		if (this.NodeMap.containsKey(src) && this.NodeMap.containsKey(dest)) {
			node_data temp_node = NodeMap.get(src);
			EdgeMap.get(temp_node).put(dest, new Edge(src,dest,w));
			edgeSize++;
			MC++;
		}
		else{
			throw new RuntimeException("src/dest is not exist in the graph");
		}
	}

	
	
	/**
	 * return a collection of Nodes that contains 
	 * all nodes inside the graph we do so simply with the .values java's command on the HashMap.
	 */
	public Collection<node_data> getV() {

		if(!this.NodeMap.isEmpty())return NodeMap.values();
		return null;
	}
	
	
	/**
	 * return a collection of the input node Edge's.
	 * @param node_id - represents the key of the Nodes.
	 */
	public Collection<edge_data> getE(int node_id) {

		if(NodeMap.containsKey(node_id)){
			node_data temp_node = NodeMap.get(node_id);

			if(EdgeMap.containsKey(temp_node)){
				return EdgeMap.get(temp_node).values(); 
			}
		}
		return null;
	}
	
	
	/**
	 * removing the given Node from the graph,
	 * also disconnect all his edges and the edges that he is them's destination , also update NodeSize
	 * @param key - the Node to be deleted key.
	 */
	public node_data removeNode(int key) {

		if (NodeMap.containsKey(key)) {
			node_data temp_node = NodeMap.get(key);
			Set<node_data> node_set = EdgeMap.keySet();
			for (node_data node_data : node_set) {
				if (EdgeMap.get(node_data).remove(key)!=null) {
					edgeSize--;
				}
			}
			edgeSize = (edgeSize) - (EdgeMap.get(temp_node).size());
			EdgeMap.remove(temp_node);
			MC++;
			nodeSize--;
			return NodeMap.remove(key);
		}
		return null;
	}
public void init(String file) {
		
		try
		{
			JSONObject objects = new JSONObject(file);
			JSONArray Edges = objects.getJSONArray("Edges");
			JSONArray Nodes = objects.getJSONArray("Nodes");
			
			for(int i = 0; i < Nodes.length(); i++)
			{
				String pos = Nodes.getJSONObject(i).getString("pos");
				String str[] = pos.split(",");
				int id = Nodes.getJSONObject(i).getInt("id");
				Point3D p = new Point3D(Double.parseDouble(str[0]),Double.parseDouble(str[1]),0.0);
				node_data n = new Node(id,0.0,p); 
				this.addNode(n);
			} 
			
			for(int i = 0; i < Edges.length(); i++)
			{
				int src = Edges.getJSONObject(i).getInt("src");
				int dst = Edges.getJSONObject(i).getInt("dest");
				double w = Edges.getJSONObject(i).getDouble("w");
				this.connect(src, dst, w);
			} 
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * removing the edge between the two given nodes.
	 * @param src - the source node.
	 * @param dest - the destination node.
	 */
	public edge_data removeEdge(int src, int dest) {

		if(NodeMap.containsKey(src) && NodeMap.containsKey(dest)){

			node_data temp_node = NodeMap.get(src);
			if (EdgeMap.get(temp_node).containsKey(dest)) {

				edgeSize--;
				MC++;
				return EdgeMap.get(temp_node).remove(dest);
			}
		}
		return null;
	}
	
	
	/**
	 * returns the number of Nodes in the graph.
	 */
	public int nodeSize() {
		return this.nodeSize;
	}
	
	
	/**
	 *  returns the number of edges in the graph.
	 */
	public int edgeSize() {
		return this.edgeSize;
	}
	
	
	/**
	 * returns the MC number of the graph.
	 */
	public int getMC() {
		return this.MC;
	}
	
	
	public void addListener(GraphRefresher listener) {
		this.listener = listener;
	}
	
	public void updateListener() {
		if(listener!= null)
			listener.graphUpdate();
	}

}
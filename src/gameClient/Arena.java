//package gameClient;
//
//import Server.game_service;
//import dataStructure.DGraph;
//import dataStructure.Robot;
//import dataStructure.node_data;
//import utils.Point3D;
//import dataStructure.Fruit;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//public class Arena {
//
//	
//    private ArrayList<Robot> robots=new ArrayList<Robot>();
//    private ArrayList<Fruit> fruits=new ArrayList<Fruit>();
//    private DGraph DG;
//    private game_service game;
//    private static double min_x = 35.18725458757062;
//	private static double max_x = 35.21315127845036;
//	private static double min_y = 32.09920263529412;
//	private static double max_y = 32.10943409579832;
//
//    
//    public Arena(game_service game){
//        DG=new DGraph();
//        DG.init(game.getGraph());
//        Json2Fruits(game.getFruits());
//        this.game=game;
//    }
//    
//    
//    
//        private void Json2Fruits(List<String> fr) {
//            
//          
//            	try {
//        			List<String> tempFruits = new ArrayList<String>(); 
//        			tempFruits = game.getFruits();
//        			for(String s: tempFruits){
//
//        				Fruit f = new Fruit();
//        				f.initFruit(s);
//        				SetFruitData(f);
//        				fruits.add(f);
//        			}
//        		}
//        		catch (Exception e) {e.printStackTrace();}
//            }
//        
//        
//        
//        
//        
//        
//        private void SetFruitData (Fruit f) {
//
//    		int fSrc = FindClosestSrcToFruit (f.getPos());
//    		int fDest = AutoFindClosestDest(fSrc, f.getPos());
//
//    		if (f.getType()==1) {
//
//    			if (fSrc>fDest) {
//    				f.setSrc(fDest);
//    				f.setDest(fSrc);
//    			}
//    			else {
//    				f.setSrc(fSrc);
//    				f.setDest(fDest);
//    			}
//    			f.setEdge(DG.getEdge(f.getSrc(), f.getDest()));
//    		}
//    	}
//        
//        
//        
//        public int FindClosestSrcToFruit (Point3D p) {
//
//    		int ans=0;
//    		int i=0;
//    		double shortestDis=0;
//
//    		Point3D Fpoint = new Point3D(scale(p.x(), min_x, max_x , 50 , this.getWidth()-50),scale(p.y(),max_y, min_y , 70 , this.getHeight()-70));
//    		Collection<node_data> Nodes = DG.getV();
//
//    		for (node_data tempNode : Nodes ) {
//
//    			double x = tempNode.getLocation().x();
//    			double y = tempNode.getLocation().y();
//    			Point3D Npoint = new Point3D(scale(x, min_x, max_x , 50 , this.getWidth()-50),scale(y,max_y, min_y , 70 , this.getHeight()-70));
//    			// Fpoint is the converting of x and y from point to frames.
//
//    			if (i==0) {
//    				shortestDis=dist(Npoint, Fpoint);
//    				i++;
//    			}
//
//    			if (dist(Npoint, Fpoint)<=shortestDis) {
//
//    				//System.out.println(tempNode.getKey());
//    				shortestDis=dist(Npoint, Fpoint);
//    				ans = tempNode.getKey();
//    			}
//    		}
//    		return ans;
//    	} 
//        
//        private double dist(Point3D p, Point3D p2) {
//    		double ans = Math.sqrt(Math.pow((p.x()-p2.x()),2)+(Math.pow((p.y()-p2.y()),2)));
//    		return ans;
//    	}
//        
//
//    	public int AutoFindClosestDest(int src , Point3D p) {
//
//    		int des = FindClosestSrcToFruit(p);
//    		if(des == src) {
//    			for(Fruit lol: fruits) {
//    				if(lol.getPos() == p) {
//    					des = lol.getSrc();
//    				}
//    			}	
//    		}
//    		int ans = -1 ;
//    		List<node_data> list = GA.shortestPath(src,des );
//    		if( list.size()>1) ans = list.get(1).getKey();
//    		else {
//    			ans = list.get(0).getKey();
//    		}
//    		return ans;
//    	}
//
//        
//        
//        
//}

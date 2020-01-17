//package gameClient;
//
//import Server.game_service;
//import dataStructure.DGraph;
//import dataStructure.Robot;
//import dataStructure.Fruit;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//public class Arena {
//
//	
//    private ArrayList<Robot> robots=new ArrayList<Robot>();
//    private ArrayList<Fruit> fruits=new ArrayList<Fruit>();
//    private DGraph graph;
//    private game_service game;
//
//    
//    public Arena(game_service game){
//        graph=new DGraph();
//        graph.init(game.getGraph());
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
//}

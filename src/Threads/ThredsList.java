package Threads;

import gameClient.MyGameGUI;

public class ThredsList extends Thread{

	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
	
		
		
		MyGameGUI GThread = new MyGameGUI();
		Thread G = new Thread (GThread);
		
		RunGameT PlaySolo = new RunGameT();
		PlaySolo.game=GThread.game;
		Thread t = new Thread(PlaySolo);
		G.setPriority(MAX_PRIORITY);
		G.run();
		t.run();
		
		
		
//		if( GThread.game!=null){
//			System.out.println("Game is not Null game should run");
//			RunGameT PlaySolo = new RunGameT();
//			PlaySolo.game=GThread.game;
//			Thread t = new Thread(PlaySolo);
//			t.run();
//		}
		
		
		
		
	
		
		
		
		

	}

}

package Threads;

import gameClient.MyGameGUI;

public class ThredsList implements Runnable{

	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
	
		
		
		MyGameGUI GThread = new MyGameGUI();
		Thread G = new Thread (GThread);
	//	MyGameGUI Test =  new MyGameGUI();
	//	Test.setVisible(true);
		//RunGameT PlaySolo = new RunGameT();
	//	PlaySolo.game=GThread.game;
		//Thread t = new Thread(PlaySolo);
		//G.setPriority(MAX_PRIORITY);
		G.start();
	//	t.start();
		
		
		
//		if( GThread.game!=null){
//			System.out.println("Game is not Null game should run");
//			RunGameT PlaySolo = new RunGameT();
//			PlaySolo.game=GThread.game;
//			Thread t = new Thread(PlaySolo);
//			t.run();
//		}
		
		
		
		
	
		
		
		
		

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}

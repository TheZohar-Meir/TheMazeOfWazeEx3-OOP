package Threads;
import java.util.List;

import Server.game_service;
import dataStructure.Robot;
import gameClient.MyGameGUI;
public class RunGameT implements Runnable{


	game_service game;
	//	public static int src = -1;
	//	public static int dest = -1;
	//	public  static boolean waiting ;
	//	public static boolean click2 ;
	//	public static boolean isAlreadyOneClick;
	//	public static Robot currentRobot = new Robot();



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {

		if(game!=null) {
			game.startGame();

			while(game.isRunning()) {
				System.out.println("I'm running");

				

			}
		}
		//		
		//		try {
		//			this.wait();
		//			System.out.println("Lets Wait!!!!!!!!!!!!!!!!!!!!!!!");
		//			StartCustom(game);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}


	}


	public void SetGame (game_service g) {

		this.game = g;



	}


	//	public static void StartCustom(game_service game)
	//	{
	//		game.startGame();
	//		//ThreadPaint(game);
	//		//ThreadMouse(game);
	//		while(game.isRunning()) {
	//			//initGUI();
	//			System.out.println("Game Is Running");
	//			ManualPlay(game);
	//		}
	//		String results = game.toString();
	//		System.out.println("Game Over: "+results);
	//
	//
	//	}



	//	public static void ManualPlay(game_service game){
	//
	//		List<String> log = game.move();
	//		int destMove=-1;
	//		if(log != null)
	//		{
	//			
	//		
	//			if(currentRobot.getDest() != null) {
	//				 destMove = currentRobot.getDest().getKey();
	//			}
	//			if(destMove!= -1) {
	//
	//				if(currentRobot!= null) {
	//					game.chooseNextEdge(currentRobot.getSrc().getKey(), destMove);
	//					game.move();
	//					//repaint();
	//				}
	//			}
	//		}
	//	}

}

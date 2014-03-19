package server;

import common.*;

import java.io.IOException;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Observable;
import java.util.Observer;

/**
 * Displays a graphical view of the game of pong
 */
class S_PongView implements Observer
{ 
	private S_PongController pongController;
	private GameObject   ball;
	private GameObject[] bats;
	private NetObjectWriter left, right;
	private int gameNo;
	private Server server;
	private int count;
	private long mills;

	public S_PongView( NetObjectWriter c1, NetObjectWriter c2, int gameNum, Server server )
	{
		count = 9;

		this.server = server;
		gameNo = gameNum;
		left = c1; right = c2;

		left.put(gameNum);
		right.put(gameNum);
	}

	/**
	 * Called from the model when its state is changed
	 * @param aPongModel Model of game
	 * @param arg Arguments - not used
	 */
	public void update( Observable aPongModel, Object arg )
	{
		count++;

		S_PongModel model = (S_PongModel) aPongModel;
		ball  = model.getBall();
		bats  = model.getBats();

		// Now need to send position of game objects to the client
		//  as the model on the server has changed

		String multiCastSend = "Game "+gameNo + ","+
				ball.getX()+","+ball.getY()+","+
				bats[0].getX()+","+bats[0].getY()+","+
				bats[1].getX()+","+bats[1].getY() +',';

		//if(count % 10 == 0)
		//{
			mills = new Date().getTime();//Get time the server sends a message
			model.setTimeMessageSent(mills);//Save it in model
		//}
		
		String p0Send = multiCastSend + model.getPing(0) + "," + mills;//Add the ping to the message
		String p1Send = multiCastSend + model.getPing(1) + "," + mills;//And send mills to client
		
		
		/*
		p0Ping = model.getTimeMessageSent() - model.getP0Time();
		String s = "P0 " + model.getTimeMessageSent() + " - " + model.getP0Time()  + " = " + (model.getTimeMessageSent() - model.getP0Time() );
		System.out.println(s);

		p1Ping = model.getTimeMessageSent() - model.getP1Time();
		s = "P1 " + model.getTimeMessageSent() + " - " + model.getP1Time()  + " = " + (model.getTimeMessageSent() - model.getP1Time() );
		System.out.println(s);
		
		String p0Send = multiCastSend+p0Ping;
		String p1Send = multiCastSend+p1Ping;

		System.out.println(p0Send);
		*/
		
		left.put( p0Send );
		right.put( p1Send );

	}


}

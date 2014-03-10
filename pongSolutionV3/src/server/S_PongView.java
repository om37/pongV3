package server;

import common.*;

import java.io.IOException;
import java.util.Date;
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
	private NetMCWriter mutlicastOut;
	private int gameNo;
	private Server server;


	public S_PongView( NetObjectWriter c1, NetObjectWriter c2, int gameNum, Server server )
	{
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
		S_PongModel model = (S_PongModel) aPongModel;
		ball  = model.getBall();
		bats  = model.getBats();
		
		long messageSent = new Date().getTime();
		model.setTimeMessageSent(messageSent);
		
		// Now need to send position of game objects to the client
		//  as the model on the server has changed

		String multiCastSend = "Game "+gameNo + ","+
						ball.getX()+","+ball.getY()+","+
						bats[0].getX()+","+bats[0].getY()+","+
						bats[1].getX()+","+bats[1].getY() +',';
		
		long p0Ping = model.getTimeMessageSent() - model.getP0Time();
		long p1Ping = model.getTimeMessageSent() - model.getP1Time();
		
		String p0Send = multiCastSend+p0Ping;
		String p1Send = multiCastSend+p1Ping;

		left.put( p0Send );
		right.put( p1Send );

	}


}

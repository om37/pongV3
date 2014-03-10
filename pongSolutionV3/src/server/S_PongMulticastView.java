package server;

import common.*;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Displays a graphical view of the game of pong
 */
class S_PongMulticastView implements Observer
{ 
	private GameObject   ball;
	private GameObject[] bats;
	private int gameNo;


	public S_PongMulticastView(int gameNum)
	{
		gameNo = gameNum;
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

		// Now need to send position of game objects to the client
		//  as the model on the server has changed

		String multiCastSend =
				"Game "+gameNo + ","+
						ball.getX()+","+ball.getY()+","+
						bats[0].getX()+","+bats[0].getY()+","+
						bats[1].getX()+","+bats[1].getY() +',';

		Global.MULTICAST_OUT.put(multiCastSend);
		//System.out.println(multiCastSend);

	}


}

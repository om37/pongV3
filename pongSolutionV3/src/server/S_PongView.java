package server;

import common.*;

import java.io.IOException;
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
	String num = String.valueOf(gameNo);
    left = c1; right = c2;
    try 
    {
		mutlicastOut = new NetMCWriter(Global.P_SERVER_WRITE, Global.MCA);		
		mutlicastOut.put(num);
	}
    catch (IOException e) 
    {
    	DEBUG.error("MC Out.put error: %s", e.getMessage());
		e.printStackTrace();
	}
    left.put(num);
	right.put(num);
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
   
    String games = server.getGameListAsString();
    String multiCastSend =
    		games+","+ "Game "+gameNo + ","+
    		ball.getX()+","+ball.getY()+","+
    		bats[0].getX()+","+bats[0].getY()+","+
    		bats[1].getX()+","+bats[1].getY() +',';
    
    String p0Send = multiCastSend+model.getP0Time();
    
    String p1Send = multiCastSend+model.getP1Time();
    
    left.put( p0Send );
    
    right.put( p1Send );
    
    try
    {
		mutlicastOut.put(multiCastSend);
	}
    catch (IOException e)
    {
		e.printStackTrace();
	}
  }

  
}

package server;

import common.*;
import static common.Global.*;

import java.util.Observable;

/**
 * Model of the game of pong
 *  The active object ActiveModel does the work of moving the ball
 */
public class S_PongModel extends Observable
{
  private GameObject ball   = new GameObject( W/2, H/2, BALL_SIZE, BALL_SIZE );
  private GameObject bats[] = new GameObject[2];
  private long 		 p0TimeReceived = 0;
  private long 		 p1TimeReceived = 0;
  private long		 timeMessageSent;
  
  private Thread activeModel;

  public S_PongModel(Server server, int gameNum)
  {
    bats[0] = new GameObject(  60, H/2, BAT_WIDTH, BAT_HEIGHT);
    bats[1] = new GameObject(W-60, H/2, BAT_WIDTH, BAT_HEIGHT);
    activeModel = new Thread( new S_ActiveModel( this, server, gameNum ) );
  }
  
  public void setTimeMessageSent(long time)
  {
	  timeMessageSent = time;
  }
  
  public long getTimeMessageSent()
  {
	  return timeMessageSent;
  }
  
  /**
   * Start the thread that moves the ball and detects collisions
   */
  public void makeActiveObject()
  {
    activeModel.start();
  }
  
  /**
   * Return the last time a message was received from p0
   * @return the (unix) time a message was last received
   */
  public long getP0Time()
  {
	  return p0TimeReceived;
  }
  
  /**
   * Set the time a message was received from p0
   * @param the (unix) time a message was received
   */
  public void setP0Time(long time)
  {
	  p0TimeReceived = time;
  }
  
  /**
   * Return the last time a message was received from p1
   * @return the (unix) time a message was last received
   */
  public long getP1Time()
  {
	  return p1TimeReceived;
  }
  
  /**
   * Set the time a message was received from p1
   * @param the (unix) time a message was received
   */
  public void setP1Time(long time)
  {
	  p1TimeReceived = time;
  }

  /**
   * Return the Game object representing the ball
   * @return the ball
   */
  public GameObject getBall()
  {
    return ball;
  }
  
  /**
   * Set a new Ball object
   * @param aBall - Ball to be set
   */
  public void setBall( GameObject aBall )
  {
    ball = aBall;
  }

  /**
   * Return the Game object representing the Bat for player
   * @param player 0 or 1
   */
  public GameObject getBat(int player )
  {
    return bats[player];
  }
  
  /**
   * Return the Game object representing the Bats
   * @return Array of two bats
   */
  public GameObject[] getBats()
  {
    return bats;
  }

  /**
   * Set the Bat for a player
   * @param player  0 or 1
   * @param theBat  Players Bat
   */
  public void setBat( int player, GameObject theBat )
  {
    bats[player] = theBat;
  }

  /**
   * Cause update of view of game
   */
  public void modelChanged()
  {
    DEBUG.trace( "S_PongModel.modelChanged");
    setChanged(); notifyObservers();
  }
  
}

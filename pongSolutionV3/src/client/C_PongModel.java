package client;

import java.util.Observable;

import common.DEBUG;
import common.GameObject;
import static common.Global.*;

/**
 * Model of the game of pong (Client)
 */
public class C_PongModel extends Observable
{
	private GameObject ball   = new GameObject( W/2, H/2, BALL_SIZE, BALL_SIZE );
	private GameObject bats[] = new GameObject[2];
	private C_PongPlayer player;
	private long sendTime = 0;
	private long recTime = 0;
	private boolean sendChanged = false;

	public C_PongModel()
	{
		bats[0] = new GameObject(  60, H/2, BAT_WIDTH, BAT_HEIGHT);
		bats[1] = new GameObject(W-60, H/2, BAT_WIDTH, BAT_HEIGHT);
		ball = new GameObject( W/2, H/2, BALL_SIZE, BALL_SIZE );
	}

	/**
	 * Adds a player to the model
	 * @param newPlayer the player to be set
	 */  
	public void addPlayer(C_PongPlayer newPlayer)
	{
		player = newPlayer;
	}

	/**
	 * Sets the time of the last message sent to server
	 * @param time the time the last message was sent.
	 */
	public void setSendTime(long time)
	{
		sendTime = time;
	}

	/**
	 * Gets the time of the last message sent to server
	 * @return the UNIX time the last message was sent to the server
	 */
	public long getSendTime()
	{
		return sendTime;
	}

	/**
	 * Sets the time of the last message received from server
	 * @param time the UNIX time the last message was received
	 */
	public void setRecTime(long time)
	{
		recTime = time;
	}

	/**
	 * Gets the time of the last message received from server
	 * @return the UNIX time the last message was received from server
	 */
	public long getRecTime()
	{
		return recTime;
	}

	/**
	 * Sets whether or not the time has changed
	 * @param changed whether or not the time has changed since the last message
	 */
	public void setChanged(boolean changed)
	{
		sendChanged = changed;
	}

	/**
	 * Gets whether or not the time has changed since the last message
	 * @return 
	 */
	public boolean getChanged()
	{
		return sendChanged;
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
	 * Sends a message to the player object indicating a bat move
	 * @param direction the direction (UP or DOWN) to move the bat
	 */
	public void moveBat(String direction)
	{
		player.moveBat(direction);
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
	 * Return the Game object representing the Bats for player
	 * @return Array of two bats
	 */
	public GameObject[] getBats()
	{
		return bats;
	}

	/**
	 * Set the Bats used
	 * @param theBats - Players Bat
	 */
	public void setBats( GameObject[] theBats )
	{
		bats = theBats;
	}

	/**
	 * Cause update of view of game
	 */
	public void modelChanged()
	{
		setChanged(); notifyObservers();
	}

}

package server;

import common.*;
import static common.Global.*;

import java.util.Date;
import java.util.Observable;

/**
 * Model of the game of pong
 *  The active object ActiveModel does the work of moving the ball
 */
public class S_PongModel extends Observable
{
	private GameObject ball   = new GameObject( W/2, H/2, BALL_SIZE, BALL_SIZE );
	private GameObject bats[] = new GameObject[2];
	
	private long		 timeMessageSent;//Message sent out from server
	
	private long 		 p0CurrMessageTime = 0;//Message back from p0
	private long 		 p1CurrMessageTime = 0;//Message back from p1
	
	private long[]		 pings 			   = new long[2];

	private Thread activeModel;

	public S_PongModel(Server server, int gameNum)
	{
		bats[0] = new GameObject(  60, H/2, BAT_WIDTH, BAT_HEIGHT);
		bats[1] = new GameObject(W-60, H/2, BAT_WIDTH, BAT_HEIGHT);
		activeModel = new Thread( new S_ActiveModel( this, server, gameNum ) );
	}
	
	public void setPlayerTime(int player)//Called from S_Player when the timestamp of a client's message matches the one we have saved
	{
		if(player == 0)
		{
			p0CurrMessageTime = new Date().getTime();//Set the time
			pings[0] = p0CurrMessageTime - timeMessageSent;//Ping = timeWeReceived it back - time we sent it
			/*
			System.out.println("**********************");
			System.out.println(p0CurrMessageTime);
			System.out.println(timeMessageSent);
			System.out.println(pings[0]);
			System.out.println("**********************");
			*/
			
			/*
			 * NOTES! 
			 * Currently, local games always seem to have 0 ping. Perhaps because it's a local game
			 * if a if(count % 10 == 0) check is put into server view, we can see the ping increase
			 * owing to the thread.sleep() in active model.
			 * 
			 * Need to test on networked machines
			 */
		}
		else if(player == 1)
		{
			p1CurrMessageTime = new Date().getTime();
			pings[1] = p1CurrMessageTime - timeMessageSent;
		}
	}
	
	public long getPing(int player)
	{
		return pings[player];
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
		return p0CurrMessageTime;
	}

	/**
	 * Return the last time a message was received from p1
	 * @return the (unix) time a message was last received
	 */
	public long getP1Time()
	{
		return p1CurrMessageTime;
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

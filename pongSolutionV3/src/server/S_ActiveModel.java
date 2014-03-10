package server;

import static common.Global.*;
import common.*;

/**
 * A class used by the server model to give it an active part
 *  which continuously moves the ball and decides what to
 *  do on a collision
 */
class S_ActiveModel implements Runnable
{
	private S_PongModel pongModel;
	private int count = 0;
	private Server server;
	private int gameNum;
	public boolean isRunning;

	public S_ActiveModel( S_PongModel aPongModel, Server server, int gameNum )
	{
		pongModel = aPongModel;
		this.gameNum = gameNum;
		System.out.println("Active model game num " + gameNum);
		this.server = server;
	}

	/**
	 * returns the game number
	 * @return int - the game number
	 */
	public int getGameNum()
	{
		//System.out.println(gameNum);
		return gameNum;
	}

	/**
	 * Code to position the ball after time interval
	 * runs as a separate thread
	 */
	public void run()
	{
		isRunning = true;
		server.addActiveGameToList(this);

		final double S = 1;           // Units to move
		try
		{
			GameObject ball    = pongModel.getBall();
			GameObject bats[]  = pongModel.getBats();

			while ( true )
			{
				if(Thread.currentThread().isInterrupted() || !isRunning)
					return;

				DEBUG.trace("Active model loop started");
				double x = ball.getX(); double y = ball.getY();
				// Deal with possible edge of board hit
				if ( x >= W-B-BALL_SIZE ) ball.changeDirectionX();
				if ( x <= 0+B           ) ball.changeDirectionX();
				if ( y >= H-B-BALL_SIZE ) ball.changeDirectionY();
				if ( y <= 0+M           ) ball.changeDirectionY();

				DEBUG.trace("Ball moving...");
				ball.moveX( S );  ball.moveY( S );

				// As only a hit on the bat is detected it is assumed to be
				// on the front or back of the bat
				// A hit on the top or bottom has an interesting affect

				if ( bats[0].collision( ball ) == GameObject.Collision.HIT  ||
						bats[1].collision( ball ) == GameObject.Collision.HIT )
				{
					ball.changeDirectionX();
				}
				
				System.out.println(count);
				count++;

				pongModel.modelChanged();      // Model changed refresh screen

				Thread.sleep( 20 );            // About 50 Hz
			}
		} catch ( Exception e ) {};
	}

}


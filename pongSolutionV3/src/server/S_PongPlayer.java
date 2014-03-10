package server;

import static common.Global.BAT_MOVE;

import java.net.Socket;
import java.util.ArrayList;

import common.DEBUG;
import common.GameObject;
import common.NetObjectReader;
import common.NetObjectWriter;

/**
 * Individual player run as a separate thread to allow
 * updates to the model when a player moves their bat
 */
class S_PongPlayer extends Thread
{
	private S_PongModel		pModel;//Player's model
	private NetObjectWriter pWriter;//Player's writer
	private NetObjectReader pReader;//Player's reader
	private int 			pNumber;//Player's number
	private int				gameNumber;//The number of the player's game
	private Server			server;

	/**
	 * Constructor
	 * @param player Player 0 or 1
	 * @param model Model of the game
	 * @param s Socket used to communicate the players bat move
	 */
	public S_PongPlayer( int player, S_PongModel model, Socket s , int gameNum, Server server )
	{  
		//Setup player and give them the model
		pNumber = player;
		pModel = model;
		gameNumber=gameNum;
		this.server = server;

		DEBUG.trace("PlayerS Constructor start");
		try
		{
			pWriter = new NetObjectWriter(s);//and their writer
			pReader = new NetObjectReader(s);//Initialise their reader		  

			DEBUG.trace("PlayerS Constructor end");
		}
		catch ( Exception e) {
			DEBUG.trace("Player Exception");
			e.printStackTrace();
		}
	}

	/**
	 * getWriter - returns the player's netobjectwriter.
	 * @return NetObjectWriter the player's writer to their socket.
	 */
	public NetObjectWriter getWriter()
	{
		DEBUG.trace("Getting player writer....");
		return pWriter;
	}

	/**
	 * Get and update the model with the latest bat movement
	 */
	public void run()                             // Execution
	{
		boolean running = true;//A flag
		
		//Endless loop, read from client, listen for keyPress message from client's controller
		//Send message to model, calc new position(inside model)
		while(running)
		{
			if(!running)
				return;
			
			String move = (String)pReader.get();//Gets the data from the reader

			//If data was read, determine if player wants to move up or down		  
			if(move != null && !move.equals(""))
			{
				String[] splitMove = move.split(",");
				switch(splitMove[0])
				{
					case "UP" :
						moveBat(-BAT_MOVE);
						setTime(splitMove[1]);
						break;
					case "DOWN" :
						moveBat(BAT_MOVE);
						setTime(splitMove[1]);
						break;
					case "CLOSED" :
						server.removeFromGameList(gameNumber);
						running = false;
						break;
					default :
						setTime(splitMove[1]);
						break;
				}
			}
		}
		if(!running)
			return;
	}

	private void moveBat(double toMove)
	{
		GameObject pBat = pModel.getBat(pNumber);//Create dummy bat (get player's bat from model)
		pBat.moveY(toMove);//Move the bat
		pModel.setBat(pNumber, pBat);//Set the bat again (with new coords)
	}

	private void setTime(String time)
	{
		if(pNumber == 0)
			pModel.setP0Time(Long.parseLong(time));
		else
			pModel.setP1Time(Long.parseLong(time));
	}
}
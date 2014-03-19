package client;

import common.*;
import static common.Global.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * A class representing a pong player on the client-side. Each player is a thread to allow view to be updated
 * immediately when bat is moved. This class handles all client-server communication and is where we set new
 * values in the model before calling model.modelChanged() to update the view.
 * 
 * C_PongPlayer is used to communicate to the server using a socket and standard TCP, for receiving updates using
 * multicast, please see C_PongMulticastPlayer
 * 
 * @author om37
 */
class C_PongPlayer extends Thread
{
	private NetObjectReader reader;
	private NetObjectWriter writer;
	private String 			moveDir;
	private C_PongModel model;

	/**
	 * Empty constructor for subclass	
	 */
	public C_PongPlayer()
	{}

	/**
	 * Constructor - adds this player to the model object and instantiates the reader and writer with
	 * the socket object
	 * @param model - model of the game
	 * @param s - Socket used to communicate with server
	 */
	public C_PongPlayer(C_PongModel aModel, Socket s )
	{
		model=aModel;
		model.addPlayer(this);
		
		moveDir="noMove";
		try 
		{
			reader = new NetObjectReader(s);
			writer = new NetObjectWriter(s);
		}
		catch (Exception e) 
		{
			DEBUG.trace("PlayerC constructor failed" );
		}
	}

	/**
	 * Called on thread.start().
	 * Listens to the socket to get the latest state of the game from the server. Uses this information
	 * to update the model and then redisplay the view with new values.  
	 */
	@Override
	public void run()
	{
		DEBUG.trace("Started player thread....");
		while(true)
		{					
			String data="";
			data = reader.get().toString();
			updateViewWithNewValues(data);
			
			if(moveDir.equals("CLOSED"))
				return;
		}
	}

	/**
	 * Updates the view with values sent from the server. Splits a comma-seperated string and assigns
	 * values in the model based on the contained data.
	 * 
	 * Round-trip time (ping) is calculated by sending a (unix) time-stamp on each iteration and having the server
	 * send it back. When it returns, we take the time again and subtract the first time-stamp from the second. 
	 * 
	 * Provided the data sent from the server is valid, the split array should contain:
	 * Index:Data
	 * 0	: GameNumber
	 * 1	: Ball's x
	 * 2	: Ball's y
	 * 
	 * 3	: Bat 0's x
	 * 4	: Bat 0s' y
	 * 
	 * 5	: Bat 1's x
	 * 6	: Bat 1's y
	 * 
	 * 7	: PING
	 * 8	: Unique message out from server (unix time)
	 * 
	 * @param inData - the string of received from the server
	 */
	protected void updateViewWithNewValues(String inData)
	{
		String data=inData;

		String[] newCoords = data.split(",");

		if(newCoords.length > 1)//Can catch the game-number output. Don't want to use that for updating game.
		{
			double newBallX=Double.parseDouble(newCoords[1]);
			double newBallY=Double.parseDouble(newCoords[2]);

			double batOneX=Double.parseDouble(newCoords[3]);
			double batOneY=Double.parseDouble(newCoords[4]);

			double batTwoX=Double.parseDouble(newCoords[5]);
			double batTwoY=Double.parseDouble(newCoords[6]);

			long time=Long.parseLong(newCoords[7]);
			long serverMessage = Long.parseLong(newCoords[8]);//Don't need to use this, just send it back to server

			//Create dummy ball object with new coords 
			GameObject newBall   = model.getBall();
			newBall.setX(newBallX);
			newBall.setY(newBallY);

			//Same for bats....
			GameObject newBats[] = model.getBats();
			newBats[0].setX(batOneX);
			newBats[0].setY(batOneY);
			
			newBats[1].setX(batTwoX);
			newBats[1].setY(batTwoY);

			//Save updates back to model
			model.setBall(newBall);
			model.setBats(newBats);
			
			//Set time
			model.setPingTime(time);
			
			//Call to update observers
			model.modelChanged();
			
			
			writer.put(moveDir+","+serverMessage);//Send back the timestamp
			moveDir="NoMove";
		}
	}

	/**
	 * Sends a message to the server to move the bat
	 * @param details The direction to move "UP" or "DOWN"
	 */
	public void moveBat(String details)
	{
		moveDir = details;
		if(details.equals("CLOSED"))
			writer.put("CLOSED,123");
	}

}

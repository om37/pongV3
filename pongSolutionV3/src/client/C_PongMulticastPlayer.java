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
 * C_PongMulticastPlayer is used to communicate to the server using multicast. For communication over the socket
 * with TCP, see C_PongPlayer.
 * 
 * C_PongMulticastPlayer is a subclass of C_PongPlayer.
 * 
 * @author om37
 */
class C_PongMulticastPlayer extends C_PongPlayer
{
	private NetObjectReader reader;
	private NetObjectWriter writer;
	private NetMCReader		mcReader;
	private String 			moveDir;
	private C_PongModel 	model;

	/**
	 * Constructor - adds this player to the model object and instantiates the reader and writer with
	 * the socket object
	 * @param model - model of the game
	 * @param s - Socket used to communicate with server
	 */

	public C_PongMulticastPlayer(C_PongModel aModel, Socket s)
	{
		model=aModel;
		model.addPlayer(this);

		try 
		{
			reader = new NetObjectReader(s);
			writer = new NetObjectWriter(s);
			mcReader = new NetMCReader(Global.P_COORD_WRITE,Global.GAME_MCA);
		}
		catch (Exception e) 
		{
			DEBUG.trace("PlayerC constructor failed" );
		}
	}

	/**
	 * Called on thread.start().
	 * First thing we do is to get the identifier (game number) of the game we are playing. The server's outputs this
	 * once when the view for the game is created, so we listen for it before listening for coordinates.
	 * 
	 * Once we have game number assigned, we can listens to the multicast channel to get the latest state of the game.
	 * Analyse returned string to determine whether or not it relates to our game. If so, use this information
	 * to update the model and then redisplay the view with new values.  
	 */
	@Override
	public void run()                             // Execution
	{
		DEBUG.trace("Started player thread....");

		/*
		 * The first thing the server outputs will be a single int with our game number in it.
		 * We listen for this and assign it
		 */
		int gameNum;
		while(true)
		{
			String stringNum = reader.get().toString();
			try
			{
				gameNum = Integer.parseInt(stringNum);
				break;
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		//Now we can listen for updates of the game
		while(true)
		{		
			String data="";
			try
			{
				data = (String) mcReader.get();
				DEBUG.set(true);
				DEBUG.trace( "Client PLayer read %s", data);
			}
			catch (IOException e)
			{
				DEBUG.error("MC In.gett error: %s", e.getMessage());
				System.out.println("FAILED ON GET");
				e.printStackTrace();
			}

			String[] newCoords = data.split(",");

			if(newCoords[0].equals("Game "+gameNum))//Is it the game we're playing?
			{
				updateViewWithNewValues(data);
			}


		}
	}

	@Override
	protected void updateViewWithNewValues(String inData)
	{
		String data=inData;

		String[] newCoords = data.split(",");

		//Decode string...
		//String gameNumber = newCoords[0];					//0: gameNo 
		double newBallX=Double.parseDouble(newCoords[1]);	//1: ballX
		double newBallY=Double.parseDouble(newCoords[2]);	//2: ballY

		double batOneX=Double.parseDouble(newCoords[3]);	//3: bat0X
		double batOneY=Double.parseDouble(newCoords[4]);	//4: bat0Y

		double batTwoX=Double.parseDouble(newCoords[5]);	//5: bat1X
		double batTwoY=Double.parseDouble(newCoords[6]);	//6: bat1Y



		//Create dummy ball object with new coords 
		//GameObject newBall = model.getBall();
		GameObject newBall   = new GameObject( W/2, H/2, BALL_SIZE, BALL_SIZE );
		newBall.setX(newBallX);
		newBall.setY(newBallY);

		//Same for bats....
		//GameObject[] newBats = model.getBats();
		GameObject newBats[] = new GameObject[2];
		newBats[0] = new GameObject(  60, H/2, BAT_WIDTH, BAT_HEIGHT);
		newBats[1] = new GameObject(W-60, H/2, BAT_WIDTH, BAT_HEIGHT);

		newBats[0].setX(batOneX);
		newBats[0].setY(batOneY);

		newBats[1].setX(batTwoX);
		newBats[1].setY(batTwoY);

		//Send to model:
		model.setBall(newBall);
		model.setBats(newBats);

		//Call to update observers
		model.modelChanged();

		writer.put(moveDir + "," + new Date().getTime());
		moveDir = "NoMove";
	}

	public void moveBat(String details)
	{
		moveDir = details;
	}

}

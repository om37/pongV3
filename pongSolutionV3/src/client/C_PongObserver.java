package client;

import common.*;
import static common.Global.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

/**
 * Start the client that will display the game for a player
 */
class C_PongObserver extends Thread
{ 
	C_PongModel       model;
    C_PongView        view;
    C_PongController  cont;
	
	String multiCastAddress;
	NetMCReader in;
	String gameToWatch;
	
  public C_PongObserver(String selectedGame)
  {
	  DEBUG.set( true );
	  DEBUG.trace( "Pong Client" );
	  DEBUG.set( false );
	  
	  gameToWatch = selectedGame;
	  model = new C_PongModel();
	  view  = new C_PongView(false);
	    
	  cont  = new C_PongController( model, view );
      model.addObserver( view );       // Add observer to the model
	  view.setVisible(true);           // Display Screen
	  DEBUG.trace("Finished");	  
	  
	  try 
	  {
		in = new NetMCReader(P_SERVER_WRITE, Global.MCA);
	  } 
	  catch (IOException e) 
	  {
		e.printStackTrace();
	  }
  }

  /**
   * Start the Observer
   */
  
  public void run()
  {
	  while(true)
	  {
	
		String data="";
		try 
		{
			data = (String) in.get();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		DEBUG.trace( "Client PLayer read %s", data);
		//System.out.println(data);
		String[] newCoords = data.split(",");
		
		if(newCoords[1].equals("Game "+gameToWatch))//Is it the game we're watching?
		{
			
			//Decode string...
			/*
			index:	dataItem
			0:		numOfGames - for multicast/observers
			1:		gameNo.
			2:		ballX
			3:		ballY
			4:		bat0X
			5:		bat0Y
			6:		bat1X
			7:		bat1Y
			8:		time
			*/
			
			double newBallX=Double.parseDouble(newCoords[2]);
			double newBallY=Double.parseDouble(newCoords[3]);
			
			double batOneX=Double.parseDouble(newCoords[4]);
			double batOneY=Double.parseDouble(newCoords[5]);
			
			double batTwoX=Double.parseDouble(newCoords[6]);
			double batTwoY=Double.parseDouble(newCoords[7]);
			
			
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
		}
	  }
  }
}

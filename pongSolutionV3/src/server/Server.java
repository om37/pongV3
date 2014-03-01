package server;
import java.io.IOException;
import java.net.*;
import java.util.Date;

import static common.Global.*;
import common.*;

/**
 * Start the game server
 *  The call to makeActiveObject() in the model 
 *  starts the play of the game
 */
public class Server
{
  private NetObjectWriter p0, p1;
  ServerSocket ss;
  
  public static void main( String args[] )
  {
   ( new Server() ).start();
  }

  /**
   * Start the server
   */
  public void start()
  {
    DEBUG.set( true );
    DEBUG.trace("Pong Server");
    DEBUG.set(false);
    //DEBUG.set( false );               // Otherwise lots of debug info
    
    
    try
    {
    	DEBUG.trace("opening server socket...");
		ss = new ServerSocket(Global.PORT);
	} 
    catch (IOException e)
    {
    	e.printStackTrace();
    }  
    
    while(true)
    {
    	System.out.println("Starting loop. Game num: " + numOfGames);
    	S_PongModel model = new S_PongModel();
	    makeContactWithClients( model );
	    
	    S_PongView  view  = new S_PongView(p0, p1, numOfGames );
	
	    model.addObserver( view );       // Add observer to the model
	    
	    model.makeActiveObject();        // Start play
		numOfGames++;
		System.out.println("Ending loop. Game num now set to: " + numOfGames);
    }
  }
  
  /**
   * Make contact with the clients who wish to play
   * Players will need to know about the model
   * @param model  Of the game
   */
  public void makeContactWithClients( S_PongModel model )
  {
	  DEBUG.trace("Starting makeContactWithClients");
	  try
	  {
		  Socket pOneSocket = ss.accept();//Accept first connection/player
		  S_PongPlayer pZero = new S_PongPlayer(0, model, pOneSocket);//Initialise zeroth player (first to connect)
		  p0 = pZero.getWriter();//Initialise the writer (player needs to be initialised first)

		  //Same as above
		  Socket pTwoSocket = ss.accept();
		  S_PongPlayer pOne = new S_PongPlayer(1, model, pTwoSocket);
		  p1 = pOne.getWriter();


		  pZero.start();
		  pOne.start();		  
	  }
	  catch(Exception e){
		  System.out.println("ERROR in SERVER MAKE CONTACT");
	  }
	  DEBUG.trace("Returning from makeContactWithClients");
  }
}

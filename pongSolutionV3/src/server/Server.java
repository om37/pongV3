package server;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
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
  private  ArrayList<Integer> gameList = new ArrayList<Integer>();
  
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
    DEBUG.set( false );               // Otherwise lots of debug info
    
    
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
	    
	    S_PongView  view  = new S_PongView(p0, p1, numOfGames, this );
	
	    model.addObserver( view );       // Add observer to the model
	    
	    model.makeActiveObject();        // Start play
	    gameList.add(numOfGames);
	    System.out.println("added to game list. new size: "+gameList.size());
		numOfGames++;
		System.out.println("Ending loop. Game num now set to: " + numOfGames);
    }
  }
  
  public void removeFromGameList(int gameNum)
  {
	  gameList.remove((Object)gameNum);
  }
  
  /**
   * Returns a string representation of the game list. Separated with "-" symbols for splitting on observer screen
   * @return String the game list
   */
  public String getGameListAsString()
  {
	  String games = "";
	  System.out.println(gameList.size());
	  for(int i=0;i<gameList.size();i++)
	  {
		  games += gameList.get(i)+"-";
	  }
	  System.out.println(games);
	  return games;
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
		  S_PongPlayer pZero = new S_PongPlayer(0, model, pOneSocket, numOfGames, this);//Initialise zeroth player (first to connect)
		  p0 = pZero.getWriter();//Initialise the writer (player needs to be initialised first)

		  //Same as above
		  Socket pTwoSocket = ss.accept();
		  S_PongPlayer pOne = new S_PongPlayer(1, model, pTwoSocket, numOfGames,this);
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

package server;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
  private ServerSocket ss;
  //private  ArrayList<Integer> gameList;
  private  ArrayList<S_ActiveModel> activeGameList;
  private int numOfGames;
  
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
    
    numOfGames = 0;
    activeGameList = new ArrayList<>();
    
    Runnable r = setupRunnable();
    Thread sendGameLists = new Thread(r);
    sendGameLists.start();
    
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
    	S_PongModel model = new S_PongModel(this, numOfGames);
    	
	    makeContactWithClients( model );
	    
	    S_PongView  view  = new S_PongView(p0, p1, numOfGames, this );
	    S_PongMulticastView mView = new S_PongMulticastView(numOfGames, this);
	
	    model.addObserver( view );       // Add observer to the model
	    model.addObserver(mView);		 // Add multicast observer to the model
	    
	    model.makeActiveObject();        // Start play
		numOfGames++;
    }
  }
  
  /**
   * Sets up the runnable which is used to send the list of available games to new starting observers
   * @return Runnable - the runnable to use
   */
  private Runnable setupRunnable()
  {
	 return new Runnable()
	 {			
		@Override
		public void run()
		{
			try
			{
				NetMCWriter writer = new NetMCWriter(Global.P_GAME_LIST, Global.LIST_MCA);
				while(true)
					writer.put(getGameListAsString());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	};
  }
  
  public void addActiveGameToList(S_ActiveModel activeGame)
  {
	  activeGameList.add(activeGame);
  }
  
  /**
   * Removes a game from the server's game list.
   * @param gameNum the game to be removed
   */
  public void removeFromGameList(int gameNum)
  {
	  int indexToRemove = -1;
	  
	  for(S_ActiveModel activeGame : activeGameList)
	  {
		  if(activeGame.getGameNum()==gameNum)
		  {
			  activeGame.isRunning = false;
			  indexToRemove = activeGameList.indexOf(activeGame);
			  break;
		  }
	  }
	  if(indexToRemove > -1)
		  activeGameList.remove(indexToRemove);
  }
   	
  /**
   * Returns a string representation of the game list. Separated with "-" symbols for splitting on observer screen
   * @return String the game list
   */
  public String getGameListAsString()
  {
	  String games = "";
	  if(activeGameList.size() > 0)
	  {
		  for(int i=0;i<activeGameList.size();i++)
		  {
			  games += activeGameList.get(i).getGameNum()+"-";
		  }
		  //System.out.println("Game string: " + games);
		  return games;
	  }
	  else
	  {
		  return games;
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

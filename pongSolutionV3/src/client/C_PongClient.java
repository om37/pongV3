package client;

import common.*;
import static common.Global.*;

import java.net.Socket;

/**
 * Start the client that will display the game for a player
 */
class C_PongClient
{ 
	
	String serverAddress;//IpAddress of server. Set in C_PlayerSplashScreen
	boolean multicast;//Boolean indicating if we are receiving bat and ball positions using Multicast
	
  public C_PongClient(String ip, boolean multi)
  {
	  serverAddress = ip;
	  multicast = multi;
  }

  /**
   * Start the Client
   */
  public void start()
  {
    DEBUG.set( true );
    DEBUG.trace( "Pong Client" );
    DEBUG.set( false );
    C_PongModel       model = new C_PongModel();
    C_PongView        view  = new C_PongView(true);
    C_PongController  cont  = new C_PongController( model, view );
                        
    makeContactWithServer( model, cont );

    model.addObserver( view );       // Add observer to the model
    view.setVisible(true);           // Display Screen
    DEBUG.trace("Finished");
  }

  /**
   * Make contact with the Server who controls the game
   * Players will need to know about the model
   * 
   * @param model Of the game
   * @param cont Controller (MVC) of the Game
   */
  public void makeContactWithServer( C_PongModel model, C_PongController  cont )
  {
    // Also starts the Player task that get the current state
    //  of the game from the server
	  try
	  {
		  DEBUG.trace("Contacting server....");
		  
		  Socket conn = new Socket(serverAddress, Global.PORT);
		  C_PongPlayer me = new C_PongPlayer(model, conn, multicast);
		  model.addPlayer(me);
		  
		  DEBUG.trace("Starting client player thread...");
		  me.start();
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
  }
}

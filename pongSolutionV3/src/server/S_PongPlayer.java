package server;

import static common.Global.BAT_MOVE;

import java.net.Socket;

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
	
  /**
   * Constructor
   * @param player Player 0 or 1
   * @param model Model of the game
   * @param s Socket used to communicate the players bat move
   */
  public S_PongPlayer( int player, S_PongModel model, Socket s  )
  {  
	  //Setup player and give them the model
	  pNumber = player;
	  pModel = model;
	  
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
	  long sendTime;
	  
	  //Endless loop, read from client, listen for keyPress message from client's controller
	  //Send message to model, calc new position(inside model)
	  while(true)
	  {
		  String move = (String)pReader.get();//Gets the data from the reader

		  //If data was read, determine if player wants to move up or down		  
		  if(move != null && !move.equals(""))
		  {
			  String[] splitMove = move.split(",");//Checks to see if time attached
			  if(splitMove[0].equals("UP"))
			  {
				  GameObject pBat = pModel.getBat(pNumber);//Create dummy bat (get player's bat from model)
				  pBat.moveY(-BAT_MOVE);//Move the bat
				  pModel.setBat(pNumber, pBat);//Set the bat again (with new coords)
				  pModel.modelChanged();//Update observers
				  
				  sendTime = Long.parseLong(splitMove[1]);
				  if(pNumber == 0)
					  pModel.setP0Time(sendTime);
				  else
					  pModel.setP1Time(sendTime);
			  }
			  
			  else if(splitMove[0].equals("DOWN"))
			  {
				  GameObject pBat = pModel.getBat(pNumber);
				  pBat.moveY(BAT_MOVE);
				  pModel.setBat(pNumber, pBat);
				  pModel.modelChanged();//Call model changed before setting the time otherwise we're out of step
				  
				  sendTime = Long.parseLong(splitMove[1]);
				  if(pNumber == 0)
					  pModel.setP0Time(sendTime);
				  else
					  pModel.setP1Time(sendTime);
			  }
		  }
	  }
  }
}
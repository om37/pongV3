package client;

import common.*;
import static common.Global.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
/**
 * Individual player run as a separate thread to allow
 * updates immediately the bat is moved
 */
class C_PongPlayer extends Thread
{
	private NetObjectReader reader;
	private NetObjectWriter writer;
	private NetMCReader		mcReader;
	private boolean			multi;
	
	private C_PongModel model;
  /**
   * Constructor
   * @param model - model of the game
   * @param s - Socket used to communicate with server
   */
  public C_PongPlayer(C_PongModel aModel, Socket s, boolean multicast  )
  {
	  multi = multicast;
	  model=aModel;

	  try 
	  {
		reader = new NetObjectReader(s);
		writer = new NetObjectWriter(s);
		mcReader = new NetMCReader(Global.P_SERVER_WRITE,Global.MCA);
	  }
	  catch (Exception e) 
	  {
		  DEBUG.trace("PlayerC constructor failed" );
	  }
  }
  
  /**
   * Get and update the model with the latest bat movement
   * sent by the server
   */
  public void run()                             // Execution
  {
	  DEBUG.trace("Started player thread....");
	  int gameNum;
	  while(true)
	  {
		  String stringNum = (String)reader.get();
		  try
		  {
			  gameNum = Integer.parseInt(stringNum);
			  break;
		  }
		  catch(NumberFormatException e)
		  {}
	  }
	  
    // Listen to network to get the latest state of the
    //  game from the server
    // Update model with this information, Redisplay model
	  if(!multi)
	  {
		  while(true)
		  {		
			updateViewWithNewValues((String) reader.get());
		  }
	  }
	  else
	  {
		  System.out.println(gameNum);
		  while(true)
		  {		
			String data="";
			try
			{
				data = (String) mcReader.get();
				DEBUG.trace( "Client PLayer read %s", data);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			String[] newCoords = data.split(",");
			if(newCoords.length>1)
			{
				if(newCoords[1].equals("Game "+gameNum))//Is it the game we're watching?
				{
					updateViewWithNewValues(data);
					//Thread.sleep(20);
				}
			}
		  }		  
	  }
  }

	private void updateViewWithNewValues(String inData)
	{
		String data=inData;
		
		String[] newCoords = data.split(",");
		
		//Decode string...
		//0: numOfGames - for multicast/observers - can ignore
        //String gameNumber = newCoords[1];					//1: gameNo 
		double newBallX=Double.parseDouble(newCoords[2]);	//2: ballX
		double newBallY=Double.parseDouble(newCoords[3]);	//3: ballY
		
		double batOneX=Double.parseDouble(newCoords[4]);	//4: bat0X
		double batOneY=Double.parseDouble(newCoords[5]);	//5: bat0Y
		
		double batTwoX=Double.parseDouble(newCoords[6]);	//6: bat1X
		double batTwoY=Double.parseDouble(newCoords[7]);	//7: bat1Y
		
		if(!multi)
		{
			long time=Long.parseLong(newCoords[8]);//8:	time/ping
			
			//Set time in model
			model.setRecTime(new Date().getTime());
			model.setChanged( false);
			if(time != model.getSendTime())
			{
				model.setSendTime(time);
				model.setChanged(true);
			}
		}
		
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
  
  public void moveBat(String details)
  {
	  long date = new Date().getTime();
	  writer.put(details+","+date);
	  model.setChanged(true);
	  //DEBUG.trace("set changed");
  }
  	
}

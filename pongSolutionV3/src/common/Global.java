package common;

/**
 * Major constants used in the game
 */
public class Global
{
  public static final int H = 450;          // Height of window
  public static final int W = 600;          // Width  of window
  
  public static final double B = 6;            // Border offset
  public static final double M = 26;           // Menu offset
  public static final double BALL_SIZE  = 15;  // Ball side
  public static final double BAT_WIDTH  = 10;  // Bat width
  public static final double BAT_HEIGHT = 100; // Bat Height
  
  public static final double BAT_MOVE=5;       // Each move is
  
//  public static int numOfGames = 0; //Number of games being played
  
  // Of course this should not be a constant
  //  but should be user settable
  public static final int    PORT = 50001;       // Port
  public static final String HOST = "localhost"; // M/C Name/IP
  
  
  public static final NetMCWriter MULTICAST_OUT = new NetMCWriter(Global.P_COORD_WRITE, Global.GAME_MCA);
  public static final NetMCWriter GAME_LIST_OUT = new NetMCWriter(Global.P_GAME_LIST, Global.LIST_MCA);
  
  //Multicast Vars
  //Ports
 public static final int P_COORD_WRITE  = 55000;  // Sever Writes on
 public static final int P_GAME_LIST    = 55002;  //Server sends game list on
 
 // MultiCast Address
 public static final String GAME_MCA = "224.0.0.7";
 public static final String LIST_MCA = "224.0.0.8";
  
}


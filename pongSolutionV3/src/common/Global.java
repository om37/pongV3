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
  
  public static int numOfGames = 0; //Number of games being played
  
  // Of course this should not be a constant
  //  but should be user settable
  public static final int    PORT = 50001;       // Port
  public static final String IP	  = "193.62.172.232";//Machine's current IP (change each time)
  public static final String HOST = "localhost"; // M/C Name/IP
  
  public static enum DIRECTIONS {UP, DOWN};
  
  //Multicast Vars
 
  //Ports
 public static final int P_SERVER_WRITE  = 55000;  // Sever Writes on
 public static final int P_SERVER_READ  = 55001;  // Server Reads on
 public static final int P_GAME_LIST    = 55002;  //Server sends game list on
 
 // MultiCast Address
 public static final String MCA = "224.0.0.7";
 public static final String LIST_MCA = "224.0.0.8";
  
}


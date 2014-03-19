package common;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Wrapper for reading an object from a socket
 */

public class NetObjectWriter extends ObjectOutputStream
{
  public NetObjectWriter( Socket s ) throws IOException
  {
      super( s.getOutputStream() );
      System.out.println("NetObjectWriter constructor");
      s.setTcpNoDelay(true);
  }

  // write object to socket returning false on error
  public synchronized boolean put( Object data )
  {
    DEBUG.trace("NetObjectWriter put");
    try
    {
      writeObject( data );                    // Write object
      flush();                                // Flush
      DEBUG.trace("Returning from put");
      return true;                            // Ok
    }
    catch ( IOException err )
    {
      DEBUG.trace("NetObjectWriter.get %s", 
                err.getMessage() );
      return false;                           // Failed write
    }
  }
}


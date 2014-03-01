package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class NetMCWriter
{
  private MulticastSocket socket = null;
  private InetAddress     group  = null;
  private int             port   = 0;

  public NetMCWriter(int aPort, String mca) throws IOException
  {
    port = aPort;
    DEBUG.trace( "NetMCWrite: port [%5d] MCA [%s]", port, mca );
    socket = new MulticastSocket( port );
    group  = InetAddress.getByName( mca );
    socket.setTimeToLive(40); 
  }

  public void close() throws IOException
  {
    socket.leaveGroup(group);
    socket.close();
  }

  public synchronized void put( String message )  throws IOException
  {
    DEBUG.trace("MCWrite: port [%5d] <%s>", port, message );

    byte[] buf = message.getBytes();
    DatagramPacket packet =
      new DatagramPacket(buf, buf.length, group, port);
    socket.send(packet);
  }
}
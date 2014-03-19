package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Multicast reader
 */
public class NetMCReader
{
  private MulticastSocket socket = null;
  private InetAddress     group  = null;
  private int             port   = 0;

  public NetMCReader(int aPort, String mca ) throws IOException
  {
    port   = aPort;
    System.out.printf("MCRead: C port [%s] MCA [%s] \n", port, mca );
    socket = new MulticastSocket( port );
    group  = InetAddress.getByName( mca );
    socket.joinGroup(group);
  }

  public void close() throws IOException
  {
    socket.leaveGroup(group);
    socket.close();
  }


  public synchronized String get() throws IOException
  {
    DEBUG.trace("MCRead: on port [%d]", port );
    byte[] buf = new byte[10240];
    DEBUG.trace("1");
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    DEBUG.trace("2" );
    try{
    socket.receive(packet);
    }
    catch(Exception e)
    {
    	System.out.println(e.getMessage());
    }
    DEBUG.trace("3");

    String m = new String( packet.getData(), 0, packet.getLength() );
    DEBUG.trace("MCRead: Read <%s>", m );
    return m;
  }
}
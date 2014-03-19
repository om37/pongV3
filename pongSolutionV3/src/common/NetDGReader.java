package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Datagram reader
 */
class NetDGReader
{
  private DatagramSocket socket  = null;
  private InetAddress    address = null;
  private int            port;

  public NetDGReader(int aPort) throws IOException
  {
    port   = aPort;
    System.out.printf("NetDGReader port [%5d] \n", port );
    socket = new DatagramSocket(port);
  }

  public void close() throws IOException
  {
   socket.close();
  }

  // Read a datagram packet and convert into a string
  //  Assume each datagram contains a whole string
  public String get()  throws IOException
  {
    DEBUG.trace("NetDGReader.get() Waiting port [%5d]", port );
    byte[] buf = new byte[512];
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);

    return new String( packet.getData(), 0, packet.getLength() );
  }
}
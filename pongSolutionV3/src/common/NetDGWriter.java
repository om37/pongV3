package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class NetDGWriter
{
  private DatagramSocket socket  = null;
  private InetAddress    address = null;
  private String         host    = "";
  private int            port    = 0;

  public NetDGWriter(int aPort, String aHost) throws IOException
  {
    port = aPort;
    host = aHost;
    DEBUG.trace( "NetDGWriter: Port [%5d] Host [%s]", port, host );

    socket  = new DatagramSocket();
    address = InetAddress.getByName(host);
  }

  public void close() throws IOException
  {
   socket.close();
  }

  // Send a string as a datagram
  //  Assumes the string will fit in the datagram
  public void put( String message )  throws IOException
  {
    DEBUG.trace("Writing DG: Port [%5d] <%s>", port, message );
    byte[] buf = message.getBytes();
    // Note port number in packet
    DatagramPacket packet =
      new DatagramPacket(buf, buf.length, address, port);
    socket.send(packet);
  }
}
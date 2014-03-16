package luc.hci.emotext.interfaces;

public interface SocketerInterface
{

	public String sendHttpRequest( String params );

	public int startListening( int port );

	public void stopListening( );

	public void exit( );

	public int getListeningPort( );

}
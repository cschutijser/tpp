

package tpp;

public class Protocol
{
	/**
	 * Port number that is used for TCP-tunnels.
	 * <br><i>Application layer</i>
	 */
	public final static char PORT = 1337;
	
	/**
	 * Chat messages should start with this prefix.
	 * <br><i>Application layer</i>
	 */
	public final static char CHAT_PREFIX = 'C';
	
	/**
	 * File messages should start with this prefix.
	 * <br><i>Application layer</i>
	 */
	public final static char FILE_PREFIX = 'F';
	
	/**
	 * Accept messages should start with this prefix.
	 * <br><i>Application layer</i>
	 */
	public final static char ACCEPT_PREFIX = 'A';
	
	/**
	 * Send messages should start with this prefix.
	 * <br><i>Application layer</i>
	 */
	public final static char SEND_PREFIX = 'S';
	
	/**
	 * Size of the frame packet (in bytes).
	 * <br><i>Transport/Network layer</i>
	 */
	public final int HEADER_SIZE = 20;
	
	/**
	 * Maximum size of the packet payload (in bytes).
	 */
	public final int MAX_PAYLOAD_SIZE = 1024;
	
	/**
	 * Maximum size of the entire packet (in bytes).
	 */
	public final int MAX_FRAME_SIZE = HEADER_SIZE + MAX_PAYLOAD_SIZE;
	
	/**
	 * Time after which an outgoing package is assumed to have been lost (in seconds).
	 */
	public final int TIMEOUT = 60;
}


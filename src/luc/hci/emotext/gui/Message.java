package luc.hci.emotext.gui;

/**
 * Message is a Custom Object to encapsulate message information/fields
 * 
 * @author Adil Soomro, Robert Martinez (customized)
 * 
 */
public class Message
{
	public static int NEUTRAL = 0;
	public static int HAPPY = 1;
	public static int SAD = 2;
	public static int FEAR = 3;
	public static int DISGUST = 4;
	public static int ANGRY = 5;
	public static int SURPRISE = 6;

	/**
	 * The content of the message
	 */
	private String message;
	/**
	 * boolean to determine, who is sender of this message
	 */
	private boolean isMine;
	/**
	 * boolean to determine, whether the message is a status message or not.
	 * it reflects the changes/updates about the sender is writing, have entered
	 * text etc
	 */
	private boolean isStatusMessage;

	/**
	 * Constructor to make a Message object
	 */
	public Message( String message, boolean isMine )
	{
		super( );
		this.message = message;
		this.isMine = isMine;
		this.isStatusMessage = false;
	}

	/**
	 * Constructor to make a status Message object
	 * consider the parameters are swaped from default Message constructor,
	 * not a good approach but have to go with it.
	 */
	public Message( boolean status, String message )
	{
		super( );
		this.message = message;
		this.isMine = false;
		this.isStatusMessage = status;
	}

	public String getMessage( )
	{
		return message;
	}

	public void setMessage( String message )
	{
		this.message = message;
	}

	public boolean isMine( )
	{
		return isMine;
	}

	public void setMine( boolean isMine )
	{
		this.isMine = isMine;
	}

	public boolean isStatusMessage( )
	{
		return isStatusMessage;
	}

	public void setStatusMessage( boolean isStatusMessage )
	{
		this.isStatusMessage = isStatusMessage;
	}

	public int emotion( )
	{
		if ( EmotionEvaluation.happyMessage( message ) )
		{
			return HAPPY;
		}
		else if ( EmotionEvaluation.angryMessage( message ) )
		{
			return ANGRY;
		}
		else if ( EmotionEvaluation.sadMessage( message ) )
		{
			return SAD;
		}
		else if ( EmotionEvaluation.fearMessage( message ) )
		{
			return FEAR;
		}
		else if ( EmotionEvaluation.surpriseMessage( message ) )
		{
			return SURPRISE;
		}
		else if ( EmotionEvaluation.disgustMessage( message ) )
		{
			return DISGUST;
		}
		else
		{
			return NEUTRAL;
		}
	}
}
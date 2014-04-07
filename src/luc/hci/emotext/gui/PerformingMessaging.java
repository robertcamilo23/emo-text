package luc.hci.emotext.gui;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import luc.hci.emotext.R;
import luc.hci.emotext.info_type.InfoOfFriend;
import luc.hci.emotext.info_type.InfoOfMessage;
import luc.hci.emotext.interfaces.Manager;
import luc.hci.emotext.service.MessagingService;
import luc.hci.emotext.toolbox.ControllerOfFriend;
import luc.hci.emotext.toolbox.StorageManipulater;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PerformingMessaging extends Activity
{

	private static final int MESSAGE_CANNOT_BE_SENT = 0;
	public String username;
	private EditText messageText;
	private EditText messageHistoryText;
	private ImageView emotionImage;
	private Button sendMessageButton;
	private Manager imService;
	private InfoOfFriend friend = new InfoOfFriend( );
	private StorageManipulater localstoragehandler;
	private Cursor dbCursor;
	private List< String > happyWords = Arrays.asList( "DELIGHT", "CONTENT", "SMILE", "GLAD", "FREE", "CAREFREE", "HEALTHY", "PROUD", "LOVING", "CONGRATULATE", "REJOICE", "PERFECTLY", "LOL" );
	private List< String > sadWords = Arrays.asList( "SORROW", "HEARTACHE", "SAD", "SADNESS", "DESPAIR", "MISERY", "GRIEF", "GUILT", "AFFLICTION", "CRY", "UNHAPPY", "BLUES" );
	private List< String > angryWords = Arrays.asList( "STEW", "FUME", "FAULT", "BLAME", "HATRED", "GRUDGE", "BACKSTAB", "JERK", "HATE", "SHUT UP", "MAD", "RAGE", "WRATH", "FURY", "HATE", "TEMPER" );
	private List< String > fearWords = Arrays.asList( "FEAR", "SCARED", "PHOBIA", "JITTERS", "CREEPY", "FREAKED", "SCARY", "FRIGHT", "FEELING", "AFRAID" );
	private List< String > surpriseWords = Arrays.asList( "!", "WOW", "SHOCKED", "SURPRISED", "SURPRISE", "UNEXPECTED", "SUDDEN", "PLEASANTLY", "ABACK", "GAPE", "VISIBLY", "OMG" );
	private List< String > disgustWords = Arrays.asList( "DISGUSTING", "GROSS", "VOMIT", "YUCK", "REVULSION", "PUKE", "NASTY", "WRONG", "REVOLTING", "REPEL", "MANGLE", "OVERCOME", "SHEER" );

	private ServiceConnection mConnection = new ServiceConnection( )
	{

		public void onServiceConnected( ComponentName className, IBinder service )
		{
			imService = ( ( MessagingService.IMBinder ) service ).getService( );
		}

		public void onServiceDisconnected( ComponentName className )
		{
			imService = null;
			Toast.makeText( PerformingMessaging.this, R.string.local_service_stopped, Toast.LENGTH_SHORT ).show( );
		}
	};

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		// TODO Auto-generated method stub
		super.onCreate( savedInstanceState );

		setContentView( R.layout.message ); // messaging_screen);
		messageHistoryText = ( EditText ) findViewById( R.id.messageHistory );
		emotionImage = ( ImageView ) findViewById( R.id.emotionImage );
		messageText = ( EditText ) findViewById( R.id.message );
		messageText.requestFocus( );
		sendMessageButton = ( Button ) findViewById( R.id.sendMessageButton );
		Bundle extras = this.getIntent( ).getExtras( );
		
		//ActionBar
		ActionBar actionBar = getActionBar();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		friend.userName = extras.getString( InfoOfFriend.USERNAME );
		friend.ip = extras.getString( InfoOfFriend.IP );
		friend.port = extras.getString( InfoOfFriend.PORT );
		String msg = extras.getString( InfoOfMessage.MESSAGETEXT );

		setTitle( "Messaging with " + friend.userName );

		// EditText friendUserName = (EditText)
		// findViewById(R.id.friendUserName);
		// friendUserName.setText(friend.userName);

		localstoragehandler = new StorageManipulater( this );
		dbCursor = localstoragehandler.get( friend.userName, MessagingService.USERNAME );

		if ( dbCursor.getCount( ) > 0 )
		{
			int noOfScorer = 0;
			dbCursor.moveToFirst( );
			while ( ( !dbCursor.isAfterLast( ) ) && noOfScorer < dbCursor.getCount( ) )
			{
				noOfScorer++;

				this.appendToMessageHistory( dbCursor.getString( 2 ), dbCursor.getString( 3 ), true );
				dbCursor.moveToNext( );
			}
		}
		localstoragehandler.close( );

		if ( msg != null )
		{
			this.appendToMessageHistory( friend.userName, msg, true );
			( ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE ) ).cancel( ( friend.userName + msg ).hashCode( ) );
		}

		sendMessageButton.setOnClickListener( new OnClickListener( )
		{
			CharSequence message;
			Handler handler = new Handler( );

			public void onClick( View arg0 )
			{
				message = messageText.getText( );
				if ( message.length( ) > 0 )
				{
					appendToMessageHistory( imService.getUsername( ), message.toString( ), true );

					localstoragehandler.insert( imService.getUsername( ), friend.userName, message.toString( ) );

					messageText.setText( "" );
					Thread thread = new Thread( )
					{
						public void run( )
						{
							try
							{
								if ( imService.sendMessage( imService.getUsername( ), friend.userName, message.toString( ) ) == null )
								{

									handler.post( new Runnable( )
									{

										public void run( )
										{

											Toast.makeText( getApplicationContext( ), R.string.message_cannot_be_sent, Toast.LENGTH_LONG ).show( );

											// showDialog(MESSAGE_CANNOT_BE_SENT);
										}

									} );
								}
							}
							catch ( UnsupportedEncodingException e )
							{
								Toast.makeText( getApplicationContext( ), R.string.message_cannot_be_sent, Toast.LENGTH_LONG ).show( );

								e.printStackTrace( );
							}
						}
					};
					thread.start( );
				}

			}
		} );

		messageText.setOnKeyListener( new OnKeyListener( )
		{
			public boolean onKey( View v, int keyCode, KeyEvent event )
			{
				if ( keyCode == 66 )
				{
					sendMessageButton.performClick( );
					return true;
				}
				return false;
			}

		} );

	}

	@Override
	protected Dialog onCreateDialog( int id )
	{
		int message = -1;
		switch ( id )
		{
		case MESSAGE_CANNOT_BE_SENT:
			message = R.string.message_cannot_be_sent;
			break;
		}

		if ( message == -1 )
		{
			return null;
		}
		else
		{
			return new AlertDialog.Builder( PerformingMessaging.this ).setMessage( message ).setPositiveButton( R.string.OK, new DialogInterface.OnClickListener( )
			{
				public void onClick( DialogInterface dialog, int whichButton )
				{
					/* User clicked OK so do some stuff */
				}
			} ).create( );
		}
	}

	@Override
	protected void onPause( )
	{
		super.onPause( );
		unregisterReceiver( messageReceiver );
		unbindService( mConnection );

		ControllerOfFriend.setActiveFriend( null );

	}

	@Override
	protected void onResume( )
	{
		super.onResume( );
		bindService( new Intent( PerformingMessaging.this, MessagingService.class ), mConnection, Context.BIND_AUTO_CREATE );

		IntentFilter i = new IntentFilter( );
		i.addAction( MessagingService.TAKE_MESSAGE );

		registerReceiver( messageReceiver, i );

		ControllerOfFriend.setActiveFriend( friend.userName );

	}

	public class MessageReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive( Context context, Intent intent )
		{
			Bundle extra = intent.getExtras( );
			String username = extra.getString( InfoOfMessage.USERID );
			String message = extra.getString( InfoOfMessage.MESSAGETEXT );

			if ( username != null && message != null )
			{
				if ( friend.userName.equals( username ) )
				{
					appendToMessageHistory( username, message, false );
					localstoragehandler.insert( username, imService.getUsername( ), message );

				}
				else
				{
					if ( message.length( ) > 15 )
					{
						message = message.substring( 0, 15 );
					}
					Toast.makeText( PerformingMessaging.this, username + " says '" + message + "'", Toast.LENGTH_SHORT ).show( );
				}
			}
		}

	};

	private MessageReceiver messageReceiver = new MessageReceiver( );

	public void appendToMessageHistory( String username, String message, boolean local )
	{
		if ( username != null && message != null )
		{
			messageHistoryText.append( username + ":\n" );
			messageHistoryText.append( message + "\n" );
			if ( !local )
			{
				setBackgroundEmotionColor( message.toUpperCase( ) );
			}
		}
	}

	private void setBackgroundEmotionColor( String message )
	{
		// HAPPY, YELLOW
		if ( messageHaveEmotion( message, happyWords ) )
		{
			messageHistoryText.setBackgroundColor( Color.YELLOW );
			emotionImage.setImageResource( R.drawable.happiness );
			emotionImage.setBackgroundColor( Color.YELLOW );
		}
		// SURPRISE, CYAN
		else if ( messageHaveEmotion( message, surpriseWords ) )
		{
			messageHistoryText.setBackgroundColor( Color.CYAN );
			emotionImage.setImageResource( R.drawable.surprise );
			emotionImage.setBackgroundColor( Color.CYAN );
		}
		// ANGRY, RED
		else if ( messageHaveEmotion( message, angryWords ) )
		{
			messageHistoryText.setBackgroundColor( Color.RED );
			emotionImage.setImageResource( R.drawable.anger );
			emotionImage.setBackgroundColor( Color.RED );
		}
		// DISGUST, ORANGE
		else if ( messageHaveEmotion( message, disgustWords ) )
		{
			messageHistoryText.setBackgroundColor( Color.rgb( 255, 153, 0 ) );
			emotionImage.setImageResource( R.drawable.disgust );
			emotionImage.setBackgroundColor( Color.rgb( 255, 153, 0 ) );
		}
		// FEAR, GREEN
		else if ( messageHaveEmotion( message, fearWords ) )
		{
			messageHistoryText.setBackgroundColor( Color.GREEN );
			emotionImage.setImageResource( R.drawable.fear );
			emotionImage.setBackgroundColor( Color.GREEN );
		}
		// SAD, BLUE
		else if ( messageHaveEmotion( message, sadWords ) )
		{
			messageHistoryText.setBackgroundColor( Color.BLUE );
			emotionImage.setImageResource( R.drawable.sadness );
			emotionImage.setBackgroundColor( Color.BLUE );
		}
		// NEUTRAL, WHITE - DEFAULT
		else
		{
			messageHistoryText.setBackgroundColor( Color.WHITE );
			emotionImage.setImageResource( 0 );
			emotionImage.setBackgroundColor( Color.WHITE );
		}
	}

	private boolean messageHaveEmotion( String message, List< String > emotionList )
	{
		for ( String emotionWord : emotionList )
		{
			if ( message.contains( emotionWord ) )
			{
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy( )
	{
		super.onDestroy( );
		if ( localstoragehandler != null )
		{
			localstoragehandler.close( );
		}
		if ( dbCursor != null )
		{
			dbCursor.close( );
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent actionBar = new Intent(this, ListOfFriends.class);
	            actionBar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(actionBar);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
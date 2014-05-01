package luc.hci.emotext.gui;

import java.util.List;

import luc.hci.emotext.R;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Adil Soomro modified by Robert Martinez
 * 
 */
public class AwesomeAdapter extends BaseAdapter
{
	private Context mContext;
	private List< Message > mMessages;

	public AwesomeAdapter( Context context, List< Message > messages )
	{
		super( );
		this.mContext = context;
		this.mMessages = messages;
	}

	@Override
	public int getCount( )
	{
		return mMessages.size( );
	}

	@Override
	public Object getItem( int position )
	{
		return mMessages.get( position );
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent )
	{
		Message message = ( Message ) this.getItem( position );

		ViewHolder holder;
		if ( convertView == null )
		{
			holder = new ViewHolder( );
			convertView = LayoutInflater.from( mContext ).inflate( R.layout.sms_row, parent, false );
			holder.message = ( TextView ) convertView.findViewById( R.id.message_text );
			convertView.setTag( holder );
		}
		else
			holder = ( ViewHolder ) convertView.getTag( );

		holder.message.setText( message.getMessage( ) );

		LayoutParams lp = ( LayoutParams ) holder.message.getLayoutParams( );
		// check if it is a status message then remove background, and change
		// text color.

		// Check whether message is mine to show green background and align
		// to right
		if ( message.isMine( ) )
		{
			holder.message.setBackgroundResource( R.drawable.speech_bubble_white );
			lp.gravity = Gravity.RIGHT;
		}
		// If not mine then it is from sender to show orange background and
		// align to left
		else
		{
			// HAPPY, GREEN
			if ( EmotionEvaluation.happyMessage( message.getMessage( ) ) )
			{
				holder.message.setBackgroundResource( R.drawable.speech_bubble_green_friend );
			}
			// SURPRISE, YELLOW
			else if ( EmotionEvaluation.surpriseMessage( message.getMessage( ) ) )
			{
				holder.message.setBackgroundResource( R.drawable.speech_bubble_yellow_friend );
			}
			// ANGRY, RED
			else if ( EmotionEvaluation.angryMessage( message.getMessage( ) ) )
			{
				holder.message.setBackgroundResource( R.drawable.speech_bubble_red_friend );
			}
			// DISGUST, MAGENTA
			else if ( EmotionEvaluation.disgustMessage( message.getMessage( ) ) )
			{
				holder.message.setBackgroundResource( R.drawable.speech_bubble_magenta_friend );
			}
			// FEAR, ORANGE
			else if ( EmotionEvaluation.fearMessage( message.getMessage( ) ) )
			{
				holder.message.setBackgroundResource( R.drawable.speech_bubble_orange_friend );
			}
			// SAD, BLUE
			else if ( EmotionEvaluation.sadMessage( message.getMessage( ) ) )
			{
				holder.message.setBackgroundResource( R.drawable.speech_bubble_blue_friend );
			}
			// NEUTRAL, DEFAULT
			else
			{
				holder.message.setBackgroundResource( R.drawable.speech_bubble_white_friend );
			}
			lp.gravity = Gravity.LEFT;
		}
		holder.message.setLayoutParams( lp );
		holder.message.setTextColor( Color.BLACK );

		return convertView;
	}

	private static class ViewHolder
	{
		TextView message;
	}

	@Override
	public long getItemId( int position )
	{
		// Unimplemented, because we aren't using Sqlite.
		return position;
	}
}
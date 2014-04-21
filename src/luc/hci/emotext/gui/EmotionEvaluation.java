package luc.hci.emotext.gui;

import java.util.Arrays;
import java.util.List;

public class EmotionEvaluation
{
	private static List< String > happyWords = Arrays.asList( "I'm", "DELIGHTED", "CONTENT", "SMILE", "GLAD", "FREE", "CAREFREE", "HEALTHY", "PROUD", "LOVING", "CONGRATULATE", "REJOICE", "PERFECTLY", "LOL", "lol", "jk" );
	private static List< String > sadWords = Arrays.asList( "I'm", "SORROW", "HEARTACHE", "SAD", "SADNESS", "DESPAIR", "MISERY", "GRIEF", "GUILT", "AFFLICTION", "CRY", "UNHAPPY", "BLUES" );
	private static List< String > angryWords = Arrays.asList( "I'm", "FUCK", "STEW", "FUME", "FAULT", "BLAME", "HATRED", "GRUDGE", "BACKSTAB", "JERK", "HATE", "SHUT UP", "MAD", "RAGE", "WRATH", "FURY", "HATE", "TEMPER" );
	private static List< String > fearWords = Arrays.asList( "I'm", "FEAR", "SCARED", "PHOBIA", "JITTERS", "CREEPY", "FREAKED", "SCARY", "FRIGHT", "FEELING", "AFRAID" );
	private static List< String > surpriseWords = Arrays.asList( "I'm", "!", "WOW", "SHOCKED", "SURPRISED", "SURPRISE", "UNEXPECTED", "SUDDEN", "PLEASANTLY", "ABACK", "GAPE", "VISIBLY", "OMG" );
	private static List< String > disgustWords = Arrays.asList( "I'm", "DISGUSTING", "GROSS", "VOMIT", "YUCK", "REVULSION", "PUKE", "NASTY", "WRONG", "REVOLTING", "REPEL", "MANGLE", "OVERCOME", "SHEER" );

	public static boolean happyMessage( String message )
	{
		return messageHaveEmotion( message.toUpperCase( ), happyWords );
	}

	public static boolean sadMessage( String message )
	{
		return messageHaveEmotion( message.toUpperCase( ), sadWords );
	}

	public static boolean angryMessage( String message )
	{
		return messageHaveEmotion( message.toUpperCase( ), angryWords );
	}

	public static boolean fearMessage( String message )
	{
		return messageHaveEmotion( message.toUpperCase( ), fearWords );
	}

	public static boolean surpriseMessage( String message )
	{
		return messageHaveEmotion( message.toUpperCase( ), surpriseWords );
	}

	public static boolean disgustMessage( String message )
	{
		return messageHaveEmotion( message.toUpperCase( ), disgustWords );
	}

	private static boolean messageHaveEmotion( String message, List< String > emotionList )
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
}
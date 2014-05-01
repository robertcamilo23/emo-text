package luc.hci.emotext.gui;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

public class EmotionEvaluation
{

	private static double NEUTRAL = 1000;
	private static double SURPRISE = 1;
	private static double HAPPY = 0.5;
	private static double DISGUST = 0.0;
	private static double ANGRY = ( -0.5 );
	private static double SAD = ( -0.1 );

	private static List< String > happyWords = Arrays.asList( "I'M", "DELIGHTED", "CONTENT", "SMILE", "GLAD", "FREE", "CAREFREE", "HEALTHY", "PROUD", "LOVING", "CONGRATULATE", "REJOICE", "PERFECTLY", "LOL", "LOL", "JK", "HAPPY", "GREAT", "NICE", "COOL", "GOOD JOB", "GOOD", "NEW", "AWARD", "BEST", "HAHA", "HA", "HAHAHA", "FUNNY", "PRIZE", "VACATION", "RELAX", "FUN", "GLAD", "BLESSED", "HHHHH", "LOVE", "I LOVE YOU", "CARE", "PRIDE", "PLEASURE", "FORGIVE", "KISS", "KISSES", "KISED", "HUG", "HUGED", "POSSITIVE", "NAKED" );
	private static List< String > sadWords = Arrays.asList( "I'M", "SORROW", "HEARTACHE", "SAD", "SADNESS", "DESPAIR", "MISERY", "GRIEF", "GUILT", "AFFLICTION", "CRY", "UNHAPPY", "BLUES", "SAD", "SAD MOOD", "BAD", "TASK", "WORST", "WORHTLESS", "I SUCK", "SUCK", "BAD DAY", "WRONG THING", "WRONG", "BAD GUY", "BREAKUP", "DIVORCE", "WORRY", "I MISS YOU", "WITHOUT", "NEGATIVE", "BREATHE", "DEAD", "NOT WORKING", "FAILED", "FAIL", "DROP OUT", "BROKEN", "NEVER" );
	private static List< String > angryWords = Arrays.asList( "I'M", "FUCK", "STEW", "FUME", "FAULT", "BLAME", "HATRED", "GRUDGE", "BACKSTAB", "JERK", "HATE", "SHUT UP", "MAD", "RAGE", "WRATH", "FURY", "HATE", "TEMPER", "BITCH", "PISSED", "MAD", "SUCKS", "TOO MUCH", "ANGRY", "KILL", "MURDER", "END", "FUCKING", "DOES NOT", "SHIT", "JUDGEMENTAL", "OFF", "WRONG", "STUPID", "TRAFFIC", "FELL", "ACCIDENT", "BORING", "HARDEST", "HARD", "ENEMY", "ARROGANT" );
	private static List< String > surpriseWords = Arrays.asList( "I'M", "!", "WOW", "SHOCKED", "SURPRISED", "SURPRISE", "UNEXPECTED", "SUDDEN", "PLEASANTLY", "ABACK", "GAPE", "VISIBLY", "OMG", "QUIT", "AMAZING", "DUDE!", "NICE!", "RIDICULOUS", "AWESOME", "I KNOW", "RAD", "COMPLETELY", "!!!", "!!", "NEWS", "REPORT", "ALERT", "DAMN", "AMBER", "PASS", "NOW" );
	private static List< String > disgustWords = Arrays.asList( "I'M", "DISGUSTING", "GROSS", "VOMIT", "YUCK", "REVULSION", "PUKE", "NASTY", "WRONG", "REVOLTING", "REPEL", "MANGLE", "OVERCOME", "SHEER", "EVERYTHING", "WORK", "HOME", "PARK", "CAR", "BENCH", "SCHOOL", "NOT CONFORTABLE", "SICK", "GOD", "HOT", "COLD", "FAIR", "VERY", "MONEY", "KNOW", "DRIVE", "EXERCISE");

	// private static List< String > fearWords = Arrays.asList( "I'm", "FEAR",
	// "SCARED", "PHOBIA", "JITTERS", "CREEPY", "FREAKED", "SCARY", "FRIGHT",
	// "FEELING", "AFRAID" );

	public static boolean surpriseMessage( String message )
	{
		double scoreEmotion = scoreEmotion( message );
		return ( scoreEmotion != NEUTRAL && scoreEmotion >= SURPRISE );
	}

	public static boolean happyMessage( String message )
	{
		double scoreEmotion = scoreEmotion( message );
		return ( DISGUST < scoreEmotion && scoreEmotion < SURPRISE );
	}

	public static boolean disgustMessage( String message )
	{
		double scoreEmotion = scoreEmotion( message );
		return ( ANGRY < scoreEmotion && scoreEmotion < HAPPY );
	}

	public static boolean angryMessage( String message )
	{
		double scoreEmotion = scoreEmotion( message );
		return ( DISGUST < scoreEmotion && scoreEmotion < SURPRISE );
	}

	public static boolean sadMessage( String message )
	{
		double scoreEmotion = scoreEmotion( message );
		return ( scoreEmotion <= SAD );
	}

	public static boolean fearMessage( String message )
	{
		// In the new model that you've proposed fear is not evaluated.
		return false;
	}

	private static double scoreEmotion( String message )
	{
		int happyScore = messageHaveEmotion( message.toUpperCase( ), happyWords );
		int sadScore = messageHaveEmotion( message.toUpperCase( ), sadWords );
		int angryScore = messageHaveEmotion( message.toUpperCase( ), angryWords );
		int surpriseScore = messageHaveEmotion( message.toUpperCase( ), surpriseWords );
		int disgustScore = messageHaveEmotion( message.toUpperCase( ), disgustWords );
		// int fearScore = messageHaveEmotion(message.toUpperCase( ),fearWords);

		int phraseScore = ( surpriseScore * 2 ) + happyScore + ( disgustScore * -1 ) + ( angryScore * -2 ) + ( sadScore * -3 );
		int totalMatches = happyScore + sadScore + angryScore + surpriseScore + disgustScore;

		double scoreEmotion = ( totalMatches == 0 ) ? NEUTRAL : ( phraseScore / totalMatches );
		Log.d( "Phrase score: ", "" + scoreEmotion );

		return scoreEmotion;
	}

	private static int messageHaveEmotion( String message, List< String > emotionList )
	{
		int matchs = 0;
		for ( String emotionWord : emotionList )
		{
			if ( message.contains( emotionWord ) )
			{
				++matchs;
			}
		}
		return matchs;
	}
}
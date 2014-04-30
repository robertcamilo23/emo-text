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

	private static List< String > happyWords = Arrays.asList( "I'm", "DELIGHTED", "CONTENT", "SMILE", "GLAD", "FREE", "CAREFREE", "HEALTHY", "PROUD", "LOVING", "CONGRATULATE", "REJOICE", "PERFECTLY", "LOL", "lol", "jk", "happy", "great", "nice", "cool", "good job", "good", "new", "award", "best", "haha", "ha", "hahaha", "funny", "prize", "vacation", "relax", "fun", "glad", "blessed", "hhhhh", "love", "I love you", "care", "pride", "pleasure", "forgive", "kiss", "kisses", "kised", "hug", "huged", "possitive", "naked" );
	private static List< String > sadWords = Arrays.asList( "I'm", "SORROW", "HEARTACHE", "SAD", "SADNESS", "DESPAIR", "MISERY", "GRIEF", "GUILT", "AFFLICTION", "CRY", "UNHAPPY", "BLUES", "sad", "sad mood", "bad", "task", "worst", "worhtless", "I suck", "suck", "bad day", "wrong thing", "wrong", "bad guy", "breakup", "divorce", "worry", "I miss you", "without", "negative", "breathe", "dead", "not working", "failed", "fail", "drop out", "broken", "never" );
	private static List< String > angryWords = Arrays.asList( "I'm", "FUCK", "STEW", "FUME", "FAULT", "BLAME", "HATRED", "GRUDGE", "BACKSTAB", "JERK", "HATE", "SHUT UP", "MAD", "RAGE", "WRATH", "FURY", "HATE", "TEMPER", "bitch", "pissed", "mad", "sucks", "too much", "angry", "kill", "murder", "end", "fucking", "does not", "shit", "judgemental", "off", "wrong", "stupid", "traffic", "fell", "accident", "boring", "hardest", "hard", "enemy", "arrogant" );
	private static List< String > surpriseWords = Arrays.asList( "I'm", "!", "WOW", "SHOCKED", "SURPRISED", "SURPRISE", "UNEXPECTED", "SUDDEN", "PLEASANTLY", "ABACK", "GAPE", "VISIBLY", "OMG", "quit", "amazing", "dude!", "nice!", "ridiculous", "awesome", "I know", "rad", "completely", "!!!", "!!", "news", "report", "alert", "damn", "amber", "pass", "now" );
	private static List< String > disgustWords = Arrays.asList( "I'm", "DISGUSTING", "GROSS", "VOMIT", "YUCK", "REVULSION", "PUKE", "NASTY", "WRONG", "REVOLTING", "REPEL", "MANGLE", "OVERCOME", "SHEER", "everything", "work", "home", "park", "car", "bench", "school", "not confortable", "sick", "God", "hot", "cold", "fair", "very", "money", "know", "drive", "exercise");

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
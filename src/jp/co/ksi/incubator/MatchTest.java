package jp.co.ksi.incubator;

import java.net.URLEncoder;
import java.util.regex.Pattern;

public class MatchTest
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	src= "";
		String	regex= "(\\A|.*, )本社(, .*|\\Z)";
		
		src= "本社";
		System.out.println( src +" - "+ src.matches( regex ) );
		src= "本社, KSI";
		System.out.println( src +" - "+ src.matches( regex ) );
		src= "KSI, 本社";
		System.out.println( src +" - "+ src.matches( regex ) );
		src= "KAMS, 本社, KSI";
		System.out.println( src +" - "+ src.matches( regex ) );
		src= "KAMS, KSI本社";
		System.out.println( src +" - "+ src.matches( regex ) );
		src= "KAMS, 東京本社, KSI";
		System.out.println( src +" - "+ src.matches( regex ) );

		src= "/testruts/facebook/active_404.png";
		regex= ".*\\.css|.*\\.jpg|.*\\.gif|.*\\.png";
		System.out.println( src +" - "+ src.matches( regex ) );

		src= "https://www.facebook.com/logout.php?next=&access_token=${accessToken}";
		src= src.replaceAll( "\\$\\{accessToken\\}", "ZYX" );
		System.out.println( src );
		
		src= "aaaclass-classLoader.aaa";
		Pattern EXLUDE_PARAMS = Pattern.compile("(^|\\W)[cC]lass\\W");
		System.out.println( "EXLUDE_PARAMS: "+ EXLUDE_PARAMS.matcher( src ).find() );
		
		src= "V1NPU2FsdF+T+QAL4xFPeyCnUur0zTFgZRxCdclaBR06j5scbMMgwbNN60gsjGuq";
		System.out.println( src.matches( ".*/.*" ) );
		src= "V1NPU2FsdF96rIP2BNiR1XuXF+oD68GwSbnP1P+tKxuBeEHZ0B+H/VfXJ+d2gslX";
		System.out.println( src.matches( ".*/.*" ) );
	}

}

package jp.co.ksi.incubator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SimpleDataFormatTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		Calendar	cal= Calendar.getInstance();
		String	pattern= "yyyy-MM-dd'T'HH:mm:ss.000Z";
		DateFormat	df= new SimpleDateFormat( pattern );
		System.out.println( df.format( cal.getTime() ) );
		
		//	2014/01/23 Kac apacheログの日時フォーマットのパース処理の習作
		//	MMMを意図通りに処理させるには、ロケールを英語圏にする必要がある？
		pattern= "dd/MMM/yyyy:HH:mm:ss Z";
		df= new SimpleDateFormat( pattern, Locale.US );
		try
		{
			System.out.println( df.format( cal.getTime() ) );
			
			System.out.println( df.parse( "23/Jan/2014:08:38:48 +0900" ) );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		String	source= "Mon, 30 Jun 2014 15:00:27 +0900";
		pattern= "EEE, dd MMM yyyy hh:mm:ss Z";
		df= DateFormat.getDateInstance( DateFormat.LONG, Locale.US );
		df= new SimpleDateFormat( pattern, Locale.US );
		try
		{
			System.out.println( "KacDebug: "+ df.format( cal.getTime() ) );
			System.out.println( df.parse( source ) );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
	}
	
}

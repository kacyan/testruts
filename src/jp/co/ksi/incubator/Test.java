package jp.co.ksi.incubator;

import java.util.ArrayList;

import org.apache.commons.lang.RandomStringUtils;

public class Test
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		int size= 10000;
		ArrayList<String>	list= new ArrayList<String>( size );
		
		for( int i= 0; i < size; i++ )
		{
			list.add( RandomStringUtils.random( 20, "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" ) );
			System.out.println( list.get( i ) );
		}
		
		System.out.println( "list.size="+ list.size() );

	}

}

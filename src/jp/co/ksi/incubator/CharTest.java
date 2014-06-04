package jp.co.ksi.incubator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.RandomStringUtils;

public class CharTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	file= "misc/iso2022jp.txt";
		String	charset= "x-windows-iso2022jp";
		BufferedReader	in= null;
		try
		{
			in= new BufferedReader(
					new InputStreamReader( new FileInputStream( file ), charset )
					);
			String	line= in.readLine();
			while( line != null )
			{
				System.out.println( line );
				line= in.readLine();
			}
			
			String	dandom= RandomStringUtils.random( 10, "0123456789abcdef" );
			System.out.println( dandom );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if( in != null )	in.close();
			}
			catch( Exception e )
			{
			}
		}
	}
	
}

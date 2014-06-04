package jp.co.ksi.incubator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class XWindowsISO2022JPTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	src= "";
		Charset	srcCharset= Charset.forName( "x-windows-iso2022jp" );
		Charset	dstCharset= Charset.forName( "utf-8" );
		BufferedReader	in= null;
		try
		{
			in= new BufferedReader(
					new InputStreamReader( new FileInputStream( "misc/iso2022jp.txt" ), srcCharset )
					);
			String	line= in.readLine();
			while( line != null )
			{
				System.out.println( line );
				src += line + "\n";
				line= in.readLine();
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			if( in != null )
			{
				try
				{
					in.close();
				}
				catch( IOException e )
				{
					e.printStackTrace();
				}
			}
		}
		PrintWriter	out= null;
		try
		{
			out= new PrintWriter(
						new OutputStreamWriter( new FileOutputStream( "misc/dst.txt" ), dstCharset )
						);
			out.println( src );
			out.flush();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			if( out != null )
			{
				out.close();
			}
		}
		
	}
	
}

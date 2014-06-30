package jp.co.ksi.incubator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 
 */

/**
 * リソースファイルをテキストファイルに吐き出すプログラム
 * @author kac
 *
 */
public class Resource2Txt
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		if( args.length < 2 )
		{
			System.out.println( "" );
			System.out.println( "usage : "+ Resource2Txt.class.getName() +" [srcFile] [dstFile]" );
			return;
		}
		String	srcFile= args[0];
		String	dstFile= args[1];
		String	charset= "utf-8";
		BufferedReader	in= null;
		PrintWriter	out= null;
		try
		{
			in= new BufferedReader( new InputStreamReader( new FileInputStream( srcFile ), charset ) );
			out= new PrintWriter( new OutputStreamWriter( new FileOutputStream( dstFile ), charset ) );
			
			String	line= in.readLine();
			while( line != null )
			{
				int	index= line.indexOf( "=" );
				if( index <= 0 )
				{
					line= in.readLine();
					continue;
				}
				
				String	name= line.substring( 0, index );
				String	value= line.substring( index+1 );
				
				System.out.println( name +"\t"+ value );
				out.println( name +"\t"+ value );
				
				line= in.readLine();
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{//	後始末
			if( in != null )
			{
				try
				{
					in.close();
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		}
	}
	
}

package jp.co.ksi.incubator;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.internet.MimeUtility;

import sun.misc.BASE64Decoder;

import net.arnx.jsonic.JSON;

public class JsonTest
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( "misc/messages.json" );
			Properties	props= JSON.decode( in, Properties.class );
			for( Enumeration<?> enumeration= props.keys(); enumeration.hasMoreElements(); )
			{
				String	key= (String)enumeration.nextElement();
				String	value= props.getProperty( key );
				System.out.println( key +"="+ value );
			}
			
			System.out.println(
					props.getProperty( "messages.0.payload.headers.11.name" )
					+"="
					+props.getProperty( "messages.0.payload.headers.11.value" )
					);
			String	body= props.getProperty( "messages.0.payload.body.data" );
			System.out.println( body );
			System.out.println( MimeUtility.decodeText( body ) );
			BASE64Decoder	decoder= new BASE64Decoder();
			System.out.println( new String( decoder.decodeBuffer( body ), "utf-8" ) );
			
			body= "5Yqg5p2l44Gn44GZ44CCDQoNCuWOn-adkeauv-OAgQ0K5LqG6Kej44GX44G-44GX44Gf44CCDQoNCuS7iuWbnuOBruS7tuOBr-";
//			body= body.replaceAll( "-", "/" );
			System.out.println( new String( decoder.decodeBuffer( body ), "utf-8" ) );
			
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				in.close();
			}
			catch( Exception e )
			{
			}
		}
	}

}

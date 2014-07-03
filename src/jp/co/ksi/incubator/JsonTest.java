package jp.co.ksi.incubator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.internet.MimeUtility;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
				if( key.matches( "messages\\.1.*" ) )
				{
					System.out.println( key +"="+ value );
				}
			}
			
			//	payload.body.size=0 ならbody.dataは無い
			//	
			//	messages.1.payload.parts.0.body.data
		}
		catch( UnsupportedEncodingException e )
		{
			e.printStackTrace();
			Map<String,Charset>	m= Charset.availableCharsets();
			for( Iterator<String> i= m.keySet().iterator(); i.hasNext(); )
			{
				String	key= i.next();
				System.out.println( key +"="+ m.get( key ) );
			}
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

	/**
	 * body.dataのエンコードされた本文をデコードします
	 * @param body
	 * @param charset
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * <pre>
	 * -を+に変換する
	 * base64デコードする
	 * </pre>
	 */
	private static String decodeBodyData( String body, String charset )
			throws UnsupportedEncodingException, IOException
	{
		body= body.replaceAll( "-", "+" );	//	-を+に変換してbase64デコード
		BASE64Decoder	decoder= new BASE64Decoder();
		try
		{
			return new String( decoder.decodeBuffer( body ), charset );
		}
		catch( Exception e )
		{
			return e.toString();
		}
	}

}

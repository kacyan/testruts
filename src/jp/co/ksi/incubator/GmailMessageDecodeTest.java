package jp.co.ksi.incubator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Decoder;

import net.arnx.jsonic.JSON;

/**
 * Gmail APIでメッセージをjsonで受け取れるようになった。
 * 本文はエンコードされている。
 * このクラスは本文の取り出し方及びデコード方法の習作。
 * @author kac
 * @since 2014/07/03
 * @version 2014/07/03
 */
public class GmailMessageDecodeTest
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
			
			Map	map= JSON.decode( in, Map.class );
			ArrayList	messages= (ArrayList)map.get( "messages" );
			String	nextPageToken= (String)map.get( "nextPageToken" );
			
			for( int i= 0; i < messages.size(); i++ )
			{
				Map	message= (Map)messages.get( i );
				System.out.println( message );
				Map	payload= (Map)message.get("payload");
				//	payload.mimeTypeでMIMEタイプを確認する
				String	mimeType= (String)payload.get( "mimeType" );
				if( mimeType.equalsIgnoreCase( "text/plain" ) )
				{//	単純なテキストメール
					//	payload.body.data
					Map<String,?>	body= (Map)payload.get( "body" );
					System.out.println( "body="+ body.getClass().getName() );
					for( Iterator<String> it= body.keySet().iterator(); it.hasNext(); )
					{
						String	key= it.next();
						System.out.println( key +"="+ body.get( key ) );
					}
					String	data= (String)body.get( "data" );
					System.out.println( data );
					System.out.println( decodeBodyData( data, "utf-8" ) );
				}
				else if( mimeType.equalsIgnoreCase( "multipart/alternative" )
					|| mimeType.equalsIgnoreCase( "multipart/mixed" )	)
				{//	マルチパート
					//	payload.parts.0.body.data
					List<Map>	parts= (List<Map>)payload.get( "parts" );
					System.out.println( "parts.size="+ parts.size() );
					for( int j= 0; j < parts.size(); j++ )
					{
						Map part= parts.get( j );
						for( Iterator<String> it= part.keySet().iterator(); it.hasNext(); )
						{
							String	key= it.next();
							System.out.println( key +"="+ part.get( key ) );
						}
						String	partMimeType= (String)part.get( "mimeType" );
						Map<String,?>	body= (Map)part.get( "body" );
						String	data= (String)body.get( "data" );
						System.out.println( data );
						if( partMimeType.equalsIgnoreCase( "text/plain" ) )
						{//	テキスト
							System.out.println( decodeBodyData( data, "utf-8" ) );
						}
						else if( partMimeType.equalsIgnoreCase( "text/html" ) )
						{//	html
							System.out.println( decodeBodyData( data, "utf-8" ) );
						}
						else
						{//	その他
							System.out.println( "part["+ j +"] "+ partMimeType +" "+ part.get( "filename" ) );
						}
					}
				}
				else
				{//	それ以外
					
				}
				System.out.println( "----- message["+ i +"] end -----" );
			}
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

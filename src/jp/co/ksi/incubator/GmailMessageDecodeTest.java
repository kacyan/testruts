package jp.co.ksi.incubator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

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

		String	charset="ISO-2022-JP";
		String	src= "6YCa55+lOg0KDQrjgr_jgqTjg4jjg6s6IOOBpuOBmeOBqA0K5pel5pmCOiAyMDE0LzA3LzAxICjngaspIDEwOjAwIO+9niAxMTowMCDmnbHkuqwNCuOCq+ODrOODs+ODgOODvDoga2Fja3NpLnRlc3RAZ21haWwuY29tDQrlj4LliqDogIU6DQogICAgICog44GL44GP44KE44KT44Gm44GZ44GoLSDnrqHnkIbogIUNCg0K5LqI5a6a44Gu6Kmz57SwOiAgDQpodHRwczovL3d3dy5nb29nbGUuY29tL2NhbGVuZGFyL2V2ZW50P2FjdGlvbj1WSUVXJmVpZD1ZV1YyY1dsdFptTnhPV1JuYW5FeGJYRnNOemxvWlhVMk1UZ2dhMkZqYTNOcExuUmxjM1JBYlEmdG9rPU1qRWphMkZqYTNOcExuUmxjM1JBWjIxaGFXd3VZMjl0TTJaa09HTmtOR1JsWTJWaU1HRmtNekV6TTJVNE1XTXdNamN3T1RSaFl6UmlOekl6TnpBMU1RJmN0ej1Bc2lhL1Rva3lvJmhsPWphDQoNCkdvb2dsZSDjgqvjg6zjg7Pjg4Djg7zjgYvjgonjga7mi5vlvoXnirY6IGh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvDQoNCuOAjGthY2tzaS50ZXN0QGdtYWlsLmNvbeOAjeOCq+ODrOODs+ODgOODvOOBrumAmuefpeOBruWPl+S_oeOBjOioreWumuOBleOCjOOBpuOBhOOCi+OBn+OCgeOAgeacrOODoeODvCANCuODq+OCkiBrYWNrc2kudGVzdEBnbWFpbC5jb20g44Gr44GK6YCB44KK44GX44Gm44GE44G+44GZ44CCDQoNCumAmuefpeOCkuWPl+OBkeWPluOCieOBquOBhOOCiOOBhuOBq+OBmeOCi+OBq+OBr+OAgWh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvIOOBq+ODreOCsOOCpCANCuODs+OBl+OAgeOCq+ODrOODs+ODgOODvOOBrumAmuefpeioreWumuOCkuWkieabtOOBl+OBpuOBj+OBoOOBleOBhOOAgg0K";
		try
		{
			//	Shift_JIS dGhpcyBtYWlsIGlzIHRlc3QuDQrjgYLjgYTjgYbjgYjjgYrjgIINCg0KDQoNCl5fXg0K
			src= "dGhpcyBtYWlsIGlzIHRlc3QuDQrjgYLjgYTjgYbjgYjjgYrjgIINCg0KDQoNCl5fXg0K";
			charset= "utf-8";	//	Shift_JIS -> utf-8
//			src= "6YCa55-lOg0KDQrjgr_jgqTjg4jjg6s6IOOBpuOBmeOBqA0K5pel5pmCOiAyMDE0LzA3LzAxICjngaspIDEwOjAwIO-9niAxMTowMCDmnbHkuqwNCuOCq-ODrOODs-ODgOODvDoga2Fja3NpLnRlc3RAZ21haWwuY29tDQrlj4LliqDogIU6DQogICAgICog44GL44GP44KE44KT44Gm44GZ44GoLSDnrqHnkIbogIUNCg0K5LqI5a6a44Gu6Kmz57SwOiAgDQpodHRwczovL3d3dy5nb29nbGUuY29tL2NhbGVuZGFyL2V2ZW50P2FjdGlvbj1WSUVXJmVpZD1ZV1YyY1dsdFptTnhPV1JuYW5FeGJYRnNOemxvWlhVMk1UZ2dhMkZqYTNOcExuUmxjM1JBYlEmdG9rPU1qRWphMkZqYTNOcExuUmxjM1JBWjIxaGFXd3VZMjl0TTJaa09HTmtOR1JsWTJWaU1HRmtNekV6TTJVNE1XTXdNamN3T1RSaFl6UmlOekl6TnpBMU1RJmN0ej1Bc2lhL1Rva3lvJmhsPWphDQoNCkdvb2dsZSDjgqvjg6zjg7Pjg4Djg7zjgYvjgonjga7mi5vlvoXnirY6IGh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvDQoNCuOAjGthY2tzaS50ZXN0QGdtYWlsLmNvbeOAjeOCq-ODrOODs-ODgOODvOOBrumAmuefpeOBruWPl-S_oeOBjOioreWumuOBleOCjOOBpuOBhOOCi-OBn-OCgeOAgeacrOODoeODvCANCuODq-OCkiBrYWNrc2kudGVzdEBnbWFpbC5jb20g44Gr44GK6YCB44KK44GX44Gm44GE44G-44GZ44CCDQoNCumAmuefpeOCkuWPl-OBkeWPluOCieOBquOBhOOCiOOBhuOBq-OBmeOCi-OBq-OBr-OAgWh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvIOOBq-ODreOCsOOCpCANCuODs-OBl-OAgeOCq-ODrOODs-ODgOODvOOBrumAmuefpeioreWumuOCkuWkieabtOOBl-OBpuOBj-OBoOOBleOBhOOAgg0K";
			charset= "utf-8";	//	ISO-2022-JP -> utf-8

			src= "6YCa55+lOg0KDQrjgr_jgqTjg4jjg6s6IOOBpuOBmeOBqA0K5pel5pmCOiAyMDE0LzA3LzAxICjngaspIDEwOjAwIO+9niAxMTowMCDmnbHkuqwNCuOCq+ODrOODs+ODgOODvDoga2Fja3NpLnRlc3RAZ21haWwuY29tDQrlj4LliqDogIU6DQogICAgICog44GL44GP44KE44KT44Gm44GZ44GoLSDnrqHnkIbogIUNCg0K5LqI5a6a44Gu6Kmz57SwOiAgDQpodHRwczovL3d3dy5nb29nbGUuY29tL2NhbGVuZGFyL2V2ZW50P2FjdGlvbj1WSUVXJmVpZD1ZV1YyY1dsdFptTnhPV1JuYW5FeGJYRnNOemxvWlhVMk1UZ2dhMkZqYTNOcExuUmxjM1JBYlEmdG9rPU1qRWphMkZqYTNOcExuUmxjM1JBWjIxaGFXd3VZMjl0TTJaa09HTmtOR1JsWTJWaU1HRmtNekV6TTJVNE1XTXdNamN3T1RSaFl6UmlOekl6TnpBMU1RJmN0ej1Bc2lhL1Rva3lvJmhsPWphDQoNCkdvb2dsZSDjgqvjg6zjg7Pjg4Djg7zjgYvjgonjga7mi5vlvoXnirY6IGh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvDQoNCuOAjGthY2tzaS50ZXN0QGdtYWlsLmNvbeOAjeOCq+ODrOODs+ODgOODvOOBrumAmuefpeOBruWPl+S_oeOBjOioreWumuOBleOCjOOBpuOBhOOCi+OBn+OCgeOAgeacrOODoeODvCANCuODq+OCkiBrYWNrc2kudGVzdEBnbWFpbC5jb20g44Gr44GK6YCB44KK44GX44Gm44GE44G+44GZ44CCDQoNCumAmuefpeOCkuWPl+OBkeWPluOCieOBquOBhOOCiOOBhuOBq+OBmeOCi+OBq+OBr+OAgWh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvIOOBq+ODreOCsOOCpCANCuODs+OBl+OAgeOCq+ODrOODs+ODgOODvOOBrumAmuefpeioreWumuOCkuWkieabtOOBl+OBpuOBj+OBoOOBleOBhOOAgg0K";
			charset= "utf-8";
			System.out.println( decodeBodyData( src, charset ) );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		//	test1();
	}

	public static void test1()
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
		
		try
		{
			
			return new String( Base64.decodeBase64( body.getBytes() ), charset );
		}
		catch( Exception e )
		{
			return e.toString();
		}
	}

}

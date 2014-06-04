package jp.co.ksi.incubator;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * java.text.MessageFormatの習作
 * @author kac
 * @since 2014/01/23
 * @version 2014/01/23
 * <pre>
 * apacheログをパースしてみる
 * LogFormat "%h %{WSO-Session-ID}i %u %P %t \"%r\" %s %b \"%{Referer}i\" \"%{User-Agent}i\"" wsolog
 *
 * 133.253.7.189 UuBWiIX9Q2MAADSjLWc S090009 12957 [23/Jan/2014:08:38:48 +0900] "GET /portal/ HTTP/1.1" 200 4234 "-" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; GTB7.4; SLCC1; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30618)"
 * </pre>
 */
public class MessageFormatTest
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		//	0	host
		//	1	wso-session-id
		//	2	uid
		//	3	port
		//	4	アクセス時刻
		//	5	リクエスト
		//	6	レスポンスコード
		//	7	バイト数
		//	8	referer
		//	9	user-aegnt
		String pattern= "{0} {1} {2} {3} [{4,date,dd/MMM/yyyy:HH:mm:ss Z}] \"{5}\" {6,number} {7} \"{8}\" \"{9}\"";
		MessageFormat	mf= new MessageFormat( pattern, Locale.US );

		String	text= "133.253.7.189 UuBWiIX9Q2MAADSjLWc S090009 12957 [23/Jan/2014:08:38:48 +0900] \"GET /portal/ HTTP/1.1\" 200 4234 \"-\" \"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; GTB7.4; SLCC1; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30618)\"";
		text= "133.253.52.34 - - 6607 [29/Jan/2014:09:02:03 +0900] \"GET /noauth/Kexit.html HTTP/1.1\" 304 - \"-\" \"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; GTB7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)\"";
		System.out.println( text );

		System.out.println( "\n[parse]" );
		try
		{
			Object[]	obj= mf.parse( text );
			for( int i= 0; i < obj.length; i++ )
			{
				System.out.println( obj[i].toString() +" - "+ obj[i].getClass().getName() );
			}
		}
		catch( ParseException e )
		{
			e.printStackTrace();
		}


	}

}

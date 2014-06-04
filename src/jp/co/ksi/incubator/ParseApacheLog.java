package jp.co.ksi.incubator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * Apacheログをパースする
 * @author kac
 * @since 2014/01/23
 * @version 2014/01/30
 * <pre>
 * [ログフォーマット]
 * LogFormat "%h %{WSO-Session-ID}i %u %P %t \"%r\" %s %b \"%{Referer}i\" \"%{User-Agent}i\"" wsolog
 * [ログの実例]
 * 133.253.7.189 UuBWiIX9Q2MAADSjLWc S090009 12957 [23/Jan/2014:08:38:48 +0900] "GET /portal/ HTTP/1.1" 200 4234 "-" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; GTB7.4; SLCC1; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30618)"
 * [パターン]
 * 	String pattern= "{0} {1} {2} {3,number} [{4,date,dd/MMM/yyyy:HH:mm:ss Z}] \"{5}\" {6,number} {7,number} \"{8}\" \"{9}\"";
 * 0	host
 * 1	wso-session-id
 * 2	uid
 * 3	port
 * 4	アクセス時刻
 * 5	リクエスト
 * 6	レスポンスコード
 * 7	バイト数
 * 8	referer
 * 9	user-agent
 * </pre>
 */
public class ParseApacheLog
{
	private static Logger	log= Logger.getLogger( ParseApacheLog.class );

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		if( args.length < 2 )
		{
			System.out.println( "usage:" );
			System.out.println( "  "+ ParseApacheLog.class.getName() +" ${srcFile} ${dst{File} ${charset}" );
			System.out.println( "  ${srcFile} : must (ex)/home/portal/logs/access_log" );
			System.out.println( "  ${dstFile} : must (ex)/home/portal/logs/dst_log" );
			System.out.println( "  ${charset} : default=utf-8" );
			System.exit( -1 );
		}
		String	srcFile= args[0];
		String	dstFile= args[1];
		Charset	cs= Charset.forName( "utf-8" );
		if( args.length > 2 )
		{
			cs= Charset.forName( args[2] );
		}

		ParseApacheLog	parseApacheLog= new ParseApacheLog();

		FileInputStream	in= null;
		FileOutputStream	out= null;
		try
		{
			in= new FileInputStream( srcFile );
			out= new FileOutputStream( dstFile );

			parseApacheLog.doParseLog( in, cs, out );
		}
		catch(Exception e )
		{
			log.error( srcFile +" -> "+ dstFile, e );
		}
		finally
		{
			if( out != null )
			{
				try
				{
					out.close();
				}
				catch( Exception e )
				{
					log.error( dstFile, e );
				}
			}
			if( in != null )
			{
				try
				{
					in.close();
				}
				catch( Exception e )
				{
					log.error( srcFile, e );
				}
			}
		}

	}

	public void doParseLog( InputStream in, Charset cs, OutputStream out )
	{
		//	パースパターン
		String srcPattern= "{0} {1} {2} {3} [{4,date,dd/MMM/yyyy:HH:mm:ss Z}] \"{5}\" {6,number} {7} \"{8}\" \"{9}\"";
		MessageFormat	srcMsgFmt= new MessageFormat( srcPattern, Locale.US );
		String dstPattern= "{0} {1} {2} [{4,date,dd/MMM/yyyy:HH:mm:ss Z}] \"{5}\" {6,number} {7} \"{8}\" \"{9}\"";
		MessageFormat	dstMsgFmt= new MessageFormat( dstPattern, Locale.US );

		try
		{
			BufferedReader reader= new BufferedReader( ( new InputStreamReader( in, cs ) ) );
			PrintWriter	writer= new PrintWriter( new OutputStreamWriter( out, cs ) );

			String	line= reader.readLine();
			int	num= 1;
			while( line != null )
			{
				String	host= "";
				String	wsoSessionId= "";
				String	uid= "";
				String	port= "";
				Date	accessTime= null;
				String	request= "";
				int	statusCode= 0;
				String	length= "";
				String	referer= "";
				String	userAgent= "";
				try
				{
					//	ログ１行をパースする
					Object[]	obj= srcMsgFmt.parse( line );
					host= (String)obj[0];
					wsoSessionId= (String)obj[1];
					uid= (String)obj[2];
					port= (String)obj[3];
					accessTime= (Date)obj[4];	//	アクセス時刻
					request= (String)obj[5];	//	リクエスト
					statusCode= ((Long)obj[6]).intValue();	//	レスポンスコード
					length= (String)obj[7];	//	バイト数
					referer= (String)obj[8];
					userAgent= (String)obj[9];

					writer.println( dstMsgFmt.format( obj ) );
				}
				catch( Exception e )
				{
					log.error( num +" "+ line, e );
				}
				log.debug( host +", "+ wsoSessionId + ", "+ uid +", "+ port +", "+ accessTime +", "+ request +", "+ statusCode +", "+ length +", "+ referer +", "+ userAgent );

				line= reader.readLine();
				num++;
			}
		}
		catch( Exception e )
		{
			log.error( "", e );
		}

	}


}

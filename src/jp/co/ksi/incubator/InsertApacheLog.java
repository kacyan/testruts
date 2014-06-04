package jp.co.ksi.incubator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * Apacheログをパースして、DBに叩き込む
 * @author kac
 * @since 2014/01/30
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
public class InsertApacheLog
{
	private static Logger	log= Logger.getLogger( InsertApacheLog.class );

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	server= "pre-wso1";
		String	file= "misc/prelog/access_log";
		String	driver= "org.postgresql.Driver";
		String	url= "jdbc:postgresql://ardbeg.dev.ksi.co.jp:5432/prelog?charSet=utf-8";
		String	user= "tomcat";
		String	password= "tomcat";

		InsertApacheLog	parseApacheLog= new InsertApacheLog();

		FileInputStream	in= null;
		Connection	con= null;
		try
		{
			in= new FileInputStream( file );

			Class.forName( driver );
			con= DriverManager.getConnection( url, user, password );

			parseApacheLog.doParseLog( in, Charset.forName( "utf-8" ), con, server );
		}
		catch( FileNotFoundException e )
		{
			log.error( file, e );
		}
		catch( SQLException e )
		{
			log.error( e.getLocalizedMessage(), e );
		}
		catch( ClassNotFoundException e )
		{
			log.error( driver, e );
		}
		finally
		{
			if( con != null )
			{
				try
				{
					con.close();
				}
				catch( Exception e )
				{
					log.error( con, e );
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
					log.error( file, e );
				}
			}
		}

	}

	public void doParseLog( InputStream in, Charset cs, Connection con, String server )
	{
		//	パースパターン
		String pattern= "{0} {1} {2} {3,number} [{4,date,dd/MMM/yyyy:HH:mm:ss Z}] \"{5}\" {6,number} {7,number} \"{8}\" \"{9}\"";
		MessageFormat	mf= new MessageFormat( pattern, Locale.US );

		String	sql= "insert into log values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		String	userAgentRegex= "check_http.*";

		try
		{
			PreparedStatement	ps= con.prepareStatement( sql );

			BufferedReader reader= new BufferedReader( ( new InputStreamReader( in, cs ) ) );
			String	line= reader.readLine();
			while( line != null )
			{
				String	host= "";
				String	wsoSessionId= "";
				String	uid= "";
				int	port= 0;;
				Date	accessTime= null;
				String	request= "";
				int	statusCode= 0;
				int	length= 0;
				String	referer= "";
				String	userAgent= "";
				try
				{
					//	ログ１行をパースする
					Object[]	obj= mf.parse( line );
					host= (String)obj[0];
					wsoSessionId= (String)obj[1];
					uid= (String)obj[2];
					port= ((Long)obj[3]).intValue();
					accessTime= (Date)obj[4];	//	アクセス時刻
					request= (String)obj[5];	//	リクエスト
					statusCode= ((Long)obj[6]).intValue();	//	レスポンスコード
					length= ((Long)obj[7]).intValue();	//	バイト数
					referer= (String)obj[8];
					userAgent= (String)obj[9];

					//	フィルタリング
					if( userAgent.matches( userAgentRegex ) )
					{
						line= reader.readLine();
						continue;
					}

					ps.setString( 1, host );
					ps.setString( 2, wsoSessionId );
					ps.setString( 3, uid );
					ps.setInt( 4, port );
					ps.setObject( 5, new Timestamp( accessTime.getTime() ) );
					ps.setString( 6, request );
					ps.setInt( 7, statusCode );
					ps.setInt( 8, length );
					ps.setString( 9, referer );
					ps.setString( 10, userAgent );
					ps.setString( 11, server );
					ps.execute();
				}
				catch( Exception e )
				{
					log.error( e.toString(), e );
				}
				log.debug( host +", "+ wsoSessionId + ", "+ uid +", "+ port +", "+ accessTime +", "+ request +", "+ statusCode +", "+ length +", "+ referer +", "+ userAgent );

				line= reader.readLine();
			}
		}
		catch( Exception e )
		{
			log.error( "", e );
		}

	}


}

package jp.co.ksi.incubator.oauth2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.Proxy.Type;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * OAuth-2.0の習作(google編)
 * @author kac
 * @since 2021/05/30
 * @version 2021/05/31
 */
public class OAuth2Test
{
	private static Logger	log= Logger.getLogger( OAuth2Test.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	url= "https://accounts.google.com/o/oauth2/token";
//		url= "http://ticket.os.ksi.co.jp/testruts/param.jsp";
//		url= "https://www.hatena.ne.jp/login";
		String method = "POST";
		String	proxyHost= "172.16.10.3";
		int	proxyPort= 3128;
		
		Proxy	proxy= Proxy.NO_PROXY;
		try
		{
			proxy= new Proxy( Type.HTTP, new InetSocketAddress( proxyHost, proxyPort ) );
		}
		catch( Exception e )
		{
			log.info( "no proxy" );
		}
		
		PrintStream	outFile= null;
		try
		{
			URL	u= new URL( url );
			URLConnection	con= u.openConnection( proxy );
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod( method );
			if( method.equalsIgnoreCase( "POST" ) )
			{
				http.setDoOutput( true );
				http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
				PrintStream	stream= new PrintStream( con.getOutputStream() );
				postParam( stream, "code", "kACAH-1Ng2IzSTwSR0SQRV79-zSzMGX_sQ7SidWilZ_tBTvtdBqfH9xr10ckWjP5Deysj2fBeK5M3vX33i3cvXyx18ZDv2hM_4Yf1cxLjwhMwnY73F3gtyXx74" );
				postParam( stream, "client_id", "494593688356-pllemu8erp8a82p9cn8qhfhjvplo90fd.apps.googleusercontent.com" );
				postParam( stream, "client_secret", "ubiPnDb_43KliRI7bFiZt-Uh" );
				postParam( stream, "redirect_uri", "http://ticket.os.ksi.co.jp/testruts/oauth2/getToken.jsp" );
				postParam( stream, "grant_type", "authorization_code" );
//				postParam( stream, "name", "tpsoft" );
//				postParam( stream, "password", "password" );
//				postParam( stream, "persistent", "" );
				stream.flush();
				stream.close();
			}
			
			log.debug( "responseCode="+ http.getResponseCode() );
			log.debug( http.getResponseMessage() );
			//	レスポンスヘッダを表示する
			Map	headers= http.getHeaderFields();
			String[]	keys= new String[headers.keySet().size()];
			keys= (String[])headers.keySet().toArray( keys );
			for( int i= 0; i < keys.length; i++ )
			{
				log.debug( keys[i] +"="+ headers.get( keys[i] ) );
			}
			
			String	resText= "";
			outFile= new PrintStream( "misc/oauth/google/json", "utf-8" );
			BufferedReader	reader= new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			String	line= reader.readLine();
			while( line != null )
			{
//				log.debug( line );
				resText += line;
				outFile.println( line );
				line= reader.readLine();
			}
			reader.close();
			log.info( resText );
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
		}
		finally
		{
			if( outFile != null )
			{
				try
				{
					outFile.close();
				}
				catch( Exception e )
				{
					log.error( e.toString(), e );
				}
			}
		}
	}

	private static void postParam( PrintStream stream, String name,
			String value ) throws UnsupportedEncodingException
	{
		String	enc= "utf-8";
		stream.print( URLEncoder.encode( name, enc ) +"="+ URLEncoder.encode( value, enc ) +"&" );
	}
	
}

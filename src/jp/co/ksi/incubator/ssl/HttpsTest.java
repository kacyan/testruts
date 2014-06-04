package jp.co.ksi.incubator.ssl;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;

public class HttpsTest
{
	private static Logger	log= Logger.getLogger( HttpsTest.class );
	
	
	/**
	 * HTTPSクライアントの習作
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	url= "https://ssl-vpn.kubota.co.jp/";
		String	proxyHost = "172.16.10.3";
		int	proxyPort = 3128;
		
		try
		{
			Proxy	proxy= new Proxy( Proxy.Type.HTTP, new InetSocketAddress( proxyHost, proxyPort ) );
			URL	u= new URL( url );
			URLConnection	con= u.openConnection( proxy );
			if( con instanceof HttpURLConnection )
			{
				HttpURLConnection	http= (HttpURLConnection)con;
				if( http instanceof HttpsURLConnection )
				{
					HttpsURLConnection https = (HttpsURLConnection)http;
					SSLContext		ssl= SSLContext.getInstance( "SSL" );
					KeyManager[]	km= {};
					TrustManager[]	tm= { new DummyTrustManager() };
					ssl.init( km, tm, null );
					https.setHostnameVerifier( new DummyHostnameVerifier() );
					https.setSSLSocketFactory( ssl.getSocketFactory() );
				}
				
				int	responseCode= http.getResponseCode();
				log.info( "responseCode="+ responseCode );
				Map	m= http.getHeaderFields();
				for( Iterator i= m.keySet().iterator(); i.hasNext(); )
				{
					String	name= (String)i.next();
					String	value= (String)m.get( name );
					log.debug( "[Header] "+ name +"="+ value );
				}
				
			}
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
		}
		
	}
	
}

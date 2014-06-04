package jp.co.ksi.incubator.oauth2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.Proxy.Type;

import org.apache.log4j.Logger;

import net.arnx.jsonic.JSON;

/**
 * Oauth-2.0の習作(google編)
 * @author kac
 * @since 2012/05/31
 * @version 2021/05/31
 */
public class CallTest
{
	private static Logger	log= Logger.getLogger( CallTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	file= "misc/oauth/google/json";
		OAuth	oauth= null;
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( file );
			oauth= JSON.decode( in, OAuth.class );
			System.out.println( oauth );
		}
		catch( Exception e )
		{
			log.error( "", e );;
			return;
		}
		finally
		{
			if( in != null )
			{
				try
				{
					in.close();
				}
				catch( Exception e )
				{
					log.error( "", e );;
				}
			}
		}
		
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
		String	url= "https://www.googleapis.com/oauth2/v1/userinfo?access_token="+ oauth.getAccess_token();
		try
		{
			URL	u= new URL( url );
			URLConnection	con= u.openConnection( proxy );
			
			BufferedReader	reader= new BufferedReader( new InputStreamReader( con.getInputStream(), "utf-8" ) );
			String	line= reader.readLine();
			while( line != null )
			{
				log.debug( line );
				line= reader.readLine();
			}
			reader.close();
		}
		catch( Exception e )
		{
			log.error( "", e );;
		}
		
	}
	
}

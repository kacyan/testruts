package jp.co.ksi.testruts.bl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.Proxy.Type;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.incubator.oauth.OAuthBaseBL;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * RSSを取得する習作
 * <pre>
 * </pre>
 * @author kac
 * @since 2012/05/15
 * @version 2012/05/24
 * @see OAuthBaseBL
 */
public class GetRss extends BaseBL
{
	private static Logger	log= Logger.getLogger( GetRss.class );
	private String serviceURL= "";
	
	@Override
	public String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		DynaActionForm dyna = (DynaActionForm)form;
		serviceURL= dyna.getString( "serviceURL" );
		log.debug( "scope="+ serviceURL );
		if( serviceURL.equals( "" ) )
		{
			log.error( "serviceURL invalid." );
			addError( "BL.ERR.PARAM", "serviceURL invalid." );
			return APL_ERR;
		}
		
		String authorization = "";

		try
		{
			Proxy	proxy= getProxy( appConfig );
			SyndFeed syndFeed = getSyndFeed( serviceURL, proxy, authorization );
			request.setAttribute( "syndFeed", syndFeed );
			log.debug( syndFeed );
			return APL_OK;
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), e.toString() );
			return APL_ERR;
		}
		finally
		{
		}
		
	}

	public static SyndFeed getSyndFeed( String rssURL, Proxy proxy,
			String authorization ) throws Exception
	{
		try
		{
			URL	u= new URL( rssURL );
			URLConnection	con= null;
			con= u.openConnection( proxy );
			
			HttpURLConnection http= (HttpURLConnection)con;
			http.setRequestMethod( "GET" );
			if( ( authorization != null ) && ( authorization.length() > 0 ) )
			{//	authorizationがあればリクエスト・ヘッダーにセットする
				http.setRequestProperty( "Authorization", authorization );
			}
			http.setRequestProperty( "Content-Type", "application/atom+xml" );
			http.setRequestProperty( "accept-language", "ja" );
			//	送信ここまで
			
			log.debug( "responseCode="+ http.getResponseCode() );
			log.debug( http.getResponseMessage() );
			
			//	レスポンスヘッダを表示する
			//	エラーが発生した場合、WWW-Authenticateに詳細情報が返ってくる
			Map	headers= http.getHeaderFields();
			String[]	keys= new String[headers.keySet().size()];
			keys= (String[])headers.keySet().toArray( keys );
			for( int i= 0; i < keys.length; i++ )
			{
				log.debug( keys[i] +"="+ headers.get( keys[i] ) );
				if( "WWW-Authenticate".equals( keys[i] ) )
				{
					String	wwwAuthenticate= headers.get( keys[i] ).toString();
					throw new Exception( wwwAuthenticate );
				}
			}
			
			SyndFeedInput	in= new SyndFeedInput();
			return in.build( new XmlReader( con ) );
		}
		catch( Exception e )
		{
			throw e;
		}
	}
	
	public static Proxy getProxy( Properties appConfig )
	{
		String	proxyHost= appConfig.getProperty( "proxy.host", "172.16.10.3" );
		log.debug( "proxyHost="+ proxyHost );
		String	proxyPort= appConfig.getProperty( "proxy.port", "3128" );
		log.debug( "proxyPort="+ proxyPort );
		try
		{
			return new Proxy( Type.HTTP, new InetSocketAddress( proxyHost, Integer.parseInt( proxyPort ) ) );
		}
		catch( Exception e )
		{
			log.info( "no proxy. proxyHost="+ proxyHost +", proxyPort="+ proxyPort, e );
			return Proxy.NO_PROXY;
		}
	}
	
}

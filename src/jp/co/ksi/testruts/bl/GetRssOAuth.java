package jp.co.ksi.testruts.bl;

import java.io.File;
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
import javax.servlet.http.HttpSession;

import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.incubator.oauth.OAuthBaseBL;
import jp.co.ksi.incubator.oauth.OAuthBean;
import jp.co.ksi.incubator.oauth.OAuthBeanUtil;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * oauth認証でRSSを取得する習作
 * <pre>
 * </pre>
 * @author kac
 * @since 2012/05/24
 * @version 2012/05/24
 * @see OAuthBaseBL
 */
public class GetRssOAuth extends BaseBL
{
	private static Logger	log= Logger.getLogger( GetRssOAuth.class );
	private String serviceURL= "";
	
	@Override
	public String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		DynaActionForm dyna = (DynaActionForm)form;
		serviceURL= dyna.getString( "serviceURL" );
		log.debug( "serviceURL="+ serviceURL );
		if( serviceURL.equals( "" ) )
		{
			log.error( "serviceURL invalid." );
			addError( "BL.ERR.PARAM", "serviceURL invalid." );
			return APL_ERR;
		}
		String	oauthName= dyna.getString( "oauthName" );
		log.debug( "oauthName="+ oauthName );
		
		String authorization = "";
		String	uid= "";
		String	basePath= appConfig.getProperty( "oauth.base.path", "/home/portal/oauth" );
		File	baseFile= new File( basePath, uid );
		log.debug( "baseFile="+ baseFile );
		
		try
		{
			//	(1)oauthを取得する
			OAuthBean	oauth= OAuthBeanUtil.load( new File( baseFile, oauthName ) );
			
			if( isOAuthInvalid( oauth ) )
			{//	(2)oauthエラー->APL_OK oauth invalid
				log.error( "oauth invalid. "+ oauth );
				request.setAttribute( "oauth", oauth );
				return APL_ERR;
			}
			
			//	(3)oauth認証でRSSを取得する
			Proxy	proxy= GetRss.getProxy( appConfig );
			SyndFeed syndFeed = GetRss.getSyndFeed( serviceURL, proxy, authorization );
			
			//	(4)RSSをリクエストにセット
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

	private boolean isOAuthInvalid( OAuthBean oauth )
	{
		if( oauth == null )
		{
			addError( "BL.ERR.PARAM", "oauth" );
			return isError();
		}
		if( oauth.getConsumer_key().equals( "" ) )
		{
			addError( "BL.ERR.PARAM", "oauth.consumer_key" );
		}
		if( oauth.getConsumer_secret().equals( "" ) )
		{
			addError( "BL.ERR.PARAM", "oauth.consumer_secret" );
		}
		if( oauth.getOauth_token().equals( "" ) )
		{
			addError( "BL.ERR.PARAM", "oauth.oauth_token" );
		}
		if( oauth.getOauth_token_secret().equals( "" ) )
		{
			addError( "BL.ERR.PARAM", "oauth.oauth_token_secret" );
		}

		return isError();
	}

}

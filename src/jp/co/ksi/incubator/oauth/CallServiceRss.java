package jp.co.ksi.incubator.oauth;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ksi.eip.commons.struts.InvokeAction;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * OAuthアクセス・トークンを使ってRSSサービスを呼び出す習作
 * <pre>
 * サービス呼出に必要なOAuthは、以下
 * 　oauth_consumer_key ------ OAuthパラメータ
 * 　oauth_consumer_secret --- 署名キーに使用
 * 　oauth_token ------------- OAuthパラメータ
 * 　oauth_token_secret ------ 署名キーに使用
 * </pre>
 * @author kac
 * @since 2012/05/10
 * @version 2012/05/10
 * @see OAuthBaseBL
 */
public class CallServiceRss extends OAuthBaseBL
{
	private static Logger	log= Logger.getLogger( CallServiceRss.class );
	private String serviceURL= "";
	private String method= "GET";
	
	@Override
	public String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		DynaActionForm dyna = (DynaActionForm)form;
		log.debug( "oauth="+ oauth );
		//	oauthが正常かチェックする
		if( isOAuthValid() )
		{
			log.error( "oauth invalid." );
			addError( "BL.ERR.DEFAULT", getClass().getName(), "oauth invalid." );
			return APL_ERR;
		}
		serviceURL= dyna.getString( "serviceURL" );
		log.debug( "scope="+ serviceURL );
		if( serviceURL.equals( "" ) )
		{
			log.error( "serviceURL invalid." );
			addError( "BL.ERR.PARAM", "serviceURL invalid." );
			return APL_ERR;
		}
		String authorization = getAuthorization( oauth, method, serviceURL );
//		url+= "?"+ param +"&oauth_signature="+ signature;
		URL	u= new URL( serviceURL );
		URLConnection	con= u.openConnection( proxy );
		if( con instanceof HttpURLConnection )
		{
			HttpURLConnection http= (HttpURLConnection)con;
			http.setRequestMethod( method );
			http.setRequestProperty( "Authorization", authorization );
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
					addError( "BL.ERR.DEFAULT", getClass().getName(), wwwAuthenticate );
				}
			}
		}

		try
		{
			SyndFeedInput	in= new SyndFeedInput();
			SyndFeed	syndFeed= in.build( new XmlReader( con ) );
			log.debug( syndFeed );
			request.setAttribute( "syndFeed", syndFeed );
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), e.toString() );
			return APL_ERR;
		}

		return APL_OK;
	}

	public static String getAuthorization( OAuthBean oauth, String method, String serviceURL ) throws UnsupportedEncodingException,
			Exception
	{
		Properties	oauthParam= new Properties();
		oauthParam.setProperty( "oauth_consumer_key", URLEncoder.encode( oauth.getConsumer_key(), OAuthBean.UTF8 ) );
		oauthParam.setProperty( "oauth_nonce", oauth.getOauth_nonce() );
		oauthParam.setProperty( "oauth_signature_method", OAUTH_SIGNATURE_METHOD );
		oauthParam.setProperty( "oauth_timestamp", String.valueOf( System.currentTimeMillis() / 1000 ) );
		oauthParam.setProperty( "oauth_token", URLEncoder.encode( oauth.getOauth_token(), OAuthBean.UTF8 ) );
		oauthParam.setProperty( "oauth_version", "1.0" );

		//	oauthParamからauthorizationヘッダーを生成する
		//	signature生成用のパラメータストリングも生成する
		String	param= "";
		String	authorization= "OAuth ";
		String[]	names= new String[oauthParam.keySet().size()];
		names= (String[])oauthParam.keySet().toArray( names );
		java.util.Arrays.sort( names );
		for( int i= 0; i < names.length; i++ )
		{
			String	value= oauthParam.getProperty( names[i] );
			authorization+= ""+ names[i] +"=\""+ value +"\",";
			param+= "&"+ names[i] +"="+ value;
		}
		param= param.substring( 1 );
		log.debug( "param="+ param );
		authorization+= "oauth_signature=\""+ oauth.getSignature( method, serviceURL, param ) +"\"";
		log.debug( "authorization="+ authorization );
		return authorization;
	}
	
	boolean isOAuthValid()
	{
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

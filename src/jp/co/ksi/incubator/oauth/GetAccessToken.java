package jp.co.ksi.incubator.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

/**
 * OAuthアクセス・トークンを取得する習作
 * @author kac
 * @since 2012/03/06
 * @version 2012/04/10 はてな対応中
 * @see GetRequestToken
 */
public class GetAccessToken extends OAuthBaseBL
{
	private static Logger	log= Logger.getLogger( GetAccessToken.class );
	private String oauthVerifier= "";
	
	@Override
	public String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		DynaActionForm dyna = (DynaActionForm)form;
		oauthVerifier= (String)dyna.get( "oauth_verifier" );
		log.debug( "oauth="+ oauth );
		log.debug( "oauthVerifier="+ oauthVerifier );
		Properties	oauthParam= new Properties();
		oauthParam.setProperty( "oauth_consumer_key", URLEncoder.encode( oauth.getConsumer_key(), OAuthBean.UTF8 ) );
		oauthParam.setProperty( "oauth_nonce", oauth.getOauth_nonce() );
		oauthParam.setProperty( "oauth_signature_method", OAUTH_SIGNATURE_METHOD );
		oauthParam.setProperty( "oauth_timestamp", String.valueOf( System.currentTimeMillis() / 1000 ) );
		oauthParam.setProperty( "oauth_token", URLEncoder.encode( oauth.getOauth_token(), OAuthBean.UTF8 ) );
		oauthParam.setProperty( "oauth_verifier", URLEncoder.encode( oauthVerifier, OAuthBean.UTF8 ) );
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
		String	url= oauth.getAccessTokenURL();
		log.debug( "url="+ url );
		String method = "GET";
		//	signatureを生成する
		String	signature= oauth.getSignature( method, url, param );
		authorization+= "oauth_signature=\""+ signature +"\"";
		log.debug( "authorization="+ authorization );

		URL	u= new URL( url );
		URLConnection	con= u.openConnection( proxy );
		if( con instanceof HttpURLConnection )
		{
			HttpURLConnection http= (HttpURLConnection)con;
			http.setRequestMethod( method );
			http.setRequestProperty( "Authorization", authorization );
//			http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
//			http.setDoOutput( true );
//			PrintWriter	writer= new PrintWriter( new OutputStreamWriter( http.getOutputStream(), "utf-8" ) );
//			writer.flush();
//			writer.close();
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
					addError( "BL.ClsName.ERR.P001", getClass().getName(), wwwAuthenticate );
				}
			}
		}

		String	resText= "";
		BufferedReader	reader= new BufferedReader( new InputStreamReader( con.getInputStream() ) );
		String	line= reader.readLine();
		while( line != null )
		{
			log.debug( line );
			resText += line;
			line= reader.readLine();
		}
		reader.close();
		log.debug( resText );
		
		String	oauthToken= OAuthBean.getResponseParamValue( "oauth_token", resText );
		log.info( "oauthToken=["+ oauthToken +"]" );
		oauth.setOauth_token( oauthToken );
		request.setAttribute( "oauthToken", oauthToken );
		String	oauthTokenSecret = OAuthBean.getResponseParamValue( "oauth_token_secret", resText );
		log.info( "oauthTokenSecret=["+ oauthTokenSecret +"]" );
		oauth.setOauth_token_secret( oauthTokenSecret );
		request.setAttribute( "oauthTokenSecret", oauthTokenSecret );
		String	oauthSessionHandle = OAuthBean.getResponseParamValue( "oauth_session_handle", resText );
		log.info( "oauthSessionHandle=["+ oauthSessionHandle +"]" );
		request.setAttribute( "oauthSessionHandle", oauthSessionHandle );
		String	oauthExpiresIn = OAuthBean.getResponseParamValue( "oauth_expires_in", resText );
		log.info( "oauthExpiresIn=["+ oauthExpiresIn +"]" );
		request.setAttribute( "oauthExpiresIn", oauthExpiresIn );
		String	oauthAuthorizationExpiresIn = OAuthBean.getResponseParamValue( "oauth_authorization_expires_in", resText );
		log.info( "oauthAuthorizationExpiresIn=["+ oauthAuthorizationExpiresIn +"]" );
		request.setAttribute( "oauthAuthorizationExpiresIn", oauthAuthorizationExpiresIn );
		String	xoauthYahooGuid = OAuthBean.getResponseParamValue( "xoauth_yahoo_guid", resText );
		log.info( "xoauthYahooGuid=["+ xoauthYahooGuid +"]" );
		request.setAttribute( "xoauthYahooGuid", xoauthYahooGuid );

		return APL_OK;
	}
}

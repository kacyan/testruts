package jp.co.ksi.incubator.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import JP.co.ksi.util.URLEncoder;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import jp.co.ksi.eip.commons.struts.InvokeAction;

/**
 * OAuthリクエスト・トークンを取得する習作
 * <pre>
 * (1)OAuthを生成し、セッションに保存する
 * (2)requestTokenURLにリクエストを送る
 * (3)レスポンスからリクエスト・トークンを取得する
 * </pre>
 * @author kac
 * @since 2012/03/06
 * @version 2012/04/10 はてな対応
 * <pre>
 * YahooとGoogleのOAuthが使えるようになった。
 * oauth_signature_methodは、HMAC-SHA1が良いみたい
 * oauthパラメータは、Authorizationリクエストヘッダーで送るのが良いみたい
 * Googleのサンプルが一般的には推奨らしい。
 * またGoogleにはテストツールがある
 * http://googlecodesamples.com/oauth_playground/
 * 問題は、以下の２点
 * １．Yahooは、scopeがあるとエラー。Googleは、scopeがないとエラー
 * ２．Googleはxoauth_request_auth_url(リダイレクト先URL)を返さない
 * 
 * [2012/04/10] はてな対応
 * リクエストで送付するoauthパラメータは、URLエンコードしておく必要がある
 * レスポンスで返ってくるoauthパラメータはURLデコードする
 * </pre>
 */
public class GetRequestToken extends OAuthBaseBL
{
	static Logger	log= Logger.getLogger( GetRequestToken.class );

	@Override
	public String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		//	(1)oauthを生成し、セッションに保存する
		DynaActionForm dyna = (DynaActionForm)form;
		HttpSession	session= request.getSession();
		oauth= new OAuthBean();
		session.setAttribute( SESS_ATTR_OAUTH, oauth );
		oauth.setConsumer_key( dyna.getString( "consumer_key" ) );
		oauth.setConsumer_secret( dyna.getString( "consumer_secret" ) );
		oauth.setOauth_callback( dyna.getString( "oauth_callback" ) );
		oauth.setScope( dyna.getString( "scope" ) );
		oauth.setRequestTokenURL( dyna.getString( "requestTokenURL" ) );
		oauth.setAuthorizeTokenURL( dyna.getString( "authorizeTokenURL" ) );
		oauth.setAccessTokenURL( dyna.getString( "accessTokenURL" ) );
		log.debug( "oauth="+ oauth );
		
		//	(2)シグネチャ用パラメータを生成する
		Properties	oauthParam= new Properties();
		oauthParam.setProperty( "oauth_callback", URLEncoder.encode( oauth.getOauth_callback(), OAuthBean.UTF8 ) );
		oauthParam.setProperty( "oauth_consumer_key", URLEncoder.encode( oauth.getConsumer_key(), OAuthBean.UTF8 ) );
		oauthParam.setProperty( "oauth_nonce", oauth.getOauth_nonce() );
//		oauthParam.setProperty( "oauth_nonce", "4ff82451b1296c80369729c20d39492d" );
		oauthParam.setProperty( "oauth_signature_method", OAUTH_SIGNATURE_METHOD );
		oauthParam.setProperty( "oauth_timestamp", String.valueOf( System.currentTimeMillis() / 1000 ) );
		oauthParam.setProperty( "oauth_version", "1.0" );

		//	(3)oauthParamからauthorizationヘッダーを生成する
		//	(4)signature生成用のパラメータストリングも生成する
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
		String	url= oauth.getRequestTokenURL();
		String	method = "GET";
		if( oauth.getScope().length() > 0 )
		{
			url+= "?scope="+ URLEncoder.encode( oauth.getScope(), OAuthBean.UTF8 );
			param+= "&scope="+ URLEncoder.encode( oauth.getScope(), OAuthBean.UTF8 );
		}
		log.debug( "url="+ url );
		log.debug( "param="+ param );
		//	signatureを生成する
		String	signature= oauth.getSignature( method, oauth.getRequestTokenURL(), param );
		authorization+= "oauth_signature=\""+ signature +"\"";
		log.debug( "authorization="+ authorization );
		
		URL	u= new URL( url );
		URLConnection	con= u.openConnection( proxy );
		if( con instanceof HttpURLConnection )
		{
			HttpURLConnection http= (HttpURLConnection)con;
			http.setRequestMethod( method );
			http.setRequestProperty( "Authorization", authorization );
			//	送信ここまで
			
			log.debug( "responseCode="+ http.getResponseCode() );
			log.debug( http.getResponseMessage() );
			
			//	レスポンスヘッダを表示する
			//	エラーが発生した場合、WWW-Authenticateに詳細情報が返ってくる
			Map<String, java.util.List<String>>	headers= http.getHeaderFields();
			String[]	keys= new String[headers.keySet().size()];
			keys= (String[])headers.keySet().toArray( keys );
			for( int i= 0; i < keys.length; i++ )
			{
				log.debug( "[RES_HEADER] "+ keys[i] +"="+ headers.get( keys[i] ) );
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
		
		String	oauthToken = OAuthBean.getResponseParamValue( "oauth_token", resText );
		log.info( "oauthToken=["+ oauthToken +"]" );
		oauth.setOauth_token( oauthToken );
//		request.setAttribute( "oauthToken", oauthToken );
		String	oauthTokenSecret = OAuthBean.getResponseParamValue( "oauth_token_secret", resText );
		log.info( "oauthTokenSecret=["+ oauthTokenSecret +"]" );
		oauth.setOauth_token_secret( oauthTokenSecret );
//		request.setAttribute( "oauthTokenSecret", oauthTokenSecret );
		String	oauthExpiresIn = OAuthBean.getResponseParamValue( "oauth_expires_in", resText );
		log.info( "oauthExpiresIn=["+ oauthExpiresIn +"]" );
		request.setAttribute( "oauthExpiresIn", oauthExpiresIn );
		String	xoauthRequestAuthUrl = OAuthBean.getResponseParamValue( "xoauth_request_auth_url", resText );
		log.info( "xoauthRequestAuthUrl=["+ xoauthRequestAuthUrl +"]" );
		oauth.setXoauth_request_auth_url( xoauthRequestAuthUrl );
//		request.setAttribute( "xoauthRequestAuthUrl", xoauthRequestAuthUrl );
		String	oauthCallbackConfirmed = OAuthBean.getResponseParamValue( "oauth_callback_confirmed", resText );
		log.info( "oauthCallbackConfirmed=["+ oauthCallbackConfirmed +"]" );
		request.setAttribute( "oauthCallbackConfirmed", oauthCallbackConfirmed );
		log.debug( "oauth="+ oauth );
		return APL_OK;

	}
	
}

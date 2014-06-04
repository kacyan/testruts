package jp.co.ksi.incubator.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ksi.eip.commons.struts.InvokeAction;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

/**
 * OAuthアクセス・トークンを使ってサービスを呼び出す習作
 * <pre>
 * サービス呼出に必要なOAuthは、以下
 * 　oauth_consumer_key ------ OAuthパラメータ
 * 　oauth_consumer_secret --- 署名キーに使用
 * 　oauth_token ------------- OAuthパラメータ
 * 　oauth_token_secret ------ 署名キーに使用
 * 　scope ------------------- サービスのURL
 * </pre>
 * @author kac
 * @since 2012/03/09
 * @version 2012/05/09 パラメータ対応
 * @see OAuthBaseBL
 */
public class CallService extends OAuthBaseBL
{
	private static Logger	log= Logger.getLogger( CallService.class );
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
			return APL_ERR;
		}
		serviceURL= dyna.getString( "serviceURL" );
		log.debug( "scope="+ serviceURL );
		if( serviceURL.equals( "" ) )
		{
			serviceURL= oauth.getScope();
		}
		Properties	oauthParam= new Properties();
		oauthParam.setProperty( "oauth_consumer_key", URLEncoder.encode( oauth.getConsumer_key(), OAuthBean.UTF8 ) );
		//	2012/04/11 Kac はてなの場合、nonceも要る？
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
		String	url= serviceURL;
		//	signatureを生成する
		String	signature= oauth.getSignature( method, url, param );
		authorization+= "oauth_signature=\""+ signature +"\"";
		log.debug( "authorization="+ authorization );
//		url+= "?"+ param +"&oauth_signature="+ signature;
		URL	u= new URL( url );
		URLConnection	con= u.openConnection( proxy );
		if( con instanceof HttpURLConnection )
		{
			HttpURLConnection http= (HttpURLConnection)con;
			http.setRequestMethod( method );
			http.setRequestProperty( "Authorization", authorization );
			http.setRequestProperty( "Content-Type", "application/atom+xml" );
			
			if( method.equals( "POST" ) )
			{
				http.setDoOutput( true );
				PrintWriter	writer= new PrintWriter( new OutputStreamWriter( http.getOutputStream(), "utf-8" ) );
				for( Iterator<String> i= dyna.getMap().keySet().iterator(); i.hasNext(); )
				{
					String	name= i.next();
					String	value= dyna.getString( name );
					log.debug( "PARAM: "+ name +"="+ value );
					if( name.equals( "scope" ) )	continue;
					if( name.equals( "method" ) )	continue;
					
					writer.println( name +"="+ value );
				}
				writer.flush();
				writer.close();
			}
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
		request.setAttribute( "wsResponse", resText );
		request.setAttribute( "scope", serviceURL );
		request.getSession().setAttribute( "serviceURL", serviceURL );

		return APL_OK;
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

package jp.co.ksi.incubator.oauth2;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.arnx.jsonic.JSON;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import JP.co.ksi.eip.Auth;

import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.incubator.portlet.OAuthConfig;
import jp.co.ksi.testruts.bl.BaseBL;

/**
 * OAuth-2.0のアクセストークン取得処理(リフレッシュトークンを使う)
 * @author kac
 * @since 2012/06/14
 * @version 2012/07/23
 */
public class RefreshAccessToken extends BaseBL
{
	public static final String RESPONSE_DATA = "responseData";
	private static Logger	log= Logger.getLogger( RefreshAccessToken.class );

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		Auth	auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{
			auth= new Auth();
			request.getSession().setAttribute( "auth", auth );
		}
		HttpSession session = request.getSession();
		DynaActionForm	dyna= (DynaActionForm)form;
		
		OAuthConfig	oauthConfig= (OAuthConfig)session.getAttribute( "oauthConfig" );
		OAuth	oauth= (OAuth)session.getAttribute( SESS_ATTR_OAUTH );
		String	tokenURL= oauthConfig.getTokenURL();
		String	method= "POST";
		String	refresh_token= oauth.getRefresh_token();
		String	client_id= oauthConfig.getClient_id();
		String	client_secret= oauthConfig.getClient_secret();
		String	grant_type= "refresh_token";
		StringBuffer	postData= new StringBuffer();
		postData.append( URLEncoder.encode( "refresh_token", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( refresh_token, ENC_UTF8 ) );
		postData.append( "&" );
		postData.append( URLEncoder.encode( "client_id", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( client_id, ENC_UTF8 ) );
		postData.append( "&" );
		postData.append( URLEncoder.encode( "client_secret", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( client_secret, ENC_UTF8 ) );
		postData.append( "&" );
		postData.append( URLEncoder.encode( "grant_type", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( grant_type, ENC_UTF8 ) );
		postData.append( "&" );
		log.debug( "["+ method +"] "+ tokenURL );
		log.debug( "["+ method +"] "+ postData.toString() );
		String	responseData= "";
		try
		{
			URL	u= new URL( tokenURL );
			URLConnection	con= u.openConnection( getProxy() );
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod( method );
			http.setDoOutput( true );
//			http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
			OutputStreamWriter	writer= new OutputStreamWriter( http.getOutputStream(), ENC_UTF8 );
			writer.write( postData.toString() );
			writer.flush();
			writer.close();

			//	レスポンスのjsonデータからoauthを生成する
			responseData= http.getResponseCode() +" "+ http.getResponseMessage();
			log.info( "responseMessage="+ responseData );
			log.info( "contentType="+ http.getContentType() );
			//	レスポンスヘッダを表示する
			Map	headers= http.getHeaderFields();
			String[]	keys= new String[headers.keySet().size()];
			keys= (String[])headers.keySet().toArray( keys );
			for( int i= 0; i < keys.length; i++ )
			{
				log.debug( "[ResHeader] "+ keys[i] +"="+ headers.get( keys[i] ) );
			}
			BufferedReader	reader= new BufferedReader( new InputStreamReader( con.getInputStream() ) );
			String	line= reader.readLine();
			responseData= "";
			while( line != null )
			{
//				log.debug( line );
				responseData+= line;
				line= reader.readLine();
			}
			reader.close();
			log.info( responseData );

			//	レスポンス(JSON)からoauthを取得する
			OAuth	recvOauth = JSON.decode( responseData, OAuth.class );	//受け取ったoauthの一時保管
			log.debug( recvOauth );
			oauth.setAccess_token( recvOauth.getAccess_token() );
			oauth.setExpires_in( recvOauth.getExpires_in() );
			oauth.setToken_type( recvOauth.getToken_type() );
			oauth.setUpdateDate( Calendar.getInstance().getTime() );
			request.setAttribute( "oauth2", oauth );

			session.setAttribute( SESS_ATTR_OAUTH, oauth );
		
		}
		catch( Exception e )
		{//	ERR
			log.error( method +" "+ tokenURL, e );
			return APL_ERR;
		}
		finally
		{
			request.setAttribute( "responseData", responseData );
		}

		String	oauthFile= oauthConfig.getOauthFile();
		if( oauthFile.equals( "" ) )
		{
			return APL_OK;
		}
		
		//	oauthをファイルに保存する
		FileOutputStream	out= null;
		try
		{
			out= new FileOutputStream( oauthFile );
			JSON.encode( oauth, out );
			log.info( "["+ auth.getUid() +"] oauth is saved "+ oauthFile );
			return APL_OK;
		}
		catch( Exception e )
		{//	ERR
			log.error( oauthFile, e );
			return APL_ERR;
		}
		finally
		{
			if( out != null )
			{//	後始末
				try
				{
					out.close();
				}
				catch( Exception e )
				{
					log.error( oauthFile, e );
				}
			}
		}
		
	}
	
}

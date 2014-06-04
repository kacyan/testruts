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
 * OAuth-2.0のアクセストークン取得処理
 * @author kac
 * @since 2012/05/31
 * @version 2012/07/23
 */
public class GetAccessToken extends BaseBL
{
	public static final String RESPONSE_DATA = "responseData";
	private static Logger	log= Logger.getLogger( GetAccessToken.class );

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		HttpSession session = request.getSession();
		//	パラメータからcodeを取得する
		DynaActionForm	dyna= (DynaActionForm)form;
		String	code= dyna.getString( "code" );
		String	state= dyna.getString( "state" );
		OAuthConfig	oauthConfig= (OAuthConfig)session.getAttribute( state );
		if( oauthConfig == null )
		{
			addError( "BL.ERR.DEFAULT", getClass().getName(), "oauthConfig="+ oauthConfig );
			log.error( APL_ERR +" oauthConfig="+ oauthConfig +", code="+ code +", state="+ state );
			return APL_ERR;
		}

		String	tokenURL= oauthConfig.getTokenURL();
		String	method= "POST";
		String	client_id= oauthConfig.getClient_id();
		String	client_secret= oauthConfig.getClient_secret();
		String	redirect_uri= oauthConfig.getRedirect_uri();
		String	grant_type= oauthConfig.getGrant_type();
		StringBuffer	postData= new StringBuffer();
		postData.append( URLEncoder.encode( "code", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( code, ENC_UTF8 ) );
		postData.append( "&" );
		postData.append( URLEncoder.encode( "client_id", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( client_id, ENC_UTF8 ) );
		postData.append( "&" );
		postData.append( URLEncoder.encode( "client_secret", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( client_secret, ENC_UTF8 ) );
		postData.append( "&" );
		postData.append( URLEncoder.encode( "redirect_uri", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( redirect_uri, ENC_UTF8 ) );
		postData.append( "&" );
		postData.append( URLEncoder.encode( "grant_type", ENC_UTF8 ) );
		postData.append( "=" );
		postData.append( URLEncoder.encode( grant_type, ENC_UTF8 ) );
		postData.append( "&" );
		log.debug( "["+ method +"] "+ tokenURL );
		log.debug( "["+ method +"] "+ postData.toString() );

		Auth	auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{
			auth= new Auth();
			request.getSession().setAttribute( "auth", auth );
		}
		String	responseData= "";
		OAuth	oauth= null;
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

			//	レスポンスを取得する
			String contentType = http.getContentType();
			responseData= http.getResponseCode() +" "+ http.getResponseMessage();
			log.info( "["+ auth.getUid() +"] responseMessage="+ responseData );
			log.info( "["+ auth.getUid() +"] contentType="+ contentType );
			if( log.isDebugEnabled() )
			{//	レスポンスヘッダを表示する
				Map	headers= http.getHeaderFields();
				String[]	keys= new String[headers.keySet().size()];
				keys= (String[])headers.keySet().toArray( keys );
				for( int i= 0; i < keys.length; i++ )
				{
					log.debug( "[ResHeader] "+ keys[i] +"="+ headers.get( keys[i] ) );
				}
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
			log.info( "["+ auth.getUid() +"] "+ RESPONSE_DATA +"="+ responseData );
			request.setAttribute( RESPONSE_DATA, responseData );
			
			if( contentType.matches( ".*json.*" ) )
			{//	レスポンス(JSON)からoauthを取得する
				oauth= JSON.decode( responseData, OAuth.class );
			}
			else if( contentType.matches( "text/plain.*" ) )
			{
				OAuthDataParser	parser= new ChankParser();
				oauth= parser.parse( responseData );
			}
			else
			{
				log.error( "not support." );
			}
			log.debug( oauth );
			oauth.setCreateDate( Calendar.getInstance().getTime() );
			request.setAttribute( "oauth2", oauth );
			
			//	セッションにも保存しておく
			session.setAttribute( "oauthConfig", oauthConfig );
			session.setAttribute( SESS_ATTR_OAUTH, oauth );
		}
		catch( Exception e )
		{//	ERR
			request.setAttribute( RESPONSE_DATA, responseData );
			log.error( method +" "+ tokenURL, e );
			return APL_ERR;
		}

		String	oauthFile= oauthConfig.getOauthFile();
		if( ( oauthFile == null ) || oauthFile.equals( "" ) )
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

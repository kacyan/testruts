package jp.co.ksi.incubator.oauth2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
 * OAuth-2.0を使ったサービス呼出の習作
 * @author kac
 * @since 2012/06/01
 * @version 2012/06/20
 */
public class CallService extends BaseBL
{
	private static Logger	log= Logger.getLogger( CallService.class );
	private OAuth oauth;

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		Auth	auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{
			auth= new Auth();
		}
		HttpSession session = request.getSession();
		DynaActionForm	dyna= (DynaActionForm)form;
		
		oauth= (OAuth)session.getAttribute( SESS_ATTR_OAUTH );
		if( oauth == null )
		{
			//	jsonファイルからoauthを読み込む
			String	oauthFile= "/home/portal/oauth/google/"+ auth.getUid() +".json";
			FileInputStream	in= null;
			try
			{
				in= new FileInputStream( oauthFile );
				oauth= JSON.decode( in, OAuth.class );
				log.info( "oauth is loaded "+ oauthFile );
			}
			catch( Exception e )
			{//	ERR
				log.error( "oauthFile="+ oauthFile, e );
				return APL_ERR;
			}
			finally
			{
				if( in != null )
				{//	後始末
					try
					{
						in.close();
					}
					catch (Exception e) {
						log.error( "oauthFile="+ oauthFile, e );
					}
				}
			}
			session.setAttribute( SESS_ATTR_OAUTH, oauth );
		}
		log.debug( "oauth="+ oauth );

		OAuthConfig	oauthConfig= (OAuthConfig)session.getAttribute( "oauthConfig" );

		String	serviceURL= dyna.getString( "serviceURL" );
		if( serviceURL.equals( "" ) )
		{
			serviceURL= oauthConfig.getServiceURL();
		}
		
		String	enc= "utf-8";
		log.debug( "serviceURL="+ serviceURL );
		String	method= "GET";
		String	responseData= "";
		try
		{
			URL	u= new URL( serviceURL );
			URLConnection	con= u.openConnection( getProxy() );
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod( method );
			log.debug( "method="+ method );
			http.setRequestProperty( "Authorization", oauth.getAuthorization() );
			log.debug( "Authorization="+ oauth.getAuthorization() );
			if( method.equalsIgnoreCase( "POST" ) )
			{
				http.setDoOutput( true );
//				http.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
				OutputStreamWriter	writer= new OutputStreamWriter( http.getOutputStream(), enc );
				writer.write( URLEncoder.encode( "access_token", enc ) );
				writer.write( "=" );
				writer.write( URLEncoder.encode( oauth.getAccess_token(), enc ) );
				writer.write( "&" );
				writer.flush();
				writer.close();
			}

			//	レスポンスデータを取得する
			responseData= http.getResponseCode() +" "+ http.getResponseMessage();
			log.info( "responseMessage="+ responseData );
			log.info( "contentType="+ http.getContentType() );
			//	レスポンスヘッダを表示する
			Map	headers= http.getHeaderFields();
			String[]	keys= new String[headers.keySet().size()];
			keys= (String[])headers.keySet().toArray( keys );
			for( int i= 0; i < keys.length; i++ )
			{
				log.debug( "[RES_HEADER] "+ keys[i] +"="+ headers.get( keys[i] ) );
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
			request.setAttribute( "responseData", responseData );
			return APL_OK;
		}
		catch( Exception e )
		{//	ERR
			log.error( method +" "+ serviceURL, e );
			request.setAttribute( "responseData", responseData );
			log.error( method +" "+ serviceURL, e );
			return APL_ERR;
		}

	}
	
}

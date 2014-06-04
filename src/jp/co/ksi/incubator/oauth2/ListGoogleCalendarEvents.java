package jp.co.ksi.incubator.oauth2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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
import jp.co.ksi.testruts.bl.BaseBL;

/**
 * OAuth-2.0を使ってGoogleCalendarEventsを取得する習作
 * @author kac
 * @since 2012/06/01
 * @version 2012/07/09 singleEventsを追加(orderBy時必須)
 * <pre>
 * 必須パラメータ：calendarId (=gmailアドレス)
 * オプション：
 * 	timeMin	指定日時以前はフィルタする 例：2012-07-01T00:00:00.000Z
 * 	timeMax	指定日時以降はフィルタする 例：2012-07-08T00:00:00.000Z
 * </pre>
 */
public class ListGoogleCalendarEvents extends BaseBL
{
	private static Logger	log= Logger.getLogger( ListGoogleCalendarEvents.class );
	private OAuth oauth;
	public static final String UTF8 = "utf-8";

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		Auth	auth= Auth.getAuth( request, appConfig );
		HttpSession session = request.getSession();
		DynaActionForm	dyna= (DynaActionForm)form;
		String	calendarId= dyna.getString( "calendarId" );
		String	maxResults= dyna.getString( "maxResults" );
		String	orderBy= dyna.getString( "orderBy" );
		String	singleEvents= dyna.getString( "singleEvents" );
		String	timeMax= dyna.getString( "timeMax" );
		String	timeMin= dyna.getString( "timeMin" );
		String	refresh= request.getParameter( "refresh" );
		if( refresh != null )
		{
			log.info( "refresh="+ refresh );
		}

		oauth= (OAuth)session.getAttribute( "oauth2" );
		if( oauth == null )
		{
			//	jsonファイルからoauthを読み込む
			String	oauthFile= "/home/portal/oauth/google/"+ auth.getUid() +".json";
			FileInputStream	in= null;
			try
			{
				in= new FileInputStream( oauthFile );
				oauth= JSON.decode( in, OAuth.class );
				log.debug( "oauth="+ oauth );
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
			session.setAttribute( "oauth2", oauth );
		}
		
		String	serviceURL= "https://www.googleapis.com/calendar/v3/calendars/"+ calendarId +"/events";
		log.debug( "serviceURL="+ serviceURL );
		String	method= "GET";
		if( maxResults.length() > 0 )
		{
			try
			{
				serviceURL += "?"+ "maxResults" +"="+ Integer.parseInt( maxResults );
			}
			catch( Exception e )
			{
			}
		}
		if( orderBy.length() > 0 )
		{
			serviceURL += "&"+ "orderBy" +"="+ URLEncoder.encode( orderBy, UTF8 );
		}
		if( singleEvents.length() > 0 )
		{
			serviceURL += "&"+ "singleEvents" +"="+ URLEncoder.encode( singleEvents, UTF8 );
		}
		if( timeMax.length() > 0 )
		{
			serviceURL += "&"+ "timeMax" +"="+ URLEncoder.encode( timeMax, UTF8 );
		}
		if( timeMin.length() > 0 )
		{
			serviceURL += "&"+ "timeMin" +"="+ URLEncoder.encode( timeMin, UTF8 );
		}
		String	responseData= "";
		int	responseCode;
		try
		{
			URL	u= new URL( serviceURL );
			URLConnection	con= u.openConnection( getProxy() );
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod( method );
			log.debug( "method="+ method );
			http.setRequestProperty( "Authorization", oauth.getAuthorization() );
			log.debug( "Authorization="+ oauth.getAuthorization() );

			//	レスポンスデータを取得する
			responseCode= http.getResponseCode();
			responseData= responseCode +" "+ http.getResponseMessage();
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
			if( ( responseCode == 401 ) && ( refresh == null ) )
			{//	401エラー：oauth再取得処理へ遷移する
				return APL_ERR +"_401";
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
			
			Map	map= JSON.decode( responseData, Map.class );
			request.setAttribute( "map", map );
			for( Iterator<String> i= map.keySet().iterator(); i.hasNext(); )
			{
				String	key= i.next();
				Object	value= map.get( key );
				log.debug( "[KacDebug] "+ key +"="+ value +" - "+ value.getClass().getName() );
			}
			ArrayList<String> items= (ArrayList<String>)map.get( "items" );
			if( items != null )
			{
				for( int i= 0; i < items.size(); i++ )
				{
					log.debug( items.get( i ) );
				}
			}
			
			return APL_OK;
		}
		catch( Exception e )
		{//	ERR
			request.setAttribute( "responseData", responseData );
			log.error( method +" "+ serviceURL, e );
			return APL_ERR;
		}

	}
	
}

package jp.co.ksi.incubator.portlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import JP.co.ksi.eip.Auth;

import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.incubator.oauth2.OAuth;
import jp.co.ksi.testruts.Const;
import jp.co.ksi.testruts.bl.BaseBL;

/**
 * グーグルカレンダーを取得するポートレット
 * @author kac
 * @since 2012/07/11
 * @version 2013/05/28 kubota.comアカウントにアクセスしてみる
 * <pre>
 * (1){serviceId}/{uid}.paramからserviceParamを読込む
 * (2){serviceId}/{uid}.jsonからoauthを読込む
 * (3)サービス用のリクエストを組み立てる
 * 　calendarId
 * 　maxResults
 * 　orderBy
 * 　timeMin, timeMax
 * (4)サービスを呼出し、レスポンスを取得する
 * 　200 -> APL_OK
 * 　401 -> APL_ERR_401
 * 　それ以外 APL_ERR
 * </pre>
 */
public class GoogleCalendarEventsList extends BaseBL
{
	private static Logger	log= Logger.getLogger( GoogleCalendarEventsList.class );
	protected Auth	auth;

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		String	baseFolder= appConfig.getProperty( Const.CONF_KEY_BASE_FOLDER, "/home/portal/data/portlet/" );
		auth= new Auth();
		auth.setUid( "s911013" );
		auth.setDn( "s911013" );
		request.getSession().setAttribute( "auth", auth );
		auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{//	auth不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "auth invalid.["+ auth +"]" );
			return APL_ERR;
		}
		
		String refresh= request.getParameter( "refresh" );
		
		//	(1){serviceId}/{uid}.paramからserviceParamを読込む
		String	serviceId= "googleCalendar";
		Properties	serviceParam= new Properties();
		String	serviceParamFile= baseFolder + serviceId +"/"+ auth.getUid() +".param";
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( serviceParamFile );
			serviceParam.load( in );
		}
		catch( Exception e )
		{
			log.error( serviceParamFile, e );
		}
		finally
		{
			log.debug( "serviceParam="+ serviceParam );
			if( in != null )
			{
				in.close();
			}
		}
		
		//	(2){serviceId}/{uid}.jsonからoauthを読込む
		OAuth	oauth= new OAuth();
		String	oauthFile= baseFolder + serviceId +"/"+ auth.getUid() +".json";
		in= null;
		try
		{
			in= new FileInputStream( oauthFile );
			oauth= JSON.decode( in, OAuth.class );
			request.setAttribute( "oauth2", oauth );
		}
		catch( Exception e )
		{
			log.error( oauthFile, e );
		}
		finally
		{
			log.debug( "oauth="+ oauth );
			if( in != null )
			{
				in.close();
			}
		}
		
		//	(3)サービス用のリクエストを組み立てる
		String	method= "GET";
		String	calendarId= serviceParam.getProperty( "calendarId", "" );
		String	serviceURL= "https://www.googleapis.com/calendar/v3/calendars/"+ URLEncoder.encode( calendarId, ENC_UTF8 ) +"/events?";
		log.debug( "serviceURL="+ serviceURL );
		String	maxResults= serviceParam.getProperty( "maxResults", "" );
		log.debug( "maxResults="+ maxResults );
		String	orderBy= serviceParam.getProperty( "orderBy", "" );
		log.debug( "orderBy="+ orderBy );
		String	singleEvents= serviceParam.getProperty( "singleEvents", "" );
		log.debug( "singleEvents="+ singleEvents );
		String	period= serviceParam.getProperty( "period", "" );
		log.debug( "period="+ period );

		String	timeMax= "";
		String	timeMin=  "";
		Calendar	cal= Calendar.getInstance();
		if( period.equalsIgnoreCase( "today" ) )
		{
			String	pattern= "yyyy-MM-dd'T'00:00:00.000'Z'";
			SimpleDateFormat	sdf= new SimpleDateFormat( pattern );
			timeMin= sdf.format( cal.getTime() );
			cal.add( Calendar.DAY_OF_MONTH, 1 );
			timeMax= sdf.format( cal.getTime() );
		}
		else if( period.equalsIgnoreCase( "this week" ) )
		{
			String	pattern= "yyyy-MM-dd'T'00:00:00.000'Z'";
			SimpleDateFormat	sdf= new SimpleDateFormat( pattern );
			int	dayOfWeek= cal.get( Calendar.DAY_OF_WEEK ) * -1;
			cal.add( Calendar.DAY_OF_MONTH, dayOfWeek );
			timeMin= sdf.format( cal.getTime() );
			cal.add( Calendar.DAY_OF_MONTH, 7 );
			timeMax= sdf.format( cal.getTime() );
		}
		else if( period.equalsIgnoreCase( "this month" ) )
		{
			String	pattern= "yyyy-MM-01'T'00:00:00.000'Z'";
			SimpleDateFormat	sdf= new SimpleDateFormat( pattern );
			timeMin= sdf.format( cal.getTime() );
			cal.add( Calendar.MONTH, 1 );
			timeMax= sdf.format( cal.getTime() );
		}
		else if( period.equalsIgnoreCase( "1 week" ) )
		{
			String	pattern= "yyyy-MM-dd'T'00:00:00.000'Z'";
			SimpleDateFormat	sdf= new SimpleDateFormat( pattern );
			timeMin= sdf.format( cal.getTime() );
			cal.add( Calendar.DAY_OF_MONTH, 7 );
			timeMax= sdf.format( cal.getTime() );
		}
		else if( period.equalsIgnoreCase( "1 month" ) )
		{
			String	pattern= "yyyy-MM-dd'T'00:00:00.000'Z'";
			SimpleDateFormat	sdf= new SimpleDateFormat( pattern );
			timeMin= sdf.format( cal.getTime() );
			cal.add( Calendar.MONTH, 1 );
			timeMax= sdf.format( cal.getTime() );
		}
		log.debug( "timeMin="+ timeMin +", timeMax="+ timeMax );
		
		if( maxResults.length() > 0 )
		{
			try
			{
				serviceURL += "maxResults" +"="+ Integer.parseInt( maxResults ) +"&";
			}
			catch( Exception e )
			{
				log.warn( e.toString() +"maxResults=["+ maxResults +"]" );
			}
		}
		if( orderBy.length() > 0 )
		{
			serviceURL += "orderBy" +"="+ URLEncoder.encode( orderBy, ENC_UTF8 ) +"&";
		}
		if( singleEvents.length() > 0 )
		{
			serviceURL += "singleEvents" +"="+ URLEncoder.encode( singleEvents, ENC_UTF8 ) +"&";
		}
		if( timeMax.length() > 0 )
		{
			serviceURL += "timeMax" +"="+ URLEncoder.encode( timeMax, ENC_UTF8 ) +"&";
		}
		if( timeMin.length() > 0 )
		{
			serviceURL += "timeMin" +"="+ URLEncoder.encode( timeMin, ENC_UTF8 ) +"&";
		}
		
		//	(4)サービスを呼出し、レスポンスを取得する
		String	responseData= "";
		int	responseCode;
		try
		{
			log.debug( "serviceURL="+ serviceURL );
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
			
			log.debug( responseData );
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

package jp.co.ksi.incubator.portlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
 * facebookのphotoを試してみる
 * @author kac
 * @since 2012/08/03
 * @version 2012/08/03
 * <pre>
 * (1){serviceId}/{uid}.paramからserviceParamを読込む
 * (2){serviceId}/{uid}.jsonからoauthを読込む
 * (3)サービス用のリクエストを組み立てる
 * 　access_token
 * (4)サービスを呼出し、レスポンスを取得する
 * 　200 -> APL_OK
 * 　401 -> APL_ERR_401
 * 　それ以外 APL_ERR
 * </pre>
 */
public class FacebookPhoto extends BaseBL
{
	private static Logger	log= Logger.getLogger( FacebookPhoto.class );
	protected Auth	auth;

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		String	baseFolder= appConfig.getProperty( Const.CONF_KEY_BASE_FOLDER, "/home/portal/data/portlet/" );
		auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{//	auth不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "auth invalid.["+ auth +"]" );
			return APL_ERR;
		}
		
		String refresh= request.getParameter( "refresh" );
		
		//	(1){serviceId}/{uid}.paramからserviceParamを読込む
		String	serviceId= "facebook";
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
			log.error( "serviceParam error "+ e.toString() +" file=["+ serviceParamFile +"]" );
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
			log.error( "oauth error "+ e.toString() +" file=["+ oauthFile +"]" );
			return APL_ERR;
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
		String	serviceURL= "https://graph.facebook.com/USER_ID/photos?access_token="+ URLEncoder.encode( oauth.getAccess_token(), ENC_UTF8 );
		serviceURL= "https://graph.facebook.com/me?access_token="+ URLEncoder.encode( oauth.getAccess_token(), ENC_UTF8 );
		log.debug( "serviceURL="+ serviceURL );
		
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

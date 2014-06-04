package jp.co.ksi.incubator.portlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
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

import net.arnx.jsonic.JSON;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import JP.co.ksi.eip.Auth;

import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.incubator.oauth2.OAuth;
import jp.co.ksi.testruts.Const;
import jp.co.ksi.testruts.bl.BaseBL;

/**
 * OAuth-2.0のアクセストークン再取得処理(汎用版)
 * @author kac
 * @since 2012/06/14
 * @version 2012/07/09
 * <pre>
 * 完全に汎用化・共通化するのは無理
 * oauthやoauthConfigの取得方法は、プロジェクトにより異なる、その為、その指定方法も異なる。
 * 今回で言えば、{serviceId}.properties, {serviceId}/{uid}.json 等がプロジェクト都合である
 * oauth及びoauthConfigの入手はプロジェクト固有と考えるべき
 * oauthとoauthConfigを使い、access_tokenを取得する処理を共通メソッド化する程度が良いと思われる(2012/07/09 Kac)
 * </pre>
 * <pre>
 * (1)パラメータからserviceIdを取得する
 * (2)serviceIdに該当するoauthを読込む
 * (3)serviceIdに該当するoauthConfigを読込む
 * (4)oauth.refresh_tokenとoauthConfigを使ってaccess_tokenリクエストを組み立てる
 * (5)POSTを行い、レスポンスを取得する
 * (6)oauthConfigからoauthFilePathを取得し、oauthをjson形式で保存する
 * </pre>
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
		String	baseFolder= appConfig.getProperty( Const.CONF_KEY_BASE_FOLDER, "/home/portal/data/portlet/" );
		Auth	auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{//	auth不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "auth invalid.["+ auth +"]" );
			return APL_ERR;
		}

		DynaActionForm	dyna= (DynaActionForm)form;
		String	serviceId= dyna.getString( "sid" );
		if( ( serviceId == null ) || serviceId.equals( "" ) )
		{//	sid不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "sid invalid.["+ serviceId +"]" );
			return APL_ERR;
		}
		
		//	(2){serviceId}/{uid}.jsonからoauthを読込む
		OAuth	oauth= new OAuth();
		String	oauthFile= baseFolder + serviceId +"/"+ auth.getUid() +".json";
		FileInputStream in= null;
		try
		{
			in= new FileInputStream( oauthFile );
			oauth= JSON.decode( in, OAuth.class );
		}
		catch( Exception e )
		{
			log.error( oauthFile, e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), "oauthFile invalid.["+ oauthFile +"]" );
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
		//	(3)serviceIdに該当するoauthConfigを読込む
		OAuthConfig	oauthConfig= new OAuthConfig();
		String	oauthConfigFile= baseFolder + serviceId +".properties";
		in= null;
		try
		{
			in= new FileInputStream( oauthConfigFile );
			oauthConfig.load( in );
			request.setAttribute( "oauthConfig", oauthConfig );
		}
		catch( Exception e )
		{
			log.error( oauthConfigFile, e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), "oauthConfigFile invalid.["+ oauthConfigFile +"]" );
			return APL_ERR;
		}
		finally
		{
			log.debug( "oauthConfig="+ oauthConfig );
			if( in != null )
			{
				in.close();
			}
		}
		
		oauth.setGrant_type( "refresh_token" );
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
			OAuth	recvOauth = JSON.decode( responseData, OAuth.class );	//受け取ったoauthの一時保管
			log.debug( recvOauth );
			oauth.setAccess_token( recvOauth.getAccess_token() );
			oauth.setExpires_in( recvOauth.getExpires_in() );
			oauth.setToken_type( recvOauth.getToken_type() );
			oauth.setUpdateDate( Calendar.getInstance().getTime() );
			request.setAttribute( "oauth2", oauth );
		}
		catch( Exception e )
		{//	ERR
			log.error( method +" "+ tokenURL, e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), e.toString() );
			return APL_ERR;
		}
		finally
		{
			request.setAttribute( "responseData", responseData );
		}

		//	oauthをファイルに保存する
		FileOutputStream	out= null;
		try
		{
			out= new FileOutputStream( oauthFile );
			JSON.encode( oauth, out );
			log.info( "oauth is saved "+ oauthFile );
			return APL_OK;
		}
		catch( Exception e )
		{//	ERR
			log.error( oauthFile, e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), e.toString() );
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

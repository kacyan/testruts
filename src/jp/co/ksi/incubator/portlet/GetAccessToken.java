package jp.co.ksi.incubator.portlet;

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
import jp.co.ksi.incubator.oauth2.OAuth;
import jp.co.ksi.testruts.Const;
import jp.co.ksi.testruts.bl.BaseBL;

/**
 * OAuth-2.0のアクセストークン取得処理(汎用版)
 * @author kac
 * @since 2012/05/31
 * @version 2012/07/24
 * <pre>
 * 汎用化の為、oauthConfigを導入した。
 * oauth取得処理で必要になる情報を前もってoauthConfigにセットする
 * oauthConfigをstateに紐付けてsessionに保存する事で、oauthConfigを受け渡す
 * </pre>
 * <pre>
 * (1)パラメータからstate,codeを取得する
 * (2)セッションからstateに該当するoauthConfigを取得する
 * 　無ければエラー終了
 * (3)codeとoauthConfigを使ってaccess_tokenリクエストを組み立てる
 * (4)POSTを行い、レスポンスを取得する
 * (5)oauthConfigからoauthFilePathを取得し、oauthをjson形式で保存する
 * </pre>
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
		String	baseFolder= appConfig.getProperty( Const.CONF_KEY_BASE_FOLDER, "/home/portal/data/portlet/" );
		Auth	auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{//	auth不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "auth invalid.["+ auth +"]" );
			return APL_ERR;
		}
		
		//	(1)パラメータからstate,codeを取得する
		DynaActionForm	dyna= (DynaActionForm)form;
		String	state= dyna.getString( "state" );
		String	code= dyna.getString( "code" );
		
		//	(2)セッションからstateに該当するoauthConfigを取得する
		HttpSession session = request.getSession();
		OAuthConfig	oauthConfig= (OAuthConfig)session.getAttribute( state );
		if( oauthConfig == null )
		{//	エラー終了
			log.error( "state invalid.["+ state +"]" );
			addError( "BL.ERR.DEFAULT", GetAccessToken.class, "state invalid.["+ state +"]" );
			return APL_ERR;
		}
		session.removeAttribute( state );
		request.setAttribute( "oauthConfig", oauthConfig );
		
		//	(3)codeとoauthConfigを使ってaccess_tokenリクエストを組み立てる
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
		String	responseData= "";
		OAuth	oauth= new OAuth();
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

			//	(4)POSTを行い、レスポンスを取得する
			responseData= http.getResponseCode() +" "+ http.getResponseMessage();
			String contentType = http.getContentType();
			log.info( "["+ auth.getUid() +"] responseMessage="+ responseData );
			log.info( "["+ auth.getUid() +"] contentType="+ contentType );
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
			log.info( "["+ auth.getUid() +"] "+ RESPONSE_DATA +"="+ responseData );
			request.setAttribute( RESPONSE_DATA, responseData );
			
			if( contentType.matches( "application/json.*" ) )
			{//	レスポンスのjsonデータからoauthを生成する
				oauth= JSON.decode( responseData, OAuth.class );
			}
			else if( contentType.matches( "text/plain.*" ) )
			{//	chankデータからoauthを生成する
				oauth.setAccess_token( getParamValue( responseData, "access_token" ) );
				oauth.setRefresh_token( getParamValue( responseData, "refresh_token" ) );
			}
			else
			{
				log.warn( "not support contentType="+ contentType );
			}
			log.debug( "oauth="+ oauth );
			oauth.setCreateDate( Calendar.getInstance().getTime() );
			oauth.setUpdateDate( Calendar.getInstance().getTime() );
			request.setAttribute( "oauth2", oauth );
		}
		catch( Exception e )
		{//	ERR
			request.setAttribute( RESPONSE_DATA, responseData );
			log.error( method +" "+ tokenURL, e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), e.toString() );
			return APL_ERR;
		}

		//	(5)oauthConfigからoauthFileを取得し、oauthをjson形式で保存する
		FileOutputStream	out= null;
		String	oauthFile= oauthConfig.getOauthFile() + auth.getUid() +".json";
		//	FIXME ファイルの保存方法はプロジェクト毎に異なる
		//	FIXME 今回は、AP側の認証とoauthは１対１で管理する必要がある
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
	
	public String getParamValue( String responseData, String name )
	{
		name+= "=";
		int	start= responseData.indexOf( name );
		if( start < 0 )
		{
			return "";
		}
		
		responseData= responseData.substring( start + name.length() );
		int end= responseData.indexOf( "&" );
		if( end < 0 )
		{
			return responseData;
		}
		
		return responseData.substring( 0, end );
	}
	
}

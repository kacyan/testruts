package jp.co.ksi.incubator.oauth2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Map;

import jp.co.ksi.incubator.portlet.OAuthConfig;

import net.arnx.jsonic.JSON;

import org.apache.log4j.Logger;

/**
 * OAuth-2.0を使用するWEBサービスを呼出す処理の基底クラス
 * @author kac
 * @since 2012/08/08
 * @version 2014/07/03
 * <pre>
 * 作っては見たものの使われてない？
 * 基底クラスを作った理由は、以下の処理が共通化できると思ったから...
 * ・サービス呼び出しで401認証エラーが返ってきた場合、トークンを再取得(refreshAccessToken)して再度サービス呼び出しを行う
 * ・サービス呼び出し処理(http通信)
 * でも責務がおかしな点もある
 * ・トークン再取得はやるのに、最初のoauth要求とトークン取得は別クラスでやってる
 * ・再取得するので、その後トークンを保存する処理も行う
 * ->保存処理は別指定できるようにしよう。例：setSaveOAuthProc()
 * ・oauthConfigに保存に関する情報が含まれてる
 * ・refreshAccessToken()内でoauth保存してるけど、どこかにあるgetAccessToken()と同じレベルにすべき
 * ・oauthConfigを使う処理はOAuthProcedureに纏めてはどうか？
 * 　↑あかん。拡張性を持たせたいのはload,saveのみ。requestToken,getToken,refreshTokenは共通処理だ。
 * 　これらのメソッド群の意味は？前者はアプリ都合、後者は決まり事だ。
 * 　決まり事だから共通クラス化する。
 * 　拡張性を持たせるために汎化を使う。
 * 　処理フローの決まり事はテンプレートメソッドパターン。
 * 　OAuthUtil -------- 決まり事の処理を共通メソッド化する
 * 　OAuthProcedure --- 拡張性のいる部分をメソッド化する
 * 　OAuthBaseService - 決まり事の処理フローをテンプレートメソッド化する
 * </pre>
 */
public class OAuthBaseService
{
	private static final String ENC_UTF8 = "utf-8";

	private static Logger	log= Logger.getLogger( OAuthBaseService.class );

	private OAuth	oauth;
	private OAuthConfig	oauthConfig;

	private String method;

	private String serviceURL;

	private String responseData;

	/**
	 * oauthを保存するクラス
	 */
	private OAuthProcedure	saveOAuthProcedure;

	public final OAuthProcedure getSaveOAuthProcedure() {
		return saveOAuthProcedure;
	}

	public final void setSaveOAuthProcedure(OAuthProcedure saveOAuthProcedure) {
		this.saveOAuthProcedure = saveOAuthProcedure;
	}

	/**
	 * oauth-2.0対応のWEBサービスを呼び出します。
	 * 401が返ってきたら、access_tokenを再取得して再度WEBサービスを呼び出します。
	 * @throws Exception
	 */
	public void callService() throws Exception
	{

		method = "GET";
		serviceURL = "https://graph.facebook.com/me/home?access_token="+ URLEncoder.encode( oauth.getAccess_token(), ENC_UTF8 );

		responseData = "";
		try
		{
			if( doRequest() == 401 )
			{
				refreshAccessToken();

				doRequest();
			}
		}
		catch( Exception e )
		{//	ERR
			log.error( method +" "+ serviceURL, e );
		}
	}

	public String getResponseData()
	{
		return responseData;
	}

	/**
	 * リクエストを送ります
	 * @return
	 * @throws Exception
	 */
	private int doRequest() throws Exception
	{
		log.debug( "serviceURL="+ serviceURL );
		URL	u= new URL( serviceURL );
		URLConnection	con= u.openConnection( getProxy() );
		HttpURLConnection http = (HttpURLConnection)con;
		log.debug( "method="+ method );
		http.setRequestMethod( method );
		log.debug( "Authorization="+ oauth.getAuthorization() );
		http.setRequestProperty( "Authorization", oauth.getAuthorization() );

		int responseCode= http.getResponseCode();
		responseData= responseCode +" "+ http.getResponseMessage();
		log.info( "responseData="+ responseData );
		log.info( "contentType="+ http.getContentType() );
		if( log.isDebugEnabled() )
		{//	レスポンスヘッダを表示する
			Map	headers= http.getHeaderFields();
			String[]	keys= new String[headers.keySet().size()];
			keys= (String[])headers.keySet().toArray( keys );
			for( int i= 0; i < keys.length; i++ )
			{
				log.debug( "[RES_HEADER] "+ keys[i] +"="+ headers.get( keys[i] ) );
			}
		}
		if( responseCode != 200 )
		{//	エラー終了
			return responseCode;
		}

		BufferedReader	reader= new BufferedReader( new InputStreamReader( con.getInputStream() ) );
		String	line= reader.readLine();
		responseData= "";
		while( line != null )
		{
//			log.debug( line );
			responseData+= line;
			line= reader.readLine();
		}
		reader.close();

		return responseCode;
	}

	private void refreshAccessToken() throws Exception
	{
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
		}
		catch( Exception e )
		{//	ERR
			log.error( method +" "+ tokenURL, e );
			throw e;
		}
		finally
		{
		}

		//	2014/07/03 oauthを保存する
		saveOAuthProcedure.saveOAuth( oauth, oauthConfig );

		//	TODO 2012/08/08 戻値は要らんか？案１．レスポンスコード
	}

	private Proxy getProxy()
	{
		return Proxy.NO_PROXY;
	}
}

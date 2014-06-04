package jp.co.ksi.incubator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.Proxy.Type;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * OAuthの習作　Yahoo編
 * @author kac
 * @since 2011/10/18
 * @version 2011/10/21
 * 
 * 手順はこんな感じ(分かったところまで)
 * (1)アプリをProviderに登録する
 * 　例：Yahoo(https://e.developer.yahoo.co.jp/dashboard/)にOAuthTestを登録
 * (2)コンシューマを登録する
 * 　例：アプリ登録時にOAuth利用をチェックするとコンシューマ登録出来る
 * 　　コンシューマ・キーとコンシューマ・シークレットが発行される
 * ここまで事前準備(プロバイダ毎にやり方は違う)
 * (3)リクエスト・トークンを取得する(C->P)
 * 　例：https://auth.login.yahoo.co.jp/oauth/v2/get_request_tokenにリクエストを送る
 * 　　パラメータとしてコンシューマ・キーとかを送る
 * 　　レスポンスデータとして以下が返ってくる
 * 　　　リクエスト・トークン、
 * 　　　リクエスト・トークン・シークレット、
 * 　　　認可要求のURL
 * (4)ユーザの認可を要求する(WEBの場合ブラウザ経由でRedirect)
 * 　例：https://auth.login.yahoo.co.jp/oauth/v2/request_auth
 * 　　パラメータとしてリクエスト・トークンとかを送る
 * 　　ユーザ認可のWEB画面が表示される
 * 　　プロバイダの認証を行い、認可を許可すると、リクエスト・べリファイ(６桁)が返ってくる
 * 　　WEBの場合、レスポンスもブラウザ経由でRedirect
 * (5)アクセス・トークンを取得する(C->P)
 * 　例：https://auth.login.yahoo.co.jp/oauth/v2/get_token
 * 　　パラメータにコンシューマ・キーやリクエスト・トークン・リクエスト・べリファイ等を送る
 * 　　レスポンスデータとして以下が返ってくる
 * 　　　アクセス・トークン
 * 　　　アクセス・トークン・シークレット
 * 　　　※Yahooの場合、セッション・ハンドラやGUIDも返ってくる(OAuthの拡張らしい)
 * (6)アクセス・トークンを使って、本当に呼び出したかったサービスを呼ぶ
 * 　バッチの場合、3-5の作業を別途行い、入手したアクセストークンを使うという事も出来るらしい
 * 　アクセストークンの有効期限を設定できるらしい
 * 
 * 多分、3と4で得られるリクエスト・トークンとリクエスト・べリファイは、
 * 3で送ったコンシューマ・キー専用なので、コンシューマ以外が悪用しても無効になると思われる
 * プロバイダの実装次第だけど
 * コンシューマ・キーとコンシューマ・シークレットが秘匿されている限り安全という事かな。
 * 
 * OAuth Consumer Key：
 * dj0yJmk9ODhNZm9hOGtsZVZPJmQ9WVdrOU5YQXhWbXhVTjJrbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD01Nw--
 * Consumer Secret：
 * 2f04a4a0505fed0f1ac5f9222339d631cb658240
 * 
 * oauth_token: kdqaenm
 * oauth_token_secret: ff0eb9e4c13831a7197dae7c0cb30d8d6e84227b
 * oauth_request_auth_url: https://auth.login.yahoo.co.jp/oauth/v2/request_auth?oauth_token=kdqaenm&oauth_callback_confirmed=true
 * 
 * OAuth Verifier:  3pbcy4
 */
public class OAuthYahooTest
{
	private static final String URL_REQ_TOKEN = "https://auth.login.yahoo.co.jp/oauth/v2/get_request_token";
	
	private static final String CONSUMER_KEY= "dj0yJmk9ODhNZm9hOGtsZVZPJmQ9WVdrOU5YQXhWbXhVTjJrbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD01Nw--";
	private static final String CONSUMER_SECRET= "2f04a4a0505fed0f1ac5f9222339d631cb658240";
	
	private static Logger	log= Logger.getLogger( OAuthYahooTest.class );

	private Proxy proxy;

	/**
	 * リクエスト・トークン：７桁の小英数字
	 */
	private String oauthToken;
	/**
	 * トークン・シークレット：16進の文字列
	 */
	private String oauthTokenSecret;
	/**
	 * リクエスト・トークンの有効期限(秒)。default=3600
	 */
	private String oauthExpiresIn;
	/**
	 * エンドユーザの認可要求のリクエストURL
	 */
	private String xoauthRequestAuthUrl;
	/**
	 * callback値が確認済である事をコンシューマに通知するための値。trueで確認済
	 */
	private String oauthCallbackConfirmed;

	private String oauthVerifier;
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		OAuthYahooTest	test= new OAuthYahooTest();
		test.initProxy();
		
		test.test1();
		test.test2();
//		test.oauthToken= "kdqaenm";
//		test.oauthTokenSecret= "ff0eb9e4c13831a7197dae7c0cb30d8d6e84227b";
//		test.oauthVerifier= "3pbcy4";
		test.test3();
	}
	
	/**
	 * リクエスト・トークンを取得します
	 */
	public void test1()
	{
		try
		{
			
			String	url= URL_REQ_TOKEN +"?oauth_consumer_key="+ CONSUMER_KEY
						+"&oauth_nonce="+ "ef3a091928d5491624c0ac54d697124422705091"
						+"&oauth_signature_method="+ "PLAINTEXT"
						+"&oauth_signature="+ URLEncoder.encode( URLEncoder.encode( CONSUMER_SECRET, "utf-8" ) +"&", "utf-8" )
						+"&oauth_timestamp="+ ( System.currentTimeMillis() / 1000 )
						+"&oauth_version="+ "1.0"
						+"&oauth_callback=oob"
						;
			
			URL	u= new URL( url );
			URLConnection	con= u.openConnection( proxy );
			if( con instanceof HttpURLConnection )
			{
				HttpURLConnection http= (HttpURLConnection)con;
				log.debug( "responseCode="+ http.getResponseCode() );
				log.debug( http.getResponseMessage() );
				
				//	レスポンスヘッダを表示する
				//	エラーが発生した場合、WWW-Authenticateに詳細情報が返ってくる
				Map<String,java.util.List<String>>	headers= http.getHeaderFields();
				String[]	keys= (String[])headers.keySet().toArray( new String[headers.size()] );
				for( int i= 0; i < keys.length; i++ )
				{
					log.debug( keys[i] +"="+ headers.get( keys[i] ) );
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
			
			oauthToken = resText.replaceAll( ".*oauth_token=([a-zA-Z0-9]+)(&.*|$)", "$1" );
			log.info( "oauthToken=["+ oauthToken +"]" );
			oauthTokenSecret = resText.replaceAll( ".*oauth_token_secret=([a-zA-Z0-9]+)(&.*|$)", "$1" );
			log.info( "oauthTokenSecret=["+ oauthTokenSecret +"]" );
			oauthExpiresIn = resText.replaceAll( ".*oauth_expires_in=([0-9]+)(&.*|$)", "$1" );
			log.info( "oauthExpiresIn=["+ oauthExpiresIn +"]" );
			xoauthRequestAuthUrl = resText.replaceAll( ".*xoauth_request_auth_url=(.+)(&.*|$)", "$1" );
			xoauthRequestAuthUrl= URLDecoder.decode( xoauthRequestAuthUrl, "utf-8" );
			log.info( "xoauthRequestAuthUrl=["+ xoauthRequestAuthUrl +"]" );
			oauthCallbackConfirmed = resText.replaceAll( ".*oauth_callback_confirmed=(.+)(&.*|$)", "$1" );
			log.info( "oauthCallbackConfirmed=["+ oauthCallbackConfirmed +"]" );
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
		}
		
		
	}

	public void initProxy()
	{
		//	proxyを初期化する
		proxy = new Proxy( Type.HTTP, new InetSocketAddress( "172.16.10.3", 3128 ) );
	}

	/**
	 * エンドユーザの認可を要求します
	 */
	public void test2()
	{
		try
		{
			
			String	url= xoauthRequestAuthUrl;
			
			URL	u= new URL( url );
			URLConnection	con= u.openConnection( proxy );
			if( con instanceof HttpURLConnection )
			{
				HttpURLConnection http= (HttpURLConnection)con;
				log.debug( "responseCode="+ http.getResponseCode() );
				log.debug( http.getResponseMessage() );
				
				//	レスポンスヘッダを表示する
				//	エラーが発生した場合、WWW-Authenticateに詳細情報が返ってくる
				Map<String,java.util.List<String>>	headers= http.getHeaderFields();
				String[]	keys= new String[headers.keySet().size()];
				keys= (String[])headers.keySet().toArray( keys );
				for( int i= 0; i < keys.length; i++ )
				{
					log.debug( keys[i] +"="+ headers.get( keys[i] ) );
				}
			}
			String	charset= "JISAutoDetect";
			String	contentType= con.getContentType();
			int	index= contentType.indexOf( "charset=" );
			if( index > 0 )
			{
				charset= contentType.substring( index+8 );
			}
			log.debug( charset );
			
			String	resText= "";
			BufferedReader	reader= new BufferedReader( new InputStreamReader( con.getInputStream(), charset ) );
			String	line= reader.readLine();
			while( line != null )
			{
				log.debug( line );
				resText += line;
				line= reader.readLine();
			}
			reader.close();
			
			log.debug( resText );
			
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
		}
		
	}

	/**
	 * アクセス・トークンを取得します
	 */
	public void test3()
	{
		try
		{
			String	url= "https://auth.login.yahoo.co.jp/oauth/v2/get_token"
				+"?oauth_consumer_key="+ CONSUMER_KEY
				+"&oauth_token="+ oauthToken
				+"&oauth_signature_method="+ "PLAINTEXT"
				+"&oauth_signature="+ URLEncoder.encode( URLEncoder.encode( CONSUMER_SECRET, "utf-8" ) +"&"+ URLEncoder.encode( oauthTokenSecret, "utf-8" ), "utf-8" )
				+"&oauth_timestamp="+ ( System.currentTimeMillis() / 1000 )
				+"&oauth_nonce="+ "ef3a091928d5491624c0ac54d697124422705091"
				+"&oauth_verifier="+ oauthVerifier
				+"&oauth_version=1.0"
				;

			URL	u= new URL( url );
			URLConnection	con= u.openConnection( proxy );
			if( con instanceof HttpURLConnection )
			{
				HttpURLConnection http= (HttpURLConnection)con;
				log.debug( "responseCode="+ http.getResponseCode() );
				log.debug( http.getResponseMessage() );
				
				//	レスポンスヘッダを表示する
				//	エラーが発生した場合、WWW-Authenticateに詳細情報が返ってくる
				Map<String,java.util.List<String>>	headers= http.getHeaderFields();
				String[]	keys= new String[headers.keySet().size()];
				keys= (String[])headers.keySet().toArray( keys );
				for( int i= 0; i < keys.length; i++ )
				{
					log.debug( keys[i] +"="+ headers.get( keys[i] ) );
				}
			}
			
			String	charset= "JISAutoDetect";
			String	contentType= con.getContentType();
			int	index= contentType.indexOf( "charset=" );
			if( index > 0 )
			{
				charset= contentType.substring( index+8 );
			}
			log.debug( charset );
			
			String	resText= "";
			BufferedReader	reader= new BufferedReader( new InputStreamReader( con.getInputStream(), charset ) );
			String	line= reader.readLine();
			while( line != null )
			{
				log.debug( line );
				resText += line;
				line= reader.readLine();
			}
			reader.close();
			log.debug( resText );
			
			//	アクセス・トークン
			oauthToken = resText.replaceAll( ".*oauth_token=([a-zA-z0-9%._-]+)(&.*|$)", "$1" );
			log.debug( "oauthToken=["+ oauthToken +"]" );
			//	アクセス・トークン・シークレット
			oauthTokenSecret = resText.replaceAll( ".*oauth_token_secret=([a-zA-Z0-9]+)(&.*|$)", "$1" );
			log.debug( "oauthTokenSecret=["+ oauthTokenSecret +"]" );
			//	セッション・ハンドラ
			String	oauthSessionHandle = resText.replaceAll( ".*oauth_session_handle=([0-9]+)(&.*|$)", "$1" );
			log.debug( "oauthSessionHandle=["+ oauthSessionHandle +"]" );
			//	アクセス・トークンの有効期限
			oauthExpiresIn = resText.replaceAll( ".*oauth_expires_in=([0-9]+)(&.*|$)", "$1" );
			log.debug( "oauthExpiresIn=["+ oauthExpiresIn +"]" );
			//	セッション・ハンドラの有効期限
			String	oauthAuthorizationExpiresIn = resText.replaceAll( ".*oauth_authorization_expires_in=([0-9]+)(&.*|$)", "$1" );
			log.debug( "oauthAuthorizationExpiresIn=["+ oauthAuthorizationExpiresIn +"]" );
			//	yahooGuid
			String yahooGuid= resText.replaceAll( ".*xoauth_yahoo_guid=(.+)(&.*|$)", "$1" );
			log.debug( "yahooGuid=["+ yahooGuid +"]" );

		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
		}
		finally
		{
			
		}
	}
	
}

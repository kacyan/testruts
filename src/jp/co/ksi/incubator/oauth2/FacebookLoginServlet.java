package jp.co.ksi.incubator.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.arnx.jsonic.JSON;

import org.apache.log4j.Logger;

import jp.co.ksi.eip.commons.servlet.Auth;
import jp.co.ksi.incubator.portlet.OAuthConfig;
import jp.co.ksi.testruts.bl.BaseBL;

/**
 * facebookのoauth認証でログインする習作
 * @author kac
 * @since 2014/02/18
 * @version 2014/02/14
 * <pre>
 * [処理の流れ]
 * (1)初期状態(OAuthサーバからのリダイレクトでない場合)は、loginPageにフォワードします
 * (2)codeを使ってアクセストークンを取得します
 * (3)アクセストークンを使ってユーザ情報を取得します
 * (4)認証成功したら、セッションを再生成して、authをセットします
 * (5)初期画面にリダイレクトします
 * 
 * [init-param]
 * oauthConfig -- (must)oauth設定ファイル(例：/WEB-INF/facebook.properties)
 * loginPage ---- ログイン画面のURL。(コンテキストを除いて指定する。default=/facebook/login.jsp)
 * startPage ---- ログイン後にリダイレクトするURL。(コンテキストを除いて指定する。default=/facebook/)
 * 
 * </pre>
 * <pre>
 * [考察]
 * (3)の処理は、oauthサーバ毎に異なる。
 * 
 * </pre>
 */
public class FacebookLoginServlet extends HttpServlet
{
	private static Logger	log= Logger.getLogger( FacebookLoginServlet.class );

	public static final String PARAM_NAME_CODE= "code";
	public static final String PARAM_NAME_STATE= "state";
	public static final String SESS_ATTR_NAME_STATE= "state";
	public static final String RESPONSE_DATA = "responseData";
	
	private static final String ENC_UTF8= "utf-8";

	/**
	 * oauth設定情報
	 * @see OAuthConfig
	 */
	private OAuthConfig oauthConfig;
	/**
	 * アプリの設定情報
	 */
	private Properties appConfig;
	/**
	 * ログインページのURL
	 */
	private String loginPage= "/facebook/login.jsp";
	/**
	 * ログイン後の初期ページのURL
	 */
	private String startPage= "/facebook/";
	
	
	@Override
	public void init() throws ServletException
	{
		ServletContext	context= getServletContext();
		appConfig= (Properties)context.getAttribute( BaseBL.CTX_ATTR_APP_CONFIG );
		
		ServletConfig config= getServletConfig();
		//	loginPage
		loginPage= config.getInitParameter( "loginPage" );
		
		//	oauthConfigをファイルから読み込む
		oauthConfig= new OAuthConfig();
		String	file= config.getInitParameter( "oauthConfig" );
		InputStream	in= null;
		try
		{
			in= context.getResourceAsStream( file );
			oauthConfig.load( in );
		}
		catch( Exception e )
		{
			log.error( "file="+ file, e );
		}
		finally
		{
			try
			{
				in.close();
			}
			catch( Exception e )
			{
			}
		}
		
		super.init();
	}

	@Override
	protected void service( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		ServletContext	context= getServletContext();
		request.setAttribute( "oauthConfig", oauthConfig );
		
		//	パラメータからcodeを取得する
		String	code= request.getParameter( PARAM_NAME_CODE );
		log.debug( "code="+ code );
		if( code == null )
		{//	エラー終了
			request.getRequestDispatcher( loginPage ).forward( request, response );
			return;
		}
		String	state= request.getParameter( PARAM_NAME_STATE );
		log.debug( "state="+ state );
		if( state == null )
		{//	エラー終了
			request.getRequestDispatcher( loginPage ).forward( request, response );
			return;
		}
		HttpSession session = request.getSession();
		String	sessionState= (String)session.getAttribute( SESS_ATTR_NAME_STATE );
		if( !state.equals( sessionState ) )
		{//	セッションのstateと値が異なる -> エラー終了
			request.getRequestDispatcher( loginPage ).forward( request, response );
			return;
		}

		String	tokenURL= oauthConfig.getTokenURL();
		String	method= "POST";
		String	client_id= oauthConfig.getClient_id();
		String	client_secret= oauthConfig.getClient_secret();
		String	redirect_uri= oauthConfig.getRedirect_uri();
		String	grant_type= oauthConfig.getGrant_type();
		StringBuffer	postData= new StringBuffer();
		postData.append( URLEncoder.encode( PARAM_NAME_CODE, ENC_UTF8 ) );
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
			log.info( "responseMessage="+ responseData );
			log.info( "contentType="+ contentType );
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
			log.info( RESPONSE_DATA +"="+ responseData );
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
		}
		catch( Exception e )
		{//	ERR
//			request.setAttribute( RESPONSE_DATA, responseData );
			log.error( method +" "+ tokenURL, e );
			response.sendError( 500, e.toString() );
			return;
		}

		//	facebookの認証APIにアクセスし、ユーザ情報を取得する
		method= "GET";
		String	serviceURL= "https://graph.facebook.com/me?access_token="+ oauth.getAccess_token();
		try
		{
			URL	u= new URL( serviceURL );
			URLConnection	con= u.openConnection( getProxy() );
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod( method );
			log.debug( "method="+ method );
			http.setRequestProperty( "Authorization", oauth.getAuthorization() );
			log.debug( "Authorization="+ oauth.getAuthorization() );
			responseData= http.getResponseCode() +" "+ http.getResponseMessage();
			log.info( "responseMessage="+ responseData );
			log.info( "contentType="+ http.getContentType() );
			//	レスポンスヘッダを表示する
			String	contextType= http.getContentType();
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
			log.info( RESPONSE_DATA +"="+ responseData );
			
			Auth	auth= new Auth();
			if( contextType.matches( "application/json.*" ) )
			{
				Properties	props= new Properties();
				props= JSON.decode( responseData, props.getClass() );
				auth.setUid( props.getProperty( "id", "" ) );
				auth.setName( props.getProperty( "name", "" ) );
				auth.setLoginTime( props.getProperty( "updated_time", "" ) );
//				BeanUtils.populate( auth, props );
				//	認証確立
				//	セッションを再生成する
				session.invalidate();
				session= request.getSession();
				session.setAttribute( Auth.SESS_ATTR_NAME_AUTH, auth );
			}
			else
			{
				log.info( responseData );
			}
			request.setAttribute( RESPONSE_DATA, responseData );
		}
		catch( Exception e )
		{
			log.error( method +" "+ serviceURL, e );
			response.sendError( 500, e.toString() );
			return;
		}
		
		//	成功したら、初期ページに遷移する
		String	contextPath= request.getContextPath();
		response.sendRedirect( contextPath + startPage );

	}
	
	protected Proxy getProxy()
	{
		Proxy	proxy= Proxy.NO_PROXY;
		String	proxyHost= appConfig.getProperty( "proxyHost", "" );
		String	proxyPort = appConfig.getProperty( "proxyPort", "" );
		try
		{
			proxy= new Proxy( Proxy.Type.HTTP, 
							new InetSocketAddress( proxyHost, Integer.parseInt( proxyPort ) )
							);
		}
		catch( Exception e )
		{//	proxy設定がおかしい場合はNO_PROXYにする
			log.debug( "no proxy "+ proxyHost +":"+ proxyPort );
			proxy= Proxy.NO_PROXY;
		}
		return proxy;
	}

}

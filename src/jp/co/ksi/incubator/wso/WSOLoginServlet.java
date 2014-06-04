package jp.co.ksi.incubator.wso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ksi.incubator.ssl.DummyHostnameVerifier;
import jp.co.ksi.incubator.ssl.DummyTrustManager;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import JP.co.ksi.ldap.LdapManager;
import JP.co.ksi.ldap.LdifProperties;
import JP.co.ksi.ldap.TimeUtil;

/**
 * WSOに対して代理認証を行うサーブレットの習作
 * @author kac
 * @since 2014/05/13
 * @version 2014/05/15
 * <pre>
 * wsoの認証条件は以下のとおりと思われる。
 * (1)WebSignOnクッキーでwsoSIDが送られている事
 * (2)ldapにwsoSIDに合致するセッションエントリが存在し、有効期限内である事
 * (3)Basic認証でuid/pwdが送付されており、セッションエントリのuid/pwdと合致する事
 * または、WebSignOnKeyクッキーが送付されおり、復号化するとuid/pwdに合致する事
 * 
 * 代理認証は以下の手順で実現できると思われる。
 * (1)ldapからユーザ情報を取得する
 * (2)ユーザ情報を元に、セッションエントリを生成する wsoSIDとpwdもランダム生成する
 * (3)wsoサーバにWebSignOnクッキーとuid/pwdのBasic認証情報を送り、set-cookieでWebSignOnKeyを受け取る
 * (4)ブラウザにWebSignOnクッキーとWebSignOnKeyクッキーをセットする
 * 
 * 注意点：
 * wsoから返されるWebSignOnKeyには / が含まれる場合があるが、これは認証エラーになる模様(バグと思われる)
 * 
 * </pre>
 */
public class WSOLoginServlet extends HttpServlet
{
	private static final String UTF_8= "utf-8";
	private static Logger	log= Logger.getLogger( WSOLoginServlet.class );

	@Override
	public void init() throws ServletException
	{
		// TODO 自動生成されたメソッド・スタブ
		super.init();
	}

	@Override
	protected void service( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		String	wsoBaseUrl= request.getHeader( "wso-base-url" );
		if( wsoBaseUrl == null )
		{
			response.sendError( HttpServletResponse.SC_BAD_REQUEST, "wso-base-url not found." );
			return;
		}
		String	remoteAddr= request.getRemoteAddr();
		String	user= request.getParameter( "user" );
		if( user == null )
		{
			log.info( "user="+ user );
			request.getRequestDispatcher( "/check.jsp" ).forward( request, response );
			return;
		}

		ServletContext	context= getServletContext();
		Properties	appConfig= (Properties)context.getAttribute( "appConfig" );
		LdapManager	ldap= new LdapManager();
		try
		{
			//	認証情報を確認する
			ldap.init( appConfig );
			String[]	userdn= ldap.searchEntry( "ou=People,ou=Master,o=WebSignOn,c=JP", "(uid="+ user +")", LdapManager.ONELEVEL_SCOPE );
			if( userdn.length != 1 )
			{
				log.info( "userdn.length="+ userdn.length );
				response.sendError( HttpServletResponse.SC_FORBIDDEN, "user not found." );
				return;
			}
			LdifProperties	userLdif= ldap.getEntry( userdn[0] );
			
			//	WSOの認証情報を生成する
			String	wsoSID= RandomStringUtils.random( 19, "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" );
			String	pwd= "password";
			
			LdifProperties sessLdif= getSessionLdif( context );
			sessLdif.put( "c", userLdif.getString( "c" ) );
			sessLdif.put( "sn", userLdif.getString( "sn" ) );
			sessLdif.put( "cn", URLEncoder.encode( userLdif.getString( "cn" ), UTF_8 ) );
			sessLdif.put( "userPassword", pwd );
			sessLdif.put( "wsoLastLogin", userLdif.getString( "wsoLastLogin" ) );
			sessLdif.put( "mail", userLdif.getString( "mail" ) );
			
			sessLdif.setDN( "wsoSID="+ wsoSID +",cn=wso3.os.ksi.co.jp,ou=Sessions,o=WebSignOn,c=JP" );
			sessLdif.put( "wsoSID", wsoSID );
			sessLdif.put( "wsoUser", userLdif.getDN() );
			String	current= TimeUtil.Calendar2LdapTime( Calendar.getInstance() );
			sessLdif.put( "wsoAccessTime", current );
			sessLdif.put( "wsoAuthTime", current );
			sessLdif.put( "wsoLoginTime", current );
//			sessLdif.put( "ipHostNumber", remoteAddr );
			log.debug( sessLdif );
			//	ユーザエントリのlastLoginを更新する
			ldap.modifyAttributeValue( userLdif.getDN(), "wsoLastLogin", current );
			
			//	LDAPに認証情報を生成する
			ldap.createEntry( sessLdif );
			
			String uid= userLdif.getString( "uid" );
			//	wsoにログインリクエストを送りログインを確立する
			String webSignOnKey= getWebSignOnKey( wsoBaseUrl, wsoSID, uid, pwd );
			while( webSignOnKey.matches( ".*/.*" ) )
			{
				webSignOnKey= getWebSignOnKey( wsoBaseUrl, wsoSID, uid, pwd );
			}

			//	cookieに認証情報をセットする
			String	domain= "ksi.co.jp";
			Cookie	cookie= new Cookie( "WebSignOn", wsoSID );
			cookie.setMaxAge( -1 );
			cookie.setDomain( domain );
			cookie.setPath( "/" );
			log.debug( "name="+ cookie.getName() +", value="+ cookie.getValue() +", domain="+ cookie.getDomain() +", path="+ cookie.getPath() );
			response.addCookie( cookie );
			Cookie	cookie2= new Cookie( "WebSignOnKey", webSignOnKey );
			cookie2.setMaxAge( -1 );
			cookie2.setDomain( domain );
			cookie2.setPath( "/" );
			log.debug( "name="+ cookie2.getName() +", value="+ cookie2.getValue() +", domain="+ cookie2.getDomain() +", path="+ cookie2.getPath() );
			response.addCookie( cookie2 );
			response.getWriter().println( "success!" );
//			response.sendRedirect( wsoBaseUrl );
		}
		catch( Exception e )
		{
			log.error( "["+ user +"]", e );
			response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString() );
		}
		finally
		{
			ldap.close();
		}
		
	}

	/**
	 * wsoにアクセスし、WebSignOnKeyを取得する
	 * @param wsoBaseUrl	wsoサーバのURL
	 * @param wsoSID	WebSinOnクッキー
	 * @param uid	ユーザID
	 * @param pwd	パスワード
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private String getWebSignOnKey( String wsoBaseUrl, String wsoSID, String uid, String pwd )
			throws MalformedURLException, IOException,
			NoSuchAlgorithmException, KeyManagementException
	{
		URL	url= new URL( wsoBaseUrl );
		HttpURLConnection	http= (HttpURLConnection)url.openConnection();
		if( http instanceof HttpsURLConnection )
		{
			HttpsURLConnection	https= (HttpsURLConnection)http;
			SSLContext		ssl= SSLContext.getInstance( "SSL" );
			KeyManager[]	km= {};
			TrustManager[]	tm= { new DummyTrustManager() };
			ssl.init( km, tm, null );
			https.setSSLSocketFactory( ssl.getSocketFactory() );
			https.setHostnameVerifier( new DummyHostnameVerifier() );
		}
		//	cookie WebSignOnをセット
		http.setRequestProperty( "Cookie", "WebSignOn="+ wsoSID );
		String	base64= uid +":"+ pwd;
		BASE64Encoder	encoder= new BASE64Encoder();
		base64= encoder.encode( base64.getBytes() );
		//	authorizationヘッダをセット
		http.setRequestProperty( "Authorization", "Basic "+ base64 );
		log.info( http.getResponseCode() +" "+ http.getResponseMessage() );
		Map<String,List<String>>	map= http.getHeaderFields();
		if( log.isDebugEnabled() )
		{
			for( Iterator<String> i= map.keySet().iterator(); i.hasNext(); )
			{
				String	name= i.next();
				Object	value= map.get( name );
				log.debug( name +"="+ value +" - "+ value.getClass().getName() );
			}
		}
		List<String>	cookies= map.get( "Set-Cookie" );
		log.debug( "cookies="+ cookies );
		String	webSignOnKey= "";
		for( int i= 0; i < cookies.size(); i++ )
		{
			log.debug( "cookie["+ i +"] "+ cookies.get( i ) );
			if( cookies.get( i ).matches( ".*WebSignOnKey=.*" ) )
			{
				webSignOnKey= cookies.get( i ).replaceAll( ".*WebSignOnKey=([a-zA-Z0-9\\+].*); path=.*", "$1" );
			}
		}
		return webSignOnKey;
	}

	/**
	 * セッションエントリのLDIF(テンプレート)を返します。
	 * @param context
	 * @return
	 */
	private LdifProperties getSessionLdif( ServletContext context )
	{
		LdifProperties	sessLdif= new LdifProperties();
		InputStream	in= null;
		try
		{
			in= context.getResourceAsStream( "/WEB-INF/session.ldif" );
			sessLdif.load( in );
		}
		catch( Exception e )
		{
			log.error( "", e );
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
		return sessLdif;
	}

}

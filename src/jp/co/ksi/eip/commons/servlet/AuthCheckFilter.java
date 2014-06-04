package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * 認証チェック用フィルター
 * @author kac
 * @since 2014/02/18
 * @version 2014/02/20
 */
public class AuthCheckFilter implements Filter
{
	private static Logger	log= Logger.getLogger( AuthCheckFilter.class );
	
	/**
	 * (must)ログインURL。未認証時このURLにリダイレクトします。
	 * (コンテキストを除いて指定する。例：/login)
	 */
	private String loginURL;
	/**
	 * 認証除外URLパターン。このパターンに一致するURLは認証チェックをしません。
	 * ( 正規表現で指定する。例：.*\.gif|.*\.jpg )
	 */
	private String exclude;

	@Override
	public void destroy()
	{
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void doFilter( ServletRequest req, ServletResponse res,
			FilterChain chain ) throws IOException, ServletException
	{
		if( !HttpServletRequest.class.isInstance( req ) )
		{
			return;
		}
		HttpServletRequest	request= (HttpServletRequest)req;
		if( !HttpServletResponse.class.isInstance( res ) )
		{
			return;
		}
		HttpServletResponse	response= (HttpServletResponse)res;
		
		//	認証チェック対象か調べる
		String	contextPath= request.getContextPath();
		log.debug( "contextPath="+ contextPath );
		String	requestURI= request.getRequestURI();
		log.debug( "requestURI="+ requestURI );
		String	path= requestURI.replaceAll( "^"+ contextPath, "" );
		log.debug( "path="+ path );
		if( path.matches( exclude ) )
		{//	認証除外
			//	後続処理へ
			chain.doFilter( req, res );
			return;
		}
		if( path.matches( loginURL ) )
		{//	loginURLも認証除外
			//	後続処理へ
			chain.doFilter( req, res );
			return;
		}

		//	セッションの認証情報を調べる
		HttpSession	session= request.getSession();
		Auth	auth= Auth.getAuth( session );
		if( auth.isValid() )
		{//	有効
			//	後続処理へ
			chain.doFilter( req, res );
			return;
		}
		
		//	ログイン先へ遷移する
		log.info( "[auth require] "+ requestURI +" -> "+ contextPath + loginURL );
		response.sendRedirect( contextPath + loginURL );
	}

	@Override
	public void init( FilterConfig config ) throws ServletException
	{
		loginURL= config.getInitParameter( "loginURL" );
		log.info( "loginURL="+ loginURL );
		exclude= config.getInitParameter( "exclude" );
		if( exclude == null )
		{
			exclude= loginURL;
		}
		log.info( "exclude="+ exclude );
	}

}

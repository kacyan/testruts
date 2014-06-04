package jp.co.ksi.incubator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * セマフォによる排他制御を実現するサーブレット・フィルター
 * @author kac
 * @since 2010/01/26
 * @version 2010/01/29
 */
public class SemaphoreFilter implements Filter
{
	private static Logger	log= Logger.getLogger( SemaphoreFilter.class );
	
	private ServletContext context;
	
	private Properties	semaphore= new Properties();
	private int	max= 5;
	
	public void destroy()
	{
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	public void init( FilterConfig config ) throws ServletException
	{
		context = config.getServletContext();
		try
		{
			max= Integer.parseInt( config.getInitParameter( "max" ) );
		}
		catch( Exception e )
		{
			log.debug( config.getInitParameter( "max" ), e );
		}
	}
	
	public void doFilter( ServletRequest req, ServletResponse res,
			FilterChain chain ) throws IOException, ServletException
	{
		log.debug( "remoteAddr="+ req.getRemoteAddr() );
		log.debug( "remoteHost="+ req.getRemoteHost() );
		log.debug( "remotePort="+ req.getRemotePort() );
		log.debug( "localAddr="+ req.getLocalAddr() );
		log.debug( "localName="+ req.getLocalName() );
		log.debug( "localPort="+ req.getLocalPort() );
		String characterEncoding = req.getCharacterEncoding();
		log.debug( "characterEncoding="+ characterEncoding );
		
		if( !HttpServletRequest.class.isInstance( req ) )
		{//	HttpServletRequestではない
			chain.doFilter( req, res );
			return;
		}
		HttpServletRequest	request= (HttpServletRequest)req;

		//	セマフォをチェックする
		String	sid= request.getSession().getId();
		if( semaphore.size() >= max )
		{//	総実行数が最大数に達している -> 実行しない
			res.setCharacterEncoding( characterEncoding );
			PrintWriter	out= res.getWriter();
			out.println( "semaphore is max.["+ semaphore.size() +"/"+ max +"]" );
			return;
		}
		if( semaphore.containsKey( sid ) )
		{//	同一セッションで複数回実行しようとしている -> 実行しない
			res.setCharacterEncoding( characterEncoding );
			PrintWriter	out= res.getWriter();
			out.println( "please wait a minutes.["+ sid +"="+ semaphore.getProperty( sid ) +"]" );
			return;
		}
		
		//	セマフォをインクリメントする
		String	user= request.getRemoteUser();
		if( user == null )
		{
			user= request.getRemoteAddr();
		}
		semaphore.setProperty( sid, user );
		log.debug( "add "+ sid +"="+ user +" ["+ semaphore.size() +"/"+ max +"]" );
		
		
		try
		{//	次の処理に回す
			chain.doFilter( req, res );
		}
		catch( Exception e )
		{//	例外発生
			log.debug( e.toString() );
		}
		finally
		{//	後始末
			//	セマフォをデクリメントする
			semaphore.remove( semaphore.size() -1 );
			log.debug( "remove "+ sid +"="+ user +" ["+ semaphore.size() +"/"+ max +"]" );
		}
	}
	
}

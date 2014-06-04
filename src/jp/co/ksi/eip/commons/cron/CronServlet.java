package jp.co.ksi.eip.commons.cron;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * サーブレットAPIでクーロン処理を実現する為のサーブレット
 * @author kac
 * @since 2009/03/16
 * @version 2010/02/16
 * <pre>
 * HTTPリクエストを受付けCronListenerをセッションにセットします
 * サーブレットの初期化処理で、クーロン用初期リクエストを発行します
 * </pre>
 */
public class CronServlet extends HttpServlet
{
	private static Logger	log= Logger.getLogger( CronServlet.class );

	/**
	 *
	 */
	protected void service( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		log.debug( "servletPath="+ request.getServletPath() );
		log.debug( "requestURI="+ request.getRequestURI() );
		
		//	コンテキストのクーロン情報を取得する
		ServletContext	context= getServletContext();
		CronInfo	info= (CronInfo)context.getAttribute( CronInfo.CTX_CRON_INFO );
		if( ( info == null ) || info.isStop() )
		{//	無いか停止中
			response.setContentType( "text/html" );
			PrintWriter	out= response.getWriter();
			out.println( "stop cron. cronInfo=["+ info +"]" );
			return;
		}
		
		//	クーロンリスナーをセッションにセットする
		CronListener	listener= new CronListener();
		listener.setStatus( CronListener.STATUS_RUNNING );
		listener.setUrl( request.getRequestURL().toString() );
		HttpSession	session= request.getSession();
		session.setAttribute( CronListener.SESS_NAME, listener );

		response.setContentType( "text/html" );
		PrintWriter	out= response.getWriter();
		out.println( "create new session.["+ new Date() +"]" );
	}

	/**
	 *
	 */
	public void init() throws ServletException
	{
		ServletContext	context= getServletContext();
		Enumeration<?>	enumeration= context.getAttributeNames();
		while( enumeration.hasMoreElements() )
		{
			String	name= (String)enumeration.nextElement();
			log.debug( name +"="+ context.getAttribute( name ) );
		}

		ServletConfig	config= getServletConfig();
		log.debug( "servletConfig="+ config.toString() );
		log.debug( "servletName="+ config.getServletName() );
		log.debug( "servletName="+ getServletName() );
		log.debug( "servletInfo="+ getServletInfo() );
		log.debug( "contextPath="+ context.getContextPath() );
		log.debug( "servletContextName="+ context.getServletContextName() );
		log.info( getServletName() +" initialized." );

		Properties	appConfig= new Properties();
		String	configFile= getInitParameter( "appConfig" );
		try
		{
			InputStream	in= context.getResourceAsStream( configFile );
			appConfig.load( in );
			in.close();
		}
		catch( Exception e )
		{
			log.error( "appConfig load error."+ configFile );
		}
		context.setAttribute( "appConfig", appConfig );

		//	CronInfoを初期化する
		CronInfo	info= new CronInfo();
		String	url= config.getInitParameter( "url" );
		if( url == null )
		{
			url= "http://localhost"+ context.getContextPath() + config.getServletName();
		}
		info.setServletURL( url );
		context.setAttribute( CronInfo.CTX_CRON_INFO, info );
		log.info( CronInfo.CTX_CRON_INFO +" initialized." );

		String	start= getInitParameter( "start" );
		if( "true".equalsIgnoreCase( start ) )
		{
//			DaemonListener.setDaemon( "http://localhost/testruts/DaemonServlet" );
			//	スレッドにしてみる
			KacThread	kac= new KacThread();
			kac.setUrl( url );
			kac.start();
		}

	}

}

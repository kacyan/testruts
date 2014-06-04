package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * パラメータurlで指定されたURLのコンテンツを返すサーブレット
 * 簡易リバースプロクシー
 * @author kac
 * @since 2012/08/01
 * @version 2021/08/01
 * <pre>
 * web.xml
 * init-param
 *  proxyHost=172.16.10.3
 * init-param
 *  proxyPort=3128
 * </pre>
 */
public class MiniProxyServlet extends HttpServlet
{
	private static Logger	log= Logger.getLogger( MiniProxyServlet.class );
	
	private Proxy proxy;

	@Override
	public void init( ServletConfig config ) throws ServletException
	{
		//	proxyを初期化する
		String	proxyHost= config.getInitParameter( "proxyHost" );
		String	proxyPort= config.getInitParameter( "proxyPort" );
		proxy = Proxy.NO_PROXY;
		try
		{
			proxy= new Proxy( Type.HTTP, new InetSocketAddress( proxyHost, Integer.parseInt( proxyPort ) ) );
		}
		catch( Exception e )
		{
			log.warn( "NO_PROXY ("+ proxyHost +":"+ proxyPort +") " + e.toString() );
			proxy= Proxy.NO_PROXY;
		}
		
		super.init( config );
	}

	@Override
	protected void service( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		String	url= request.getParameter( "url" );
		if( url == null )	url= "";
		
		try
		{
			URL	u= new URL( url );
			HttpURLConnection	http= (HttpURLConnection)u.openConnection( proxy );
			
			String contentType = http.getContentType();
			log.debug( "contentType="+ contentType );
			response.setContentType( contentType );
			int contentLength = http.getContentLength();
			log.debug( "contentLength="+ contentLength );
			response.setContentLength( contentLength );
			
			OutputStream	out= response.getOutputStream();
			InputStream	in= http.getInputStream();
			byte[]	b= new byte[4096];
			int	len= in.read( b );
			while( len > 0 )
			{
				out.write( b, 0, len );
				len= in.read( b );
			}
			out.close();
		}
		catch( Exception e )
		{
			log.error( url, e );
			response.setContentType( "text/plain" );
			PrintStream	out= new PrintStream( response.getOutputStream() );
			e.printStackTrace( out );
			out.close();
		}
		
	}
	
}

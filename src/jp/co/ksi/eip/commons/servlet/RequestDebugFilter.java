package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * リクエストをデバッグするためのフィルター
 * @author kac
 * @since ?
 * @version 2015/02/27
 */
public class RequestDebugFilter implements Filter
{
	private static Logger	log= Logger.getLogger( RequestDebugFilter.class );

	public void destroy()
	{
	}

	public void doFilter( ServletRequest req, ServletResponse res,
			FilterChain chain ) throws IOException, ServletException
	{
		log.info( "----+----1----+----2----+----3----+----4----+----5" );
		log.info( "remoteAddr="+ req.getRemoteAddr() );
		log.debug( "characterEncoding="+ req.getCharacterEncoding() );
		log.debug( "contentType="+ req.getContentType() );
		log.debug( "locale="+ req.getLocale() );
		log.debug( "protocol="+ req.getProtocol() );
		log.debug( "contentLength="+ req.getContentLength() );
		if( req instanceof HttpServletRequest )
		{
			HttpServletRequest request= (HttpServletRequest)req;
			log.info( "userAgent="+ request.getHeader( "user-agent" ) );
		}
		
		//	パラメータ
		Enumeration	enumeration= req.getParameterNames();
		while( enumeration.hasMoreElements() )
		{
			String	name= (String)enumeration.nextElement();
			String[]	values= req.getParameterValues( name );
			if( values != null )
			{
				for( int i= 0; i < values.length; i++ )
				{
					log.info( "[Param]"+ name +"="+ values[i] );
				}
			}
			else
			{
				log.info( "[Param]"+ name +"=null" );
			}
		}
		
		chain.doFilter( req, res );
	}

	public void init( FilterConfig config ) throws ServletException
	{
	}

}

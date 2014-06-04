package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class RequestDebugFilter implements Filter
{
	private static Logger	log= Logger.getLogger( RequestDebugFilter.class );

	public void destroy()
	{
	}

	public void doFilter( ServletRequest req, ServletResponse res,
			FilterChain chain ) throws IOException, ServletException
	{
		log.debug( "CharacterEncoding="+ req.getCharacterEncoding() );
		log.debug( "ContentType="+ req.getContentType() );
		log.debug( "Locale="+ req.getLocale() );
		log.debug( "Protocol="+ req.getProtocol() );
		log.debug( "ContentLength="+ req.getContentLength() );
		
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
					log.debug( "[Param]"+ name +"="+ values[i] );
				}
			}
			else
			{
				log.debug( "[Param]"+ name +"=null" );
			}
		}
		
		chain.doFilter( req, res );
	}

	public void init( FilterConfig config ) throws ServletException
	{
	}

}

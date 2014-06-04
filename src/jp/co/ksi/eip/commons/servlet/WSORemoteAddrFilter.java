package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * WSOヘッダを使ってRemoteAddrを返す為のサーブレット・フィルタ
 * @author kac
 * @since 2012/04/19
 */
public class WSORemoteAddrFilter implements Filter
{
	private static final String HEADER_WSO_REMOTE_ADDR = "WSO-Remote-Addr";
	private static Logger	log= Logger.getLogger( WSORemoteAddrFilter.class );

	public void destroy()
	{
	}

	public void doFilter( ServletRequest req, ServletResponse res,
			FilterChain chain ) throws IOException, ServletException
	{
		if( !HttpServletRequest.class.isInstance( req ) )
		{
			chain.doFilter( req, res );
			return;
		}

		HttpServletRequest request= (HttpServletRequest)req;
		
		String	wsoRemoteAddr= request.getHeader( HEADER_WSO_REMOTE_ADDR );
		if( wsoRemoteAddr == null )	wsoRemoteAddr= "";
		log.debug( "wsoRemoteAddr="+ wsoRemoteAddr );
		
		RewriteRequestWrapper	wrapper= new RewriteRequestWrapper( request );
		if( wsoRemoteAddr.length() > 0 )
		{//	wsoRemoteAddrがあればセットする
			wrapper.setRemoteAddr( wsoRemoteAddr );
			wrapper.setRemoteHost( wsoRemoteAddr );
		}
		
		chain.doFilter( wrapper, res );
	}

	public void init( FilterConfig config ) throws ServletException
	{
		log.debug( config );
	}

}

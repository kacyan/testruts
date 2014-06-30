package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


/**
 * Basic認証情報を使ってRemoteUserを返す為のサーブレット・フィルタ
 * @author kac
 * @since 2011/09/21
 * @version 2013/03/07
 * <pre>
 * [2013/03/07]
 * BASE64デコード処理を com.sun.BASE64Decoder から org.apache.commons.codec.binary.Base64 に変更した。
 * </pre>
 */
public class BasicRemoteUserFilter implements Filter
{
	private static final String BASIC_PREFIX = "[bB][aA][sS][iI][cC] ";
	private static final String HEADER_AUTHORIZATION = "authorization";
	private static Logger	log= Logger.getLogger( BasicRemoteUserFilter.class );

	public void destroy()
	{
	}

	public void doFilter( ServletRequest req, ServletResponse res,
			FilterChain chain ) throws IOException, ServletException
	{
		if( req instanceof HttpServletRequest )
		{
			HttpServletRequest request= (HttpServletRequest)req;

			String charset= request.getCharacterEncoding();
			log.debug( "charset="+ charset );
			
			String	authorization= request.getHeader( HEADER_AUTHORIZATION );
			if( authorization == null )	authorization= "";
			if( authorization.matches( BASIC_PREFIX +".*" ) )
			{//	Basic認証だ

				//	base64部分を取り出して、デコードする
				String	base64= authorization.replaceAll( BASIC_PREFIX, "" );
				log.debug( "base64="+ base64 );
				base64= new String( Base64.decodeBase64( base64.getBytes("US-ASCII") ), "US-ASCII" );

				//	ユーザID部分を取り出す
				String	user= base64.replaceAll( "(^.*):.*", "$1" );
				log.debug( "user="+ user );

				//	remoteUserを書き換える為のラッパーを生成する
				RewriteRequestWrapper	wrapper= new RewriteRequestWrapper( request );
				wrapper.setRemoteUser( user );
				wrapper.setAuthType( "Basic" );
				chain.doFilter( wrapper, res );
				return;
			}
		}

		chain.doFilter( req, res );
	}

	public void init( FilterConfig config ) throws ServletException
	{
		log.debug( config );
	}

}

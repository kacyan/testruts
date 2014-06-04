package jp.co.ksi.eip.commons.servlet;

import java.security.Principal;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

/**
 * リクエストを書き換える為のリクエスト・ラッパー
 * <pre>
 * 元々はRemoteUser専用でしたが、RemoteAddrや他のパラメータも書き換えれるように拡張しました。
 * </pre>
 * @author kac
 * @since 2011/09/21
 * @version 2012/04/19
 */
public class RewriteRequestWrapper extends HttpServletRequestWrapper
{
	private static Logger	log= Logger.getLogger( RewriteRequestWrapper.class );

	private String remoteUser= "";
	private String authType= "";
	private String remoteAddr;
	private String remoteHost;
	private int remotePort;
	private String scheme;

	public RewriteRequestWrapper( HttpServletRequest request )
	{
		super( request );
		remoteUser= request.getRemoteUser();
		authType= request.getAuthType();
		remoteAddr= request.getRemoteAddr();
		remoteHost = request.getRemoteHost();
		remotePort = super.getRemotePort();
		log.debug( "remotePort="+ remotePort );
		scheme = super.getScheme();
		log.debug( "scheme="+ scheme );
	}

	/**
	 * remoteUserをセットします
	 * @param user
	 */
	public void setRemoteUser( String user )
	{
		this.remoteUser= user;
	}
	
	@Override
	public String getRemoteUser()
	{
		return remoteUser;
	}

	/**
	 * authTypeをセットします
	 * @param authType
	 */
	public void setAuthType( String authType )
	{
		this.authType= authType;
	}
	
	@Override
	public String getAuthType()
	{
		return authType;
	}

	@Override
	public Principal getUserPrincipal()
	{
		Principal	principal= null;
		principal= super.getUserPrincipal();
		log.debug( "principal="+ principal );
		return principal;
	}

	@Override
	public boolean isUserInRole( String role )
	{
		log.debug( role +"="+ super.isUserInRole( role ) );
		return super.isUserInRole( role );
	}

	//	2012/04/19
	@Override
	public String getRemoteAddr()
	{
		return remoteAddr;
	}
	public void setRemoteAddr( String remoteAddr )
	{
		this.remoteAddr= remoteAddr;
	}

	@Override
	public String getRemoteHost()
	{
		return remoteHost;
	}
	public void setRemoteHost( String remoteHost )
	{
		this.remoteHost = remoteHost;
	}

	@Override
	public int getRemotePort()
	{
		return remotePort;
	}
	public void setRemotePort( int remotePort )
	{
		this.remotePort = remotePort;
	}

	@Override
	public String getScheme()
	{
		return scheme;
	}
	public void setScheme( String scheme )
	{
		this.scheme = scheme;
	}

	
}

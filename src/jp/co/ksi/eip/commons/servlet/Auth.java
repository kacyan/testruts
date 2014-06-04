package jp.co.ksi.eip.commons.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

public class Auth implements HttpSessionBindingListener
{
	private static Logger	log= Logger.getLogger( Auth.class );
	
	/**
	 * authを示すセッション属性名
	 */
	public static final String SESS_ATTR_NAME_AUTH= "auth";

	private String	uid;
	private String	name;
	private String	loginTime;
	
	/**
	 * デフォルト・コンストラクタ
	 */
	public Auth()
	{
		
	}
	/**
	 * サーブレットセッションからauthを取得します
	 * @param request
	 * @return auth。セッションに無い場合は空のauthを返します
	 */
	public static Auth getAuth( HttpSession session )
	{
		Auth	auth= (Auth)session.getAttribute( SESS_ATTR_NAME_AUTH );
		if( auth == null )
		{
			auth= new Auth();
		}
		return auth;
	}

	public String getUid()
	{
		return uid;
	}
	public void setUid( String uid )
	{
		this.uid= uid;
	}
	public String getName()
	{
		return name;
	}
	public void setName( String name )
	{
		this.name= name;
	}
	public String getLoginTime()
	{
		return loginTime;
	}
	public void setLoginTime( String loginTime )
	{
		this.loginTime= loginTime;
	}
	/**
	 * 有効かどうかを返します
	 * @return
	 */
	public boolean isValid()
	{
		if( uid == null )
		{
			return false;
		}
		else if( !uid.matches( "[a-zA-Z0-9]+" ) )
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public String toString()
	{
		return getUid() +"["+ getName() +"]";
	}
	@Override
	public void valueBound( HttpSessionBindingEvent event )
	{
		log.info( "["+ uid +"]"+ event );
	}
	@Override
	public void valueUnbound( HttpSessionBindingEvent event )
	{
		log.info( "["+ uid +"]"+ event );
	}
}

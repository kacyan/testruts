package jp.co.ksi.incubator.oauth;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import jp.co.ksi.eip.commons.struts.IStruts;
import jp.co.ksi.eip.commons.struts.InvokeAction;

/**
 * oauthの習作
 * @author kac
 * @since 2012/03/06
 * @version 2021/03/06
 * @see jp.co.ksi.incubator.OAuthYahooTest
 */
public class OAuthBaseBL implements IStruts
{
	
	public static final String SESS_ATTR_OAUTH = "oauth";
	public static final String CTX_ATTR_APP_CONFIG = "appConfig";
	public static final String OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
	
	private ActionMessages errorMessages;

	protected Proxy proxy;
	
	protected OAuthBean	oauth;

	private static Logger	log= Logger.getLogger( OAuthBaseBL.class );
	
	
	/* (非 Javadoc)
	 * @see jp.co.ksi.eip.commons.struts.IStruts#execute(jp.co.ksi.eip.commons.struts.InvokeAction, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String execute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		log.debug( "--- B/L start -----" );
		ServletContext	context= action.getServlet().getServletContext();
		Properties	appConfig= (Properties)context.getAttribute( CTX_ATTR_APP_CONFIG );

		errorMessages= new ActionMessages();
		
		String	proxyHost= appConfig.getProperty( "proxy.host", "172.16.10.3" );
		int	proxyPort= 3128;
		try
		{
			proxyPort= Integer.parseInt( appConfig.getProperty( "proxy.port", "3128" ) );
		}
		catch( Exception e )
		{
			log.warn( "proxyPort not integer."+ appConfig.getProperty( "proxy.port", "3128" ), e );
		}

		HttpSession	session= request.getSession();
		oauth= (OAuthBean)session.getAttribute( SESS_ATTR_OAUTH );
		if( oauth == null )
		{
			oauth= new OAuthBean();
			session.setAttribute( SESS_ATTR_OAUTH, oauth );
		}
		log.debug( "oauth="+ oauth );
		
		try
		{
			proxy= new Proxy( Type.HTTP, new InetSocketAddress( proxyHost, proxyPort ) );
			
			return subExecute( action, form, request, response );
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
			addError( "BL.ClsName.ERR.P001", getClass().getName(), e.toString() );
			return APL_ABEND;
		}
		finally
		{
			action.saveErrors( request, errorMessages );
		}
	}

	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		return APL_OK;
	}

	/**
	 * エラーメッセージがあるか？
	 * @return
	 */
	protected boolean isError()
	{
		return !errorMessages.isEmpty();
	}
	void addError( String key )
	{
		ActionMessage	msg= new ActionMessage( key );
		errorMessages.add( ActionMessages.GLOBAL_MESSAGE, msg );
	}
	void addError( String key, Object value1 )
	{
		ActionMessage	msg= new ActionMessage( key, value1 );
		errorMessages.add( ActionMessages.GLOBAL_MESSAGE, msg );
	}
	void addError( String key, Object value1, Object value2 )
	{
		ActionMessage	msg= new ActionMessage( key, value1, value2 );
		errorMessages.add( ActionMessages.GLOBAL_MESSAGE, msg );
	}

}
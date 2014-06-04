package jp.co.ksi.testruts.bl;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class BaseBL implements IStruts
{
	private static Logger	log= Logger.getLogger( BaseBL.class );
	
	public static final String CTX_ATTR_APP_CONFIG = "appConfig";

	public static final String ENC_UTF8 = "utf-8";

	public static final String SESS_ATTR_OAUTH = "oauth2";
	
	private ActionMessages errorMessages;

	protected Properties appConfig;

	/* (非 Javadoc)
	 * @see jp.co.ksi.eip.commons.struts.IStruts#execute(jp.co.ksi.eip.commons.struts.InvokeAction, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String execute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		log.debug( "----- start "+ getClass().getSimpleName() +" -----" );
		ServletContext	context= action.getServlet().getServletContext();
		appConfig = (Properties)context.getAttribute( CTX_ATTR_APP_CONFIG );

		errorMessages= new ActionMessages();
		
		try
		{
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
	protected void addError( String key )
	{
		ActionMessage	msg= new ActionMessage( key );
		errorMessages.add( ActionMessages.GLOBAL_MESSAGE, msg );
	}
	protected void addError( String key, Object value1 )
	{
		ActionMessage	msg= new ActionMessage( key, value1 );
		errorMessages.add( ActionMessages.GLOBAL_MESSAGE, msg );
	}
	protected void addError( String key, Object value1, Object value2 )
	{
		ActionMessage	msg= new ActionMessage( key, value1, value2 );
		errorMessages.add( ActionMessages.GLOBAL_MESSAGE, msg );
	}

	protected Proxy getProxy()
	{
		Proxy	proxy= Proxy.NO_PROXY;
		String	proxyHost= appConfig.getProperty( "proxyHost", "" );
		String	proxyPort = appConfig.getProperty( "proxyPort", "" );
		try
		{
			proxy= new Proxy( Proxy.Type.HTTP, 
							new InetSocketAddress( proxyHost, Integer.parseInt( proxyPort ) )
							);
		}
		catch( Exception e )
		{//	proxy設定がおかしい場合はNO_PROXYにする
			log.info( "no proxy "+ proxyHost +":"+ proxyPort );
			proxy= Proxy.NO_PROXY;
		}
		return proxy;
	}

}
package jp.co.ksi.incubator.portlet;

import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import JP.co.ksi.eip.Auth;

import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.testruts.Const;
import jp.co.ksi.testruts.bl.BaseBL;

/**
 * OAuth-2.0のoauthリクエストのフォームを生成する
 * @author kac
 * @since 2012/07/10
 * @version 2012/07/10
 * <pre>
 * (1)パラメータからserviceIdを取得する
 * (2)serviceIdに該当するoauthConfigを読込む
 * (3)stateを生成する
 * (4)oauthConfigをsessionに保存する
 * </pre>
 */
public class GetOAuthConfig extends BaseBL
{
	private static Logger	log= Logger.getLogger( GetOAuthConfig.class );

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		String	baseFolder= appConfig.getProperty( Const.CONF_KEY_BASE_FOLDER, "/home/portal/data/portlet/" );
		Auth	auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{//	auth不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "auth invalid.["+ auth +"]" );
			return APL_ERR;
		}
		
		//	(1)パラメータからserviceIdを取得する
		DynaActionForm	dyna= (DynaActionForm)form;
		String	serviceId= dyna.getString( "sid" );
		if( ( serviceId == null ) || serviceId.equals( "" ) )
		{//	sid不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "sid invalid.["+ serviceId +"]" );
			return APL_ERR;
		}
		
		//	(2)serviceIdに該当するoauthConfigを読込む
		OAuthConfig	oauthConfig= new OAuthConfig();
		String	oauthConfigFile= baseFolder + serviceId +".properties";
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( oauthConfigFile );
			oauthConfig.load( in );
		}
		catch( Exception e )
		{
			log.error( oauthConfigFile, e );
			return APL_ERR;
		}
		finally
		{
			log.debug( "loaded oauthConfig="+ oauthConfig );
			if( in != null )
			{
				in.close();
			}
		}

		 //	(3)stateを生成する
		String	state= serviceId +"."+ String.valueOf( System.currentTimeMillis() );
		oauthConfig.setState( state );
		log.debug( "generated state="+ state );
		
		 //	(4)oauthConfigをsessionに保存する
		HttpSession	session= request.getSession();
		session.setAttribute( state, oauthConfig );
		
		request.setAttribute( "oauthConfig", oauthConfig );
		
		return APL_OK;
	}
	
}

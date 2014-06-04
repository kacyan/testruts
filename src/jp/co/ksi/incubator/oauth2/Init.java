package jp.co.ksi.incubator.oauth2;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.incubator.portlet.OAuthConfig;
import jp.co.ksi.testruts.bl.BaseBL;


public class Init extends BaseBL
{

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		OAuthConfig	oauthConfig= new OAuthConfig();
		DynaActionForm	dyna= (DynaActionForm)form;
		HttpSession session = request.getSession();

		oauthConfig.setServiceURL( dyna.getString( "serviceURL" ) );
		oauthConfig.setAuthURL( dyna.getString( "authURL" ) );
		oauthConfig.setTokenURL( dyna.getString( "tokenURL" ) );
		oauthConfig.setClient_id( dyna.getString( "client_id" ) );
		oauthConfig.setClient_secret( dyna.getString( "client_secret" ) );
		oauthConfig.setResponse_type( dyna.getString( "response_type" ) );
		oauthConfig.setRedirect_uri( dyna.getString( "redirect_uri" ) );
		oauthConfig.setScope( dyna.getString( "scope" ) );
		String	state= dyna.getString( "state" );
		if( state.equals( "" ) )
		{
			state= String.valueOf( System.currentTimeMillis() );
		}
		oauthConfig.setState( state );
		oauthConfig.setAccess_type( dyna.getString( "access_type" ) );
		oauthConfig.setApproval_prompt( dyna.getString( "approval_prompt" ) );
		oauthConfig.setGrant_type( dyna.getString( "grant_type" ) );
		
		session.setAttribute( state, oauthConfig );
		request.setAttribute( "oauthConfig", oauthConfig );
		
		return APL_OK;
	}
	
}

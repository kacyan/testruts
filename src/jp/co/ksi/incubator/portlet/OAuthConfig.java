package jp.co.ksi.incubator.portlet;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

/**
 * oauth-2.0のaccess_token取得に必要な情報を持つクラス
 * @author kac
 * @since 2012/07/09
 * @version 2012/07/11
 */
public class OAuthConfig implements HttpSessionBindingListener
{
	private static Logger	log= Logger.getLogger( OAuthConfig.class );
	
	private String client_id= "";
	private String client_secret= "";
	private String redirect_uri= "";
	private String scope= "";
	private String state= "";
	private String response_type= "";
	private String access_type= "";
	private String approval_prompt= "";
	private String grant_type= "";
	private String authURL= "";
	private String tokenURL= "";
	private String serviceURL= "";
	private String oauthFile= "";
	
	public String getClient_id()
	{
		return client_id;
	}
	public void setClient_id( String clientId )
	{
		client_id = clientId;
	}
	public String getClient_secret()
	{
		return client_secret;
	}
	public void setClient_secret( String clientSecret )
	{
		client_secret = clientSecret;
	}
	public String getRedirect_uri()
	{
		return redirect_uri;
	}
	public void setRedirect_uri( String redirectUri )
	{
		redirect_uri = redirectUri;
	}
	public String getScope()
	{
		return scope;
	}
	public void setScope( String scope )
	{
		this.scope = scope;
	}
	public String getState()
	{
		return state;
	}
	public void setState( String state )
	{
		this.state = state;
	}
	public String getResponse_type()
	{
		return response_type;
	}
	public void setResponse_type( String responseType )
	{
		response_type = responseType;
	}
	public String getAccess_type()
	{
		return access_type;
	}
	public void setAccess_type( String accessType )
	{
		access_type = accessType;
	}
	public String getApproval_prompt()
	{
		return approval_prompt;
	}
	public void setApproval_prompt( String approvalPrompt )
	{
		approval_prompt = approvalPrompt;
	}
	public String getGrant_type()
	{
		return grant_type;
	}
	public void setGrant_type( String grantType )
	{
		grant_type = grantType;
	}
	public String getAuthURL()
	{
		return authURL;
	}
	public void setAuthURL( String authURL )
	{
		this.authURL = authURL;
	}
	public String getTokenURL()
	{
		return tokenURL;
	}
	public void setTokenURL( String tokenURL )
	{
		this.tokenURL = tokenURL;
	}
	public String getServiceURL()
	{
		return serviceURL;
	}
	public void setServiceURL( String serviceURL )
	{
		this.serviceURL = serviceURL;
	}
	public String getOauthFile()
	{
		return oauthFile;
	}
	public void setOauthFile( String oauthFile )
	{
		this.oauthFile = oauthFile;
	}
	
	public void load( InputStream in ) throws Exception
	{
		Properties	props= new Properties();
		try
		{
			props.load( in );
			BeanUtils.populate( this, props );
		}
		catch( Exception e )
		{
			log.error( "props="+ props, e );
			throw e;
		}
	}
	
	@Override
	public void valueBound( HttpSessionBindingEvent event )
	{
		log.debug( event );
	}
	@Override
	public void valueUnbound( HttpSessionBindingEvent event )
	{
		log.debug( event );
	}
	
}

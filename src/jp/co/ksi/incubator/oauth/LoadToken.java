package jp.co.ksi.incubator.oauth;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ksi.eip.commons.struts.InvokeAction;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

/**
 * OAuthアクセス・トークンを読み込んでみる習作
 * <pre>
 * サービス呼出に必要なOAuthは、以下
 * 　oauth_consumer_key ------ OAuthパラメータ
 * 　oauth_consumer_secret --- 署名キーに使用
 * 　oauth_token ------------- OAuthパラメータ
 * 　oauth_token_secret ------ 署名キーに使用
 * 　scope ------------------- サービスのURL
 * </pre>
 * @author kac
 * @since 2012/03/14
 * @version 2021/05/09 サービスURL対応
 * @see OAuthBaseBL
 */
public class LoadToken extends OAuthBaseBL
{
	private static Logger	log= Logger.getLogger( LoadToken.class );
	private String scope= "";
	
	@Override
	public String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
//		DynaActionForm dyna = (DynaActionForm)form;
		request.setAttribute( "scope", scope );
		request.setAttribute( "oauthToken", "" );
		request.setAttribute( "oauthTokenSecret", "" );
		request.setAttribute( "oauthSessionHandle", "" );
		request.setAttribute( "oauthExpiresIn", "" );
		request.setAttribute( "oauthAuthorizationExpiresIn", "" );
		request.setAttribute( "xoauthYahooGuid", "" );

		String	uid= request.getRemoteUser();
		if( uid == null )	uid= "hoge";

		ServletContext	context= action.getServlet().getServletContext();
		String	file= "/WEB-INF/"+ uid +".properties";
		file= context.getRealPath( file );
		log.debug( "file="+ file );
		
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( file );
			
			Properties	props= new Properties();
			props.load( in );
			
			BeanUtilsBean	util= new BeanUtilsBean();
			util.populate( oauth, props );
			log.debug( "oauth="+ oauth );
			//	サービスURL
			request.getSession().setAttribute( "serviceURL", props.getProperty( "serviceURL", "" ) );

			return APL_OK;
		}
		catch( Exception e )
		{
			log.error( file, e );

			return APL_ERR;
		}
		finally
		{
			if( in != null )
			{
				in.close();
			}
		}

	}
}

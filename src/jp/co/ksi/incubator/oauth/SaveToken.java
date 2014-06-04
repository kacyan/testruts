package jp.co.ksi.incubator.oauth;

import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ksi.eip.commons.struts.InvokeAction;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

/**
 * OAuthアクセス・トークンを保存してみる習作
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
public class SaveToken extends OAuthBaseBL
{
	private static Logger	log= Logger.getLogger( SaveToken.class );
	private String scope= "";
	
	@Override
	public String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
//		DynaActionForm dyna = (DynaActionForm)form;
		request.setAttribute( "scope", scope );
		String	uid= request.getRemoteUser();
		if( uid == null )	uid= "hoge";
		String	serviceURL= request.getParameter( "serviceURL" );
		if( serviceURL == null )	serviceURL= "";

		log.debug( "oauth="+ oauth );
		PropertyUtilsBean	util= new PropertyUtilsBean();
		Map	m= util.describe( oauth );
		log.debug( m );
		log.debug( m.getClass().getName() );
		Properties	props= new Properties();
		props.putAll( m );
		props.remove( "class" );
		//	サービスURL
		props.setProperty( "serviceURL", serviceURL );
		log.debug( props );
		
		ServletContext	context= action.getServlet().getServletContext();
		String	file= "/WEB-INF/"+ uid +".properties";
		file= context.getRealPath( file );
		log.debug( "file="+ file );
		
		FileOutputStream	out= null;
		try
		{
			out= new FileOutputStream( file );
			props.store( out, "OAuth" );

			return APL_OK;
		}
		catch( Exception e )
		{
			log.error( file, e );
			Enumeration	enumeration= props.keys();
			while( enumeration.hasMoreElements() )
			{
				Object	key= enumeration.nextElement();
				Object	value= props.get( key );
				if( !( key instanceof String ) )
				{
					log.error( "key["+ key +"]="+ key.getClass().getName()  );
				}
				if( !( value instanceof String ) )
				{
					log.error( "value["+ value +"]="+ value.getClass().getName()  );
				}
			}

			return APL_ERR;
		}
		finally
		{
			if( out != null )
			{
				out.close();
			}
		}

	}
}

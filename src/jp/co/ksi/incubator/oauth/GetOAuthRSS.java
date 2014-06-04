/**
 * 
 */
package jp.co.ksi.incubator.oauth;

import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import JP.co.ksi.eip.Auth;

import jp.co.ksi.eip.commons.struts.InvokeAction;

/**
 * @author kac
 *
 */
public class GetOAuthRSS extends OAuthBaseBL
{
	private static Logger	log= Logger.getLogger( GetOAuthRSS.class );
	
	/* (非 Javadoc)
	 * @see jp.co.ksi.eip.commons.struts.IStruts#execute(jp.co.ksi.eip.commons.struts.InvokeAction, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		ServletContext	context= action.getServlet().getServletContext();
		Properties	appConfig= (Properties)context.getAttribute( "appConfig" );
		Auth	auth= Auth.getAuth( request, appConfig );
		
		String	file= "/home/portal/data/user/"+ auth.getUid() +"/gmail.properties";
		
		OAuthBean	oauth= new OAuthBean();
		String	serviceURL= "";
		FileInputStream	in= null;
		try
		{
			Properties	props= new Properties();
			in= new FileInputStream( file );
			props.load( in );
			
			BeanUtilsBean	util= new BeanUtilsBean();
			util.populate( oauth, props );
			log.debug( "oauth="+ oauth );

			//	サービスURL
			serviceURL= props.getProperty( "serviceURL", "" );
		}
		catch( Exception e )
		{
			log.error( file, e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), file +" - "+ e.toString() );
			return APL_ERR;
		}
		finally
		{
			if( in != null )
			{
				in.close();
			}
		}
		
		String	method= "GET";
		//	authorizationヘッダーを生成する
		String	authorization= CallServiceRss.getAuthorization( oauth, method, serviceURL );
		
		try
		{
			URL	u= new URL( serviceURL );
			URLConnection	con= u.openConnection( proxy );
			if( con instanceof HttpURLConnection )
			{
				HttpURLConnection http= (HttpURLConnection)con;
				http.setRequestMethod( method );
				http.setRequestProperty( "Authorization", authorization );
				http.setRequestProperty( "Content-Type", "application/atom+xml" );
				http.setRequestProperty( "accept-language", "ja" );
				//	送信ここまで
				
				log.debug( "responseCode="+ http.getResponseCode() );
				log.debug( http.getResponseMessage() );
				
				//	レスポンスヘッダを表示する
				//	エラーが発生した場合、WWW-Authenticateに詳細情報が返ってくる
				Map	headers= http.getHeaderFields();
				String[]	keys= new String[headers.keySet().size()];
				keys= (String[])headers.keySet().toArray( keys );
				for( int i= 0; i < keys.length; i++ )
				{
					log.debug( keys[i] +"="+ headers.get( keys[i] ) );
					if( "WWW-Authenticate".equals( keys[i] ) )
					{
						String	wwwAuthenticate= headers.get( keys[i] ).toString();
						addError( "BL.ERR.DEFAULT", getClass().getName(), wwwAuthenticate );
					}
				}
			}
			
			SyndFeedInput	input= new SyndFeedInput();
			SyndFeed	syndFeed= input.build( new XmlReader( con ) );
			log.debug( syndFeed );
			request.setAttribute( "syndFeed", syndFeed );
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), e.toString() );
			return APL_ERR;
		}

		return APL_OK;
	}
	
}

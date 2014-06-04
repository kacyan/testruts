/**
 * 
 */
package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * このサーブレットは、WEBアプリ全体の初期化処理を行います。<br>
 * @author kac
 * @since 2010/11/17
 * @deprecated これからはInitContextListenerを使うほうが良さそうです
 * <pre>
 *  web.xmlの設定例
 *  &lt;servlet&gt;
 *   &lt;servlet-name&gt;InitServlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;jp.co.ksi.eip.acl.servlet.InitServlet&lt;/servlet-class&gt;
 *   &lt;init-param&gt;
 *    &lt;param-name&gt;properties&lt;/param-name&gt;
 *    &lt;param-value&gt;/WEB-INF/default.properties&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;init-param&gt;
 *    &lt;param-name&gt;ctxAttrName&lt;/param-name&gt;
 *    &lt;param-value&gt;webappConfig&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;init-param&gt;
 *    &lt;param-name&gt;debug&lt;/param-name&gt;
 *    &lt;param-value&gt;1&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 *  &lt;/servlet&gt;
 * </pre>
 */
public final class InitServlet extends HttpServlet
{
	public static final String SERVLET_INIT_PARAM_PROPERTIES= "properties";
	public static final String SERVLET_INIT_PARAM_CTX_ATTR_NAME= "ctxAttrName";
		
	private String properties= "";
	private String ctxAttrName= "";
	
	/**
	 * 
	 * (1)指定されたプロパティファイルを読み込み、指定されたキー名でコンテキストにセットします<br>
	 * (2)プロパティの値を用いて、Log4Jの初期化を行います<br>
	 */
	public void init() throws ServletException
	{
		super.init();
		
		//	servlet init-param
		properties= getInitParameter( SERVLET_INIT_PARAM_PROPERTIES );
		if( properties == null )
		{//	DEFAULT値をセットする
			properties= "/WEB-INF/default.properties";
		}
		ctxAttrName= getInitParameter( SERVLET_INIT_PARAM_CTX_ATTR_NAME );
		if( ctxAttrName == null )
		{//	DEFAULT値をセットする
			ctxAttrName= "webappConfig";
		}
		String	debug= getInitParameter( "debug" );
		if( debug == null )
		{//	DEFAULT値をセットする
			debug= "";
		}

		String scheme= "";
		try
		{
			scheme= properties.substring(0,5);
		}
		catch( RuntimeException e )
		{
			scheme= "";
		}

		//	propertiesを読み込む
		Properties props= null;
		try
		{
			if( scheme.equals("http:") )
			{//	URLだ
				props= loadProperties( new URL( properties ) );
			}
			else
			{//	ファイルとしましょ
				props= loadProperties( properties );
			}

			//	Log4jを初期化する
			PropertyConfigurator.configure( props );
			
			//	propertiesをcontextにセットする
			ServletContext	context= getServletContext();
			context.setAttribute( ctxAttrName, props );
			
			if( debug.equals("") || debug.equals( "0" ) || debug.equalsIgnoreCase( "false" ) )
			{
				
			}
			else
			{
				Object[]	key= props.keySet().toArray();
				Arrays.sort( key );
				for( int i= 0; i < key.length; i++ )
				{
					log( "["+ getServletName() +"] "+ key[i] +"="+ props.getProperty( (String)key[i] ) );
				}
			}
		}
		catch( MalformedURLException e )
		{
			log( "["+ getServletName() +"] init error. properties="+ properties, e );
		}
		catch( IOException e )
		{
			log( "["+ getServletName() +"] init error. properties="+ properties, e );
		}
		catch( Exception e )
		{
			log( "["+ getServletName() +"] init error. properties="+ properties, e );
		}
		
	}

	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		init();
		
		ServletContext	context= getServletContext();
		Properties	props= (Properties)context.getAttribute( ctxAttrName );
		
		response.setContentType( "text/plain; charset=utf-8" );
		props.list( response.getWriter() );
	}

	/**
	 * コンテキスト配下のファイルとしてプロパティを読み込みます
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private Properties loadProperties( String path ) throws Exception
	{
		Properties props= new Properties();;
		ServletContext	context= getServletContext();
		InputStream	in= context.getResourceAsStream( path );

		props.load( in );

		in.close();

		return props;
	}

	/**
	 * 指定されたURLからプロパティを読み込みます
	 * @param u
	 * @return
	 * @throws IOException
	 */
	private Properties loadProperties( URL u ) throws IOException
	{
		Properties	props= new Properties();
		URLConnection	con= u.openConnection();
		InputStream in= con.getInputStream();
		props.load( in );
		in.close();

		return props;
	}

}

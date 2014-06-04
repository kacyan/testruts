package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jp.co.ksi.eip.commons.util.KsiBeanUtilsResolver;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * コンテキストの初期化を行います
 * @author kac
 * @since 2011/09/21
 * @version 2014/05/15 BeanUtilsの脆弱性対応
 * @see KsiBeanUtilsResolver
 * @see BeanUtilsBean
 * @see Logger
 * @see ServletContextListener
 * <pre>
 * [web.xmlの記述例]
 *  &lt;context-param&gt;
 *   &lt;description&gt;properties&lt;/description&gt;
 *   &lt;param-name&gt;properties&lt;/param-name&gt;
 *   &lt;param-value&gt;/WEB-INF/testruts.properties&lt;/param-value&gt;
 *  &lt;/context-param&gt;
 *  
 *  &lt;filter&gt; .... &lt;/filter&gt;
 *  &lt;filter-mapping&gt; .... &lt;/filter-mapping&gt;
 *  
 *  &lt;listener&gt;
 *   &lt;listener-class&gt;jp.co.ksi.incubator.ContextListener&lt;/listener-class&gt;
 *  &lt;/listener&gt;
 * </pre>
 */
public class InitContextListener implements ServletContextListener,
		ServletContextAttributeListener
{
	private static Logger	log= Logger.getLogger( InitContextListener.class );
	private long createTime;
	
	public InitContextListener()
	{
		createTime= System.currentTimeMillis();
	}

	public void contextDestroyed( ServletContextEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
	}

	/**
	 * 設定ファイルを読み込み、log4jを初期化します
	 */
	public void contextInitialized( ServletContextEvent event )
	{
		ServletContext	context= event.getServletContext();
		String	contextPath= context.getContextPath();
		
		//	2014/05/15 Kac BeanUtils脆弱性対応
		BeanUtilsBean.getInstance().getPropertyUtils().setResolver( new KsiBeanUtilsResolver() );
		
		//	設定ファイルを読み込む
		Properties	props= new Properties();
		String	attrName= context.getInitParameter( "attrName" );
		if( attrName == null )	attrName= "appConfig";
		if( attrName.equals( "" ) )	attrName= "appConfig";
		String	file= context.getInitParameter( "properties" );
		if( file == null )
		{//	propertiesの指定が無ければ、何もしない
			return;
		}
		
		InputStream in= null;
		try
		{
			in = context.getResourceAsStream( file );
			props.load( in );
		}
		catch( IOException e )
		{
			System.out.println( "["+ contextPath +"] "+ e.toString() );
			e.printStackTrace();
		}
		finally
		{//	後始末
			try
			{
				in.close();
			}
			catch( Exception e )
			{
			}
		}

		//	log4jを初期化する
		PropertyConfigurator.configure( props );
		
		//	META-INF/MANIFEST.MFからバージョン情報を取得する
		String version = getManifestVersion( context );
		log.info( "---- "+ context.getServletContextName() +"("+ version +") initialized ----" );

		if( log.isDebugEnabled() )
		{
			String[]	keys= new String[props.size()];
			keys= (String[])props.keySet().toArray( keys );
			Arrays.sort( keys );
			for( int i= 0; i < keys.length; i++ )
			{
				log.debug( keys[i] +"="+ props.getProperty( keys[i] ) );
			}
		}

		//	コンテキストにセットする
		context.setAttribute( attrName, props );
		log.info( "set context attribute "+ attrName );
	}

	/**
	 * META-INF/MANIFEST.MFのバージョン情報を返します
	 * @param context
	 * @return
	 */
	private String getManifestVersion( ServletContext context )
	{
		String		version= "unknown";
		String		build= "unknown";
		InputStream	in= context.getResourceAsStream( "/META-INF/MANIFEST.MF" );
		try
		{
			Manifest	mf= new Manifest( in );
			Attributes	attr= mf.getMainAttributes();
			version= attr.getValue( Attributes.Name.SPECIFICATION_VERSION );
			build= attr.getValue( Attributes.Name.IMPLEMENTATION_VERSION );
		}
		catch( Exception e )
		{
			version= "Exception";
			build= e.toString();
		}
		finally
		{
			if( in != null )
			{
				try
				{
					in.close();
				}
				catch( Exception e )
				{
				}
			}
		}
		return version +"-"+ build;
	}

	public void attributeAdded( ServletContextAttributeEvent event )
	{
		log.debug( "["+ createTime +"]"+ event );
	}

	public void attributeRemoved( ServletContextAttributeEvent event )
	{
		log.debug( "["+ createTime +"]"+ event );
	}

	public void attributeReplaced( ServletContextAttributeEvent event )
	{
		log.debug( "["+ createTime +"]"+ event );
	}

}

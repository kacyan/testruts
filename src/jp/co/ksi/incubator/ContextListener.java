package jp.co.ksi.incubator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * コンテキストのイベント処理の習作
 * @author kac
 * @since 2009/03/17
 * @version 2009/03/17
 */
public class ContextListener implements ServletContextListener,
		ServletContextAttributeListener
{
	private static Logger	log= Logger.getLogger( ContextListener.class );
	private long createTime;
	
	public ContextListener()
	{
		createTime= System.currentTimeMillis();
		log.info( "["+ createTime +"]誰が生成してくれてる？" );
		System.out.println( "【ContextListener()】"+ createTime );
	}

	public void contextDestroyed( ServletContextEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
		System.out.println( "【contextDestroyed】" );
	}

	public void contextInitialized( ServletContextEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
		System.out.println( "【contextInitialized】" );
		ServletContext	context= event.getServletContext();
		//	設定ファイルを読み込む
		Properties	props= new Properties();
		String	file= context.getInitParameter( "properties" );
		if( file == null )	file= "/WEB-INF/default.properties";
		InputStream in= null;
		try
		{
			in = context.getResourceAsStream( file );
			props.load( in );
			//	log4jを初期化する
			PropertyConfigurator.configure( props );
			log.info( "---- log4j initialized ----" );
		}
		catch( IOException e )
		{
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
	}

	public void attributeAdded( ServletContextAttributeEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
	}

	public void attributeRemoved( ServletContextAttributeEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
	}

	public void attributeReplaced( ServletContextAttributeEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
	}

}

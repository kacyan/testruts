package jp.co.ksi.incubator;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * セッションのイベント処理の習作
 * @author kac
 * @since 2009/03/16
 * @version 2009/03/16
 * <pre>
 * セッションにセットしたオブジェクトに通知するリスナー
 * 　HttpSessionBindingListener
 * 　HttpSessionActivationListener
 * セッションにセットされたオブジェクトに通知される
 * それ以外は、コンテナがインスタンス化し、通知される
 * 誰が何時インスタンス化してるのだろう？
 * 初期化を行う方法は無いのだろうか？
 * </pre>
 */
public class SessionListener implements HttpSessionActivationListener,
		HttpSessionAttributeListener, 
		HttpSessionListener,
		HttpSessionBindingListener
{
	public static final String SESS_NAME= "jp.co.ksi.commons.servlet.SessionListener";
	
	private static Logger	log= Logger.getLogger( SessionListener.class );

	private int counter= 0;

	private long createTime;
	
	public SessionListener()
	{
		createTime= System.currentTimeMillis();
		log.info( "["+ createTime +"]誰が生成してくれてる？" );
	}
	
	public void sessionDidActivate( HttpSessionEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
	}

	public void sessionWillPassivate( HttpSessionEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
	}

	/**
	 * セッションに何かが追加されると呼ばれます。
	 * session.setAttribute()をコンテナが処理する途中で呼ばれる感じ...
	 * このメソッドでwaitをかけるとsession.setAttribute()が待たされる(tomcat5.5で実証済)
	 */
	public void attributeAdded( HttpSessionBindingEvent event )
	{
		counter++;
		log.info( "["+ createTime +"]"+ event.getName() +"="+ event.getValue() );
	}

	public void attributeRemoved( HttpSessionBindingEvent event )
	{
		log.info( "["+ createTime +"]"+ event.getName() +"="+ event.getValue() );
		counter--;
	}

	public void attributeReplaced( HttpSessionBindingEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
	}

	public void sessionCreated( HttpSessionEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
		Object	source= event.getSource();
		log.debug( "source=["+ source +"]" );
		Class[]	iface= source.getClass().getInterfaces();
		for( int i= 0; i < iface.length; i++ )
		{
			log.debug( "interface["+ i +"]="+ iface[i] );
		}
		Class	cls= source.getClass().getSuperclass();
		while( cls != null )
		{
			log.debug( "superclass="+ cls.getName() );
			cls= cls.getSuperclass();
		}
	}

	public void sessionDestroyed( HttpSessionEvent event )
	{
		log.info( "["+ createTime +"]"+ event );
	}

	/**
	 * セッションに追加されたオブジェクトに通知される
	 * session.setAttribute()をコンテナが処理する途中で呼ばれる感じ...
	 * このメソッドでwaitをかけるとsession.setAttribute()が待たされる(tomcat5.5で実証済)
	 * tomcat5.5での順番は、
	 * session.setAttribute()
	 * 　オブジェクトのvalueBound()
	 * 　リスナーのattributeAdded()
	 */
	public void valueBound( HttpSessionBindingEvent event )
	{
		log.info( "["+ createTime +"]"+ event.getName() +"="+ event.getValue() );
	}

	public void valueUnbound( HttpSessionBindingEvent event )
	{
		log.info( "["+ createTime +"]"+ event.getName() +"="+ event.getValue() );
	}

}

package jp.co.ksi.eip.commons.cron;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;

import jp.co.ksi.eip.commons.batch.TaskInvoker;

import org.apache.log4j.Logger;

/**
 * サーブレットAPIでクーロンを実現する為のセッションリスナー
 * @author kac
 * @since 2009/03/17
 * @version 2010/02/08
 * @see CronServlet
 * <pre>
 * valueBound()でTaskInvokerを実行し、任意のタスクを実行します
 * valueUnbound()でCronServletにリクエストを送り、再度セッションを生成します
 * CronListenerの動作はcontextのcronInfoで制御されます
 * </pre>
 */
public class CronListener implements HttpSessionActivationListener,
		HttpSessionBindingListener
{
	/**
	 * セッション属性名：CronListenerを示す
	 */
	public static final String SESS_NAME= "jp.co.ksi.commons.servlet.CronListener";
	/**
	 * CronListenerが稼働中である事を示す
	 */
	public static final String STATUS_RUNNING = "running";
	/**
	 * CronListenerが停止中である事を示す
	 */
	public static final String STATUS_STOP = "stop";

	private static Logger	log= Logger.getLogger( CronListener.class );

	/**
	 * このリスナーの状態
	 * <pre>
	 * 以下の値をとります
	 * STATUS_RUNNING	稼働中
	 * STATUS_STOP	停止中
	 * </pre>
	 * @deprecated 外部から制御可能にするため、cron情報はcontextに持つ
	 */
	private String status= "";

	/**
	 * 新しいセッションを生成する為にアクセスするURL
	 * @deprecated 外部から制御可能にするため、cron情報はcontextに持つ
	 */
	private String	url;

	/**
	 * 生成された時間
	 */
	private long createTime;

	public CronListener()
	{
		createTime= System.currentTimeMillis();
		log.debug( "["+ createTime +"]" );
	}

	public void sessionDidActivate( HttpSessionEvent event )
	{
		log.debug( "["+ createTime +"]"+ event );
	}

	/**
	 * ServletAPI2.3のJavaDocによると、セッションが非活性化される時に呼ばれます
	 * <pre>
	 * コンテキストが停止する時に呼ばれるようです。
	 * </pre>
	 */
	public void sessionWillPassivate( HttpSessionEvent event )
	{
		log.debug( "["+ createTime +"]"+ event +", url="+ url );
		//	これ以上セッションを再生成しないようにステータスを変更する
		status= STATUS_STOP;
	}

	/**
	 * セッションにオブジェクト(こいつ)がセットされる時に呼出されます
	 * <pre>
	 * (1)contextからcronInfoを取得する
	 * (2)cronInfoが稼働中なら、タスクを実行する
	 * (3)自身のstatusを稼働中にする
	 * </pre>
	 * TODO 2009/03/30 contextのcronInfoのstatusを見て、自身のstatusを変更する
	 */
	public void valueBound( HttpSessionBindingEvent event )
	{
		//	(1)contextからcronInfoを取得する
		ServletContext	context= event.getSession().getServletContext();
		CronInfo	info= (CronInfo)context.getAttribute( CronInfo.CTX_CRON_INFO );
		if( info == null )
		{//	初回だ
			log.debug( "cronInfo null." );
			info= new CronInfo();
//			context.setAttribute( CronInfo.CTX_DAEMON_INFO, info );
		}
		log.debug( "【KacDebug】"+ status +"-"+ info );
		info.setCount( info.getCount() +1 );
		if( STATUS_RUNNING.equals( status ) && info.isRunning() )
		{// (2)cronInfoが稼働中なら、タスクを実行する
			log.debug( "source="+ event.getSource() );
			log.debug( "value="+ event.getValue() );

			//	タスクの実行はTaskInvokerに任せる
			String invokerName = "jp.co.ksi.commons.batch.TaskInvoker";
			try
			{
				TaskInvoker	invoker= (TaskInvoker)Class.forName( invokerName ).newInstance();
				Properties	appConfig= (Properties)context.getAttribute( "appConfig" );
				if( appConfig == null )
				{
					appConfig= new Properties();
					log.debug( "appConfig created." );
				}
				invoker.execute( appConfig );
			}
			catch( Exception e )
			{//	TaskInvokerで例外発生
				log.fatal( "invokerName=["+ invokerName+ "]", e );
				//	TODO 2010/02/08 Kac TaskInvokerの実行エラーが一定回数に達したら、cronを停止させるか？
			}

			//	(3)自身のstatusを稼働中にする
			status= STATUS_RUNNING;
		}
		else
		{
			status= STATUS_STOP;
		}
		log.debug( "["+ createTime +"]"+ event.getName() +"="+ event.getValue() +" url="+ url +" status="+ status );
	}

	/**
	 * セッションにセットしたオブジェクト(こいつ)がセッションから除去される時に呼出されます
	 * <pre>
	 * (1)ステータスが稼働中なら、新しいセッションを生成する為にCronServletのURLにアクセスする
	 * </pre>
	 */
	public void valueUnbound( HttpSessionBindingEvent event )
	{
		log.debug( "["+ createTime +"]"+ event.getName() +"="+ event.getValue() +" url="+ url +" status="+ status );
		if( STATUS_RUNNING.equals( status ) )
		{//	稼働中
			//	(1)新しいセッションを生成する為にCronServletのURLにアクセスする
			//	(1)contextからcronInfoを取得する
			ServletContext	context= event.getSession().getServletContext();
			CronInfo	info= (CronInfo)context.getAttribute( CronInfo.CTX_CRON_INFO );
			setCron( info.getServletURL() );
		}
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus( String status )
	{
		this.status = status;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	/**
	 * @param url
	 */
	public static void setCron( String url )
	{
		try
		{
			log.debug( "start "+ url );
			URL	u= new URL( url );
			URLConnection	con= u.openConnection();
//			con.setConnectTimeout( 1000 );
//			log.debug( "setTimeout "+ url );
			con.getContent();
			log.debug( "done. "+ url );
		}
		catch( MalformedURLException e )
		{
			log.error( e.toString(), e );
		}
		catch( IOException e )
		{
			log.error( e.toString(), e );
		}
	}

}

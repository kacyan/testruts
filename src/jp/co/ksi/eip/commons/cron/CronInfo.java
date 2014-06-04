package jp.co.ksi.eip.commons.cron;

import java.util.Date;

/**
 * クーロンの情報を保持するクラス
 * @author kac
 * @since 2009/03/30
 * @version 2010/01/16
 */
public class CronInfo
{
	/**
	 * コンテキスト属性名：CronInfoを示す
	 */
	public static final String CTX_CRON_INFO = "jp.co.ksi.commons.cron.CronInfo";
	/**
	 * CronListenerが稼働中である事を示す
	 */
	public static final int STATUS_RUNNING = 1;
	/**
	 * CronListenerが停止中である事を示す
	 */
	public static final int STATUS_STOP = 0;
	
	/**
	 * cronの状態を示します
	 */
	private int status;
	/**
	 * cronの開始時刻
	 */
	private Date startTime;
	/**
	 * cronの実行回数
	 */
	private int count;
	/**
	 * cronがアクセスするURL
	 */
	private String	servletURL;
	
	/**
	 * デフォルト・コンストラクタ
	 */
	public CronInfo()
	{
		startTime= new Date();
		status= STATUS_RUNNING;
		count= 0;
		servletURL= "";
	}
	
	public int getStatus()
	{
		return status;
	}
	public void setStatus( int status )
	{
		this.status = status;
	}
	public boolean isRunning()
	{
		return status == STATUS_RUNNING;
	}
	public boolean isStop()
	{
		return status == STATUS_STOP;
	}
	public Date getStartTime()
	{
		return startTime;
	}
	public void setStartTime( Date startTime )
	{
		this.startTime = startTime;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount( int count )
	{
		this.count = count;
	}
	public void setServletURL( String url )
	{
		this.servletURL = url;
	}
	public String getServletURL()
	{
		return servletURL;
	}

	public String toString()
	{
		return "["+ status +"]"+ startTime +"("+ count +")";
	}

}

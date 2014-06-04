package jp.co.ksi.eip.commons.cron;


import org.apache.log4j.Logger;

public class KacThread extends Thread
{
	private static Logger	log= Logger.getLogger( KacThread.class );

	private String url = "http://localhost/testruts/CronServlet";


	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	/**
	 *
	 */
	public synchronized void start()
	{
		log.info( "started!" );

		super.start();
	}

	/**
	 *
	 */
	public void run()
	{
		log.info( "run step1" );
		CronListener.setCron( url );
		log.info( "run step2" );
	}

	/**
	 *
	 */
	public void destroy()
	{
		log.info( "destroy!" );
	}

}

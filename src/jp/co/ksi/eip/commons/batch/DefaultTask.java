package jp.co.ksi.eip.commons.batch;

import java.util.Properties;

import org.apache.log4j.Logger;

public class DefaultTask implements Task
{
	private static Logger	log= Logger.getLogger( DefaultTask.class );
	
	/**
	 * 共通の設定ファイル
	 */
	protected Properties config;

	public void setConfig( Properties config )
	{
		this.config = config;
	}

	/* (非 Javadoc)
	 * @see jp.co.ksi.commons.servlet.Task#execute()
	 */
	public String execute( Properties param ) throws Exception
	{
		log.debug( "done." );
		return TaskInvoker.APL_OK;
	}

	public String getResultInfo()
	{
		return "";
	}

}

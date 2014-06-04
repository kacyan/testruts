package jp.co.ksi.eip.commons.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * タスクを実行するクラス
 * @author kac
 * @since 2010/01/15
 * @version 2010/02/08
 * @see jp.co.ksi.eip.commons.batch.Task
 * @see jp.co.ksi.eip.commons.cron.CronListener
 */
public class TaskInvoker
{
	/**
	 * 実行するタスクのパラメータファイルを示すキー名：jp.co.ksi.commons.batch.TaskInvoker.taskParamFile
	 */
	private static final String KEY_TASK_PARAM_FILE = "jp.co.ksi.commons.batch.TaskInvoker.taskParamFile";
	/**
	 * 実行するタスクを示すキー名：jp.co.ksi.commons.batch.TaskInvoker.taskClsName
	 */
	private static final String KEY_TASK_CLS_NAME = "jp.co.ksi.commons.batch.TaskInvoker.taskClsName";
	/**
	 * TODO 2010/02/05 Kac タスクの実行可否ファイルのキー名：jp.co.ksi.commons.batch.TaskInvoker.taskStatFile
	 */
	public static final String KEY_TASK_STAT_FILE = "jp.co.ksi.commons.batch.TaskInvoker.taskStatFile";
	
	/**
	 * APL_OK
	 */
	public static final String APL_OK = "APL_OK";
	/**
	 * APL_ERR
	 */
	public static final String APL_ERR = "APL_ERR";
	/**
	 * APL_ABEND
	 */
	public static final String APL_ABEND = "APL_ABEND";
	/**
	 * APL_DISABLE
	 */
	public static final String APL_DISABLE = "APL_DISABLE";
	/**
	 * SUPPORT_ABEND
	 */
	public static final String SUPPORT_ABEND = "SUPPORT_ABEND";
	
	private static Logger	log= Logger.getLogger( TaskInvoker.class );

	/**
	 * 単体実行用のメイン
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	configFile= "test.properties";
		if( args.length > 0 )	configFile= args[0];
		
		Properties	appConfig= new Properties();
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( configFile );
			appConfig.load( in );
		}
		catch( Exception e )
		{
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}
		}
		PropertyConfigurator.configure( appConfig );
		
		TaskInvoker	invoker= new TaskInvoker();
		invoker.execute( appConfig );
	}
	
	/**
	 * TaskInvokerの実行メソッド(暫定)
	 * @param appConfig	実行時の環境設定情報
	 * @return
	 * <pre>
	 * このメソッドは、CronListenerから呼ばれるので、I/Fを変えるときは十分注意すること
	 * TODO 2010/01/15 Kac Taskは複数指定や、スケジュール制御できるようにする
	 * TODO 2010/01/15 Kac どのcronから実行されたかをログ出力するべきかも
	 * </pre>
	 */
	public String execute( Properties appConfig )
	{
		String	result= APL_OK;
		String clsName= "";
		try
		{
			//	TODO 2010/01/15 Kac 今は暫定で、invokeするTaskをappConfigから取得している
			clsName= appConfig.getProperty( KEY_TASK_CLS_NAME, "jp.co.ksi.commons.batch.DefaultTask" );
			//	TODO 2010/01/15 Kac 今は暫定で、taskParamをappConfigから取得している
			String paramFile= appConfig.getProperty( KEY_TASK_PARAM_FILE, clsName +".properties" );
			Properties taskParam = loadProperties( paramFile );
			//	TODO 2010/02/08 Kac タスク実行可否を...
			String	batchStatusFile= appConfig.getProperty( KEY_TASK_STAT_FILE, clsName +".stat" );
			if( BatchUtil.isEnable( batchStatusFile ) )
			{//	実行可
				result= invokeTask( clsName, appConfig, taskParam );
			}
			else
			{//	実行不可
				result= APL_DISABLE;
				log.info( "["+ result +"] "+ clsName );
			}
			
		}
		catch( Exception e )
		{//	SUPPORT_ABEND
			result= SUPPORT_ABEND;
			log.error( "["+ result +"] "+ clsName, e );
		}
		return result;
	}

	/**
	 * Taskクラスをinvokeします
	 * @param clsName	タスククラス名
	 * @param appConfig	共通設定情報
	 * @param taskParam	タスクパラメータ
	 * @return String	実行結果( APL_OK, APL_ERR, APL_ABEND )
	 * <pre>
	 * このメソッドはTaskクラスの呼出しI/Fになりますので、TaskInvokerで共通になります(と思います)
	 * (1)タスクを生成する
	 * (2)タスクに共通設定情報をセットする
	 * (3)タスクを実行する
	 * </pre>
	 */
	protected String invokeTask( String clsName, Properties appConfig, Properties taskParam )
	{
		String	result= APL_ERR;
		try
		{
			//	(1)タスクを生成する
			Task	task= (Task)Class.forName( clsName ).newInstance();
			//	(2)タスクに共通設定情報をセットする
			task.setConfig( appConfig );
			//	(3)タスクを実行する
			result= task.execute( taskParam );
			log.info( "["+ result +"] "+ clsName +"("+ task.getResultInfo() +")" );
		}
		catch (Exception e)
		{//	APL_ABEND
			result= APL_ABEND;
			log.error( "["+ result +"] "+ clsName, e );
			//	TODO 2010/02/05 Kac APL_ABENDしたら、次回の実行を不可にすべきか？
			//	TODO 2010/02/08 Kac もし実装するなら、ここじゃなくてexecute()のほうがよいかも？
		}
		return result;
	}

	private Properties loadProperties( String paramFile )
	{
		Properties	taskParam= new Properties();
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( paramFile );
			taskParam.load( in );
		}
		catch (Exception e)
		{
			log.info( e.toString() );
		}
		finally
		{
			if( in != null )
			{
				try
				{
					in.close();
				}
				catch( IOException e )
				{
					log.error( e.toString(), e );
				}
			}
		}
		return taskParam;
	}
}

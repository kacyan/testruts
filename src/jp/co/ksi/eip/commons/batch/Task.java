package jp.co.ksi.eip.commons.batch;

import java.util.Properties;

/**
 * 実行されるタスクのI/F
 * @author kac
 * @since 2010/01/15
 * @version 2010/01/16
 * @see jp.co.ksi.eip.commons.batch.TaskInvoker
 */
public interface Task
{

	/**
	 * このタスクの共通設定情報をセットします
	 * @param appConfig	共通設定情報
	 */
	public abstract void setConfig( Properties appConfig ) throws Exception;
	
	/**
	 * 処理を実行します
	 * @param taskParam	タスクのパラメータ
	 * @return String	実行結果を返します( APL_OK, APL_ERR, APL_ABEND )
	 */
	public abstract String execute( Properties taskParam ) throws Exception;

	/**
	 * 実行結果の情報を返します
	 * @return
	 */
	public abstract String getResultInfo();
	
}
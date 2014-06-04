/**
 * 
 */
package jp.co.ksi.eip.commons.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;


/**
 * Commandパターンを実現するためのB/L用のインターフェースです。
 * @author kac
 * @version 1.0.0
 * @since 1.0.0
 * @see InvokeAction
 * <pre>
 * このインターフェースを実装したクラスはInvokeActionから呼ぶ事ができます。
 * </pre>
 */
public interface IStruts
{
	
	/**
	 * ActionForward用の戻り値：APL_OK
	 * タスクの実行が正常終了したことを示す
	 */
	public static final String APL_OK= "APL_OK";
	public static final String RESULT_SUCCESS= APL_OK;

	/**
	 * ActionForward用の戻り値：APL_ERR
	 * タスクの実行がエラー終了したことを示す
	 */
	public static final String APL_ERR= "APL_ERR";

	/**
	 * ActionForward用の戻り値：APL_ABEND
	 * タスクの実行がABENDしたことを示す
	 */
	public static final String APL_ABEND= "APL_ABEND";
	
	public String execute(
		InvokeAction	action,
		ActionForm	form,
		HttpServletRequest	request,
		HttpServletResponse	response
		) throws Exception;
	
}

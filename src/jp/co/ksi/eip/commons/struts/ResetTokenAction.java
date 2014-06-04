/*
 * 作成日: 2005/01/14
 */
package jp.co.ksi.eip.commons.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * 二重POST防止用Actionクラス
 * InvokeActionのexecuteを実行した後に、トークンを削除します
 * @author kac
 * @since 1.1.5
 * @version 1.1.5
 */
public class ResetTokenAction extends InvokeAction
{
	private static Logger	log= Logger.getLogger(ResetTokenAction.class);
	
	/**
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		ActionForward	forward= super.execute(mapping, form, request, response);
		if( forward.getName().equals(IStruts.APL_OK) )
		{//	B/Lが正常終了した
			//	トークンを削除する
			resetToken( request );
			log.debug( "resetToken "+ forward );
		}
		return forward;
	}
}

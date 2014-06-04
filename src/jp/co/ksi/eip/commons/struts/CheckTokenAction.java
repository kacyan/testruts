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
 * InvokeActionのexecuteを実行する前に、トークンをチェックします
 * トークンが正しく無い場合、InvokeAction.RESULT_TOKEN_FAILEDを返します
 * @author kac
 * @since 1.1.5
 * @version 1.1.5
 */
public class CheckTokenAction extends InvokeAction
{
	/**
	 * "token.failed"トークンが正しくない事を示します
	 */
	public static final String RESULT_TOKEN_FAILED= "token.failed";
	
	private static Logger	log= Logger.getLogger(CheckTokenAction.class);
	
	/**
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		if( log.isDebugEnabled() )
		{
			log.debug( "param: "+ request.getParameter( org.apache.struts.taglib.html.Constants.TOKEN_KEY ) );
			log.debug( "session: "+ request.getSession().getAttribute( org.apache.struts.Globals.TRANSACTION_TOKEN_KEY ) );
		}
		if( isTokenValid(request) )
		{
			log.debug( "token valid." );
			ActionForward	forward= super.execute(mapping, form, request, response);
			if( forward.getName().equals(IStruts.RESULT_SUCCESS) )
			{//	B/Lが正常終了した
				//	トークンを削除する
				resetToken( request );
				log.debug( "resetToken "+ forward );
			}
			return forward;
		}
		else
		{
			String	blName= mapping.getParameter();
			log.error( mapping.getPath() +"->"+ blName +"["+ RESULT_TOKEN_FAILED +"]" );
			request.setAttribute("result",RESULT_TOKEN_FAILED);
			return mapping.findForward(RESULT_TOKEN_FAILED);
		}
	}
}

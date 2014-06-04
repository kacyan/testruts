/*
 * 作成日: 2005/01/22
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
 * InvokeActionのexecuteを実行した後、StrutsのsaveTokenを呼びます
 * @author kac
 * @since 1.1.5
 * @version 1.1.5
 */
public class SaveTokenAction extends InvokeAction
{
	private static Logger	log= Logger.getLogger(SaveTokenAction.class);

	/* (非 Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response
				)
			throws Exception
	{
//		log.debug( "getAttribute="+ mapping.getAttribute() );
//		log.debug( "getClass="+ mapping.getClass() );
//		log.debug( "getForward="+ mapping.getForward() );
//		log.debug( "getInclude"+ mapping.getInclude() );
//		log.debug( "getInput="+ mapping.getInput() );
//		log.debug( "getMultipartClass="+ mapping.getMultipartClass() );
//		log.debug( "getName="+ mapping.getName() );
//		log.debug( "getParameter="+ mapping.getParameter() );
//		log.debug( "getPath="+ mapping.getPath() );
//		log.debug( "getPrefix="+ mapping.getPrefix() );
//		log.debug( "getRoles="+ mapping.getRoles() );
//		log.debug( "getScope="+ mapping.getScope() );
//		log.debug( "getSuffix="+ mapping.getSuffix() );
//		log.debug( "getType="+ mapping.getType() );
//		log.debug( "getUnknown="+ mapping.getUnknown() );
//		log.debug( "getValidate="+ mapping.getValidate() );
		
		ActionForward	forward= super.execute(mapping, form, request, response);
		if( forward.getName().equals( IStruts.APL_OK ) )
		{//	B/Lが正常終了した
			//	トークンを保存する
			saveToken( request );
			log.debug( "saveToken "+ forward );
		}
		return forward;
	}
}

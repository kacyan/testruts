package jp.co.ksi.testruts.bl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ksi.eip.commons.struts.InvokeAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

/**
 * SwicthActionの習作の為のタスク
 * @author kac
 * @since 2013/12/26
 * @version 2013/12/26
 */
public class SwicthActionTestBL extends BaseBL
{

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		
		DynaActionForm	dyna= (DynaActionForm)form;
		String	result= dyna.getString( "result" );
		return result;
	}

}

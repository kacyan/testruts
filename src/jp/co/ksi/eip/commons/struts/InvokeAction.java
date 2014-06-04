/**
 * 
 */
package jp.co.ksi.eip.commons.struts;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

/**
 * Commandパターンを実現するStruts用のActionクラスです。
 * @author kac
 * @version 1.2.0 2013/12/26 forward時のエラー処理を追加した
 * @since 1.0.0
 * <pre>
 * struts-config.xmlの設定に従い、IStrutsを実装したB/Lを実行します。
 * </pre>
 * <pre>
 * ログ：
 *  model実行後に
 *  log.info( mapping.getPath() +"->"+ blName + msec +"["+ result +"]->"+ forward.getPath() );
 *  例外発生時に
 *  log.error( mapping.getPath() +"->"+ blName +"["+ result +"]", e );
 * </pre>
 */
public class InvokeAction extends Action
{
	/**
	 * ActionForward用の戻り値：invoke.failed
	 * modelの実行に失敗したことを示す
	 */
	public static final String RESULT_INVOKE_FAILED= "invoke.failed";
	/**
	 * ActionForward用の戻り値：success
	 * modelの実行が正常終了したことを示す
	 */
	public static final String RESULT_SUCCESS= "success";
	
	private static Logger	log= Logger.getLogger(InvokeAction.class);
	

	/* (非 Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		long	start= 0;
		if( log.isInfoEnabled() )
		{
			start= System.currentTimeMillis();
		}
		String result= RESULT_INVOKE_FAILED;
		String	blName= mapping.getParameter();
		
		try
		{
			IStruts	model= (IStruts)Class.forName( blName ).newInstance();
			result= model.execute( this, form, request, response );
		}
		catch( InstantiationException e )
		{
			result= RESULT_INVOKE_FAILED;
			log.error( mapping.getPath() +"->"+ blName +"["+ result +"]", e );
		}
		catch( IllegalAccessException e )
		{
			result= RESULT_INVOKE_FAILED;
			log.error( mapping.getPath() +"->"+ blName +"["+ result +"]", e );
		}
		catch( ClassNotFoundException e )
		{
			result= RESULT_INVOKE_FAILED;
			log.error( mapping.getPath() +"->"+ blName +"["+ result +"]", e );
		}
		catch( Exception e )
		{
			result= RESULT_INVOKE_FAILED;
			log.error( mapping.getPath() +"->"+ blName +"["+ result +"]", e );
		}
		
		request.setAttribute( "result", result );
		String	msec= "";
		if( log.isInfoEnabled() )
		{
			msec= "("+ (System.currentTimeMillis()-start) + "msec)";
		}
		ActionForward forward= mapping.findForward( result );
		if( forward != null )
		{
			log.info( mapping.getPath() +"->"+ blName + msec +"["+ result +"]->"+ forward.getPath() );
		}
		else
		{
			log.info( mapping.getPath() +"->"+ blName + msec +"["+ result +"]->"+ "forward not found" );
			
			response.setContentType( "text/html" );
			PrintWriter	out= response.getWriter();
			out.println( "<pre>");
			out.println( getClass().getName() +" ERROR." );
			out.println( mapping.getPath() +"->"+ blName + msec +"["+ result +"]->"+ "forward not found" );
			out.println( "</pre>");
			
		}
		return forward;
	}

	/* (非 Javadoc)
	 * @see org.apache.struts.action.Action#getResources(javax.servlet.http.HttpServletRequest)
	 */
	public MessageResources getResources( HttpServletRequest request )
	{
		return super.getResources( request );
	}

	/**
	 * BLのエラー処理にActionMessagesを使いたいのでオーバライトしました
	 * <pre>
	 * ISturutsにgetActionMessages()というメソッドを追加するのもありかも
	 * </pre>
	 * @see org.apache.struts.action.Action#saveErrors(javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionMessages)
	 */
	public void saveErrors( HttpServletRequest request, ActionMessages messages )
	{
		super.saveErrors( request, messages );
	}

	public Locale getLocale( HttpServletRequest request )
	{
		return super.getLocale( request );
	}

}

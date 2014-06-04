/*
 * 作成日: 2006/02/27
 */
package jp.co.ksi.eip.commons.struts;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * リクエストパラメータの "submit.XXXX"という値で処理を切り替えるためのAction
 * 最初に見つかった"submit.XXXX"をforward名として返します。
 * &lt;forward="submit.XXXX" path="/XXXX.do"/&gt;
 * &lt;forward="dispatch.failed" path="/default.jsp"/&gt;
 * このようにstruts-configに記述することでディスパッチ処理を実現できます
 * @author kac
 * @version 1.2.0 2013/12/25 infoレベルのログ出力を追加した
 * @since 1.2.0
 * <pre>
 * struts-1.2.9 以降に導入された org.apache.struts.actions.EventDispatchAction
 * も同等の事が出来ます。
 * 違いとしては、
 * EventDispatchAction では、Action内のメソッドにディスパッチを行います。
 * この為、
 * (1)Action内にロジックを実装する場合は、都合が良い
 * (2)InvokeActionのようにActionからロジッククラスを動的に呼出してる場合は
 * 　結局Action内にディスパッチのコードを書く必要がある。
 * (3)EventDispatchActionで他のActionの機能を使いたい場合、(ダウンロードやトークンチェックなど)
 * 　結局forwardさせるしかない。そうなるとリクエストの受渡しを別途実装する事になる
 * 　Actionにロジックを実装するかぎり楽にはならないのではないか？
 * </pre>
 */
public class DispatchAction extends Action
{
	public static final String RESULT_DISPATCH_FAILED= "dispatch.failed";

	private static Logger	log= Logger.getLogger( DispatchAction.class );
	
	public ActionForward execute(
		ActionMapping	mapping,
		ActionForm	form,
		HttpServletRequest	request,
		HttpServletResponse	response
		) throws Exception
	{
		Enumeration<?>	enumeration= request.getParameterNames();
		while( enumeration.hasMoreElements() )
		{
			String	name= (String)enumeration.nextElement();
			log.debug( "REQUEST: "+ name +"="+ request.getParameter( name ) );
			int	index= name.indexOf( "submit." );
			if( index == 0 )
			{
				ActionForward	forward= mapping.findForward( name );
				if( forward == null )
				{//	forward先が無いので無視する
					log.warn( name +" forward not found.");
					continue;
				}
				log.info( mapping.getPath() +"->"+ "["+ name +"]->"+ forward.getPath() );
				return forward;
			}
		}

		ActionForward forward= mapping.findForward( RESULT_DISPATCH_FAILED );
		log.info( mapping.getPath() +"->"+ "["+ RESULT_DISPATCH_FAILED +"]->"+ forward.getPath() );
		return forward;
	}
}

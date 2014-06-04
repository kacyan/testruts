package jp.co.ksi.eip.commons.struts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import JP.co.ksi.util.HtmlFilter;

/**
 * strutsのエラーを表示するタグリブです。
 * @author kac
 * @version 1.2.0
 * @since 1.2.0
 * <pre>
 * 
 * </pre>
 */
public class ErrorTag extends TagSupport
{

	public int doEndTag() throws JspException
	{
		//	セッションからstrutsのロケールを取得する
		Locale	locale= (Locale)pageContext.getAttribute( Globals.LOCALE_KEY, PageContext.SESSION_SCOPE );
		if( locale == null )
		{//	無ければリクエストから取得する
			locale= pageContext.getRequest().getLocale();
		}
		//	メッセージ・リソースをコンテキストから取得する
		MessageResources	resources= (MessageResources)pageContext.getAttribute( Globals.MESSAGES_KEY, PageContext.APPLICATION_SCOPE );
		//	エラーをリクエストから取得する
		ActionMessages	messages= (ActionMessages)pageContext.getAttribute( Globals.ERROR_KEY, PageContext.REQUEST_SCOPE );
		Iterator<?>	it= messages.get();
		while( it.hasNext() )
		{
			ActionMessage msg= (ActionMessage)it.next();
			Object[]	values= msg.getValues();
			if( values == null )	values= new Object[0];

			ArrayList<String>	list= new ArrayList<String>();
			for( int i= 0; i < values.length; i++ )
			{
				//	エラーメッセージに付加された値はHTMLタグをエスケープしておく
				list.add( HtmlFilter.parseTag( values[i].toString() ) );
			}
			String	text= resources.getMessage( locale, msg.getKey(), list.toArray() );

			try
			{
				pageContext.getOut().println( text +"<br>" );
			}
			catch( IOException e )
			{
				throw new JspException( e );
			}
		}
		return EVAL_PAGE;
	}
	
}

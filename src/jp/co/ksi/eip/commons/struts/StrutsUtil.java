package jp.co.ksi.eip.commons.struts;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

public class StrutsUtil
{
	/**
	 * strutsがセッションに保持しているロケールを返す
	 * @param request
	 * @return Locale
	 * <pre>
	 * strutsがセッションに保持しているロケールを取得する
	 * セッションに無ければリクエストのロケールを採用する
	 * </pre>
	 */
	public static Locale getLocale( HttpServletRequest request )
	{
		//	strutsがセッションに保持しているロケールを取得する
		Locale	locale= (Locale)request.getSession().getAttribute( Globals.LOCALE_KEY );
		if( locale == null )
		{//	セッションに無ければリクエストのロケールを採用する
			locale= request.getLocale();
		}
		return locale;
	}

	/**
	 * strutsがセッションに保持しているロケールを返す
	 * @param pageContext
	 * @return
	 * <pre>
	 * strutsがセッションに保持しているロケールを取得する
	 * セッションに無ければリクエストのロケールを採用する
	 * </pre>
	 */
	public static Locale getLocale( PageContext pageContext )
	{
		//	strutsがセッションに保持しているロケールを取得する
		Locale	locale= (Locale)pageContext.getSession().getAttribute( Globals.LOCALE_KEY );
		if( locale == null )
		{//	セッションに無ければリクエストのロケールを採用する
			locale= pageContext.getRequest().getLocale();
		}
		return locale;
	}

	/**
	 * strutsのメッセージリソースを返します
	 * @param context
	 * @return
	 */
	public static MessageResources getMessageResources( ServletContext context )
	{
		return (MessageResources)context.getAttribute( Globals.MESSAGES_KEY );
	}
}

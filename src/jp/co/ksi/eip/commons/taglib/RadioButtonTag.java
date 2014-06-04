package jp.co.ksi.eip.commons.taglib;

import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import jp.co.ksi.eip.commons.struts.StrutsUtil;


import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import JP.co.ksi.util.HtmlFilter;

/**
 * RadioButtonを簡単に表示する為のタグリブ
 * @author kac
 * @since 2012/03/21
 * @version 2012/03/22
 * @see eip.tld
 */
public class RadioButtonTag extends TagSupport
{
	private static Logger	log= Logger.getLogger( RadioButtonTag.class );
	
	/**
	 * リクエスト属性名(must)
	 */
	private String	name;
	/**
	 * リクエストパラメータ名
	 */
	private String	paramName;
	/**
	 * 選択中の値
	 */
	private String	checkedValue;
	/**
	 * HTMLエスケープするか？(default=true)
	 */
	private boolean	escape= true;
	/**
	 * displayValueをメッセージリソースのキーとして扱い、メッセージを取得するか？
	 * (default=false)
	 */
	private boolean messageKey;

	public void setName( String name )
	{
		this.name = name;
	}
	public void setParamName( String paramName )
	{
		this.paramName = paramName;
	}
	public void setCheckedValue( String checkedValue )
	{
		this.checkedValue = checkedValue;
	}
	public void setEscape( boolean escape )
	{
		log.debug( "escape="+ escape );
		this.escape= escape;
	}
	public void setMessageKey( boolean messageKey )
	{
		this.messageKey= messageKey;
	}

	@Override
	public int doEndTag() throws JspException
	{
		JspWriter	writer= pageContext.getOut();
		try
		{
			MessageResources	resources= null;
			Locale	locale= null;
			if( messageKey )
			{//	多言語対応
				resources= StrutsUtil.getMessageResources( pageContext.getServletContext() );
				locale= StrutsUtil.getLocale( pageContext );
			}
			if( paramName == null )
			{//	パラメータ名が指定されていない->nameを使用する
				paramName= name;
			}

			List	list= (List)pageContext.findAttribute( name );
			for( int i= 0; i < list.size(); i++ )
			{
				Option	option= (Option)list.get( i );
				String	value= option.getName();
				String	displayValue= option.getDisplayValue();
				if( messageKey )
				{//	多言語対応
					displayValue= resources.getMessage( locale, displayValue );
				}
				String	checked= "";
				if( value.equals( checkedValue ) )
				{
					checked= " checked=\"checked\"";
				}
				if( escape )
				{
					value= HtmlFilter.parseTag( value );
					displayValue= HtmlFilter.parseTag( displayValue );
				}
				writer.print( "<input type=\"radio\" name=\""+ paramName +"\" value=\""+ value +"\""+ checked +">" );
				writer.println( displayValue );
			}

		}
		catch( Exception e )
		{
			log.error( "name="+ name +", paramName="+ paramName +", checkedValue="+ checkedValue, e );
		    throw new JspException( e.getMessage() );
		}
		
		return super.doEndTag();
	}
	
}

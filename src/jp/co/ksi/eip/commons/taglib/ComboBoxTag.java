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
 * ComboBoxを簡単に表示する為のタグリブ
 * @author kac
 * @since 2012/03/21
 * @version 2012/03/22
 * @see eip.tld
 */
public class ComboBoxTag extends TagSupport
{
	private static Logger	log= Logger.getLogger( ComboBoxTag.class );
	
	/**
	 * リクエスト属性名(must) この属性名でArrayList<Option>を取得します
	 */
	private String	name;
	/**
	 * リクエストパラメータ名
	 */
	private String	paramName;
	/**
	 * 選択中の値
	 */
	private String	selectedValue;
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
	public void setSelectedValue( String selectedValue )
	{
		this.selectedValue = selectedValue;
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
			writer.println( "<select name=\""+ paramName +"\">" );
			
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
				String	selected= "";
				if( value.equals( selectedValue ) )
				{
					selected= " selected=\"selected\"";
				}
				if( escape )
				{
					value= HtmlFilter.parseTag( value );
					displayValue= HtmlFilter.parseTag( displayValue );
				}
				writer.print( "<option value=\""+ value +"\""+ selected +">" );
				writer.print( displayValue );
				writer.println( "</option>" );
			}

			writer.println( "</select>" );
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
		    throw new JspException( e.getMessage() );
		}
		
		return super.doEndTag();
	}
	
}

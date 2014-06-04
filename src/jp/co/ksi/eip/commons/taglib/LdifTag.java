package jp.co.ksi.eip.commons.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import JP.co.ksi.ldap.LdifProperties;
import JP.co.ksi.util.HtmlFilter;

/**
 * LdifPropertiesの属性を簡単に表示する為のタグリブ
 * @author kac
 * @since 2012/02/28
 * @version 2012/03/05
 */
public class LdifTag extends TagSupport
{
	private static Logger	log= Logger.getLogger( LdifTag.class );
	
	private String	name;
	private String	property;
	private boolean	escape= true;

	public void setName( String name )
	{
		this.name = name;
	}

	public void setProperty( String property )
	{
		this.property = property;
	}
	
	public void setEscape( boolean escape )
	{
		log.debug( "escape="+ escape );
		this.escape= escape;
	}

	@Override
	public int doEndTag() throws JspException
	{
		Object	obj= pageContext.findAttribute( name );
		log.debug( name +"="+ obj );
		String	value= "";
		if( obj instanceof LdifProperties )
		{
			LdifProperties ldif = (LdifProperties)obj;
			if( property != null )
			{
				value= ldif.getString( property );
			}
			else
			{
				value= ldif.getDN();
			}
		}
		else
		{
			value= name +" is not LdifProperties. "+ ( obj != null ? obj.getClass().getName() : obj );
		}
		
		if( escape )
		{
			value= HtmlFilter.parseTag( value );
		}
		
		try
		{
			pageContext.getOut().print( value );
		}
		catch( Exception e )
		{
		    throw new JspException( e.getMessage() );
		}

		return super.doEndTag();
	}
	
}

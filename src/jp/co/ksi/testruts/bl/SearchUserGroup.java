package jp.co.ksi.testruts.bl;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import JP.co.ksi.ldap.LdifProperties;

import jp.co.ksi.eip.commons.struts.IStruts;
import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.eip.commons.util.PagingArray;

/**
 * グループへのユーザ追加の習作
 * @author kac
 * @since 2012/02/28
 * @version 2012/02/28
 */
public class SearchUserGroup implements IStruts
{
	private static Logger	log= Logger.getLogger( SearchUserGroup.class );
	
	private String	groupdn;
	Integer	start;
	Integer	max;
	
	@Override
	public String execute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		if( form instanceof DynaActionForm )
		{
			DynaActionForm dyna= (DynaActionForm)form;
			
			groupdn= dyna.getString( "groupdn" );
			start= (Integer)dyna.get( "start" );
			max= (Integer)dyna.get( "max" );
		}
		
		LdifProperties	groupLdif= new LdifProperties();
		groupLdif.setDN( groupdn );
		groupLdif.put( "cn", "hoge" );
		groupLdif.put( "description", "<script>alert(document.cookie);</script>" );
		
		int	total= 100;
		PagingArray	pa= new PagingArray();
		pa.setStart( start );
		pa.setTotal( total );
		pa.setMax( max );
		pa.setCurrent( 0 );
		pa.setType( LdifProperties.class );
		int	end= start + max;
		log.debug( total +" / "+ start +" - "+ end );
		DecimalFormat	df000= new DecimalFormat( "000" );
		DecimalFormat	df000000= new DecimalFormat( "000000" );
		for( int i= start; i < end; i++ )
		{
			if( i >= total )	break;
			
			LdifProperties	ldif= new LdifProperties();
			ldif.setDN( "uid="+ df000.format( i ) +",ou=People,o=WebSignOn,c=JP" );
			ldif.put( "uid", df000.format( i ) );
			ldif.put( "cn", "氏名"+ i );
			ldif.put( "employeeNumber", "0001"+ df000000.format( i ) );
			ldif.put( "officeName", "<img src=\"\"/>" );
			pa.add( ldif );
		}
		
		request.setAttribute( "groupLdif", groupLdif );
		request.setAttribute( "userArray", pa );
		
		return IStruts.APL_OK;
	}
	
}

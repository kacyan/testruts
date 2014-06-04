package jp.co.ksi.incubator;

import java.util.ArrayList;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.SortControl;
import javax.naming.ldap.SortKey;

import org.apache.log4j.Logger;

import JP.co.ksi.eip.entity.UserEntityHelper;

/**
 * LDAP検索時、サーバ側にソートをさせる習作
 * @author kac
 * @since 2011/06/10
 */
public class LdapSearchTest extends LdapTest
{
	private static Logger	log= Logger.getLogger( LdapTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		LdapSearchTest	test= new LdapSearchTest();
		try
		{
			test.init();

			test.doTest();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			test.close();
		}
		
	}

	@Override
	void doTest()
	{
		long	msec= System.currentTimeMillis();
		
		ArrayList	list= new ArrayList<Attributes>();
		Attributes[]	attrs= {};
		
		String	dn= "ou=People";
		String	searchText= "(uid=*)";
		String sortKey = "employeeNumber";
		String[]	attrIds= UserEntityHelper.USER_ATTRS;
//		String[]	attrIds= { "cn", "sn", "uid", "wsoLastLogin","createTimestamp", "creatorsName", "modifyTimestamp", "modifiersName" };
		SearchControls	cons= new SearchControls( SearchControls.SUBTREE_SCOPE, 0, 0, attrIds, true, false );
		try
		{
			//	ソートの指定
			Control[]	requestControls= new Control[1];
			SortKey[]	sortKeys= new SortKey[1];
			sortKeys[0]= new SortKey( sortKey, false, null );
			requestControls[0]= new SortControl( sortKeys, Control.CRITICAL );
			ctx.setRequestControls( requestControls );
			
			NamingEnumeration<SearchResult>	searchResults= ctx.search( dn, searchText, cons );
			while( searchResults.hasMore() )
			{
				SearchResult	searchResult= searchResults.next();
//				log.debug( searchResult );
				list.add( searchResult.getAttributes() );
			}
			attrs= new Attributes[list.size()];
			attrs= (Attributes[])list.toArray( attrs );
		}
		catch( Exception e )
		{//	
			log.error( dn, e );
		}
		finally
		{
			
		}
		log.info( "attrs.length="+ attrs.length );
		msec= System.currentTimeMillis() - msec;
		log.info( msec +"msec" );

		if( log.isDebugEnabled() )
		{
			for( int i= 0; i < attrs.length; i++ )
			{
				log.debug( attrs[i].get( sortKey ) +" "+ attrs[i].get( "uid" ) +"\t"+ attrs[i].get( "cn" ) );
			}
		}
	}
	
	
}

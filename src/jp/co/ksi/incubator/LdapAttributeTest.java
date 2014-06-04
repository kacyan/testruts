package jp.co.ksi.incubator;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.SortControl;
import javax.naming.ldap.SortKey;

import org.apache.log4j.Logger;

/**
 * LDAP属性値のソートを試みる習作
 * @author kac
 * @since 2011/06/13
 * 2011/06/10 Kac 属性値のソートは出来ない？
 */
public class LdapAttributeTest extends LdapTest
{
	private static Logger	log= Logger.getLogger( LdapTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		LdapAttributeTest	test= new LdapAttributeTest();
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
		
		Attributes	attrs= null;
		
		String	dn= "cn=hoge,ou=groups,ou=system";
		dn= "cn=ksi,ou=Groups";
		String attrName = "member";
		String[]	attrIds= { attrName };
		try
		{
			Control[]	requestControls= new Control[1];
			SortKey[]	sortKeys= new SortKey[1];
			sortKeys[0]= new SortKey( attrName, true, null );
			requestControls[0]= new SortControl( sortKeys, Control.CRITICAL );
			ctx.setRequestControls( requestControls );

			SearchControls	cons= new SearchControls( SearchControls.OBJECT_SCOPE, 0, 0, attrIds, true, false );
			NamingEnumeration<SearchResult>	searchResults= ctx.search( dn, "member=*", cons );
			while( searchResults.hasMore() )
			{
				SearchResult	searchResult= searchResults.next();
				log.debug( searchResult );
				attrs= searchResult.getAttributes();
				Attribute	attr= attrs.get( attrName );
				log.debug( "isOrder="+ attr.isOrdered() );
				NamingEnumeration	values= attr.getAll();
				while( values.hasMore() )
				{
					log.debug( values.next() );
				}
			}
//			attrs= ctx.getAttributes( dn, attrIds );
		}
		catch( Exception e )
		{//	
			log.error( dn, e );
		}
		finally
		{
			
		}

		msec= System.currentTimeMillis() - msec;
		log.info( msec +"msec" );

	}
	
}

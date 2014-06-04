package jp.co.ksi.incubator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import jp.co.ksi.testruts.Const;

import org.apache.log4j.Logger;

import JP.co.ksi.eip.entity.UserEntityHelper;
import JP.co.ksi.ldap.LdapManager;

/**
 * LDAP検索時、検索結果をPRG側でソートする習作
 * サーバ側ソートとの比較用
 * @author kac
 * @since 2011/06/10
 */
public class LdapSearch2Test extends LdapTest
{
	private static Logger	log= Logger.getLogger( LdapTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		LdapSearch2Test	test= new LdapSearch2Test();
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
//			Control[]	requestControls= new Control[1];
//			SortKey[]	sortKeys= new SortKey[1];
//			sortKeys[0]= new SortKey( "wsoLastLogin", false, null );
//			requestControls[0]= new SortControl( sortKeys, Control.CRITICAL );
//			ctx.setRequestControls( requestControls );
			
			NamingEnumeration<SearchResult>	searchResults= ctx.search( dn, searchText, cons );
			while( searchResults.hasMore() )
			{
				SearchResult	searchResult= searchResults.next();
//				log.debug( searchResult );
				list.add( searchResult.getAttributes() );
			}
			attrs= new Attributes[list.size()];
			attrs= (Attributes[])list.toArray( attrs );
			
			Arrays.sort( attrs, new AttributesComparator( sortKey, -1 ) );
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
	
	public class AttributesComparator implements Comparator<Attributes>
	{
		String	attrID;
		int	order= 1;
		
		public AttributesComparator( String sortKey, int order )
		{
			this.attrID= sortKey;
			this.order= order;
		}
		
		@Override
		public int compare( Attributes o1, Attributes o2 )
		{
			Attribute attr1 = o1.get( attrID );
			Attribute attr2 = o2.get( attrID );
			if( attr1 == null )
			{
				if( attr2 == null )
				{
					return -1 + order;
				}
				else
				{
					return 1 * order;
				}
			}
			else
			{
				if( attr2 == null )
				{
					return -1 * order;
				}
				else
				{
//					log.debug( attr1 );
					return attr1.toString().compareToIgnoreCase( attr2.toString() ) * order;
				}
			}
		}
		
	}
}

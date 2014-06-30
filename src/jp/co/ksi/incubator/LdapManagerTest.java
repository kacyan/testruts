package jp.co.ksi.incubator;

import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;

import JP.co.ksi.ldap.LdapManager;


/**
 * LdapManagerのテスト
 * @author kac
 * @since 2012/06/27
 */
public class LdapManagerTest
{
	private static Logger	log= Logger.getLogger( LdapManagerTest.class );
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Properties	props= new Properties( System.getProperties() );
		props.setProperty( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
		props.setProperty( "java.naming.security.authentication", "simple" );
//		props.setProperty( "java.naming.provider.url", "ldap://bowmore.dev.ksi.co.jp:50389/" );
//		props.setProperty( "java.naming.security.principal", "cn=Directory Manager" );
//		props.setProperty( "java.naming.security.credentials", "amAdmin25" );
		props.setProperty( "java.naming.provider.url", "ldap://fscs1.ap.ksi.co.jp/" );
//		props.setProperty( "java.naming.security.principal", "cn=Directory Manager" );
//		props.setProperty( "java.naming.security.credentials", "amAdmin25" );
		String	base= "OU=User,OU=Account,DC=oa,DC=ksi,DC=co,DC=jp";
		
		String	searchText= "(objectClass=*)";
		int	scope= LdapManager.ONELEVEL_SCOPE;
		LdapManager	ldap= new LdapManager();
		try
		{
			ldap= new LdapManager( props );
			System.out.println( "baseDN="+ ldap.getBaseDN() );
			ldap.searchEntry( base, searchText, scope );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			ldap.close();
		}
	}

}

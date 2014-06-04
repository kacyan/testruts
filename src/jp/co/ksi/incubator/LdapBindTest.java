package jp.co.ksi.incubator;

import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;

public class LdapBindTest
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		
		String	uid= "";
		String	password= "";
		
		Properties	env= new Properties();
		env.setProperty( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
		env.setProperty( "java.naming.security.authentication", "simple" );
		env.setProperty( "java.naming.provider.url", "ldap://bowmore.dev.ksi.co.jp:50389/" );
		env.setProperty( "java.naming.security.principal", "cn=Directory Manager" );
		env.setProperty( "java.naming.security.credentials", "amAdmin25" );
		
		
		Control[]	ctls= {};
		InitialLdapContext ldap= null;
		try
		{
			ldap= new InitialLdapContext( env, ctls );
			
			String	name= "dc=openam,dc=forgerock,dc=org";
			String	filter= "(uid="+ uid +")";
			SearchControls	cons= new SearchControls();
			cons.setSearchScope( SearchControls.SUBTREE_SCOPE );
			
			NamingEnumeration<SearchResult>	enumeration= ldap.search( name, filter, cons );
			while( enumeration.hasMore() )
			{
				SearchResult result= enumeration.next();
				String dn= result.getName() +","+ name;
				System.out.println( dn );
				
				Properties	env2= new Properties();
				env2.setProperty( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
				env2.setProperty( "java.naming.security.authentication", "simple" );
				env2.setProperty( "java.naming.provider.url", "ldap://bowmore.dev.ksi.co.jp:50389/" );
				env2.setProperty( "java.naming.security.principal", dn );
				env2.setProperty( "java.naming.security.credentials", password );
				InitialLdapContext	ldap2= new InitialLdapContext( env2, ctls );
				
				System.out.println( "ldap bind ok." );
				
				ldap2.close();
			}
			
		}
		catch( NamingException e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ldap.close();
			}
			catch( Exception e )
			{
			}
		}

	}

}

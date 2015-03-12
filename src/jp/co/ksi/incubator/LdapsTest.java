package jp.co.ksi.incubator;

import java.util.Enumeration;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;

public class LdapsTest
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		
		String	uid= "s9110*";
		
		Properties	env= new Properties();
		env.setProperty( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
		env.setProperty( "java.naming.security.authentication", "simple" );
		env.setProperty( "java.naming.provider.url", "ldaps://fscs1.ap.ksi.co.jp/" );
		env.setProperty( "java.naming.security.principal", "s911013@oa.ksi.co.jp" );
		env.setProperty( "java.naming.security.credentials", "nobu@2611" );
		env.setProperty( Context.SECURITY_PROTOCOL, "ssl" );
		env.setProperty( "java.naming.ldap.factory.socket", "jp.co.ksi.incubator.ssl.MySSLSocketFactory" );
		
		Control[]	ctls= {};
		InitialLdapContext ldap= null;
		try
		{
			
			ldap= new InitialLdapContext( env, ctls );
			
			java.util.Hashtable<?,?>	enviroment= ldap.getEnvironment();
			Enumeration<?>	enumeration2= enviroment.keys();
			while( enumeration2.hasMoreElements() )
			{
				Object	key= enumeration2.nextElement();
				Object	value= enviroment.get( key );
				System.out.println( key +"="+ value );
			}
			
			
			String	name= "OU=User,OU=Account,DC=oa,DC=ksi,DC=co,DC=jp";
			String	filter= "(cn="+ uid +")";
			SearchControls	cons= new SearchControls();
			cons.setSearchScope( SearchControls.ONELEVEL_SCOPE );
			
			NamingEnumeration<SearchResult>	enumeration= ldap.search( name, filter, cons );
			while( enumeration.hasMore() )
			{
				SearchResult result= enumeration.next();
				String dn= result.getName() +","+ name;
				System.out.println( dn );

				Attributes	attrs= result.getAttributes();
				System.out.println( "\t"+ attrs.get( "sn" ) );
				
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

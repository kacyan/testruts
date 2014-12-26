package jp.co.ksi.incubator.kerberos;

import java.security.PrivilegedAction;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * ケルベロス認証でLDAPアクセスする習作
 * @author kac
 * @since 2014/12/26
 * @version 2014/12/26
 */
public class LdapAction implements PrivilegedAction<LdapContext>
{

	@Override
	public LdapContext run()
	{
		System.out.println( LdapAction.class.getSimpleName() +":run "+ "" );
		
		LdapContext	ctx= null;
		Control[]	ctls= {};
		Properties	env= new Properties();
		env.setProperty( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
		env.setProperty( "java.naming.provider.url", "ldap://fscs1.oa.ksi.co.jp/DC=oa,DC=ksi,DC=co,DC=jp" );
		env.setProperty( "java.naming.security.authentication", "GSSAPI");
//		env.setProperty( "java.naming.security.authentication", "simple");
//		env.setProperty( "javax.security.sasl.server.authentication", "true" );
		try
		{
			ctx= new InitialLdapContext( env, ctls );
			System.out.println( ctx );
		}
		catch( NamingException e )
		{
			e.printStackTrace();
		}
		return ctx;
	}

}

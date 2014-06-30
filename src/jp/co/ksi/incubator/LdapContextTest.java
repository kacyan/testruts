package jp.co.ksi.incubator;

import java.util.Arrays;
import java.util.Properties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import org.apache.log4j.Logger;


/**
 * LDAP-V3の機能を試すため、LdapContextを使う習作です
 * ・隠し属性へのアクセス
 * ・属性値のソート
 * ・検索結果のソート
 * を目標にします
 * LdapContextを使う場合、接続操作とは別に設定を行うようです。
 * @author kac
 * @since 2011/06/06
 */
public class LdapContextTest
{
	protected InitialLdapContext ctx= null;

	private static Logger	log= Logger.getLogger( LdapContextTest.class );
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		LdapContextTest	test= new LdapContextTest();
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

	/**
	 * 初期化：LDAPに接続します
	 * @throws NamingException
	 */
	void init() throws NamingException
	{
		Properties	env= new Properties();
		env.setProperty( "java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory" );
		env.setProperty( "java.naming.security.authentication", "simple" );
		env.setProperty( "java.naming.provider.url", "ldap://eip-ldap/ou=Master,o=WebSignOn,c=JP" );
		env.setProperty( "java.naming.security.principal", "cn=root" );
		env.setProperty( "java.naming.security.credentials", "secret" );

//		env.setProperty( "java.naming.provider.url", "ldap://172.16.90.188/o=WebSignOn,c=JP" );
//		env.setProperty( "java.naming.provider.url", "ldap://172.16.9.187/o=WebSignOn,c=JP" );
//		env.setProperty( "java.naming.security.principal", "cn=Directory Manager" );
//		env.setProperty( "java.naming.security.credentials", "password" );
		
//		env.setProperty( "java.naming.provider.url", "ldap://macallan.osk.ksi.co.jp:10389/ou=system" );
//		env.setProperty( "java.naming.security.principal", "uid=admin,ou=system" );
//		env.setProperty( "java.naming.security.credentials", "secret" );

		try
		{
			Control[]	ctls= {};
//			ctx= new InitialDirContext( env );
			ctx= new InitialLdapContext( env, ctls );
			log.info( ctx );
			Control[]	controls= ctx.getResponseControls();
			log.debug( "controls="+ Arrays.toString( controls ) );
		}
		catch( NamingException e )
		{
			throw e;
		}
		
	}
	
	/**
	 * 終了：LDAPとの接続を閉じます
	 */
	void close()
	{
		if( ctx != null )
		{
			try
			{
				ctx.close();
			}
			catch( NamingException e )
			{
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * テスト用メソッド
	 */
	void doTest()
	{
		String	dn= "cn=hoge,ou=Groups";
		String[]	attrIds= { "cn", "member", "createTimestamp", "creatorsName", "modifyTimestamp", "modifiersName" };
		try
		{
			Attributes	attrs= ctx.getAttributes( dn, attrIds );
			
			NamingEnumeration	namingEnumeration= attrs.getAll();
			while( namingEnumeration.hasMore() )
			{
				Attribute	attr= (Attribute)namingEnumeration.next();
//				log.debug( attr +" - "+ attr.getClass().getName() );
				NamingEnumeration	values= attr.getAll();
				while( values.hasMore() )
				{
					log.debug( attr.getID() +"="+ values.next() );
				}
			}
		}
		catch( Exception e )
		{
			log.error( dn, e );
		}
		
	}
}

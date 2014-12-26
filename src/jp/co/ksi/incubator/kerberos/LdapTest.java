package jp.co.ksi.incubator.kerberos;


import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

/**
 * ケルベロス認証でLDAPアクセスする習作
 * @author kac
 * @since 2014/12/26
 * @version 2014/12/26
 */
public class LdapTest
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		System.out.println( System.getProperty( "java.version" ) );
		
		// TGT取得に使用する細かい設定 
		System.setProperty("java.security.auth.login.config" ,"misc/jaas.conf");
//		System.setProperty("java.security.krb5.conf", "misc/krb5.conf");

		System.setProperty("java.security.krb5.realm" ,"OA.KSI.CO.JP");	//	レルムは大文字で指定するらしい
		System.setProperty("java.security.krb5.kdc" ,"fscs1.oa.ksi.co.jp");	//	ケルベロスサーバ 

		if( args.length < 1 )
		{
			System.out.println( "usage : Test2 {uid} {pwd}" );
			System.out.println( " uid --- (must)ユーザID" );
			System.out.println( " pwd --- (must)パスワード" );
			return;
		}
		String	uid= args[0];
		String	pwd= args[1];
		
		LoginContext	lc= null;
		try
		{
			//	ケルベロス認証
			lc= new LoginContext( "SampleClient", new MyCallbackHandler( uid, pwd ) );
			lc.login();
			Subject	subject= lc.getSubject();
//			System.out.println( "subject="+ subject );
			
			//	LdapActionを呼出して、ldapに接続する
			LdapContext	ldap= Subject.doAs( subject, new LdapAction() );
			System.out.println( ldap );

			Attributes	attrs= ldap.getAttributes( "CN=s911013,OU=User,OU=Account" );
			NamingEnumeration<?> enumeration= attrs.getAll();
			while( enumeration.hasMore() )
			{
				Object	obj= enumeration.next();
				if( obj instanceof Attribute )
				{
					Attribute attr= (Attribute)obj;
					System.out.println( attr );
				}
			}
			
			//	後始末
			ldap.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

}

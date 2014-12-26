package jp.co.ksi.incubator.kerberos;


import java.security.Principal;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;

import sun.security.krb5.Credentials;

public class Test2
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		System.out.println( System.getProperty( "java.version" ) );
		
		// TGT取得に使用する細かい設定 
		System.setProperty("java.security.auth.login.config" ,"misc/jaas.conf");
		System.setProperty("java.security.krb5.conf", "misc/krb5.conf");

		System.setProperty("java.security.krb5.realm" ,"OA.KSI.CO.JP"); 
		System.setProperty("java.security.krb5.kdc" ,"172.16.90.87"); //ケルベロスサーバのIP 

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
			lc= new LoginContext( "SampleClient", new MyCallbackHandler( uid, pwd ) );
			lc.login();
			Subject	subject= lc.getSubject();
			System.out.println( "subject="+ subject );
			Set<Principal>	principals= subject.getPrincipals();
			for( Iterator<Principal> it= principals.iterator(); it.hasNext(); )
			{
				Principal	principal= it.next();
				System.out.println( "principal= "+ principal );
			}
			Set<Object>	set= subject.getPrivateCredentials();
			for( Iterator<Object> it= set.iterator(); it.hasNext(); )
			{
				Object object= it.next();
				if( object instanceof KerberosTicket )
				{
					KerberosTicket ticket= (KerberosTicket)object;
				}
			}
			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

}

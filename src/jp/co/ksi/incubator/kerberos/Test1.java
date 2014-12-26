package jp.co.ksi.incubator.kerberos;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

import com.sun.security.auth.callback.TextCallbackHandler;

public class Test1
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

		LoginContext	lc= null;
		try
		{
			lc= new LoginContext( "SampleClient", new TextCallbackHandler() );
			lc.login();
			Subject	subject= lc.getSubject();
			System.out.println( "subject="+ subject );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

}

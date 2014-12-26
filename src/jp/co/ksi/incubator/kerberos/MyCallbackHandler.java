package jp.co.ksi.incubator.kerberos;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * ケルベロス認証の習作
 * @author kac
 * @since 2014/12/25
 * @version 2014/12/25
 */
public class MyCallbackHandler implements CallbackHandler
{
	private String	uid= "";
	private String	password= "";

	/**
	 * デフォルト・コンストラクタ
	 */
	public MyCallbackHandler()
	{
		
	}
	
	/**
	 * カスタム・コンストラクタ
	 * @param uid
	 * @param password
	 */
	public MyCallbackHandler( String uid, String password )
	{
		setUid( uid );
		setPassword( password );
	}
	
	@Override
	public void handle( Callback[] callbacks ) throws IOException,
			UnsupportedCallbackException
	{
		for( int i= 0; i < callbacks.length; i++ )
		{
			if( callbacks[i] instanceof TextOutputCallback )
			{
				TextOutputCallback toc= (TextOutputCallback)callbacks[i];
				System.out.println( "toc="+ toc );
			}
			else if( callbacks[i] instanceof NameCallback )
			{
				NameCallback	nc= (NameCallback)callbacks[i];
				System.out.println( "nc="+ nc );
				nc.setName( uid );
			}
			else if( callbacks[i] instanceof PasswordCallback )
			{
				PasswordCallback pc= (PasswordCallback)callbacks[i];
				System.out.println( "pc"+ pc );
				pc.setPassword( password.toCharArray() );
			}
			else
			{
				System.out.println( "unknown. "+ callbacks[i] );
			}
		}
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid( String uid )
	{
		this.uid= uid;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword( String password )
	{
		this.password= password;
	}

}

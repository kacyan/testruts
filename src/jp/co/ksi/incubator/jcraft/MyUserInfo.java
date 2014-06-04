package jp.co.ksi.incubator.jcraft;

import org.apache.log4j.Logger;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class MyUserInfo implements UserInfo, UIKeyboardInteractive
{
	private static Logger	log= Logger.getLogger( MyUserInfo.class );
	
	private String	password= "";

	@Override
	public String getPassphrase()
	{
		log.trace( "" );
		return null;
	}

	@Override
	public String getPassword()
	{
		log.trace( password );
		return password;
	}
	public void setPassword( String pwd )
	{
		this.password= pwd;
	}

	@Override
	public boolean promptPassphrase( String arg0 )
	{
		log.trace( "arg0="+ arg0 );
		return false;
	}

	@Override
	public boolean promptPassword( String arg0 )
	{
		log.trace( "arg0="+ arg0 );
		return false;
	}

	@Override
	public boolean promptYesNo( String arg0 )
	{
		log.trace( "arg0="+ arg0 );
		return true;
	}

	@Override
	public void showMessage( String arg0 )
	{
		log.trace( "arg0="+ arg0 );
	}

	@Override
	public String[] promptKeyboardInteractive( String arg0, String arg1,
			String arg2, String[] arg3, boolean[] arg4 )
	{
		log.trace( "arg0="+ arg0 +"arg1="+ arg1 +"arg2="+ arg2 +"arg3[]="+ arg3.length +"arg4="+ arg4 );
		return null;
	}
	
}

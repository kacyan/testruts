package jp.co.ksi.incubator.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

public class MySSLSocketFactory extends SSLSocketFactory
{

	@Override
	public Socket createSocket( Socket arg0, String arg1, int arg2, boolean arg3 )
			throws IOException
	{
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String[] getDefaultCipherSuites()
	{
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String[] getSupportedCipherSuites()
	{
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Socket createSocket( String arg0, int arg1 ) throws IOException,
			UnknownHostException
	{
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Socket createSocket( InetAddress arg0, int arg1 ) throws IOException
	{
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Socket createSocket( String arg0, int arg1, InetAddress arg2,
			int arg3 ) throws IOException, UnknownHostException
	{
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Socket createSocket( InetAddress arg0, int arg1, InetAddress arg2,
			int arg3 ) throws IOException
	{
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}

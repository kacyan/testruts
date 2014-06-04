package jp.co.ksi.incubator.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;

/**
 * 証明書のホスト名と実際のホスト名をチェックするクラス。
 * なんちゃってはホスト名も異なるのでオーバライトして、常にtrueを返す
 * @author kac
 * @since 2012/06/20
 */
public class DummyHostnameVerifier implements HostnameVerifier
{
	private static Logger	log= Logger.getLogger( DummyHostnameVerifier.class );
	
	/**
	 * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String, javax.net.ssl.SSLSession)
	 */
	public boolean verify(String arg0, SSLSession arg1)
	{
		log.debug( "arg0="+ arg0 +", arg1="+ arg1 );
		return true;
	}
}

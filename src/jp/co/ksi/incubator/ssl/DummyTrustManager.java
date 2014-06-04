package jp.co.ksi.incubator.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

/**
 * @author kac
 */
public class DummyTrustManager implements X509TrustManager
{
	private static Logger	log= Logger.getLogger( DummyTrustManager.class );
	
	/**
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	public X509Certificate[] getAcceptedIssuers()
	{
		log.debug( "" );
		return null;
	}
	/**
	 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
	 */
	public void checkClientTrusted(X509Certificate[] certs, String arg1)
		throws CertificateException
	{
			log.debug( "arg1="+ arg1 );
	}
	/**
	 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
	 */
	public void checkServerTrusted(X509Certificate[] certs, String arg1)
		throws CertificateException
	{
			log.debug( "arg1="+ arg1 );
	}
}

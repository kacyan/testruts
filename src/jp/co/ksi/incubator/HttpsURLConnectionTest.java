package jp.co.ksi.incubator;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import jp.co.ksi.incubator.ssl.DummyHostnameVerifier;
import jp.co.ksi.incubator.ssl.DummyTrustManager;

public class HttpsURLConnectionTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	url= "https://wso3.os.ksi.co.jp/portal/check.jsp";
		String	webSignOn= "T@EZZawQWn4AACVGcC0";
		String	webSignOnKey= "V1NPU2FsdF8S4M0auxbGIqXyMRyrMCBWO31YQpbkXqDAedshxD0sIw";
		try
		{
			URL	u= new URL( url );
			HttpURLConnection	http= (HttpURLConnection)u.openConnection();
			if( http instanceof HttpsURLConnection )
			{
				HttpsURLConnection https = (HttpsURLConnection)http;
				SSLContext		ssl= SSLContext.getInstance( "SSL" );
				KeyManager[]	km= {};
				TrustManager[]	tm= { new DummyTrustManager() };
				ssl.init( km, tm, null );
				https.setHostnameVerifier( new DummyHostnameVerifier() );
				https.setSSLSocketFactory( ssl.getSocketFactory() );
			}
			http.addRequestProperty( "Cookie", "WebSignOn="+ webSignOn );
			http.addRequestProperty( "Cookie", "WebSignOnKey="+ webSignOnKey );
			
			
			int resCode= http.getResponseCode();
			String	resMsg= http.getResponseMessage();
			System.out.println( resCode +" "+ resMsg );
			
			String	contentType= http.getContentType();
			System.out.println( contentType );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
}

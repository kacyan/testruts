import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.Proxy.Type;

import javax.net.ssl.HttpsURLConnection;


public class URLConnectionTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	url= "https://ardbeg.dev.ksi.co.jp/ksiperson/";
//		url= "https://wso.ap.ksi.co.jp/";
		url= "https://172.16.98.137/testruts/";
		url= "https://133.253.67.110:10443";
		url= "http://133.253.67.110/eip-ws/test.jsp";
		String	charset= "utf-8";
		String	proxyHost= "172.16.10.3";
		int	proxyPort= 3128;
		try
		{
			Proxy	proxy= new Proxy( Type.HTTP, new InetSocketAddress( proxyHost, proxyPort ) );
			URL	u= new URL( url );
			URLConnection	con= u.openConnection();
			con.setUseCaches( false );
			if( con instanceof HttpsURLConnection )
			{
				HttpsURLConnection https = (HttpsURLConnection)con;
			}
			
//			con.setDoOutput( true );
//			PrintWriter	out= new PrintWriter( new OutputStreamWriter( con.getOutputStream(), charset ) );
//			out.print( URLEncoder.encode( "subject", charset ) );
//			out.print( "=" );
//			out.print( URLEncoder.encode( "件名", charset ) );
//			out.print( "&" );
//			out.print( URLEncoder.encode( "content", charset ) );
//			out.print( "=" );
//			out.print( URLEncoder.encode( "本文", charset ) );
//			out.print( "&" );
//			out.flush();
//			out.close();
			
			String	contentType= con.getContentType();
			System.out.println( "contentType="+ contentType );
			if( contentType != null )
			{
				int	index= contentType.indexOf( "charset=" );
				if( index > 0 )
				{
					charset= contentType.substring( index+ "charset=".length() ).trim();
				}
			}
			System.out.println( "charset=["+ charset +"]" );

			BufferedReader	in= new BufferedReader( new InputStreamReader( con.getInputStream(), charset ) );
			String	line= in.readLine();
			while( line != null )
			{
				System.out.println( line );
				line= in.readLine();
			}
			in.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
}

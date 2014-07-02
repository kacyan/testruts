package jp.co.ksi.incubator;


public class ReplaceTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String regex = "(:$|:443$|:80$)";
		String	src= "wso2.kspirit.gr.kubota.co.jp:443";
		String	dst= src.replaceAll( regex, "" );
		System.out.println( dst );
		
		src= "wso2.kspirit.gr.kubota.co.jp:80";
		dst= src.replaceAll( regex, "" );
		System.out.println( dst );
		
		src= "wso2.kspirit.gr.kubota.co.jp:8080";
		dst= src.replaceAll( regex, "" );
		System.out.println( dst );
		
		src= "wso2.kspirit.gr.kubota.co.jp:8443";
		dst= src.replaceAll( regex, "" );
		System.out.println( dst );
		
		src= "wso2.kspirit.gr.kubota.co.jp:10080";
		dst= src.replaceAll( regex, "" );
		System.out.println( dst );

		src= "wso2.kspirit.gr.kubota.co.jp:";
		dst= src.replaceAll( regex, "" );
		System.out.println( dst );

		src= "wso2.kspirit.gr.kubota.co.jp";
		dst= src.replaceAll( regex, "" );
		System.out.println( dst );
		
		src= "/testruts/SQLServlet/aaa";
		dst= src.replaceAll( "/aaa\\Z", "" );
		System.out.println( dst );
		src= "/testruts/SQLServlet/";
		dst= src.replaceAll( "/\\Z", "" );
		System.out.println( dst );
		src= "/testruts/SQLServlet/aaa.123-b_c";
		dst= src.replaceAll( "/aaa.123-b_c\\Z", "" );
		System.out.println( dst );

		src= "https://www.googleapis.com/gmail/v1/${mailAddress}/messages?maxResults=20";
		dst= src.replaceAll( "\\$\\{mailAddress\\}", "kacyan@gmail.com");
		System.out.println( dst );
	}
	
}

package jp.co.ksi.incubator;

import JP.co.ksi.util.URLDecoder;

public class URLDecoderTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	src= "TF%40CdL%40C%40CdN%60LTF%60LDC%60L%60LPF%60L%60LBC%40CZFIC%5BRG";
		String	dst= URLDecoder.decode( src, "utf-8" );
		System.out.println( dst );
		src= "TF%40CRF%60L%60LdNPFBC%40ClMPF%60L%40C%40C%60LTFACdRFICoCCA";
		dst= URLDecoder.decode( src, "utf-8" );
		System.out.println( dst );
		
		src= "ardbeg.dev.ksi.co.jp%3A8080/";
		dst= URLDecoder.decode( src, "utf-8" );
		System.out.println( dst );
	}
	
}

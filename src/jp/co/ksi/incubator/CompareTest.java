package jp.co.ksi.incubator;

public class CompareTest
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	text1= "wso: 2009";
		String	text2= "wso: 2008";
		
		System.out.println( text1 +" "+ text1.compareToIgnoreCase( text2 ) +" "+ text2 );
		
		text2= "wso: 2010";
		System.out.println( text1 +" "+ text1.compareToIgnoreCase( text2 ) +" "+ text2 );
		
		text2= "wso: 2000";
		System.out.println( text1 +" "+ text1.compareToIgnoreCase( text2 ) +" "+ text2 );
	}
	
}

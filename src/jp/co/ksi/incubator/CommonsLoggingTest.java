package jp.co.ksi.incubator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.LogFactoryImpl;

public class CommonsLoggingTest
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		
		Log	log= LogFactoryImpl.getLog( "" );
		System.out.println( log );
	}

}

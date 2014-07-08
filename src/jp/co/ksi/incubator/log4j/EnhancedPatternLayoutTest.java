package jp.co.ksi.incubator.log4j;

import java.util.Properties;
import java.util.TimeZone;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * log4jのEnhancedPatternLayoutの習作
 * @author kac
 * @since 2014/07/08
 * @version 2014/07/08
 * <pre>
 * log4j.appender.STDOUT=org.apache.log4j.ConsoleAppender
 * log4j.appender.STDOUT.layout=org.apache.log4j.EnhancedPatternLayout
 * #log4j.appender.STDOUT.layout.ConversionPattern=%d{HH:mm:ss.SSS z}{America/Los_Angeles} %-5p %C{1} %M.%L - %m\n
 * #log4j.appender.STDOUT.layout.ConversionPattern=%d{HH:mm:ss.SSS (z)zzzz}{Asia/Jakarta} %-5p %C{1} %M.%L - %m\n
 * log4j.appender.STDOUT.layout.ConversionPattern=%d{HH:mm:ss.SSS (z)zzzz}{JST} %-5p %C{1} %M.%L - %m\n
 * #log4j.appender.STDOUT.threshold=WARN
 * </pre>
 */
public class EnhancedPatternLayoutTest
{
	private static Logger	log= Logger.getLogger( EnhancedPatternLayoutTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		Properties	props= new Properties();
		props.setProperty( "log4j.appender.STDOUT", "org.apache.log4j.ConsoleAppender" );
		props.setProperty( "log4j.appender.STDOUT.layout", "org.apache.log4j.EnhancedPatternLayout" );
		props.setProperty( "log4j.appender.STDOUT.layout.ConversionPattern", "%d{HH:mm:ss.SSS (z)zzzz}{Asia/Jakarta} %-5p %C{1} %F %M.%L - %m\n" );
		props.setProperty( "log4j.rootLogger", "INFO, STDOUT" );
		props.setProperty( "log4j.logger.jp.co.ksi.incubator", "DEBUG" );
		PropertyConfigurator.configure( props );
		
		log.debug( "KacDebug" );
		TestA	a= new TestA();
		a.test( "from EnhancedPatternLayoutTest" );
	}
	
}

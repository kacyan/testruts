package jp.co.ksi.incubator.log4j;

import org.apache.log4j.Logger;

/**
 * EnhancedPatternLayout のテスト用クラス
 * @author kac
 * @since 2014/07/08
 * @version 2014/07/08
 */
public class TestA
{
	private static Logger	log= Logger.getLogger( TestA.class );
	
	public void test( String text )
	{
		log.info( "text="+ text );
	}
}

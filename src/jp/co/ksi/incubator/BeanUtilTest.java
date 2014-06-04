package jp.co.ksi.incubator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.co.ksi.eip.commons.util.KsiBeanUtilsResolver;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

/**
 * BeanUtilsの脆弱性と対応策を調べる
 * @author kac
 * @since 2014/04/25
 * @version 2014/05/16
 */
public class BeanUtilTest
{
	private static Logger	log= Logger.getLogger( BeanUtilTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		TestBean	bean= new TestBean();
		ClassLoader	cl= bean.getClass().getClassLoader();
		while( cl != null )
		{
			System.out.println( cl );
			cl= cl.getParent();
		}
		
		BeanUtilsBean.getInstance().getPropertyUtils().setResolver( new KsiBeanUtilsResolver() );
		
		HashMap<String, Object>	props= new HashMap<String, Object>();
		props.put( "name", "なまえ" );
		props.put( "description", "記述" );
		props.put( "multipartRequestHandler.mapping.moduleConfig.actionForwardClass", "" );
		props.put( "multipartRequestHandler.servlet.internal.escape", "false" );
		props.put( "sub.name", "サブ名" );
		props.put( "sub.name.file.name", "サブのファイル名" );
		props.put( "sub.servletWrapper", "" );
		props.put( "class.classLoader.defaultAssertionStatus", "true" );
		props.put( "*.classLoader.defaultAssertionStatus", "true" );
//		props.setProperty( "class.classLoader.packageAssertionStatus", "ccc" );
//		props.setProperty( "class.classLoader.signers", "ddd" );
//		props.put( "class.classLoader.classLoader.resources.context.dirContext.docBase", "ddd" );
		String[]	value= {"テスト"};
		props.put( "test", value );
		
		try
		{
			BeanUtils.populate( bean, props );
			
			System.out.println( bean.getName() );
			System.out.println( bean.getDescription() );
			System.out.println( bean.getTest() );
			System.out.println( bean.getSub().getName() );
			
			Map<?, ?>	map= BeanUtils.describe( bean );
			for( Iterator<?> i= map.keySet().iterator(); i.hasNext(); )
			{
				Object	key= i.next();
				log.debug( "map: "+ key +"="+ map.get( key ) );
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
	}

}

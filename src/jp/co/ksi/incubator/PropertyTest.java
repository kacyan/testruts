package jp.co.ksi.incubator;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public class PropertyTest
{
	private static Logger	log= Logger.getLogger( PropertyTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		try
		{
			Class	cls= Class.forName( "jp.co.ksi.testruts.ChangePasswordForm" );
			Object	obj= cls.newInstance();
			
			Properties	props= new Properties();
			props.setProperty( "uid", "uid" );
			
			BeanUtils.populate( obj, props );
			log.debug( obj.toString() );
			
			Map	map= BeanUtils.describe( obj );
			for( Iterator i= map.keySet().iterator(); i.hasNext(); )
			{
				String	key= (String)i.next();
				Object	value= map.get( key );
				log.debug( key +"="+ value );
			}
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
		}
		
	}
	
}

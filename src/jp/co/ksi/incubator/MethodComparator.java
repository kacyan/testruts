package jp.co.ksi.incubator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * sortの習作
 * @author kac
 * @since 2014/05/08
 * @version 2014/05/08
 */
@SuppressWarnings( "rawtypes" )
public class MethodComparator implements Comparator
{
	/**
	 * @param args
	 */
	@SuppressWarnings( "unchecked" )
	public static void main( String[] args )
	{
		ClassLoader	cl= MethodComparator.class.getClassLoader();
		Method[]	method= cl.getClass().getMethods();
		Arrays.sort( method, new MethodComparator() );
		for( int i= 0; i < method.length; i++ )
		{
			System.out.println( method[i] );
		}
	}

	@Override
	public int compare( Object o1, Object o2 )
	{
		if( o1 == null )	return 0;
		if( o2 == null )	return 0;
		
		if( ( o1 instanceof Method ) && ( o2 instanceof Method ) )
		{
			Method	m1= (Method)o1;
			Method	m2= (Method)o2;
			
			return m1.getName().compareTo( m2.getName() );
		}
		else
		{
			return o1.toString().compareTo( o2.toString() );
		}
	}
	
}

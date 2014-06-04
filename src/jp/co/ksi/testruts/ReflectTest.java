package jp.co.ksi.testruts;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import jp.co.ksi.incubator.TestBean;

public class ReflectTest
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		TestBean	test= new TestBean();
		reflect( test );
	}

	public static void reflect( Object test )
	{
		Class	cls= test.getClass();
		System.out.println( "----- "+ cls.getName() +" -----" );
		Method[]	methods= cls.getMethods();
		for( int i= 0; i < methods.length; i++ )
		{
			System.out.print( getMethodShortString( methods[i] ) );
			if( methods[i].getName().matches( "getClass" ) )
			{
				System.out.println();
				continue;
			}
			if( methods[i].getName().matches( "get.*" ) )
			{//	getメソッド
				if( methods[i].getParameterTypes().length == 0 )
				{//	パラメータなし
					try
					{
						Object	result= methods[i].invoke( test, (Object[])null );
						System.out.println( " [invoke] "+ result );
						if( result != null )
						{
							reflect( result );
						}
					}
					catch( Exception e )
					{
						e.printStackTrace();
					}
				}
			}
			System.out.println();
		}
		System.out.println( "----------" );
	}

	public static String getMethodString( Method method )
	{
		StringBuffer	buff= new StringBuffer();
		int	mod= method.getModifiers();
		if( Modifier.isPublic( mod ) )	buff.append( "public " );
		if( Modifier.isProtected( mod ) )	buff.append( "protected " );
		if( Modifier.isPrivate( mod ) )	buff.append( "private " );
		if( Modifier.isStatic( mod ) )	buff.append( "static " );
		if( Modifier.isStrict( mod ) )	buff.append( "strict " );
		if( Modifier.isSynchronized( mod ) )	buff.append( "synchronized " );
		if( Modifier.isTransient( mod ) )	buff.append( "transient " );
		if( Modifier.isAbstract( mod ) )	buff.append( "abstract " );
		if( Modifier.isFinal( mod ) )	buff.append( "final " );
		if( Modifier.isInterface( mod ) )	buff.append( "interface " );
		if( Modifier.isVolatile( mod ) )	buff.append( "volatile " );
		if( Modifier.isNative( mod ) )	buff.append( "native " );
		buff.append( method.getReturnType().getName() +" " );
		buff.append( method.getName() +"(" );
		Class[] parameterTypes= method.getParameterTypes();
		if( parameterTypes.length > 0 )
		{
			buff.append( " "+ parameterTypes[0].getName() );
			for( int j= 1; j < parameterTypes.length; j++ )
			{
				buff.append( ", "+ parameterTypes[j].getName() );
			}
		}
		buff.append( ")" );
		Class[]	exceptionTypes= method.getExceptionTypes();
		if( exceptionTypes.length > 0 )
		{
			buff.append( " throws "+ exceptionTypes[0].getName() );
			for( int j= 1; j < exceptionTypes.length; j++ )
			{
				buff.append( ", "+ exceptionTypes[j].getName() );
			}
		}
		buff.append( " - "+ method.getDeclaringClass().getName() );
		return buff.toString();
	}

	public static String getMethodShortString( Method method )
	{
		StringBuffer	buff= new StringBuffer();
//		buff.append( method.getReturnType().getName() +" " );
		buff.append( method.getName() +"(" );
		Class[] parameterTypes= method.getParameterTypes();
		if( parameterTypes.length > 0 )
		{
			buff.append( " "+ parameterTypes[0].getCanonicalName() );
			for( int j= 1; j < parameterTypes.length; j++ )
			{
				buff.append( ", "+ parameterTypes[j].getCanonicalName() );
			}
			buff.append( " " );
		}
		buff.append( ")" );
		return buff.toString();
	}
}

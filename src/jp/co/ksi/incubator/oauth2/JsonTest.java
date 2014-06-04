package jp.co.ksi.incubator.oauth2;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;

import net.arnx.jsonic.JSON;

/**
 * JSONICを使ったJSONの習作
 * @author kac
 * @since 2012/05/31
 * @version 2021/06/20
 */
public class JsonTest
{
	private static Logger	log= Logger.getLogger( JsonTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	file= "misc/oauth/google/json";
		file= "misc/kac-events.json";
		Properties	props= null;
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( file );
			props= JSON.decode( in, Properties.class );
			System.out.println( props );
			
			if( log.isDebugEnabled() )
			{
				String[]	keys= new String[props.size()];
				keys= props.keySet().toArray( keys );
				Arrays.sort( keys );
				for( int i= 0; i < keys.length; i++ )
				{
					log.debug( keys[i] +"="+ props.getProperty( keys[i] ) );
				}
			}

			for( int i= 0; true; i++ )
			{
				String	start= props.getProperty( "items."+ i +".start.dateTime" );
				if( start == null )
				{
					start= props.getProperty( "items."+ i +".start.date" );
				}
				String	end= props.getProperty( "items."+ i +".end.dateTime" );
				if( end == null )
				{
					end= props.getProperty( "items."+ i +".end.date" );
				}
				String	summary= props.getProperty( "items."+ i +".summary" );
				String	id= props.getProperty( "items."+ i +".id" );
				if( summary == null )	break;
				
				log.debug( "["+ i +"] "+ summary +" "+ start +"-"+ end +"("+ id +")" );
			}
			
		}
		catch( Exception e )
		{
			log.error( "", e );;
			return;
		}
		finally
		{
			if( in != null )
			{
				try
				{
					in.close();
				}
				catch( Exception e )
				{
					log.error( "", e );;
				}
			}
		}
		
	}
	
}

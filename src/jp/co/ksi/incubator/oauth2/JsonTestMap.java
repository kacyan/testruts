package jp.co.ksi.incubator.oauth2;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import net.arnx.jsonic.JSON;

/**
 * JSONICを使ったJSONの習作
 * @author kac
 * @since 2012/06/20
 * @version 2021/06/20
 */
public class JsonTestMap
{
	private static Logger	log= Logger.getLogger( JsonTestMap.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	file= "misc/oauth/google/json";
		file= "misc/kac-event.json";
		HashMap<String, ArrayList>	map= new HashMap<String, ArrayList>();
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( file );
			map= JSON.decode( in, map.getClass() );
			System.out.println( map );
			
			String[] keys;
			debugMap( map );
			
			//	itemsを取得する
			ArrayList	list= map.get( "items" );
			for( int i= 0; i < list.size(); i++ )
			{
				Object	obj= list.get( i );
				if( obj instanceof Properties )
				{
					Properties props = (Properties)obj;
					log.debug( "["+ i +"]"+ props );
				}
				else if( obj instanceof Map )
				{
					Map<String,?> m = (Map<String,?>)obj;
					keys= new String[m.size()];
					keys= m.keySet().toArray( keys );
					for( int j= 0; j < keys.length; j++ )
					{
						log.debug( "items"+"["+ i +"]"+ keys[j] +"="+ m.get( keys[j] ) );
					}
					
					log.debug( m.get( "start" ).getClass().getName() );
					log.debug( m.get( "end" ).getClass().getName() );
					log.debug( m.get( "summary" ) );
				}
				else
				{
					log.debug( "["+ i +"]"+ obj );
				}
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

	public static void debugMap( HashMap<String, ArrayList> map )
	{
		String[]	keys= new String[map.size()];
		keys= map.keySet().toArray( keys );
		Arrays.sort( keys );
		for( int i= 0; i < keys.length; i++ )
		{
			Object	obj= map.get( keys[i] );
			if( obj instanceof String )
			{
				log.debug( keys[i] +"="+ obj );
			}
			else if( obj instanceof ArrayList )
			{
				ArrayList list = (ArrayList)obj;
				for( int j = 0; j < list.size(); j++ )
				{
					log.debug( keys[i] +"["+ j +"]="+  list.get( j ) );
				}
			}
			else if( obj instanceof Map )
			{
				Map	m= (Map)obj;
				for( Iterator it= m.keySet().iterator(); it.hasNext(); )
				{
					String	key= (String)it.next();
					log.debug( keys[i] +"."+ key +"="+  m.get( key ) );
				}
			}
			else
			{
				log.debug( keys[i] +"="+ obj.getClass().getName() +" - "+ obj );
			}
		}
	}
	
}

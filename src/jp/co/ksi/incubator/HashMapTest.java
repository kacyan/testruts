package jp.co.ksi.incubator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

/**
 * HashMapのハッシュ衝突を調べる習作
 * @param args
 * @since 2012/07/26
 */
public class HashMapTest
{
	private int value;
	
	public static void main( String[] args )
	{
		HashMap<HashMapTest,String>	map= new HashMap<HashMapTest,String>();

		DecimalFormat	df= new DecimalFormat( "00" );
		for( int i= 0; i < 10000; i++ )
		{
			map.put( new HashMapTest(i), "hoge"+ df.format( i ) );
		}

		ArrayList<Integer>	list= new ArrayList<Integer>();
		Set<Entry<HashMapTest, String>>	set= map.entrySet();
		for( Iterator<Entry<HashMapTest, String>> i= set.iterator(); i.hasNext(); )
		{
			Entry<HashMapTest, String>	entry= i.next();
			int hashCode = entry.hashCode();
			if( list.contains( hashCode ) )
			{//	ハッシュコード重複
//				System.out.println( entry +" - "+ hashCode );
			}
			else
			{
				list.add( hashCode );
			}
		}
		System.out.println( "ハッシュ重複="+ ( map.size() - list.size() ) );
		
		String	hoge= "hoge";
		hoge.hashCode();
	}

	public HashMapTest( int i )
	{
		value= i;
	}
	
	@Override
	public int hashCode()
	{
		return 10000 - value;
	}

	@Override
	public String toString()
	{
		return String.valueOf( value );
	}
	
}

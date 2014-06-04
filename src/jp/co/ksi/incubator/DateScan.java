package jp.co.ksi.incubator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文字列から日付部分を抽出してみる習作
 * @author kac
 * @since 2012/01/19
 * @version 2012/01/19
 */
public class DateScan
{
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	content= "申請済 2011/11/30 13:09  依頼者：中西 康仁";
		
		String	found= content.replaceAll( "[^0-9]*([0-9][0-9]/[0-9][0-9])[^0-9]*.*", "$1" );
		System.out.println( found );
		
		SimpleDateFormat	sdf= new SimpleDateFormat( "yyyy/MM/dd" );
		//	日付っぽい文字列で分割してみる
		String[]	ret= content.split( "[0-9]+/[0-9]+/[0-9]+" );
		System.out.println( ret.length );
		for( int i= 0; i < ret.length; i++ )
		{
			System.out.println( "("+ i +")["+ ret[i] +"]" );
			try
			{
				int	start= content.indexOf( ret[i] ) + ret[i].length();
				found= content.substring( start );
				System.out.println( "substring("+ start +")->"+ found );
				int	end= content.indexOf( " ", start ) - start;
				if( end > 0 )
				{//	分割した箇所の文字列を取得する->日付文字列の可能性がある
					found= found.substring( 0, end );
					System.out.println( "substring(0,"+ end +")->"+found );
					//	Date変換してみる
					Date	d= sdf.parse( found );
					System.out.println( "FOUND=["+ sdf.format( d ) +"]" );
				}
				else
				{
					continue;
				}
				
				String	prefix= content.substring( 0, start );
				String	suffix= content.substring( end + found.length() );
				System.out.println( "prefix=["+ prefix +"]" );
				System.out.println( "found =["+ found +"]" );
				System.out.println( "suffix=["+ suffix +"]" );
				
				System.out.println( prefix + "<a href=>"+ found +"</a>"+ suffix );

				System.out.println( content.replaceAll( found, "[KacDebug]" ) );
			}
			catch( Exception e )
			{
				System.out.println( e.toString() );
			}
		}
		
	}
	
}

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Test {

	public static void main( String[] args )
	{
		String	src= "2012-03-17T15:00:00+09:00";
		SimpleDateFormat	sdf= new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
		SimpleDateFormat	outFormatter= new SimpleDateFormat( "yyyy/MM/dd HH:mm" );
		try
		{
			Date date = sdf.parse( src );
			System.out.println( date );
			System.out.println( outFormatter.format( date ) );
		}
		catch( ParseException e )
		{
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}

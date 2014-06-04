package jp.co.ksi.incubator;

import java.sql.Connection;
import java.sql.DriverManager;

public class JavaDBTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String	url= "jdbc:derby:/temp/javadb/sample1;create=true";
		Connection	con= null;
		try
		{
			Class.forName( "org.apache.derby.jdbc.EmbeddedDriver" );
			con= DriverManager.getConnection( url );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch( Exception e )
			{
			}
		}
	}

}

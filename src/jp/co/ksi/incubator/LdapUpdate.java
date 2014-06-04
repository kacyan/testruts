package jp.co.ksi.incubator;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import JP.co.ksi.ldap.LdapManager;
import JP.co.ksi.ldap.LdifProperties;

/**
 * 
 * @author kac
 * @since 2012/07/04
 * @version 2012/07/04
 */
public class LdapUpdate
{
	private static Logger	log= Logger.getLogger( LdapUpdate.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	confFile= "ldap.properties";
		if( args.length < 1 )
		{//	引数エラー
			System.out.println( "" );
			System.out.println( " usage : LdapUpdate {dn} {ldifFile}" );
			System.out.println( "\tdn (must) : uid=s999999,ou=People,ou=Master,o=WebSignOn,c=JP" );
			System.out.println( "\tldifFile  : s999999.ldif" );
			System.out.println();
			return;
		}
		String	dn= args[0];
		String	ldifFile= "";
		if( args.length > 1 )
		{
			ldifFile= args[1];
		}
		
		Properties	appConfig= new Properties();
		try
		{
			FileInputStream	in= new FileInputStream( confFile );
			appConfig.load( in );
			in.close();
			
			PropertyConfigurator.configure( appConfig );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return;
		}
		
		LdapManager	ldap= null;
		try
		{
			ldap= new LdapManager( appConfig );
			
			//	現在の値を一旦出力する
			LdifProperties	ldif= ldap.getEntry( dn );
			ldif.store( System.out, "" );
			
			if( ldifFile.length() > 0 )
			{//	ldifFileの指定があれば読込んで更新する
				try
				{
					FileInputStream	in= new FileInputStream( ldifFile );
					ldif= new LdifProperties();
					ldif.load( in );
					in.close();
					
					if( ldap.modifyEntry( ldif ) )
					{//	成功
						log.info( "[UPDATE] "+ ldif );
					}
					else
					{//	失敗
						log.error( "["+ ldap.getLastError() +"] "+ ldif );
					}
				}
				catch( Exception e )
				{
					log.error( "ldifFile=["+ ldifFile +"]", e );
				}
			}
		}
		catch( Exception e )
		{
			log.error( "appConfig="+ appConfig, e );
		}
		finally
		{
			if( ldap != null )
			{//	後始末
				ldap.close();
			}
		}
	}
	
	
}

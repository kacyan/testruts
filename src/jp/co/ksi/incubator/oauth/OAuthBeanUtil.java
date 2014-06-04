package jp.co.ksi.incubator.oauth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;

/**
 * OAuthBeanの生成・保存などを支援するクラス
 * @author kac
 * @since 2012/05/24
 * @version 2012/05/24
 */
public class OAuthBeanUtil
{
	private static Logger	log= Logger.getLogger( OAuthBeanUtil.class );
	
	
	public static void main( String[] args )
	{
		String	file= "misc/oauth.properties";
		log.debug( "file="+ file );
		
		OAuthBean oauth = load( new File(file) );
		
		String	saveFile= "misc/save.oauth";
		
		save( oauth, new File(saveFile) );
		
		
		System.out.println( oauth );
	}

	public static void save( OAuthBean oauth, File file )
	{
		FileOutputStream	out= null;
		try
		{
			out= new FileOutputStream( file );
			
			save( oauth, out );
			
		}
		catch( Exception e )
		{
			log.error( file, e );
			
		}
		finally
		{
			if( out != null )
			{//	後始末
				try
				{
					out.close();
				}
				catch( Exception e )
				{
					log.error( file, e );
				}
			}
		}
	}

	public static void save( OAuthBean oauth, FileOutputStream out ) throws Exception
	{
		log.debug( "oauth="+ oauth );
		PropertyUtilsBean	util= new PropertyUtilsBean();
		Map	m= util.describe( oauth );
		log.debug( m );
		log.debug( m.getClass().getName() );
		Properties	props= new Properties();
		props.putAll( m );
		props.remove( "class" );
		
		props.store( out, "OAuth" );
	}
	
	public static OAuthBean load( File file )
	{
		OAuthBean oauth= null;
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( file );
			oauth= load( in );

		}
		catch( Exception e )
		{
			log.error( file, e );
		}
		finally
		{
			if( in != null )
			{//	後始末
				try
				{
					in.close();
				}
				catch( Exception e )
				{
					log.error( file, e );
				}
			}
		}
		return oauth;
	}
	
	public static OAuthBean load( FileInputStream in ) throws Exception
	{
		OAuthBean	oauth= new OAuthBean();
		
		Properties	props= new Properties();
		props.load( in );
		
		BeanUtilsBean	util= new BeanUtilsBean();
		util.populate( oauth, props );
		log.debug( "oauth="+ oauth );
		return oauth;
	}

}

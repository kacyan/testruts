package jp.co.ksi.incubator.jcraft;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import jp.co.ksi.eip.commons.struts.DownloadAction;

import org.apache.log4j.Logger;

/**
 * sftpのget
 * @author kac
 * @since 2011/12/13
 * @version 2011/12/13
 * <pre>
 * [in]
 * 	path
 * [out]
 * 	???
 * </pre>
 */
public class SftpGetBL extends SftpBL
{
	static Logger	log= Logger.getLogger( SftpGetBL.class );
	
	protected String exec() throws Exception
	{
		String path = getParam( "path", "" );
		File	f= new File( path );
		
		File	dst= File.createTempFile( "sftp", ".dat" );
		log.debug( "createTempFile "+ dst );
		InputStream	in= null;
		FileOutputStream	out= null;
		try
		{
			out= new FileOutputStream( dst );
			in= sftp.get( path );
			
			byte[]	b= new byte[1024];
			for( int len= 0; len >= 0; len= in.read( b ) )
			{
				out.write( b, 0, len );
			}
			out.flush();
			log.info( "[success] "+ getConnectInfo() +":"+ path );
			return APL_OK;
		}
		catch( Exception e )
		{
			log.error( "", e );
			return APL_ERR;
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
					log.error( "in close", e );
				}
			}
			if( out != null )
			{
				try
				{
					out.close();
				}
				catch( Exception e )
				{
					log.error( "out close", e );
				}
			}
			outData.put( DownloadAction.REQ_ATTR_NAME_FILE, dst );
			outData.put( DownloadAction.REQ_ATTR_NAME_DEL, "true" );
			outData.put( DownloadAction.REQ_ATTR_NAME_FILENAME, f.getName() );
		}
	}

}

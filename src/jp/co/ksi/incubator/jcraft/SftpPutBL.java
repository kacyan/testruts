package jp.co.ksi.incubator.jcraft;

import java.io.OutputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

/**
 * sftpのput
 * @author kac
 * @since 2011/12/14
 * @version 2011/12/14
 * <pre>
 * [in]
 * 	path
 * [out]
 * 	???
 * </pre>
 */
public class SftpPutBL extends SftpBL
{
	static Logger	log= Logger.getLogger( SftpPutBL.class );
	
	/**
	 * アップロード時のフォームでの名前：jspでの指定と一致させること
	 */
	public static final String FORM_FILE_NAME = "file";

	protected String exec() throws Exception
	{
		//	put先のパス
		String	path = getParam( "path", "" );

		FormFile	formFile= (FormFile)formData.get( FORM_FILE_NAME );
		if( formFile == null )
		{
			log.error( "formFile is null" );
			return APL_ERR;
		}
		if( formFile.getFileName() == null )
		{
			log.error( "formFile.fileName is null" );
			return APL_ERR;
		}
		if( formFile.getFileName().equals("") )
		{
			log.error( "formFile.fileName is blank" );
			return APL_ERR;
		}
		
		if( path.matches( ".*/$" ) )
		{//	pathの末尾が/ -> フォルダ
			//	ファイル名を付加する
			path= path + formFile.getFileName();
		}
		
		InputStream	in= formFile.getInputStream();
		OutputStream	out= null;
		try
		{
			out= sftp.put( path );
			
			byte[]	b= new byte[1024];
			for( int len= 0; len >= 0; len= in.read( b ) )
			{
				out.write( b, 0, len );
			}
			out.flush();
			log.info( "[success] "+ formFile.getFileName() +"->"+ getConnectInfo() +":"+ path );
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
		}
	}

}

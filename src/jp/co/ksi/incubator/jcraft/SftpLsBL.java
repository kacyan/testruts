package jp.co.ksi.incubator.jcraft;

import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * sftpのls
 * @author kac
 * @since 2011/12/13
 * @version 2011/12/13
 * <pre>
 * [in]
 * 	path
 * [out]
 * 	java.util.Vector(LsEntry)	vecLsEntry
 * </pre>
 */
public class SftpLsBL extends SftpBL
{
	static Logger	log= Logger.getLogger( SftpLsBL.class );
	
	protected String exec() throws Exception
	{
		String path = getParam( "path", "" );
		Vector	vec= sftp.ls( path );
		log.info( "[success] "+ getConnectInfo() +":"+ path );
		outData.put( "vecLsEntry", vec );
		return APL_OK;
	}

}

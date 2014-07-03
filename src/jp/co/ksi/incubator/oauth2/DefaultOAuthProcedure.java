package jp.co.ksi.incubator.oauth2;

import java.io.FileOutputStream;

import jp.co.ksi.incubator.portlet.OAuthConfig;

import org.apache.log4j.Logger;

import net.arnx.jsonic.JSON;

/**
 * oauthを保存するクラスのデフォルト実装
 * @author kac
 * @since 2014/07/03
 * @version 2014/07/03
 */
public class DefaultOAuthProcedure implements OAuthProcedure {

	private static Logger	log= Logger.getLogger(DefaultOAuthProcedure.class);


	@Override
	public void saveOAuth( OAuth oauth, OAuthConfig oauthConfig ) throws Exception {

		//	oauthをファイルに保存する
		FileOutputStream	out= null;
		String	oauthFile= oauthConfig.getOauthFile();
		try
		{
			out= new FileOutputStream( oauthFile );
			JSON.encode( oauth, out );
			log.info( "oauth is saved "+ oauthFile );
		}
		catch( Exception e )
		{//	ERR
			log.error( oauthFile, e );
			throw e;
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
					log.error( oauthFile, e );
				}
			}
		}
	}

}

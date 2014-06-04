package jp.co.ksi.eip.commons.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * バッチ実行特有の共通処理を実装してみます
 * @author kac
 * @since 2010/02/05
 * @version 2010/02/05
 */
public class BatchUtil
{
	private static final String STAT_ENABLE = "enable";
	private static final String STAT_DISABLE = "disable";
	private static final String KEY_STATUS = "status";
	private static Logger	log= Logger.getLogger( BatchUtil.class );
	
	/**
	 * バッチ稼働状況を確認します
	 * @param batchStatusFile
	 * @throws Exception	稼働不可の場合、例外を吐く
	 * TODO この処理は呼び出し側で行うべきかもしれない。なので切り出しやすいようにメソッド化しておく
	 */
	public static boolean isEnable( String batchStatusFile )
	{
		File	f= new File( batchStatusFile );
		if( !f.exists() )
		{//	バッチ稼働状況ファイルが無い -> 稼働可能
			return true;
		}
		//	バッチ稼働状況ファイルを読み込む
		Properties	props= new Properties();
		FileInputStream in= null;
		try
		{
			in= new FileInputStream( batchStatusFile );
			props.load( in );
			String	batchStatus= props.getProperty( KEY_STATUS, "" );
			if( batchStatus.equalsIgnoreCase( STAT_ENABLE ) )
			{//	稼働可能
				log.debug( "batchStatus=["+ batchStatus +"] "+ batchStatusFile );
				return true;
			}
			else
			{//	稼働不可 -> 終了
				log.debug( "batchStatus=["+ batchStatus +"] "+ batchStatusFile );
				return false;
			}
		}
		catch( Exception e )
		{//	ファイル読み込み中にエラー -> 終了
			log.fatal( e.toString() +" "+ batchStatusFile );
			return false;
		}
		finally
		{//	後始末
			try
			{
				in.close();
			}
			catch( Exception e )
			{
				log.fatal( e.toString() );
			}
		}
	}

	public static void setDisable( String batchStatusFile )
	{
		Properties	props= new Properties();
		props.setProperty( KEY_STATUS, STAT_DISABLE );

		FileOutputStream	out= null;
		try
		{
			out= new FileOutputStream( batchStatusFile );
			props.store( out, "" );
			log.info( "batchStatus=[disable] "+ batchStatusFile );
		}
		catch( Exception e )
		{//	ファイル書き込み中にエラー(どうしようもない)
			log.error( e.toString(), e );
		}
		finally
		{//	後始末
			try
			{
				out.close();
			}
			catch( Exception e )
			{
				log.error( e.toString() );
			}
		}
	}

	public static void setEnable( String batchStatusFile )
	{
		File	f= new File( batchStatusFile );
		if( f.delete() )
		{//	成功
			log.info( "batchStatus=[enable] "+ batchStatusFile );
		}
		else
		{//	失敗
			log.error( batchStatusFile );
		}
	}
}

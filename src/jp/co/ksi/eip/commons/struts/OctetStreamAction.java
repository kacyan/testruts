/**
 * 
 */
package jp.co.ksi.eip.commons.struts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ダウンロード用Actionクラス
 * @author kac
 * @version 1.0.0
 * @since 1.0.0
 * StrutsModel実行後、リクエストにセットされたFileオブジェクトを
 * レスポンスに出力することでダウンロード処理を実現します
 * ファイルが見つからない等ダウンロードに失敗した場合は、
 * RESULT_DOWNLOAD_FAILEDを返します
 * <pre>
 * 連続トランザクションにしたくなかったので、InvokeActionをオーバライトしました
 * InvokeActionが正常終了したかをActionForwardで確認しています。
 * このため、forwardしませんがaction-mappingにforward successの指定が必要になります
 * 
 * DownloadActionとの違いは、レスポンス・ヘッダーに
 * Content-Disposition attachment ... をセットしないだけです。
 * </pre>
 */
public class OctetStreamAction extends InvokeAction
{
	/**
	 * ActionForwardの名前
	 * "download.failed"ダウンロード失敗を示します
	 */
	public static final String RESULT_DOWNLOAD_FAILED= "download.failed";

	private static Logger	log= Logger.getLogger(OctetStreamAction.class);

	/* (非 Javadoc)
	 * @see JP.co.ksi.struts.InvokeAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		ActionForward	forward= super.execute( mapping, form, request, response );
		if( forward == null )
		{
			log.debug( "forward="+ forward );
			log.debug( "mapping.name="+ mapping.getName() );
			log.debug( "mapping.name="+ mapping );
		}
		else if( forward.getName().equals( IStruts.APL_OK ) )
		{//	B/Lが正常終了した
			//	リクエストからFileオブジェクトを取得する
			File	file= (File)request.getAttribute( DownloadAction.REQ_ATTR_NAME_FILE );
			if( file == null )
			{
				log.error( RESULT_DOWNLOAD_FAILED +" file not set." );
				return mapping.findForward( RESULT_DOWNLOAD_FAILED );
			}
			//	Fileを読み込む
			FileInputStream in= null;
			try
			{
				in= new FileInputStream( file );
			}
			catch( FileNotFoundException e )
			{
				log.error( RESULT_DOWNLOAD_FAILED +" file not found.", e );
				return mapping.findForward( RESULT_DOWNLOAD_FAILED );
			}
			
			//	リクエストからファイル名を取得する
			String	filename= (String)request.getAttribute( DownloadAction.REQ_ATTR_NAME_FILENAME );
			if( ( filename == null ) || filename.equals( "" ) )
			{//	ファイル名の指定がない
				filename= file.getName();
			}
			filename= filename.toLowerCase();
			log.debug( "filename="+ filename );

			//	コンテントタイプを指定する
			String	contentType= "Application/Octet-Stream";
			if( filename.matches( ".*\\.jpeg$|.*\\.jpg$" ) )
			{//	JPEG
				contentType= "image/jpeg";
			}
			else if( filename.matches( ".*\\.gif$" ) )
			{//	GIF
				contentType= "image/gif";
			}
			
			//	レスポンスへ出力する
			try
			{
				response.setContentType( contentType );
				log.debug( "contentType="+ contentType );
				response.setContentLength( (int)file.length() );
				ServletOutputStream	out= response.getOutputStream();
				
				byte[]	buf= new byte[4096];
				int	ret= in.read( buf );
				int	debug= ret;
				while( ret > 0 )
				{
					log.debug( "read="+ debug );
					debug+= ret;
					out.write( buf, 0, ret );
					ret= in.read( buf );
				}
			}
			catch( UnsupportedEncodingException e )
			{
				log.error( RESULT_DOWNLOAD_FAILED +" "+ e.toString(), e );
				return mapping.findForward( RESULT_DOWNLOAD_FAILED );
			}
			catch( IOException e )
			{
				log.error( RESULT_DOWNLOAD_FAILED +" "+ e.toString(), e );
				return mapping.findForward( RESULT_DOWNLOAD_FAILED );
			}
			
			if( in != null )
			{
				in.close();
				log.debug( file+ " close." );
				String	delFlg= (String)request.getAttribute( DownloadAction.REQ_ATTR_NAME_DEL );
				if( ( delFlg != null ) && delFlg.equalsIgnoreCase( "true" ) )
				{//	ファイルを削除する
					file.delete();
					log.debug( file+ " deleted." );
				}
				//	正常終了
				return null;
			}
			
		}
		return forward;
	}

}

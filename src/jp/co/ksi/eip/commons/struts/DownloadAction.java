/**
 * 
 */
package jp.co.ksi.eip.commons.struts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import JP.co.ksi.util.URLEncoder;


/**
 * ダウンロード用Actionクラス
 * @author kac
 * @version 1.5.0 2013/12/19
 * @since 1.0.0
 * <pre>
 * StrutsModel実行後、リクエストにセットされたFileオブジェクトを
 * レスポンスに出力することでダウンロード処理を実現します
 * ファイルが見つからない等ダウンロードに失敗した場合は、
 * RESULT_DOWNLOAD_FAILEDを返します
 * B/L側では、以下の値をoutDataにセットします
 * REQ_ATTR_NAME_FILE(must)
 * 　ダウンロードさせたいファイルのFileオブジェクト
 * REQ_ATTR_NAME_FILENAME
 * 　ダウンロード時に指定するファイル名
 * 　指定がない場合はFileオブジェクトの名前が使用されます
 * REQ_ATTR_NAME_FILENAME_ENC_TYPE
 * 	ファイル名のエンコード方法
 * 	指定が無い場合、user-agentでIE/Moziilaを判断し、それぞれの方法でエンコードします(推奨)
 * 	IE_JAを指定すると、IE(日本語版)だけで通用する方法でエンコードします
 * REQ_ATTR_NAME_ENC_NAME_LENGTH
 * 	エンコードしたファイル名の最大長
 * 	この属性で指定された値に、エンコードファイル名の長さを調整します
 * 	指定が無い場合はDEFAULT_DOWNLOAD_ENC_NAME_LENGTH(=110)
 * REQ_ATTR_NAME_DEL
 * 　ダウンロード完了後、ファイルを削除する場合、trueを指定してください
 * 　何も指定しない場合、ファイルは削除されません
 * </pre>
 * <pre>
 * 連続トランザクションにしたくなかったので、InvokeActionをオーバライトしました
 * InvokeActionが正常終了したかをActionForwardで確認しています。
 * このため、forwardしませんがaction-mappingにforward successの指定が必要になります
 * </pre>
 * <pre>
 * [2006/12/21 Kac]
 * Fileオブジェクトと実際にダウンロードさせたいファイル名が異なる場合にも
 * 対応できるように、ファイル名もリクエストから取得するようにしました。
 * リクエストにファイル名の指定が無い場合、Fileオブジェクトの名前を使用します。
 * </pre>
 * <pre>
 * [2007/08/09 Kac]
 * IEでダウンロード時に[開く]を押すとDDEエラーが発生する件に対応するため以下を行いました
 * (1)日本語IEでしか通用しない方法でファイル名をエンコードする
 * 　この方式を使うかどうかを選択できるように
 * 　REQ_ATTR_NAME_FILENAME_ENC_TYPE(ダウンロードファイル名のエンコード方法)を追加しました
 * 　【注意】
 * 　この方式は通信の観点で良くない（HTTPヘッダに制御コードが紛れ込む）
 * 　またIE側にShift_JISの2byte目の制御コード対応がなされていないので化ける文字が存在する
 * 　例：能(0x94,0x5c)->農(0x94,0x5f)
 * 　　\(0x5c)はァイル名として無効なので_(0x5f)にIEが変換してると思われる
 * (2)utf-8でURLエンコードした結果が指定byte数に収まるようにカットする
 * 　byte数を指定できるように
 * 　REQ_ATTR_NAME_ENC_NAME_LENGTH(エンコードファイル名の最大長)を追加しました
 * 　【備考】
 * 　DDEエラーについて、http://support.microsoft.com/kb/325573/ja
 * 　　Excelは、217byteまでOK
 * 　IEでのダウンロード時のフルパスは以下のようになる
 * 　{IEの一時フォルダ}\{ファイル名}[1]{拡張子}
 * 　IEの一時フォルダは、OS毎に異なり、Vistaが一番長い
 * 　またパスにユーザIDが含まれるため、ユーザ毎に長さは異なる
 * 　一般的な最大長：VistaでAdministratorの場合の長さ104byte
 * 　[1]は、IEが勝手に付加するようで3byte消費する
 * 　残りがファイル名として使用できる長さ
 * 　217-(104+3)=110byte
 * </pre>
 * <pre>
 * [2008/03/21 Kac]
 * 　content-lengthをセットするようにしました。
 * 　DoCoMo携帯にPDFをダウンロードさせる時、
 * content-lengthの値が実ファイルサイズと同じになってる必要があるみたいです。
 * [2013/12/19 Kac]
 * 　Mozillaの場合のファイル名エンコードをutf-8にした。
 * </pre>
 */
public class DownloadAction extends InvokeAction
{
	/**
	 * リクエスト・アトリビュートにセットするFileオブジェクトの属性名
	 * "file"
	 */
	public static final String REQ_ATTR_NAME_FILE= "file";
	/**
	 * リクエスト・アトリビュートにセットするfilenameの属性名
	 */
	public static final String REQ_ATTR_NAME_FILENAME= "filename";
	/**
	 * リクエスト・アトリビュートにセットするファイル名符号化フラグの属性名
	 */
	public static final String REQ_ATTR_NAME_FILENAME_ENC_TYPE= "encType";
	/**
	 * ファイル名符号化タイプ：日本語IE専用(古式)を示す
	 */
	public static final String IE_JA= "IE_JA";
	/**
	 * リクエスト・アトリビュートにセットする符号化ファイル名の最大長の属性名
	 */
	public static final String REQ_ATTR_NAME_ENC_NAME_LENGTH= "encNameLength";
	/**
	 * 符号化ファイル名の最大長のDefault値
	 */
	public static final int DEFAULT_DOWNLOAD_ENC_NAME_LENGTH= 110;
	/**
	 * リクエスト・アトリビュートにセットする削除フラグの属性名
	 */
	public static final String REQ_ATTR_NAME_DEL= "delFlg";

	/**
	 * ActionForwardの名前
	 * "download.failed"ダウンロード失敗を示します
	 */
	public static final String RESULT_DOWNLOAD_FAILED= "download.failed";

	private static Logger	log= Logger.getLogger(DownloadAction.class);

	/**
	 * InvokeActionのexecute()を実行後、requestのfileオブジェクトをレスポンスに出力します
	 * @see JP.co.ksi.struts.InvokeAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * <pre>
	 * レスポンス・ヘッダーを出力する処理は、user-agent毎にカスタマイズ
	 * する事が予想される。
	 * </pre>
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
		else if( forward.getName().equals( IStruts.RESULT_SUCCESS ) )
		{//	B/Lが正常終了した
			//	リクエストからFileオブジェクトを取得する
			File	file= (File)request.getAttribute( REQ_ATTR_NAME_FILE );
			log.debug( "file="+ file );
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
			String	filename= (String)request.getAttribute( REQ_ATTR_NAME_FILENAME );
			if( ( filename == null ) || filename.equals( "" ) )
			{//	ファイル名の指定がない
				filename= file.getName();
			}
			
			//	リクエストからファイル名のエンコード方法を取得する
			String	encType= (String)request.getAttribute( REQ_ATTR_NAME_FILENAME_ENC_TYPE );
			
			//	レスポンスヘッダーを出力する
			String	userAgent= request.getHeader( "user-agent" );
			log.debug( "userAgent="+ userAgent );
			if( userAgent == null )	userAgent= "";
			Locale	locale= request.getLocale();
			log.debug( "locale="+ locale );
			String	encFilename= "";
			if( userAgent.matches( ".*MSIE.*" ) )
			{//	Browser:IE
				if( ( encType != null ) && encType.equalsIgnoreCase( IE_JA ) )
				{//	日本語IEでのみ通用する方(古式？)
					byte[]	b= filename.getBytes("ms932");
					encFilename= new String( b, "ISO8859_1" );
					/* 2007/08/09 Kac この方法では、IEにバグがあるため、文字が変わってしまう場合がある
					 * Shift_JISの2byte目が、0x5c(\)や0x7c(|)等の場合、IEが0x5f(_)に変換してしまう
					 * このため、能(0x94,0x5c)が農(0x94,0x5f)に、ソ(0x83,0x5c)がダ(0x83,0x5f)に化ける
					 * ファイル名として禁止文字は_に変換していると思われる
					 */
					log.debug( "IE "+ encType +": encFilename="+ encFilename );
				}
				else
				{
					//	名前と拡張子に分離する
					String	name= filename;
					String	ext= "";
					int	index= filename.lastIndexOf( "." );
					if( index > 0 )
					{
						name= filename.substring( 0,index );
						ext= filename.substring( index );
					}
					//	エンコードファイル名の最大長を取得する
					int	encNameLength= DEFAULT_DOWNLOAD_ENC_NAME_LENGTH;
					try
					{
						encNameLength= Integer.parseInt( (String)request.getAttribute( REQ_ATTR_NAME_ENC_NAME_LENGTH ) );
					}
					catch( Exception e )
					{
						encNameLength= DEFAULT_DOWNLOAD_ENC_NAME_LENGTH;
					}
					//	URLエンコードしてencNameLengthの長さに収まるようにカットする
					while( name.length() > 0 )
					{
						encFilename= URLEncoder.encode( name + ext, "utf-8" );
						log.debug( name + ext +" ("+ encFilename.length() +") "+ encFilename );

						if( encFilename.length() <= encNameLength )	break;

						name= name.substring( 0, name.length()-1 );
					}
					log.debug( "IE "+ encType +": encNameLength="+ encNameLength +", encFilename("+ encFilename.length() +")="+ encFilename );
				}
				response.setContentType( "Application/Octet-Stream" );
				response.setContentLength( (int)file.length() );
				response.setHeader( "Content-Disposition", "attachment;filename=\""+ encFilename +"\"" );
			}
			else
			{//	Browser:Other(Mozilla)
				encFilename= MimeUtility.encodeWord( filename, "utf-8", "B" );
				log.debug( "Mozilla: encFilename="+ encFilename );
				response.setContentType( "Application/Octet-Stream" );
				response.setContentLength( (int)file.length() );
				response.setHeader( "Content-Disposition", "attachment;filename=\""+ encFilename +"\"" );
			}
			//	レスポンスボディを出力する
			try
			{
				ServletOutputStream	out= response.getOutputStream();
				
				byte[]	buf= new byte[4096];
				int	ret= in.read( buf );
				while( ret > 0 )
				{
					out.write( buf, 0, ret );
					ret= in.read( buf );
				}
			}
			catch( Exception e )
			{
				log.error( RESULT_DOWNLOAD_FAILED +" "+ e.toString(), e );
				return mapping.findForward( RESULT_DOWNLOAD_FAILED );
			}
			finally
			{
				if( in != null )
				{
					in.close();
					log.debug( file+ " close." );
					String	delFlg= (String)request.getAttribute( REQ_ATTR_NAME_DEL );
					if( ( delFlg != null ) && delFlg.equalsIgnoreCase( "true" ) )
					{//	ファイルを削除する
						file.delete();
						log.debug( file+ " deleted." );
					}
					//	正常終了
					return null;
				}
			}
			
		}
		return forward;
	}

}

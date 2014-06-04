package jp.co.ksi.eip.commons.struts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;

/**
 * リクエストURIに仮想化用ID(vid)を埋め込んで、
 * 別のURIとして同一の機能を、別の環境で利用するためのリクエストプロセッサ<br>
 * URIを変えれるので、ACL制御などにも利用できます<br>
 * 仮想化用IDは、リクエスト属性にREQ_ATTR_KEY_IDが示すキー名でセットされます<br>
 * @author kac
 * @since 1.1.0
 * @version 2.0 2011/04/08 TilesRequestProcessor依存を除去した
 * <pre>
 * 使用例：
 * Action-Mappingには path="/hoge" と定義しておき
 * 実際には以下のようなURIで呼び出します
 *  /{contextRoot}/{vid}/hoge.do
 * {vid}の部分をユーザやグループを切り分ける情報に利用すれば
 * URIによる仮想化的な事ができるようになるかもしれません
 * また、EIPのACL機能を併用すれば、仮想化の単位でアクセスを制御することも
 * 可能になります
 * </pre>
 * <pre>
 * 補足：
 * なぜ、RequestProcessorではなく、TilesRequestProcessorを継承してるのか？
 * Struts1.2ではtilesプラグインが標準で組み込まれており、
 * これを外すと、jsp:include等が正しく動作しない現象に見舞われました。
 * 根本原因はわかりませんが、とりあえずtilesの機能も持っといたほうが
 * よいみたいなのでこのような継承にしています。
 * </pre>
 */
public class VirtualRequestProcessor extends RequestProcessor
{
	/**
	 * リクエスト属性の仮想化用IDを示すキー名
	 */
	public static final String REQ_ATTR_KEY_ID= VirtualRequestProcessor.class.getName() +".VID";
	
	private static Logger	log= Logger.getLogger(VirtualRequestProcessor.class);
	
	/* (非 Javadoc)
	 * @see org.apache.struts.action.RequestProcessor#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void process( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		log.debug( "requestURI="+ request.getRequestURI() );
		log.debug( "pathInfo="+ request.getPathInfo() );
		
		super.process( request, response );
	}

	/* (非 Javadoc)
	 * @see org.apache.struts.action.RequestProcessor#processMapping(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	protected ActionMapping processMapping( HttpServletRequest request, HttpServletResponse response, String path ) throws IOException
	{
		log.debug( "path="+ path );
		return super.processMapping( request, response, path );
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.RequestProcessor#processForward(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionMapping)
	 */
	protected boolean processForward( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping ) throws IOException, ServletException
	{
		log.debug( "mapping="+ mapping );
		return super.processForward( request, response, mapping );
	}

	/**
	 * アクションの連続呼出を可能にするためにオーバライトしました<br>
	 * pathが.doの場合は、vidを付加してパスの辻褄を合わせます<br>
	 * <br>
	 * 2006/10/04 Kac
	 * struts-configをモジュール化している場合、pathにはprefixが含まれています<br>
	 * このため、pathのprefix部分の後ろにvidを付加する必要があります<br>
	 * 内部処理では、prefixはリクエストにセットされたModuleConfigから取得しています<br>
	 * この処理はstruts1.2では動作確認できてますが、
	 * バージョンが変わると属性名が変わるかもしれないので注意が必要です<br>
	 * @see org.apache.struts.tiles.TilesRequestProcessor#doForward(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doForward( String path, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		log.debug( "path="+ path +" do? "+ path.matches( ".*\\.do$" ) );
		//	vidをリクエストから取得する
		String	vid= (String)request.getAttribute( REQ_ATTR_KEY_ID );
		log.debug( "vid="+ vid );

		//	prefixを取得する(大抵モジュール化してるからな)
		ModuleConfig	config= (ModuleConfig)request.getAttribute( "org.apache.struts.action.MODULE" );
		String	prefix= config.getPrefix();
		log.debug( "prefix="+ prefix );
//		Enumeration	enumeration= request.getAttributeNames();
//		while( enumeration.hasMoreElements() )
//		{
//			String	name= (String)enumeration.nextElement();
//			log.debug( name +"="+ request.getAttribute( name ) );
//		}

		if( path.matches( ".*\\.do$" ) )
		{//	.doの場合はvidを付加する
			String	doPath= path.substring( prefix.length() );
			path= prefix +"/"+ vid + doPath;
			log.debug( "path="+ path );
		}
		//	オリジナル処理へ
		super.doForward( path, request, response );
	}

	/**
	 * パスに含まれるvidをカットして返します<br>
	 * vidはリクエストにセットします<br>
	 * @see org.apache.struts.action.RequestProcessor#processPath(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String processPath( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		String	path= super.processPath( request, response );
		log.debug( "path="+ path );
		int	index= path.indexOf("/",1);
		if( index > 0 )
		{
			String	vid= path.substring( 1, index );
			String	path2= path.substring( index );
			log.debug( REQ_ATTR_KEY_ID +"="+ vid );
			log.debug( "path2="+ path2 );
			request.setAttribute( REQ_ATTR_KEY_ID, vid );
			path= path2;
		}
		return path;
	}

}

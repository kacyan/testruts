package jp.co.ksi.testruts.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Arrays;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import jp.co.ksi.eip.commons.util.FileComparator;
import jp.co.ksi.eip.commons.util.StringUtil;

/**
 * ファイル一覧を表示するサーブレット
 * @author kac
 * @since 2021/09/08
 * @version 2021/09/09
 * <pre>
 * servlet-mappingを /{servlet}/* と登録する
 * init-param:baseFolderで指定したフォルダーのファイル一覧を表示する
 * pathInfoに従いフォルダー階層をおりる
 * ファイルの場合は、init-param:baseLinkで指定したリンク表示にする
 * </pre>
 * TODO スマホ用の表示を検討する
 * <pre>
 * 2021/09/09
 * pathInfoは文字化けする時があるのでrequestURLをURLデコードして使う方が良い
 * </pre>
 */
public class ListFileServlet extends HttpServlet
{
	private static final long serialVersionUID= 1L;
	private static Logger	log= Logger.getLogger( ListFileServlet.class );
	private static final String UTF8 = "utf-8";

	/**
	 * init-param
	 * ファイル一覧表示させるサーバ上のフォルダ
	 */
	public static final String BASE_LINK = "baseLink";
	/**
	 * init-param
	 * リンク表示させる際のベースとなるパス
	 */
	public static final String BASE_FOLDER = "baseFolder";

	@Override
	protected void service( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		String	requestURI= request.getRequestURI();
		log.debug( "requestURI="+ requestURI );
		requestURI= URLDecoder.decode(requestURI,UTF8);
		log.debug( "requestURI="+ requestURI );
		String	contextPath= request.getContextPath();
		log.debug( "contextPath="+ contextPath );
		String	servletPath= request.getServletPath();
		log.debug( "servletPath="+ servletPath );
		String pathInfo = request.getPathInfo();
		log.debug( "pathInfo="+ pathInfo );
		//	pathInfoは多言語だと文字化けするのでrequestURIから取得する
		pathInfo= requestURI.replaceFirst(contextPath, "");
		log.debug( "pathInfo(2)="+ pathInfo );
		pathInfo= pathInfo.replaceFirst(servletPath, "");
		log.debug( "pathInfo(3)="+ pathInfo );

		ServletConfig	config= getServletConfig();
		String baseFolder = config.getInitParameter( BASE_FOLDER );
		log.debug( BASE_FOLDER +"="+ baseFolder );
		String baseLink = config.getInitParameter( BASE_LINK );
		log.debug( BASE_LINK +"="+ baseLink );

		String	viewMode= request.getParameter( "viewMode" );
		if( viewMode == null  )	viewMode= "";

		String	path= baseFolder + pathInfo ;
		log.debug( "path="+ path );
		File	file= new File( path );
		File[]	children= file.listFiles();
		if( children != null )
		{
			//	ファイル名でソートする
			FileComparator	comparator= new FileComparator(FileComparator.ASCENT, FileComparator.TYPE_NAME_IGNORECASE);
			Arrays.sort( children, comparator );
		}
		else
		{
			children= new File[0];
		}

		//	2021/09/09 Kac
		if( viewMode.equalsIgnoreCase("img") )
		{//	画像として表示する
			request.setAttribute("requestURI", requestURI);
			request.setAttribute("baseLink", baseLink);
			request.setAttribute("pathInfo", pathInfo);
			request.setAttribute("children", children);
			request.getRequestDispatcher("/kac/t1.jsp").forward(request, response);
			return;
		}
		else if( viewMode.equalsIgnoreCase("img-fit") )
		{//	画像として表示する
			request.setAttribute("requestURI", requestURI);
			request.setAttribute("baseLink", baseLink);
			request.setAttribute("pathInfo", pathInfo);
			request.setAttribute("children", children);
			request.getRequestDispatcher("/kac/t2.jsp").forward(request, response);
			return;
		}

		response.setContentType( "text/html; charset=utf-8" );
		PrintWriter	pw= response.getWriter();
		pw.println( "requestURI="+ requestURI +"<br/>" );
//		pw.println( "path="+ path +"<br/>" );
		pw.println( "<form action=\""+ requestURI +"\" method=\"POST\">" );
		pw.println( "<input type=\"submit\" name=\"viewMode\" value=\"img\">" );
		pw.println( "<input type=\"submit\" name=\"viewMode\" value=\"img-fit\"><br/>" );
		pw.println( "</form>" );

		pw.println("<ol>");
		for( int i= 0; i < children.length; i++ )
		{
			String	childName= children[i].getName();
			String	link= "";
			if( children[i].isDirectory() )
			{
				link= StringUtil.concatPath( childName, "/");
			}
			else
			{
				link= StringUtil.concatPath(baseLink, pathInfo);
				link= StringUtil.concatPath(link, childName);

			}
			log.debug( "link="+ link );
			pw.println( "<li><a href=\""+ link +"\">"+ childName +"</a>" );
		}
		pw.println("</ol>");
		if( children.length == 0 )
		{
			pw.println( "Not found." );
		}

	}

}

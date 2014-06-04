/**
 * 
 */
package jp.co.ksi.testruts.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Properties;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * ダウンロード時のファイル名のテスト
 * @author kac
 * @since 2012/04/16
 * @version 2012/04/16
 */
public class DownloadTestServlet extends HttpServlet
{
	private static final String ENC_TYPE_ISO8859_1 = "ISO8859_1";
	private static final String ENC_TYPE_MIME_ENCODE = "MimeEncode";
	private static final String ENC_TYPE_URL_ENCODE = "URLEncode";
	
	private static Logger	log= Logger.getLogger( DownloadTestServlet.class );
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		String	filename= request.getParameter( "filename" );
		if( filename == null )
		{
			filename= getServletConfig().getInitParameter( "filename" );
		}
		if( filename == null )
		{
			filename= "";
		}
		String	userAgent= request.getHeader( "user-agent" );
		Properties	selected= new Properties();
		if( userAgent == null )	userAgent= "";

		if( userAgent.matches( ".*MSIE.*" ) )
		{//	MSIE
			selected.setProperty( ENC_TYPE_URL_ENCODE, "selected" );
		}
		else if( userAgent.matches( ".*Firefox.*" ) )
		{//	Firefox
			selected.setProperty( ENC_TYPE_MIME_ENCODE, "selected" );
		}
		else if( userAgent.matches( ".*Chrome.*" ) )
		{//	Chrome
			selected.setProperty( ENC_TYPE_URL_ENCODE, "selected" );
		}
		
		response.setContentType( "text/html; charset=utf-8" );
		PrintWriter	out= response.getWriter();
		out.println( "<html>"
				+ "<body>"
				+ "<form action=\"\" method=\"post\">"
				+ "filename : <input type=\"text\" name=\"filename\" value=\""+ filename +"\" style=\"width:90%;\"><br>"
				+ "charset : <input type=\"text\" name=\"charset\" value=\"utf-8\"><br>"
				+ "encType : <select name=\"encType\">"
				+ "<option value=\"\"></option>"
				+ "<option value=\""+ ENC_TYPE_URL_ENCODE +"\" "+ selected.getProperty( ENC_TYPE_URL_ENCODE, "" ) +">"+ ENC_TYPE_URL_ENCODE +"</option>"
				+ "<option value=\""+ ENC_TYPE_MIME_ENCODE + "\" "+ selected.getProperty( ENC_TYPE_MIME_ENCODE, "" ) +">"+ ENC_TYPE_MIME_ENCODE +"</option>"
				+ "<option value=\""+ ENC_TYPE_ISO8859_1 +"\" "+ selected.getProperty( ENC_TYPE_ISO8859_1, "" ) +">"+ ENC_TYPE_ISO8859_1 +"</option>"
				+ "</select>"
				+ "<br>"
				+ "content : <textarea name=\"content\">"
				+ "1234567890"
				+ "</textarea><br>"
				+ "<input type=\"submit\" value=\" O K \">"
				+ "</form>"
				+ userAgent +"<br/>"
				+ "</body></html>"
				);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		String	filename= request.getParameter( "filename" );
		if( filename == null )	filename= "default";
		String	content= request.getParameter( "content" );
		if( content == null )	content= "";
		String	charset= request.getParameter( "charset" );
		if( charset == null )	charset= "utf-8";
		String	encType= request.getParameter( "encType" );
		if( encType == null )	encType= "";
		log.debug( "filename="+ filename );
		log.debug( "content="+ content );
		log.debug( "charset="+ charset );
		log.debug( "encType="+ encType );

		//	ファイル名をエンコードする
		if( encType.equals( ENC_TYPE_URL_ENCODE ) )
		{
			filename= URLEncoder.encode( filename, charset );
		}
		else if( encType.equals( ENC_TYPE_MIME_ENCODE ) )
		{
			filename= MimeUtility.encodeWord( filename, charset, "B" );
		}
		else if( encType.equals( ENC_TYPE_ISO8859_1 ) )
		{
			filename= new String( filename.getBytes(charset), "ISO8859_1" );
		}
		log.debug( "filename="+ filename );
			
		response.setContentType( "Application/Octet-Stream" );
		response.setHeader( "Content-Disposition", "attachment; filename="+ filename );

		//	ダウンロードデータを出力する
		OutputStream	out= response.getOutputStream();
		out.write( content.getBytes( "Shift_JIS" ) );
		//	後始末
		out.close();
	}

}

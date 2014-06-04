package jp.co.ksi.testruts.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import JP.co.ksi.util.HtmlFilter;

/**
 * テスト用のサーブレット
 * @author kac
 * @since 2014/01/28
 * @version 2014/01/28
 */
public class TestServlet extends HttpServlet
{
	private static final long serialVersionUID= 1L;

	private static Logger	log= Logger.getLogger( TestServlet.class );

	@Override
	protected void service( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		String	requestURI= request.getRequestURI();
		String	contextPath= request.getContextPath();

		response.setContentType( "text/html; charset=utf-8" );
		PrintWriter	pw= response.getWriter();

		pw.println( "<form action=\""+ requestURI +"\" method=\"POST\">" );
		String	param1= request.getParameter( "param1" );
		if( param1 == null )	param1= "";
		pw.println( "<input type=\"text\" name=\"param1\" value=\""+ HtmlFilter.parseTag( param1 ) +"\"><br/>" );
		pw.println( "<input type=\"submit\" name=\"submit.ok\" value=\"OK\"><br/>" );
		pw.println( "</form>" );
		
		log.debug( "requestURI="+ requestURI );
		pw.println( "requestURI="+ requestURI +"<br/>" );
		
		Enumeration<?> enumeration= request.getHeaderNames();
		while( enumeration.hasMoreElements() )
		{
			String name= (String)enumeration.nextElement();
			Enumeration<?>	value= request.getHeaders( name );
			while( value.hasMoreElements() )
			{
				String element= (String)value.nextElement();
				log.debug( "HEADER: "+ name +"="+ element );
				pw.println( "HEADER: "+ name +"="+ element +"<br/>" );
			}
		}
		
		enumeration= request.getParameterNames();
		while( enumeration.hasMoreElements() )
		{
			String name= (String)enumeration.nextElement();
			String[]	value= request.getParameterValues( name );
			for( int i= 0; i < value.length; i++ )
			{
				log.debug( "PARAM: "+ name +"="+ value[i] );
				pw.println( "PARAM: "+ name +"="+ value[i] +"<br/>" );
			}
		}
		
	}

}

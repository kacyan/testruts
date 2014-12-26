package jp.co.ksi.testruts.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * リダイレクトの習作
 * @author kac
 * @since 2014/09/19
 * @version 2014/09/19
 */
public class RedirecTestServlet extends HttpServlet
{

	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException
	{
		String	requestURI= req.getRequestURI();
		
		resp.setContentType( "text/html; charset=utf-8" );
		PrintWriter	pw= resp.getWriter();
		pw.println( "<html>" );
		pw.println( "<body>" );
		pw.println( "<h2>"+ getServletName() +"</h2>" );
		pw.println( "<form action=\""+ requestURI +"\" method=\"post\">" );
		pw.println( "<input type=\"text\" name=\"password\" value=\"hogehuga\">" );
		pw.println( "<input type=\"submit\" value=\"doPost\">" );
		pw.println( "</form>" );
		pw.println( "</body>" );
		pw.println( "</html>" );
	}

	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException
	{
		String	contextPath= req.getContextPath();
		resp.sendRedirect( contextPath +"/check.jsp" );
	}

	
}

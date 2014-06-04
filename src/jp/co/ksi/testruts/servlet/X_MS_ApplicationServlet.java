package jp.co.ksi.testruts.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * clickonceの起動を確認するための習作
 * @author kac
 * @sicne 2012/06/20
 */
public class X_MS_ApplicationServlet extends HttpServlet
{

	@Override
	protected void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException
	{
		res.setContentType( "application/x-ms-application" );
		PrintWriter	writer= res.getWriter();
		writer.println( "hogehoge" );
	}
	
}

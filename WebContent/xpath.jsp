<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.IOException" %>
<%@ page import="javax.xml.parsers.DocumentBuilder" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory" %>
<%-- XPathを使用する為JDK1.5以上 --%>
<%@ page import="javax.xml.xpath.XPath"%>
<%@ page import="javax.xml.xpath.XPathFactory"%>
<%@ page import="javax.xml.xpath.XPathExpression"%>
<%@ page import="javax.xml.xpath.XPathConstants"%>
<%@ page import="org.w3c.dom.Document" %>
<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="org.xml.sax.SAXException"%>
<%@ page import="org.xml.sax.EntityResolver"%>
<%@ page import="org.xml.sax.InputSource"%>
<!-- struts.jsp -->
<!-- 2010/11/19 -->
<%
String	file= "/WEB-INF/struts-config.xml";
DocumentBuilderFactory	factory= DocumentBuilderFactory.newInstance();
DocumentBuilder	builder= factory.newDocumentBuilder();
builder.setEntityResolver(
		new EntityResolver()
		{
			public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException
			{
//				log( "publicId: " + publicId );
//				log( "systemId: " + systemId );
				String	file= systemId;
				int	index= file.lastIndexOf( "/" );
				if( index >= 0 )
				{
					file= file.substring( index );
				}
//				log( "file: " + file );
				InputStream	in= getServletContext().getResourceAsStream( file );
				if( in == null )
				{
					return null;
				}
				return new InputSource(in);
			}
		}
		);

InputStream	in= application.getResourceAsStream( file );
Document	doc= builder.parse( in );
in.close();

XPath	xpath= XPathFactory.newInstance().newXPath();
XPathExpression	expAction= xpath.compile( "/struts-config/action-mappings/action" );
XPathExpression	expServletName= xpath.compile( "path" );
XPathExpression	expUrlPattern= xpath.compile( "type" );
NodeList	actionList= (NodeList)expAction.evaluate( doc, XPathConstants.NODESET );
for( int i= 0; i < actionList.getLength(); i++ )
{
	Node	node= actionList.item( i );
	String	servletName= expServletName.evaluate( node );
	String	urlPattern= expUrlPattern.evaluate( node );
%><a href=".<%=urlPattern %>"><%=servletName %></a><br>
<%
}

%>
<hr>

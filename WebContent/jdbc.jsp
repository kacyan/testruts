<%@page import="JP.co.ksi.util.HtmlFilter"%>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileOutputStream"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Arrays"%>
<%	//	リクエストパラメータから接続情報とSQLを取得する
String	driver= request.getParameter( "driver" );
if( driver == null )	driver= "";
driver= driver.trim();
String	url= request.getParameter( "url" );
if( url == null )	url= "";
url= url.trim();
String	user= request.getParameter( "user" );
if( user == null )	user= "";
user= user.trim();
String	password= request.getParameter( "password" );
if( password == null )	password= "";
String	schema= request.getParameter( "schema" );
if( schema == null )	schema= "";
String	sql= request.getParameter( "sql" );
if( sql == null )	sql= "";
//	設定関係
String	loadFile= request.getParameter( "loadFile" );
if( loadFile == null )	loadFile= "";
String	saveFile= request.getParameter( "saveFile" );
if( saveFile == null )	saveFile= "";
String	cmd= request.getParameter( "cmd" );
if( cmd == null )	cmd= "";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
.fit
{
	width: 100%;
}
form
{
	margin-bottom: 0px;
	margin-top: 0px;
}
table
{
	margin-bottom: 0px;
	margin-top: 0px;
	border-spacing: 0px;
	empty-cells: show;
}
tr
{
	margin-bottom: 0px;
	margin-top: 0px;
}
td
{
	margin-bottom: 0px;
	margin-top: 0px;
}
input
{
	margin-bottom: 0px;
	margin-top: 0px;
}
</style>
</head>
<body>

<%
if( cmd.equals("設定を読込") )
{
	Properties	props= new Properties();
	try
	{
		if( !loadFile.matches( "[a-zA-Z0-9-_]" ) )
		{
			throw new Exception( "loadFile invalid. "+ loadFile );
		}
		InputStream	inStream= application.getResourceAsStream( "/WEB-INF/jdbc/"+ loadFile );
		props.load(inStream);
		inStream.close();
	}
	catch( Exception e )
	{
		java.io.StringWriter sw= new java.io.StringWriter();
		e.printStackTrace( new java.io.PrintWriter( sw ) );
		out.print( "<pre>");
		out.print( HtmlFilter.parseTag( sw.toString() ) );
		out.print("</pre>");
	}
	driver= props.getProperty("driver","");
	url= props.getProperty("url","");
	user= props.getProperty("user","");
	password= props.getProperty("password","");
	schema= props.getProperty("schema","");
	sql= props.getProperty("sql","");
}
else if( cmd.equals("設定を保存") )
{
	Properties	props= new Properties();
	props.setProperty("driver",driver);
	props.setProperty("url",url);
	props.setProperty("user",user);
	props.setProperty("password",password);
	props.setProperty("schema",schema);
	props.setProperty("sql",sql);
	try
	{
		if( !saveFile.matches( "[a-zA-Z0-9-_]" ) )
		{
			throw new Exception( "saveFile invalid. "+ saveFile );
		}
		FileOutputStream	outStream= new FileOutputStream( application.getRealPath( "/WEB-INF/jdbc/"+ saveFile ) );
		props.store(outStream,"jdbc.jsp");
		outStream.close();
	}
	catch( Exception e )
	{
		java.io.StringWriter sw= new java.io.StringWriter();
		e.printStackTrace( new java.io.PrintWriter( sw ) );
		out.print( "<pre>");
		out.print( HtmlFilter.parseTag( sw.toString() ) );
		out.print("</pre>");
	}
}
%>
<%-- フォームの表示 --%>
<form action="" method="post" >
<table border="0" cellpadding="0" cellspacing="0" width="100%">
 <tr>
  <td>driver</td>
  <td width="100%"><input type="text" name="driver" value="<%=HtmlFilter.parseTag( driver ) %>" size="60" class="fit"></td>
 </tr>
 <tr>
  <td>url</td>
  <td><input type="text" name="url" value="<%=HtmlFilter.parseTag( url ) %>" size="60" class="fit"></td>
 </tr>
 <tr>
  <td>user</td>
  <td><input type="text" name="user" value="<%=HtmlFilter.parseTag( user ) %>" size="40"></td>
 </tr>
 <tr>
  <td>password</td>
  <td><input type="password" name="password" value="<%=HtmlFilter.parseTag( password ) %>" size="40"></td>
 </tr>
 <tr>
  <td>schema</td>
  <td><input type="text" name="schema" value="<%=HtmlFilter.parseTag( schema ) %>" size="40"></td>
 </tr>
 <tr>
  <td>sql</td>
  <td>
   <textarea name="sql" cols="40" rows="10" class="fit"><%=HtmlFilter.parseTag( sql ) %></textarea>
  </td>
 </tr>
 <tr>
  <td colspan="2">
   <!-- post sql -->
   <input type="submit" name="cmd">
   <!-- load setting -->
   <select name="loadFile">
    <option value="" />&nbsp;&nbsp;&nbsp;&nbsp;
<%
String	jdbcFolder= application.getRealPath("/WEB-INF/jdbc");
File	f= new File( jdbcFolder );
String[]	files= f.list();
if( files != null )
{
	Arrays.sort( files );
	for( int i= 0; i < files.length; i++ )
	{
%>    <option value="<%=files[i] %>" /><%=files[i] %>
<%
	}
}
%>
   </select>
   <input type="submit" name="cmd" value="設定を読込">
   <!-- save setting -->
   <input type="text" name="saveFile" value="">
   <input type="submit" name="cmd" value="設定を保存">
   <font color="#e0e0e0">&lt;---未完成</font>
  </td>
 </tr>
</table>
</form>

<hr>

<%
if( driver.equals("") || url.equals("") )
{//	driverやURLが指定されて無い場合は、例を示して終わる
%>
driverの指定例：
<ul>
<li>org.hsqldb.jdbcDriver</li>
<li>org.postgresql.Driver</li>
<li>com.ibm.db2.jcc.DB2Driver</li>
<li>com.microsoft.jdbc.sqlserver.SQLServerDriver</li>
<li>oracle.jdbc.driver.OracleDriver</li>
</ul>
urlの指定例：
<ul>
<li>jdbc:hsqldb:hsql://localhost/${DB}</li>
<li>jdbc:postgresql://localhost/${DB}</li>
<li>jdbc:db2://localhost:50000/${DB}</li>
<li>jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=${DB}</li>
<li>jdbc:oracle:thin:@${host}:1521:${DB}</li>
</ul>
<%
	return;
}
%>
<%	//	処理の開始、これ以降try内の処理
Connection	con= null;
try
{
	//	コネクトする
	Class.forName( driver );
	con= DriverManager.getConnection( url, user, password );
	DatabaseMetaData	db= con.getMetaData();
%>
<%-- 接続に成功したら、DB名とドライバ名を表示する --%>
<table border="1">
 <tr>
  <th>DatabaseProduct</th>
  <td><%=db.getDatabaseProductName() %><%=db.getDatabaseProductVersion() %></td>
 </tr>
 <tr>
  <th>Driver</th>
  <td><%=db.getDriverName() %><%=db.getDriverVersion() %></td>
 </tr>
 <tr>
  <th>DatabaseMetadata</th>
  <td><%=db.toString() %></td>
 </tr>
</table>
<hr>

<%
	if( sql.length() > 0 )
	{//	クエリーを実行する
		Statement	st= con.createStatement();
		if( schema.length() > 0 )
		{//	スキーマ指定がある場合はセットする
			st.execute( "SET CURRENT SCHEMA "+ schema );
		}
		boolean	ret= st.execute( sql );
		if( ret )
		{//	クエリー結果がある場合、ResultSetの内容を表示する
%>
<table border="1">
 <tr>
  <td></td>
<%
			ResultSet	rs= st.getResultSet();
			ResultSetMetaData	rsmd= rs.getMetaData();
			int	colCount= rsmd.getColumnCount();
			for( int i= 1; i <= colCount; i++ )
			{//	カラム名を１つずつ表示する
%>
  <th>
   <%=HtmlFilter.parseTag( rsmd.getColumnName( i ) ) %><br>
   <small><%=HtmlFilter.parseTag( rsmd.getColumnTypeName( i ) ) %>(<%=rsmd.getColumnDisplaySize( i )%>)</small>
  </th>
<%
			}
%>
 </tr>
<%
			int	rowCount= 0;
			while( rs.next() )
			{//	一行ずつループする
				if( ++rowCount > 50 )	break;
%>
 <tr>
  <td><%=rowCount%></td>
<%
				for( int i= 1; i <= colCount; i++ )
				{//	カラムデータを１つずつ表示する
%>
  <td><%=HtmlFilter.parseTag( rs.getString( i ) ) %></td>
<%
				}
%>
 </tr>
<%
			}
			rs.close();
%>
</table>
<%
		}
		else
		{//	クエリー結果が無い場合、UpdateCountでも表示してみる
%>
UpdateCount=<%= st.getUpdateCount() %><br>
<%
		}
	}
}
catch( Exception e )
{//	例外をキャッチしたら、表示してみる
	java.io.StringWriter sw= new java.io.StringWriter();
	e.printStackTrace( new java.io.PrintWriter( sw ) );
	out.print( "<pre>");
	out.print( HtmlFilter.parseTag( sw.toString() ) );
	out.print("</pre>");
}

try
{//	後始末
	if( con != null )	con.close();
}
catch( Exception e )
{//	ええやろ？
}
%>
</body>
</html>

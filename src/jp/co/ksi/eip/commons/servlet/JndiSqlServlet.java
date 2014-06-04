/**
 *
 */
package jp.co.ksi.eip.commons.servlet;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.directory.InitialDirContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import JP.co.ksi.util.HtmlFilter;

/**
 * JNDI版SQLServlet
 * @author kac
 * @since 2009/02/06
 * @version 2014/02/04 XSS脆弱性対応
 * <pre>
 * 【導入方法】
 * (1)使用するDataSourceをJNDI登録する
 * 　例：tomcat-6.0
 * 　META-INF/context.xml にResourceとして定義する
 * (2)web.xmlにこのクラスをservlet登録する
 * 【使用方法】
 * (1)web.xmlに登録したURLにアクセスする
 * (2)JNDIを選択し[SQL実行]を押す
 * </pre>
 * <pre>
 * 2013/05/17 設定ファイルを指定して直実行できるようにしてみた
 * [2010/01/25]
 * JNDI名は検索して取得するようにした
 * [2010/01/22]
 * statement生成時にresultSetTypeとresultSetConcurrencyを指定できるようにした
 * </pre>
 */
public class JndiSqlServlet extends HttpServlet
{
	private static final String UID_SEPARATER = "@";

	/**
	 * ユーザのファイルを抽出するフィルター
	 * @author kac
	 */
	class UserFileFilter implements FileFilter
	{
		String	uid= "";

		public UserFileFilter( String uid )
		{
			this.uid= uid;
		}

		public boolean accept( File file )
		{
			String	expr= "^"+ uid +UID_SEPARATER+".*";
			return file.getName().matches( expr );
		}
	}
	/**
	 * ファイルを比較する
	 * @author kac
	 */
	class FileComparator implements Comparator<File>
	{
		public FileComparator()
		{
		}

		public int compare( File o1, File o2 )
		{
			return o1.toString().compareTo( o2.toString() );
		}
	}

	/**
	 * 設定ファイルを保存するフォルダー
	 */
	private String baseFolder = "";

	/**
	 * データソースを取得するのに使用するJNDI名の一覧
	 */
	private ArrayList<String> jndiList= new ArrayList<String>();

	/**
	 * log4j
	 */
	private static Logger	log= Logger.getLogger( JndiSqlServlet.class );

	/**
	 * コマンドボタン
	 */
	private static final String CMD_EXECUTE= "SQL実行";
	private static final String CMD_LOAD= "読込";
	private static final String CMD_SAVE= "保存";
	private static final String CMD_DELETE= "削除";

	/**
	 * このサーブレットの初期化を行います
	 * web.xmlに記載されたJNDIリストを取得し、jndiListを生成します
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException
	{
		super.init();

		list( "java:comp/env", jndiList );

		//	baseFolderを取得する
		baseFolder= getInitParameter( "baseFolder" );
		if( baseFolder == null )	baseFolder= getServletName();
		baseFolder = "/"+ baseFolder +"/";
		baseFolder= baseFolder.replaceAll( "//", "/" );
		//	baseFolderを準備する
		File	folder= new File( getServletContext().getRealPath( baseFolder ) );
		if( !folder.exists() )
		{//	フォルダが無い -> 作る
			if( folder.mkdir() )
			{
				log.info( "baseFolder created. ["+ baseFolder +"]" );
			}
			else
			{
				log.fatal( "baseFolder create error. ["+ baseFolder +"]" );
			}
		}
		else if( !folder.isDirectory() )
		{//	あるけどフォルダぢゃない -> 問題だ
			log.fatal( "baseFolder not folder. ["+ baseFolder +"]" );
		}
		else
		{//	準備OK
			log.info( "baseFolder exist ok. ["+ baseFolder +"]" );
		}
	}

	private void list( String jndiName, ArrayList<String> jndiList )
	{
		InitialDirContext	ctx= null;
		try
		{
			ctx= new InitialDirContext();
			NamingEnumeration<Binding>	ne= ctx.listBindings( jndiName );
			while( ne.hasMore() )
			{
				Binding bind= null;
				String	name= jndiName;
				Object	value= null;
				try
				{
					bind= ne.next();
					name= jndiName +"/"+ bind.getName();
					value= bind.getObject();
				}
				catch( Exception e )
				{
					log.warn( name +" - "+ e.toString() );
				}
				if( value instanceof Context )
				{//	再帰呼び出し
					list( name, jndiList );
				}
				else if( value instanceof DataSource )
				{//	DataSourceだ
					jndiList.add( name );
				}
				else
				{
					log.debug( name +" - "+ value );
				}
			}
		}
		catch (Exception e)
		{
			log.error( jndiName, e );
		}
		finally
		{
			if( ctx != null )
			{//	後始末
				try
				{
					ctx.close();
				}
				catch (Exception e)
				{
					log.error( jndiName, e );
				}
			}
		}
	}

	/**
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		String	charset= request.getCharacterEncoding();
		ServletContext	context= getServletContext();
		StringBuffer	msg= new StringBuffer();

		//	cmd
		String cmd = "";
		//	jndi
		String	jndi= getParam( request, "jndi", "" );
		//	max
		int	max= getParamInt( request, "max", 20 );
		int	resultSetType= getParamInt( request, "resultSetType", 0 );
		int	resultSetConcurrency= getParamInt( request, "resultSetConcurrency", 0 );
		//	sql
		String	sql= getParam( request, "sql", "" );
		//	loadFile
		String	loadFile= getParam( request, "loadFile", "" );
		//	saveFile
		String	saveFile= getParam( request, "saveFile", "" );
		//	uid
		String uid = getUid( request );

		//	pathInfo
		String	pathInfo= request.getPathInfo();
		if( ( pathInfo != null ) && ( pathInfo.length() > 1 ) )
		{//	設定ファイルを読込む
			cmd= CMD_LOAD;
			Properties	props= new Properties();
			String	file= baseFolder + uid +UID_SEPARATER+ pathInfo.substring( 1 );
			try
			{
				InputStream	inStream= context.getResourceAsStream( file );
				props.load( inStream );
				inStream.close();
				log.info( "load file="+ file );
				cmd= CMD_EXECUTE;
			}
			catch( Exception e )
			{
				msg.append( "["+ cmd +"] file="+ file +"<br>"+ HtmlFilter.parseTag( e.toString() ) );
				log.info( "["+ cmd +"] file="+ file +" "+ e.toString() );
			}
			jndi= props.getProperty( "jndi", "" );
			log.debug( "jndi="+ jndi );
			try
			{
				max= Integer.parseInt( props.getProperty("max","20") );
			}
			catch (Exception e)
			{
			}
			try
			{
				resultSetType= Integer.parseInt( props.getProperty("resultSetType","0") );
			}
			catch (Exception e)
			{
			}
			log.debug( "resultSetType="+ resultSetType );
			try
			{
				resultSetConcurrency= Integer.parseInt( props.getProperty("resultSetConcurrency","0") );
			}
			catch (Exception e)
			{
			}
			log.debug( "resultSetConcurrency="+ resultSetConcurrency );
			sql= props.getProperty( "sql", "" );
			log.debug( "sql="+ sql );
			saveFile= "";
		}


		//	loadFile一覧
		File	folder= new File( context.getRealPath( baseFolder ) );
		File[]	files= folder.listFiles( new UserFileFilter( uid ) );
		if( files == null )	files= new File[0];
		Arrays.sort( files, new FileComparator() );

		response.setContentType( "text/html; charset="+ charset );
		PrintWriter	out= response.getWriter();

		out.println( "<html>" );
		out.println( "<head>" );
		out.println( "<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+ charset +"\">" );
		outputStyle( out );
		out.println( "</head>" );
		out.println( "<body>" );
		outputForm( out, request, jndi, max, resultSetType, resultSetConcurrency, sql, loadFile, saveFile, files );
		out.println( "<hr>" );
		out.println( msg.toString() );

		if( cmd.equals( CMD_EXECUTE ) )
		{//	SQLを実行する
			Connection	con= null;
			try
			{
				InitialContext	ctx= new InitialContext();
				DataSource	ds= (DataSource)ctx.lookup( jndi );
				log.debug( "ds="+ ds.getClass().getName() );
				con = ds.getConnection();
				if( sql.equals( "" ) )
				{//	SQLなしの場合、DB情報を表示する
					DatabaseMetaData	dbmd= con.getMetaData();
					out.println( "<table border=\"1\" class=\"listtable\">" );
					out.println( "<tr>" );
					out.println( "<th style=\"width:8em\">DatabaseProduct</th>" );
					out.println( "<td>"+ dbmd.getDatabaseProductName() +" - "+ dbmd.getDatabaseProductVersion() +"</td>" );
					out.println( "</tr>" );
					out.println( "<tr>" );
					out.println( "<th>JDBCDriver</th>" );
					out.println( "<td>"+ dbmd.getDriverName() +" - "+ dbmd.getDriverVersion() +"</td>" );
					out.println( "</tr>" );
					out.println( "<tr>" );
					out.println( "<th>URL</th>" );
					out.println( "<td>"+ dbmd.getURL() +" - "+ dbmd.getUserName() +"</td>" );
					out.println( "</tr>" );
					out.println( "<tr>" );
					out.println( "<th>SearchStringEscape</th>" );
					out.println( "<td>"+ dbmd.getSearchStringEscape() +"</td>" );
					out.println( "</tr>" );
					out.println( "<tr>" );
					out.println( "<th>DataSource</th>" );
					out.println( "<td>"+ ds.getClass().getName() +"</td>" );
					out.println( "</tr>" );
					out.println( "</table>" );
				}
				else
				{//	SQLを実行する
					StringTokenizer	token= new StringTokenizer( sql, ";" );
					while( token.hasMoreTokens() )
					{//	";"で区切られたSQL文を順に実行する
						String	part= token.nextToken();
						Statement	st= null;
						try
						{
							log.debug( "createStatement( "+ resultSetType +", "+ resultSetConcurrency +" )" );
							st= con.createStatement( resultSetType, resultSetConcurrency );
							int	count= 0;
							log.debug( "exec: "+ part );
							if( st.execute( part ) )
							{//	結果あり
								count= outputResultSet( out, st.getResultSet(), max, true );
							}
							else
							{//	結果なし
								//	件数を取得する
								count= st.getUpdateCount();
							}
							out.println( "count="+ count +"<br>" );
						}
						catch( Throwable e1 )
						{
							log.error( "sql error. "+ part, e1 );
							StringWriter	sw= new StringWriter();
							e1.printStackTrace( new PrintWriter( sw ) );
							out.println( "<pre>" );
							out.println( HtmlFilter.parseTag( sw.toString() ) );
							out.println( "</pre>" );
						}
						finally
						{//	後始末
							out.println( "<hr>" );
							try
							{
								if( st != null )	st.close();
							}
							catch( Exception e )
							{
								log.warn( "st close error. "+ e.toString() );
							}
						}
					}
				}
			}
			catch( Exception e )
			{
				log.error( e.toString(), e );
				StringWriter	sw= new StringWriter();
				e.printStackTrace( new PrintWriter( sw ) );
				out.println( "<pre>" );
				out.println( HtmlFilter.parseTag( sw.toString() ) );
				out.println( "</pre>" );
			}
			finally
			{//	後始末
				try
				{
					if( con != null )	con.close();
				}
				catch( Exception e )
				{
				}
			}
		}

		out.println( "<hr>" );
		out.println( "</body>" );
		out.println( "</html>" );
	}

	/**
	 *
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		String	charset= request.getCharacterEncoding();
		ServletContext	context= getServletContext();
		StringBuffer	msg= new StringBuffer();

		//	cmd
		String cmd = getParam( request, "cmd", "" );
		log.debug( "called. "+ cmd );
		//	jndi
		String	jndi= getParam( request, "jndi", "" );
		//	max
		int	max= getParamInt( request, "max", 20 );
		int	resultSetType= getParamInt( request, "resultSetType", 0 );
		int	resultSetConcurrency= getParamInt( request, "resultSetConcurrency", 0 );
		//	sql
		String	sql= getParam( request, "sql", "" );
		//	loadFile
		String	loadFile= getParam( request, "loadFile", "" );
		//	saveFile
		String	saveFile= getParam( request, "saveFile", "" );
		//	uid
		String	uid= getUid( request );

		if( cmd.equals( CMD_LOAD ) && loadFile.matches( "[a-zA-Z0-9-_]+" ) )
		{//	設定ファイルを読込む
			Properties	props= new Properties();
			String	file= baseFolder + uid +UID_SEPARATER+ loadFile;
			try
			{
				InputStream	inStream= context.getResourceAsStream( file );
				props.load( inStream );
				inStream.close();
				log.info( "load file="+ file );
			}
			catch( Exception e )
			{
				msg.append( "["+ cmd +"] file="+ HtmlFilter.parseTag( file ) +"<br>"+ HtmlFilter.parseTag( e.toString() ) );
				log.info( "["+ cmd +"] file="+ file +" "+ e.toString() );
			}
			jndi= props.getProperty( "jndi", "" );
			log.debug( "jndi="+ jndi );
			try
			{
				max= Integer.parseInt( props.getProperty("max","20") );
			}
			catch (Exception e)
			{
			}
			try
			{
				resultSetType= Integer.parseInt( props.getProperty("resultSetType","0") );
			}
			catch (Exception e)
			{
			}
			log.debug( "resultSetType="+ resultSetType );
			try
			{
				resultSetConcurrency= Integer.parseInt( props.getProperty("resultSetConcurrency","0") );
			}
			catch (Exception e)
			{
			}
			log.debug( "resultSetConcurrency="+ resultSetConcurrency );
			sql= props.getProperty( "sql", "" );
			log.debug( "sql="+ sql );
			saveFile= "";
		}
		else if( cmd.equals( CMD_DELETE ) && loadFile.matches( "[a-zA-Z0-9]+" ) )
		{//	設定ファイルを削除する
			String	file= baseFolder + uid +UID_SEPARATER+ loadFile;
			try
			{
				File	f= new File( context.getRealPath( file ) );
				f.delete();
				log.info( "delete file="+ file );
			}
			catch( Exception e )
			{
				msg.append( "["+ cmd +"] file="+ HtmlFilter.parseTag( file ) +"<br>"+ HtmlFilter.parseTag( e.toString() ) );
				log.error( "["+ cmd +"] file="+ file, e );
			}
			jndi= "";
			max= 20;
			sql= "";
			loadFile= "";
			saveFile= "";
		}
		else if( cmd.equals( CMD_SAVE ) )
		{//	設定をファイルに保存する
			log.info( cmd );
			if( !saveFile.matches( "[a-zA-Z0-9]+" ) )
			{//	パラメータエラー
				msg.append( "["+ cmd +"] saveFile が正しくありません。"+ HtmlFilter.parseTag( saveFile ) );
				log.error( "["+ cmd +"] saveFile が正しくありません。"+ saveFile );
			}
			else
			{
				Properties	props= new Properties();
				props.setProperty( "jndi", jndi );
				log.debug( "jndi="+ jndi );
				props.setProperty( "max", String.valueOf(max) );
				log.debug( "max="+ max );
				props.setProperty( "resultSetType", String.valueOf(resultSetType) );
				log.debug( "resultSetType="+ resultSetType );
				props.setProperty( "resultSetConcurrency", String.valueOf(resultSetConcurrency) );
				log.debug( "resultSetConcurrency="+ resultSetConcurrency );
				props.setProperty( "sql", sql );
				log.debug( "sql="+ sql );
				String	file= baseFolder + uid +UID_SEPARATER+ saveFile;
				try
				{
					FileOutputStream	outStream= new FileOutputStream( context.getRealPath( file ) );
					props.store( outStream, getClass().getName() );
					outStream.close();
					log.info( "save file="+ file );
				}
				catch( Exception e )
				{
					msg.append( "["+ cmd +"] file="+ HtmlFilter.parseTag( file ) +"<br>"+ HtmlFilter.parseTag( e.toString() ) );
					log.error( "["+ cmd +"] file="+ file, e );
				}
			}
		}
		//	loadFile一覧
		File	folder= new File( context.getRealPath( baseFolder ) );
		File[]	files= folder.listFiles( new UserFileFilter( uid ) );
		if( files == null )	files= new File[0];
		Arrays.sort( files, new FileComparator() );

		response.setContentType( "text/html; charset="+ charset );
		PrintWriter	out= response.getWriter();

		out.println( "<html>" );
		out.println( "<head>" );
		out.println( "<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+ charset +"\">" );
		outputStyle( out );
		out.println( "</head>" );
		out.println( "<body>" );
		outputForm( out, request, jndi, max, resultSetType, resultSetConcurrency, sql, loadFile, saveFile, files );
		out.println( "<hr>" );
		out.println( msg.toString() );

		if( cmd.equals( CMD_EXECUTE ) )
		{//	SQLを実行する
			Connection	con= null;
			try
			{
				InitialContext	ctx= new InitialContext();
				DataSource	ds= (DataSource)ctx.lookup( jndi );
				log.debug( "ds="+ ds.getClass().getName() );
				con = ds.getConnection();
				if( sql.equals( "" ) )
				{//	SQLなしの場合、DB情報を表示する
					DatabaseMetaData	dbmd= con.getMetaData();
					out.println( "<table border=\"1\" class=\"listtable\">" );
					out.println( "<tr>" );
					out.println( "<th style=\"width:8em\">DatabaseProduct</th>" );
					out.println( "<td>"+ dbmd.getDatabaseProductName() +" - "+ dbmd.getDatabaseProductVersion() +"</td>" );
					out.println( "</tr>" );
					out.println( "<tr>" );
					out.println( "<th>JDBCDriver</th>" );
					out.println( "<td>"+ dbmd.getDriverName() +" - "+ dbmd.getDriverVersion() +"</td>" );
					out.println( "</tr>" );
					out.println( "<tr>" );
					out.println( "<th>URL</th>" );
					out.println( "<td>"+ dbmd.getURL() +" - "+ dbmd.getUserName() +"</td>" );
					out.println( "</tr>" );
					out.println( "<tr>" );
					out.println( "<th>SearchStringEscape</th>" );
					out.println( "<td>"+ dbmd.getSearchStringEscape() +"</td>" );
					out.println( "</tr>" );
					out.println( "<tr>" );
					out.println( "<th>DataSource</th>" );
					out.println( "<td>"+ ds.getClass().getName() +"</td>" );
					out.println( "</tr>" );
					out.println( "</table>" );
				}
				else
				{//	SQLを実行する
					StringTokenizer	token= new StringTokenizer( sql, ";" );
					while( token.hasMoreTokens() )
					{//	";"で区切られたSQL文を順に実行する
						String	part= token.nextToken();
						Statement	st= null;
						try
						{
							log.debug( "createStatement( "+ resultSetType +", "+ resultSetConcurrency +" )" );
							st= con.createStatement( resultSetType, resultSetConcurrency );
							int	count= 0;
							log.debug( "exec: "+ part );
							if( st.execute( part ) )
							{//	結果あり
								count= outputResultSet( out, st.getResultSet(), max, true );
							}
							else
							{//	結果なし
								//	件数を取得する
								count= st.getUpdateCount();
							}
							out.println( "count="+ count +"<br>" );
						}
						catch( Throwable e1 )
						{
							log.error( "sql error. "+ part, e1 );
							StringWriter	sw= new StringWriter();
							e1.printStackTrace( new PrintWriter( sw ) );
							out.println( "<pre>" );
							out.println( HtmlFilter.parseTag( sw.toString() ) );
							out.println( "</pre>" );
						}
						finally
						{//	後始末
							out.println( "<hr>" );
							try
							{
								if( st != null )	st.close();
							}
							catch( Exception e )
							{
								log.warn( "st close error. "+ e.toString() );
							}
						}
					}
				}
			}
			catch( Exception e )
			{
				log.error( e.toString(), e );
				StringWriter	sw= new StringWriter();
				e.printStackTrace( new PrintWriter( sw ) );
				out.println( "<pre>" );
				out.println( HtmlFilter.parseTag( sw.toString() ) );
				out.println( "</pre>" );
			}
			finally
			{//	後始末
				try
				{
					if( con != null )	con.close();
				}
				catch( Exception e )
				{
				}
			}
		}

		out.println( "<hr>" );
		out.println( "</body>" );
		out.println( "</html>" );
	}

	/**
	 * SQLの結果(ResultSet)を出力します
	 * @param out	出力先
	 * @param rs	SQLの結果
	 * @param max	出力する最大件数
	 * @param close	ResultSetを閉じるかを示すフラグ(true=閉じる)
	 * @return	int 結果の件数を返します
	 * @throws SQLException
	 */
	private int outputResultSet( PrintWriter out, ResultSet rs, int max, boolean close ) throws SQLException
	{
		out.println( "<table class=\"listtable\" border=\"1\">" );
		int count= 0;
		try
		{
			ResultSetMetaData	rsmd= rs.getMetaData();
			//	カラム名
			out.println( "<tr><th></th>" );
			for( int i= 1; i <= rsmd.getColumnCount(); i++ )
			{
				out.println( "<th>"+ HtmlFilter.parseTag( rsmd.getColumnName(i) ) +"</th>" );
			}
			out.println( "</tr>" );
			//	カラムタイプとカラム長
			out.println( "<tr><th></th>" );
			for( int i= 1; i <= rsmd.getColumnCount(); i++ )
			{
				int	colSize= rsmd.getColumnDisplaySize( i );
				String	colType= rsmd.getColumnTypeName( i );
				out.println( "<th style=\"font-size:80%\">"+ HtmlFilter.parseTag( colType ) +"("+ colSize +")</th>" );
			}
			out.println( "</tr>" );
			while( rs.next() )
			{//	カラムデータ
				if( count++ >= max )
				{
					try
					{//	件数を取得する
						rs.last();
						count= rs.getRow();
					}
					catch( Exception e2 )
					{
						log.warn( "count unknown. "+ e2.toString() );
					}
					break;
				}

				out.println( "<tr><td>"+ count +"</td>" );
				for( int i= 1; i <= rsmd.getColumnCount(); i++ )
				{
					out.println( "<td>"+ HtmlFilter.parseTag( rs.getString(i) ) +"</td>" );
				}
				out.println( "</tr>" );
			}
		}
		catch( SQLException e )
		{
			log.error( "count="+ count +" "+ e.toString() );
			throw e;
		}
		finally
		{
			out.println( "</table>" );
			try
			{
				if( rs != null )	rs.close();
			}
			catch( Exception e )
			{
				log.warn( "rs close error. "+ e.toString() );
			}
		}
		return count;
	}

	/**
	 * 入力フォームを出力します
	 * @param out
	 * @param request
	 * @param jndi
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param sql
	 * @param loadFile
	 * @param saveFile
	 * @param files
	 */
	private void outputForm( PrintWriter out, HttpServletRequest request, String jndi, int max, int resultSetType, int resultSetConcurrency, String sql, String loadFile, String saveFile, File[] files )
	{
		String	requestURI= request.getRequestURI();
		log.debug( "requestURI="+ requestURI );
		String	pathInfo= request.getPathInfo();
		log.debug( "pathInfo="+ pathInfo );
		if( pathInfo != null )
		{//	pathInfoがある場合はカットする
			requestURI= requestURI.replaceAll( pathInfo +"\\Z", "" );
		}

		out.println( "<form action=\""+ requestURI +"\" method=\"post\">" );
		out.println( "<table class=\"formtable\">" );
		out.println( "<tr>" );
		out.println( "<th style=\"width:4em;\">JNDI</th>" );
		out.println( "<td>" );
		out.println( "<select name=\"jndi\">" );
		for( int i= 0; i < jndiList.size(); i++ )
		{
			String	selected= jndiList.get( i ).matches( jndi ) ? "selected" : "";
			String	tmp= HtmlFilter.parseTag( jndiList.get( i ) );
			out.println( "<option value=\""+ tmp +"\""+ selected +">"+ tmp );
		}
		out.println( "</select>" );
		out.println( "表示件数:<input type=\"text\" name=\"max\" value=\""+ max +"\" maxLength=\"3\" style=\"width:3em;\">" );
		out.println( "</td>" );
		out.println( "</tr>" );
		out.println( "<tr>" );
		out.println( "<th>Option</th>" );
		out.println( "<td>" );
		out.println( "<select name=\"resultSetType\">" );
		out.println( "<option value=\""+ ResultSet.TYPE_FORWARD_ONLY +"\""+ (resultSetType==ResultSet.TYPE_FORWARD_ONLY ?"selected":"") +">"+ "TYPE_FORWARD_ONLY" );
		out.println( "<option value=\""+ ResultSet.TYPE_SCROLL_INSENSITIVE +"\""+ (resultSetType==ResultSet.TYPE_SCROLL_INSENSITIVE ?"selected":"") +">"+ "TYPE_SCROLL_INSENSITIVE" );
		out.println( "<option value=\""+ ResultSet.TYPE_SCROLL_SENSITIVE +"\""+ (resultSetType==ResultSet.TYPE_SCROLL_SENSITIVE ?"selected":"") +">"+ "TYPE_SCROLL_SENSITIVE" );
		out.println( "</select>" );
		out.println( "<select name=\"resultSetConcurrency\">" );
		out.println( "<option value=\""+ ResultSet.CONCUR_READ_ONLY +"\""+ (resultSetConcurrency==ResultSet.CONCUR_READ_ONLY ?"selected":"") +">"+ "CONCUR_READ_ONLY" );
		out.println( "<option value=\""+ ResultSet.CONCUR_UPDATABLE +"\""+ (resultSetConcurrency==ResultSet.CONCUR_UPDATABLE ?"selected":"") +">"+ "CONCUR_UPDATABLE" );
		out.println( "</select>" );
		out.println( "</td>" );
		out.println( "</tr>" );
		out.println( "<tr>" );
		out.println( "<th>SQL</th>" );
		out.println( "<td style=\"width:100%;\">" );
		out.println( "<textarea name=\"sql\" style=\"width:100%; height:10em;\">"+ HtmlFilter.parseTag( sql ) +"</textarea>" );
		out.println( "</td>" );
		out.println( "</tr>" );
		out.println( "</table>" );
		out.println( "<input type=\"submit\" name=\"cmd\" value=\""+ CMD_EXECUTE +"\">" );
		out.println( "<select name=\"loadFile\">" );
		out.println( "<option value=\"\">" );
		String	expr= "^"+ getUid( request ) +UID_SEPARATER;
		for( int i= 0; i < files.length; i++ )
		{
			String	file= files[i].getName();
			file= file.replaceAll( expr, "" );
			String	selected= file.matches( loadFile ) ? "selected" : "";
			String	tmp= HtmlFilter.parseTag( file );
			out.println( "<option value=\""+ tmp +"\""+ selected +">"+ tmp );
		}
		out.println( "</select>" );
		out.println( "<input type=\"submit\" name=\"cmd\" value=\""+ CMD_LOAD +"\">" );
		out.println( "<input type=\"submit\" name=\"cmd\" value=\""+ CMD_DELETE +"\">" );
		out.println( "<input type=\"text\" name=\"saveFile\" value=\""+ HtmlFilter.parseTag( saveFile ) +"\">" );
		out.println( "<input type=\"submit\" name=\"cmd\" value=\""+ CMD_SAVE +"\">" );
		out.println( "</form>" );
	}

	/**
	 * スタイルシートの記述を出力します
	 * @param out
	 */
	private void outputStyle( PrintWriter out )
	{
		out.println( "<style type=\"text/css\">" );
		out.println( "table.formtable{" );
		out.println( "	width: 100%;" );
		out.println( "	border-spacing: 1;" );
		out.println( "	table-layout: fixed;" );
		out.println( "}" );
		out.println( "table.formtable tr th{" );
		out.println( "	background: #E7F3D9;" );
		out.println( "	color: #333333;" );
		out.println( "}" );
		out.println( "table.formtable tr td{" );
		out.println( "}" );
		out.println( "table.formtable tr td input{" );
		out.println( "	font-size: 80%;" );
		out.println( "}" );
		out.println( "table.listtable{" );
//		out.println( "	width: 100%;" );
		out.println( "	border-spacing: 1;" );
//		out.println( "	table-layout: fixed;" );
		out.println( "}" );
		out.println( "table.listtable tr th{" );
		out.println( "	background: #E7F3D9;" );
		out.println( "	color: #333333;" );
		out.println( "}" );
		out.println( "table.listtable tr td{" );
		out.println( "}" );
		out.println( "table.formtable tr td input{" );
		out.println( "	font-size: 80%;" );
		out.println( "}" );
		out.println( "form{" );
		out.println( "	margin-top: 0px;" );
		out.println( "	margin-bottom: 0px;" );
		out.println( "}" );
		out.println( "</style>" );
	}

	private String getParam( HttpServletRequest request, String name, String def )
	{
		String	value= request.getParameter( name );
		if( value == null )	value= def;
		return value;
	}
	private int getParamInt( HttpServletRequest request, String name, int def )
	{
		try
		{
			return Integer.parseInt( request.getParameter( name ) );
		}
		catch( Exception e )
		{
			return def;
		}
	}

	private String getUid( HttpServletRequest request )
	{
		//	remote_userを使う
		String	uid= request.getRemoteUser();
		if( uid == null )
		{//	無ければwso-user-dnを使う
			uid= request.getHeader( "wso-user-dn" );
			if( uid == null )	uid= "";
			uid= uid.replaceAll( "^uid=", "" );
			uid= uid.replaceAll( ",ou=People,.*", "" );
		}
		return uid;
	}

}

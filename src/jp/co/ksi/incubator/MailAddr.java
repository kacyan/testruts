package jp.co.ksi.incubator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import jp.co.ksi.eip.commons.batch.Task;
import jp.co.ksi.eip.commons.batch.TaskInvoker;

/**
 * NewKOAのメールアドレス検索からメールアドレスを入手するプログラムの習作
 * @author kac
 * @since 2010/02/18
 */
public class MailAddr implements Task
{
	private String resultInfo= "";
	
	/**
	 * デフォルト・コンストラクタ
	 */
	public MailAddr()
	{
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		Properties	appConfig= new Properties();
		Properties	taskParam= new Properties();
		
		MailAddr	mailAddr= new MailAddr();
		try
		{
			mailAddr.setConfig( appConfig );
			mailAddr.execute( taskParam );
		}
		catch( Exception e )
		{//	APL_ABEND
			mailAddr.resultInfo= TaskInvoker.APL_ABEND;
			e.printStackTrace();
		}
	}

	public String execute( Properties taskParam ) throws Exception
	{
		String	file= taskParam.getProperty( "file", "http://address.kubota.co.jp/mailaddress_search/default.asp" );
//		file= "http://address.kubota.co.jp/mailaddress_search/javascript.inc";
//		file= "http://address.kubota.co.jp/mailaddress_search/vbscript.inc";
//		file= "http://address.kubota.co.jp/mailaddress_search/default.asp?hid_kensakubtn=1&btn_kensaku="+URLEncoder.encode( "検　索" )+"&rdo_kensaku=2&txt_kensaku=051489";
		String	charset= taskParam.getProperty( "charset", "ms932" );
		String	uid= taskParam.getProperty( "uid", "670194" );
		URL	u= new URL( file );
		URLConnection	con= u.openConnection();

		//	POST
		con.setDoOutput( true );
		PrintStream	out= null;
		try
		{
			out= new PrintStream( con.getOutputStream() );
			out.print( URLEncoder.encode( "hid_kensakubtn", charset ) );
			out.print( "="+ URLEncoder.encode( "1",charset ) );
			out.print( "&"+ URLEncoder.encode( "btn_kensaku", charset ) );
			out.print( "="+ URLEncoder.encode( "検　索",charset ) );
			out.print( "&"+ URLEncoder.encode( "txt_kensaku", charset ) );
			out.print( "="+ URLEncoder.encode( uid,charset ) );
			out.print( "&"+ URLEncoder.encode( "txt_dummy", charset ) );
			out.print( "="+ URLEncoder.encode( "",charset ) );
			out.print( "&"+ URLEncoder.encode( "rdo_kensaku", charset ) );
			out.print( "="+ URLEncoder.encode( "2",charset ) );
			out.print( "&"+ URLEncoder.encode( "hid_busho1", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "hid_busho1_name", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "hid_busho2", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "hid_busho2_name", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "hid_busho3", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "hid_busho3_name", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "hid_browserversion", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "hid_radio", charset ) +"=%8F%5D%8B%C6%88%F5%94%D4%8D%86" );
			out.print( "&"+ URLEncoder.encode( "hid_joutai", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "slt_busho1", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "slt_busho2", charset ) +"=" );
			out.print( "&"+ URLEncoder.encode( "slt_busho3", charset ) +"=" );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			if( out != null )
			{
				out.close();
			}
		}

		//	Recieve
		InputStream	in= null;
		try
		{
			in= con.getInputStream();
			BufferedReader	reader = new BufferedReader( new InputStreamReader( in, charset) );
			String	line= reader.readLine();
			String	mailAddress= "";
			boolean	found= false;
			while( line != null )
			{
				if( line.matches( ".*"+ uid +".*見つかりました.*" ) )
				{
					found= true;
				}
				if( found )
				{
					if( line.matches( ".*<input type=\"hidden\" name=\"hid_MailAddress1.*" ) )
					{
						System.out.println( line );
						mailAddress= line.replaceAll( ".*<input type=\"hidden\" name=\"hid_MailAddress1\" value=\"(.*@.*)\">", "$1" );
						System.out.println( "mailAddress=["+ mailAddress +"]" );
					}
				}
				line= reader.readLine();
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			if( in != null )
			{
				in.close();
			}
		}
		
		return TaskInvoker.APL_OK;
	}


	public String getResultInfo()
	{
		return resultInfo;
	}


	public void setConfig( Properties appConfig ) throws Exception
	{
	}
	
}

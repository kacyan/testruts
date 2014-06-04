package jp.co.ksi.incubator.jcraft;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import jp.co.ksi.eip.commons.struts.IStruts;
import jp.co.ksi.eip.commons.struts.InvokeAction;

/**
 * SFTPのベースB/L
 * @author kac
 * @since 2011/12/13
 * @version 2011/12/13
 */
public abstract class SftpBL implements IStruts
{
	private static Logger	log= Logger.getLogger( SftpBL.class );
	
	/**
	 * 入力パラメータ
	 */
	private Map inData;
	/**
	 * 出力データ
	 */
	protected HashMap outData = new HashMap();
	/**
	 * フォームデータ(uploadとかで使用します)
	 */
	protected Map	formData;
	/**
	 * エラー
	 */
	private ActionErrors errors = new ActionErrors();
	/**
	 * SFTPチャネル
	 */
	protected ChannelSftp sftp;

	@Override
	public String execute( InvokeAction action, ActionForm form, HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		inData = request.getParameterMap();
		
		log.debug( "form="+ form );
		if( form instanceof DynaActionForm )
		{//	formDataを初期化する
			DynaActionForm dyna= (DynaActionForm)form;
			log.debug( "dyna="+ dyna );
			formData= dyna.getMap();
			if( log.isDebugEnabled() )
			{
				Object[]	key= formData.keySet().toArray();
				log.debug( "formData.length="+ key.length );
				for( int i= 0; i< key.length; i++ )
				{
					Object	value= formData.get( key[i] );
					log.debug( "form("+ i +")"+ key[i] +"="+ value );
				}
			}
		}
		
		String host = getParam( "host", "" );
		int port = getParamInt( "port", 22 );
		String uid = getParam( "uid", "" );
		String pwd = getParam( "pwd", "" );
		
		JSch	jsch= new JSch();
		Session	sess= null;
		try
		{
			sess= jsch.getSession( uid, host, port );
			MyUserInfo	ui= new MyUserInfo();
			ui.setPassword( pwd );
			sess.setUserInfo( ui );
			sess.setPassword( pwd );
			sess.connect();
			log.info( "connected. "+ uid +"@"+ host +":"+ port );

			sftp = (ChannelSftp)sess.openChannel( "sftp" );
	
			sftp.connect();
			log.info( "sftp connect. " + getConnectInfo() );
			
			String	result = exec();
			
			sftp.disconnect();
			log.info( "sftp disconnect. "+ getConnectInfo() );
			
			return result;
		}
		catch( Exception e )
		{
			log.error( "host=["+ host +"], port=["+ port +"], uid=["+ uid +"], pwd=["+ pwd +"]", e );
			errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "BL.ERR.DEFAULT", getClass().getName(), e.toString() ) );
			return APL_ERR;
		}
		finally
		{
			for( Iterator<?> i= outData.keySet().iterator(); i.hasNext(); )
			{
				String	key= (String)i.next();
				Object	value= outData.get( key );
				log.debug( "REQ_ATTR: "+ key +"="+ value );
				request.setAttribute( key, value );
			}
			
			action.saveErrors( request, errors );
			if( ( sess != null ) && sess.isConnected() )
			{
				try
				{
					sess.disconnect();
					log.info( "disconnect. "+ uid +"@"+ host +":"+ port );
				}
				catch( Exception e )
				{
					log.error( e.toString(), e );
				}
			}
		}
	}

	protected String getConnectInfo() throws JSchException
	{
		String string = sftp.getSession().getUserName() +"@"+ sftp.getSession().getHost() +":"+ sftp.getSession().getPort();
		return string;
	}

	protected abstract String exec() throws Exception;

	/**
	 * 入力パラメータをStringで返します
	 * @param name	パラメータ名
	 * @param defaultValue	デフォルト値
	 * @return String
	 * <pre>
	 * パラメータ値が複数ある場合、最初に見つけた値を返します
	 * </pre>
	 */
	public final String getParam( String name, String defaultValue )
	{
		Object	obj= inData.get( name );
		if( obj == null ) return defaultValue;
		if( obj instanceof String[] )
		{
			String[]	value= (String[])obj;
			if( value.length == 0 )	return defaultValue;
			if( value[0] == null )	return defaultValue;
			if( value[0].equals( "" ) )	return defaultValue;
			return value[0];
		}
		return obj.toString();
	}

	/**
	 * 入力パラメータをintで返します
	 * @param name	パラメータ名
	 * @param defaultValue	デフォルト値
	 * @return int
	 * <pre>
	 * パラメータ値が複数ある場合、最初に見つけた値を返します
	 * </pre>
	 */
	public final int getParamInt( String name, int defaultValue )
	{
		try
		{
			return Integer.parseInt( getParam( name, String.valueOf(defaultValue) ) );
		}
		catch( NumberFormatException e )
		{
			return defaultValue;
		}
	}

	/**
	 * 入力パラメータをString[]で返します
	 * @param name	パラメータ名
	 * @return String[]
	 * <pre>
	 * パラメータ値が複数ある場合に使用します
	 * </pre>
	 */
	public final String[] getMultiParam( String name )
	{
		Object	obj= inData.get( name );
		if( obj == null ) return new String[0];
		if( obj instanceof String[] )
		{
			return (String[])obj;
		}
		return  new String[0];
	}

	/**
	 * 入力パラメータをint[]で返します
	 * @param name	パラメータ名
	 * @param defaultValue	デフォルト値
	 * @return int[]
	 * <pre>
	 * パラメータ値が複数ある場合に使用します
	 * </pre>
	 */
	public final int[] getMultiParamInt( String name, int defaultValue )
	{
		String[]	value= getMultiParam( name );
		int[]	intValue= new int[value.length];
		for( int i= 0; i < value.length; i++ )
		{
			try
			{
				intValue[i]= Integer.parseInt( value[i] );
			}
			catch( NumberFormatException e )
			{
				intValue[i]= defaultValue;
			}
		}
		return intValue;
	}
	
}
package jp.co.ksi.incubator.portlet;

import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import JP.co.ksi.eip.Auth;

import jp.co.ksi.eip.commons.struts.InvokeAction;
import jp.co.ksi.testruts.Const;
import jp.co.ksi.testruts.bl.BaseBL;

/**
 * サービスパラメータをファイルに保存します
 * @author kac
 * @since 2012/07/11
 * @version 2012/07/11
 * <pre>
 * [in]
 * 	String	sid
 * [out]
 * 	Properties	serviceParam
 * 
 * (1)パラメータからserviceIdを取得する
 * (2)パラメータを{serviceId}/{uid}.paramに保存する
 * </pre>
 */
public class SaveServiceParam extends BaseBL
{
	private static Logger	log= Logger.getLogger( SaveServiceParam.class );

	@Override
	protected String subExecute( InvokeAction action, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		String	baseFolder= appConfig.getProperty( Const.CONF_KEY_BASE_FOLDER, "/home/portal/data/portlet/" );
		Auth	auth= Auth.getAuth( request, appConfig );
		if( auth == null )
		{//	auth不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "auth invalid.["+ auth +"]" );
			return APL_ERR;
		}
		
		//	(1)パラメータからserviceIdを取得する
		DynaActionForm	dyna= (DynaActionForm)form;
		String	serviceId= dyna.getString( "sid" );
		if( ( serviceId == null ) || serviceId.equals( "" ) )
		{//	sid不正->エラー終了
			addError( "BL.ERR.DEFAULT", getClass().getName(), "sid invalid.["+ serviceId +"]" );
			return APL_ERR;
		}
		
		//	(2)パラメータを{serviceId}/{uid}.paramに保存する
		Properties	serviceParam= new Properties();
		Enumeration	enumeration= request.getParameterNames();
		while( enumeration.hasMoreElements() )
		{
			String	key= (String)enumeration.nextElement();
			serviceParam.setProperty( key, request.getParameter( key ) );
		}
		String	serviceParamFile= baseFolder + serviceId +"/"+ auth.getUid() +".param";
		FileOutputStream	out= null;
		try
		{
			out= new FileOutputStream( serviceParamFile );
			serviceParam.store( out, "" );
		}
		catch( Exception e )
		{
			log.error( serviceParamFile, e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), "serviceParamFile invalid.["+ serviceParamFile +"]" );
		}
		finally
		{
			log.debug( "serviceParam="+ serviceParam );
			if( out != null )
			{
				out.close();
			}
		}

		request.setAttribute( "serviceParam", serviceParam );
		
		return APL_OK;
	}
	
}

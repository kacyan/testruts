package jp.co.ksi.incubator.portlet;

import java.io.FileInputStream;
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
 * サービスパラメータファイルを読み込みます
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
 * (2){serviceId}/{uid}.paramからserviceParamを読込む
 * </pre>
 */
public class LoadServiceParam extends BaseBL
{
	private static Logger	log= Logger.getLogger( LoadServiceParam.class );

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
		
		//	(2){serviceId}/{uid}.paramからserviceParamを読込む
		Properties	serviceParam= new Properties();
		String	serviceParamFile= baseFolder + serviceId +"/"+ auth.getUid() +".param";
		FileInputStream	in= null;
		try
		{
			in= new FileInputStream( serviceParamFile );
			serviceParam.load( in );
		}
		catch( Exception e )
		{
			log.error( serviceParamFile, e );
			addError( "BL.ERR.DEFAULT", getClass().getName(), "serviceParamFile invalid.["+ serviceParamFile +"]" );
		}
		finally
		{
			log.debug( "serviceParam="+ serviceParam );
			if( in != null )
			{
				in.close();
			}
		}

		request.setAttribute( "serviceParam", serviceParam );
		
		return APL_OK;
	}
	
}

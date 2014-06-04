package jp.co.ksi.eip.commons.struts;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.ksi.incubator.TestBean;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.ActionServletWrapper;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.PlugInConfig;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.util.MessageResources;

/**
 * デバッグ用のアクション
 * @author kac
 * @since 2011/04/08
 * @version 2014/04/29
 */
public class DebugAction extends Action
{
	private static Logger	log= Logger.getLogger( DebugAction.class );

	@Override
	public ActionForward execute( ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response )
			throws Exception
	{
		log.debug( "mapping.actionId="+ mapping.getActionId() );
		log.debug( "mapping.attribute="+ mapping.getAttribute() );
		log.debug( "mapping.cancellable="+ mapping.getCancellable() );
		log.debug( "mapping.catalog="+ mapping.getCatalog() );
		log.debug( "mapping.command="+ mapping.getCommand() );
		log.debug( "mapping.extends="+ mapping.getExtends() );
		log.debug( "mapping.forward="+ mapping.getForward() );
		log.debug( "mapping.include="+ mapping.getInclude() );
		log.debug( "mapping.input="+ mapping.getInput() );
		log.debug( "mapping.inputForward="+ mapping.getInputForward() );
		log.debug( "mapping.moduleConfig="+ mapping.getModuleConfig() );
		log.debug( "mapping.multipartClass="+ mapping.getMultipartClass() );
		log.debug( "mapping.name="+ mapping.getName() );
		log.debug( "mapping.parameter="+ mapping.getParameter() );
		log.debug( "mapping.path="+ mapping.getPath() );
		log.debug( "mapping.prefix="+ mapping.getPrefix() );
		String[]	roleNames= mapping.getRoleNames();
		log.debug( "mapping.roleNames.length="+ roleNames.length );
		for( int i= 0; i < roleNames.length; i++ )
		{
			log.debug( "mapping.roleNames["+ i+"]="+ roleNames[i] );
		}
		log.debug( "mapping.roles="+ mapping.getRoles() );
		log.debug( "mapping.scope="+ mapping.getScope() );
		log.debug( "mapping.suffix="+ mapping.getSuffix() );
		log.debug( "mapping.type="+ mapping.getType() );
		log.debug( "mapping.unknown="+ mapping.getUnknown() );
		log.debug( "mapping.validate="+ mapping.getValidate() );

		ExceptionConfig[]	exceptionConfigs= mapping.findExceptionConfigs();
		log.debug( "exceptionConfigs.length="+ exceptionConfigs.length );
		for( int i= 0; i < exceptionConfigs.length; i++ )
		{
			log.debug( "exceptionConfigs["+ i+"]="+ exceptionConfigs[i] );
		}
		ForwardConfig[]	forwardConfigs= mapping.findForwardConfigs();
		log.debug( "forwardConfigs.length="+ forwardConfigs.length );
		for( int i= 0; i < forwardConfigs.length; i++ )
		{
			log.debug( "forwardConfigs["+ i +"]="+ forwardConfigs[i] );
		}
		String[]	forwards= mapping.findForwards();
		log.debug( "forwards.length="+ forwards.length );
		for( int i= 0; i < forwards.length; i++ )
		{
			log.debug( "forwards["+ i +"]="+ forwards[i] );
		}

		//	ModuleConfig
		ModuleConfig	moduleConfig= mapping.getModuleConfig();
		ActionConfig[]	actionConfigs= moduleConfig.findActionConfigs();
		log.debug( "actionConfigs.length="+ actionConfigs.length );
		for( int i= 0; i < actionConfigs.length; i++ )
		{
			log.debug( "actionConfigs["+ i +"]="+ actionConfigs[i] );
		}
		exceptionConfigs= moduleConfig.findExceptionConfigs();
		log.debug( "exceptionConfigs.length="+ exceptionConfigs.length );
		for( int i= 0; i < exceptionConfigs.length; i++ )
		{
			log.debug( "exceptionConfigs["+ i+"]="+ exceptionConfigs[i] );
		}
		FormBeanConfig[]	formBeanConfigs= moduleConfig.findFormBeanConfigs();
		log.debug( "formBeanConfigs.length="+ formBeanConfigs.length );
		for( int i= 0; i < formBeanConfigs.length; i++ )
		{
			log.debug( "formBeanConfigs["+ i+"]="+ formBeanConfigs[i] );
		}
		forwardConfigs= moduleConfig.findForwardConfigs();
		log.debug( "forwardConfigs.length="+ forwardConfigs.length );
		for( int i= 0; i < forwardConfigs.length; i++ )
		{
			log.debug( "forwardConfigs["+ i +"]="+ forwardConfigs[i] );
		}
		MessageResourcesConfig[]	messageResourcesConfigs= moduleConfig.findMessageResourcesConfigs();
		log.debug( "messageResourcesConfigs.length="+ messageResourcesConfigs.length );
		for( int i= 0; i < messageResourcesConfigs.length; i++ )
		{
			log.debug( "messageResourcesConfigs["+ i +"]="+ messageResourcesConfigs[i] );
		}
		PlugInConfig[]	plugInConfigs= moduleConfig.findPlugInConfigs();
		log.debug( "plugInConfigs.length="+ plugInConfigs.length );
		for( int i= 0; i < plugInConfigs.length; i++ )
		{
			log.debug( "plugInConfigs["+ i +"]="+ plugInConfigs[i] );
		}

		log.debug( "form="+ form );
		if( form instanceof DynaActionForm )
		{
			DynaActionForm dynaActionForm= (DynaActionForm)form;
			for( Iterator<String> i= dynaActionForm.getMap().keySet().iterator(); i.hasNext(); )
			{
				String	name= i.next();
				log.debug( "DynaActionForm: "+ name +"="+ dynaActionForm.get( name ) );
			}
		}
		else if( form instanceof TestBean )
		{//	2014/05/15 脆弱性(CVE-2014-0094)調査
			TestBean	test= (TestBean)form;

			ActionServletWrapper servletWrapper= test.getServletWrapper();
			log.debug( "servletWrapper="+ servletWrapper );
			if( servletWrapper instanceof org.apache.struts.action.ActionServletWrapper )
			{
				org.apache.struts.action.ActionServletWrapper actionServletWrapper= (org.apache.struts.action.ActionServletWrapper)servletWrapper;
			}
			
			MultipartRequestHandler multipartRequestHandler= test.getMultipartRequestHandler();
			log.debug( "multipartRequestHandler="+ multipartRequestHandler );
			if( multipartRequestHandler != null )
			{
				ActionServlet	actionServlet= multipartRequestHandler.getServlet();
				log.debug( "actionServlet="+ actionServlet.getClass().getName() );
				MessageResources 	messageResources= actionServlet.getInternal();
				log.debug( "messageResources="+ messageResources );
				org.apache.struts.util.PropertyMessageResources propertyMessageResources= (org.apache.struts.util.PropertyMessageResources)messageResources;
				
				ServletContext	servletContext= actionServlet.getServletContext();
				log.debug( "servletContext="+ servletContext );
			}
			
		}
		
		//	2014/04/29 add
		ClassLoader	cl= getClass().getClassLoader();
		while( cl != null )
		{
			log.debug( "ClassLoader: "+ cl );
			cl= cl.getParent();
		}
		
		return mapping.findForward( IStruts.APL_OK );
	}

}

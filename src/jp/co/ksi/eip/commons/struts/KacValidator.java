package jp.co.ksi.eip.commons.struts;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;

/**
 * カスタム・バリデーター
 * @author kac
 *
 */
public class KacValidator implements Serializable
{
	private static Logger	log= Logger.getLogger( KacValidator.class );
	
	/**
	 * さぁ、存分にバリって下さい
	 * @param bean
	 * @param va
	 * @param field
	 * @param errors
	 * @param validator
	 * @param request
	 * @return
	 */
	public static boolean validate(Object bean, ValidatorAction va, Field field, ActionMessages errors, Validator validator, HttpServletRequest request)
	{
		log.debug( "bean="+ bean );
		log.debug( "va="+ va );
		log.debug( "field="+ field );
		log.debug( "errors="+ errors );
		log.debug( "validator="+ validator );
		log.debug( "request="+ request );
		String	value= ValidatorUtils.getValueAsString( bean, field.getProperty() );
		
		if( GenericValidator.isBlankOrNull( value ) )
		{//	error
			return false;
		}
		
		return true;
    }

}

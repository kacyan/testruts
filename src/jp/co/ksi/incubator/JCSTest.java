package jp.co.ksi.incubator;

import jp.co.ksi.testruts.Const;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.behavior.IElementAttributes;

public class JCSTest
{
	public static JCS	jcs= null;
	
	public static JCS getJCS() throws CacheException
	{
		if( jcs == null )
		{
			jcs= JCS.getInstance( Const.REGION_NAME );
			IElementAttributes elementAttrs= jcs.getDefaultElementAttributes();
			elementAttrs.addElementEventHandler( new JcsIElementEventHandler() );
			jcs.setDefaultElementAttributes( elementAttrs );
		}
		
		return jcs;
		
	}
	
	
}

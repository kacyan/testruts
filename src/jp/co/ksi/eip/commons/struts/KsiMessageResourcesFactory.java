package jp.co.ksi.eip.commons.struts;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

/**
 * KsiMessageResourcesを生成するためのFactoryクラス
 * @author kac
 * @since 1.2.0
 * @version 1.2.0
 */
public class KsiMessageResourcesFactory extends MessageResourcesFactory
{
	private static Logger	log= Logger.getLogger( KsiMessageResourcesFactory.class );

	public KsiMessageResourcesFactory()
	{
		log.debug( "create KsiMessageResourcesFactory. " );
	}

	public MessageResources createResources( String config )
	{
		log.debug( "create KsiMessageResources. config="+ config );
		return new KsiMessageResources(this, config, returnNull);
	}

}

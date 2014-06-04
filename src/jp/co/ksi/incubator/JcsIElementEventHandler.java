package jp.co.ksi.incubator;

import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.control.event.ElementEvent;
import org.apache.jcs.engine.control.event.behavior.IElementEvent;
import org.apache.jcs.engine.control.event.behavior.IElementEventHandler;
import org.apache.log4j.Logger;

/**
 * JCSのイベント・ハンドラの習作
 * @author kac
 * @since 2011/12/09
 * @version 2011/12/09
 */
public class JcsIElementEventHandler implements IElementEventHandler
{
	Logger	log= Logger.getLogger( JcsIElementEventHandler.class );
	
	@Override
	public void handleElementEvent( IElementEvent event )
	{
		log.debug( event );
		log.debug( "event="+ event.getElementEvent() +" - "+ event.getClass().getName() );
		if( event instanceof ElementEvent )
		{
			ElementEvent elementEvent = (ElementEvent)event;
			Object source = elementEvent.getSource();
			log.debug( "source="+ source );
			if( source instanceof CacheElement )
			{
				CacheElement	cacheElement= (CacheElement)source;
				log.debug( "key="+ cacheElement.getKey() );
				log.debug( "val="+ cacheElement.getVal() );
			}
		}
	}
	
}

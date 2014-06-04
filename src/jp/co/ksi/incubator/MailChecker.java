package jp.co.ksi.incubator;

import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;

import org.apache.log4j.Logger;

/**
 * ストア・リスナーの習作
 * @author kac
 * @since 2010/02/19
 */
public class MailChecker implements StoreListener
{
	private static Logger	log= Logger.getLogger( MailChecker.class );
	
	public void notification( StoreEvent event )
	{
		log.info( event );
		log.debug( event.getMessage() +" - "+ event.getMessageType() );
	}
	
}

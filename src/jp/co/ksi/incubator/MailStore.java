package jp.co.ksi.incubator;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

/**
 * ストア・リスナーの習作用
 * @author kac
 * @since 2010/02/19
 */
public class MailStore implements HttpSessionBindingListener
{
	private Store	store;
	
	private static Logger	log= Logger.getLogger( MailStore.class );
	

	public void connect( String host, String user, String password ) throws MessagingException
	{
		Properties	props= new Properties();
		props.setProperty( "mail.store.protocol", "pop3" );
		Session	session= Session.getInstance( props );
		try
		{
			//	メールストアを生成する
			store= session.getStore();
			//	メールサーバに接続する
			store.connect( host, user, password );
			log.info( "connected "+ store );
			
			//	ストアリスナーを登録する
			store.addStoreListener( new MailChecker() );
		}
		catch( MessagingException e )
		{
			log.error( e.toString(), e );
			throw e;
		}
	}
	
	public void disconnect()
	{
		if( store != null )
		{
			try
			{
				store.close();
				log.info( "closed "+ store );
				store= null;
			}
			catch( MessagingException e )
			{
				log.error( e.toString(), e );
			}
		}
	}
	
	public boolean isConnected()
	{
		if( store == null )
		{
			return false;
		}
		return store.isConnected();
	}
	
	public void valueBound( HttpSessionBindingEvent event )
	{
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	public void valueUnbound( HttpSessionBindingEvent event )
	{
		disconnect();
	}
	
}

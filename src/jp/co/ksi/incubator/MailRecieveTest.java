package jp.co.ksi.incubator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import JP.co.ksi.util.CharCodeChecker;

/**
 * 受信メールを取得する習作
 * @author kac
 * @since 2014/06/30
 */
public class MailRecieveTest
{
	private Session	session;
	private Store	store;
	private Folder	fol;

	private static Logger	log= Logger.getLogger( MailRecieveTest.class );

	public MailRecieveTest()
	{

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable
	{
		exit();
		super.finalize();
	}

	public Folder getFolder()
	{
		return fol;
	}

	public void init(
		Properties	props
		) throws Exception
	{
		String	host= props.getProperty( "mail.host", "nukem.osk.ksi.co.jp" );
		String	user= props.getProperty( "mail.user", "hoge" );
		String	password= props.getProperty( "mail.passwd", "huga" );

		session= Session.getInstance( props );
		store= session.getStore();
		log.debug( host +" "+ user +" "+ password );
		store.connect( host, user, password );
		fol= store.getDefaultFolder().getFolder( "INBOX" );
		fol.open( Folder.READ_WRITE );
		log.info( "fol open. msg="+ fol.getFullName() +"("+ fol.getMessageCount() +")" );

	}
	public void check() throws MessagingException
	{
		int	count= fol.getMessageCount();
		for( int i= 1; i <= count; i++ )
		{
			try
			{
				Message	msg= fol.getMessage( i );
				log.info( "("+ i +")"+ msg.getSubject() );
				log.debug( CharCodeChecker.toUnicode( msg.getSubject() ) );
				String subject= getSubject( msg );
				log.debug( subject );
			}
			catch( MessagingException e )
			{
				log.error( "("+ i +")", e );
			}
		}
	}

	/**
	 * メールのSubjectを取り出します
	 * @param msg
	 * @return
	 * @throws MessagingException
	 */
	private String getSubject( Message msg ) throws MessagingException
	{
		StringBuffer	buff= new StringBuffer();
		String[]	header= msg.getHeader( "subject" );
		for( int j= 0; j < header.length; j++ )
		{
			String	dec= header[j];
			dec = mimeDecode( dec );
			buff.append( dec );
			log.debug( dec );
		}
		return buff.toString();
	}

	/**
	 * MIMEエンコードされた文字列をデコードします
	 * @param string
	 * @return
	 */
	public String mimeDecode( String string )
	{
		int	start= string.indexOf( "=?" );
		int	encEnd= string.indexOf( "?B?", start );
		int	end= string.indexOf( "?=", encEnd );
		if( 0 <= start && start < encEnd && start < end )
		{
			String	prefix= string.substring( 0, start );
			String	enc= string.substring( start + "=?".length(), encEnd );
			String	b64= string.substring( encEnd + "?B?".length(), end );
			String	suffix= string.substring( end + "?=".length() );
			if( enc.equalsIgnoreCase( "iso-2022-jp" ) && Charset.isSupported( "x-windows-iso2022jp" ) )
			{//	x-windows-iso2022jpがサポートされている
				enc= "x-windows-iso2022jp";
			}
			log.debug( prefix +" "+ enc +" "+ b64 +" "+ suffix );
			try
			{
				string= prefix + new String( Base64.decodeBase64(b64.getBytes("US-ASCII")), enc ) + suffix;
			}
			catch( Exception e )
			{
				log.error( enc, e );
			}
		}
		else
		{
		}
		return string;
	}

	public void exit()
	{
		if( ( fol != null ) && fol.isOpen() )
		{//	フォルダを閉じる
			try
			{
				log.info( "fol close. "+ fol.getName() );
				fol.close( false );
			}
			catch( MessagingException e )
			{
				e.printStackTrace();
			}
		}
		if( ( store != null ) && store.isConnected() )
		{//	ストアを切断する
			try
			{
				log.info( "store close. "+ store );
				store.close();
			}
			catch( MessagingException e )
			{
				e.printStackTrace();
			}
		}
	}


	public static void main( String[] args )
	{
		String	file= "misc/batch.properties";
		Properties	props= new Properties();
		try
		{
			FileInputStream	in= new FileInputStream( file );
			props.load( in );
			in.close();
			PropertyConfigurator.configure( props );
			log.info( "---------- log4j initialized. ----------" );
		}
		catch( FileNotFoundException e )
		{
			e.printStackTrace();
			return;
		}
		catch( IOException e )
		{
			e.printStackTrace();
			return;
		}
		if( args.length > 0 )	props.setProperty( "mail.user", args[0] );
		if( args.length > 1 )	props.setProperty( "mail.password", args[1] );
		if( args.length > 2 )	props.setProperty( "mail.host", args[2] );

		Charset.forName( "iso-2022-jp" ).aliases();

		MailRecieveTest	checker= new MailRecieveTest();
		try
		{
			checker.init( props );
			checker.check();
		}
		catch( Exception e )
		{//	APL_ABEND
			e.printStackTrace();
		}
		finally
		{
			checker.exit();
		}

	}

}

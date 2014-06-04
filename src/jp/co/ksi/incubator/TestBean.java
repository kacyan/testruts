package jp.co.ksi.incubator;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

public class TestBean extends ActionForm
{
	private static Logger	log= Logger.getLogger( TestBean.class );
	
	private String name;
	private String description;
	private String test;
	private SubBean	sub= new SubBean();
	;
	public SubBean getSub()
	{
		return sub;
	}
	public void setSub( SubBean sub )
	{
		this.sub= sub;
	}

	private org.apache.struts.upload.FormFile	file;
	
	public org.apache.struts.upload.FormFile getFile()
	{
		return file;
	}
	public void setFile( org.apache.struts.upload.FormFile file )
	{
		this.file= file;
	}
	public String getName()
	{
		return name;
	}
	public void setName( String name )
	{
		this.name= name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription( String description )
	{
		this.description= description;
	}
	public String getTest()
	{
		return test;
	}
	public void setTest( String test )
	{
		log.debug( getClass().getClassLoader() );
		this.test= test;
	}
	
	protected void doTest( String text )
	{
		
	}
	
	public static final Proxy getProxy()
	{
		Proxy	proxy= null;
		String	hostname= System.getProperty( "http.proxyHost" );
		String	port= System.getProperty( "http.proxyPort" );
		try
		{
			InetSocketAddress	socketAddress= new InetSocketAddress( hostname, Integer.parseInt( port ) );
			proxy= new Proxy( Proxy.Type.HTTP, socketAddress );
		}
		catch( Exception e )
		{
			proxy= Proxy.NO_PROXY;
		}
		
		return proxy;
	}
	
}

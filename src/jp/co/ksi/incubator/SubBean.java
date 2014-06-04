package jp.co.ksi.incubator;


import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

public class SubBean extends ActionForm
{
	private static Logger	log= Logger.getLogger( SubBean.class );
	
	private String name;
	private String description;
	private String test;
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
	
}

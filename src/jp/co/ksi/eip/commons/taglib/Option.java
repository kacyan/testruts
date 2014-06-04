package jp.co.ksi.eip.commons.taglib;

/**
 * タグリブで使用するクラス
 * @author kac
 * @since 2012/03/21
 * @version 2012/03/22
 */
public class Option
{
	private String value;
	private String displayValue;

	public Option()
	{
		
	}
	public Option( String value, String display )
	{
		setValue( value );
		setDisplayValue( display );
	}
	public String getName()
	{
		return value;
	}
	public void setValue( String value )
	{
		this.value = value;
	}
	public String getDisplayValue()
	{
		return displayValue != null ? displayValue : value;
	}
	public void setDisplayValue( String display )
	{
		this.displayValue = display;
	}
	
}

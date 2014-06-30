package jp.co.ksi.eip.commons.util;

public class PasswordString
{

	private String	password;
	
	/**
	 * デフォルト・コンストラクタ
	 */
	public PasswordString()
	{
		password= "";
	}
	
	/**
	 * カスタム・コンストラクタ
	 * @param password
	 */
	public PasswordString( String password )
	{
		this.password= password;
	}
	
	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		if( obj != null )
		{
			if( obj instanceof String )
			{
				return password.equals( (String)obj );
			}
			else if( obj instanceof PasswordString )
			{
				return password.equals( ((PasswordString)obj).password );
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return password;
	}
	
}

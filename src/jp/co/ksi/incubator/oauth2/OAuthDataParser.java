package jp.co.ksi.incubator.oauth2;

public abstract class OAuthDataParser
{
	public abstract OAuth parse( String text ) throws Exception;
}

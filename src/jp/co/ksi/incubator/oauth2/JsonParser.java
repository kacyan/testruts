package jp.co.ksi.incubator.oauth2;

import net.arnx.jsonic.JSON;

public class JsonParser extends OAuthDataParser
{
	
	@Override
	public OAuth parse( String text )
	{
		return JSON.decode( text );
	}
	
}

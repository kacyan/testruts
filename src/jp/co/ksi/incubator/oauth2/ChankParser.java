package jp.co.ksi.incubator.oauth2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

public class ChankParser extends OAuthDataParser
{
	private static Logger	log= Logger.getLogger( ChankParser.class );
	
	private static final String UTF8= "utf-8";
	
	@Override
	public OAuth parse( String text ) throws Exception
	{
		OAuth	oauth= new OAuth();
		oauth.setAccess_token( getResponseParamValue( "access_token", text ) );
		try
		{
			oauth.setExpires_in( Integer.parseInt( getResponseParamValue( "expires_in", text ) ) );
		}
		catch( Exception e )
		{
		}
		oauth.setRefresh_token( getResponseParamValue( "refresh_token", text ) );
		oauth.setToken_type( getResponseParamValue( "token_type", text ) );

		return oauth;
	}

	public static String getResponseParamValue( String name, String responseText ) throws UnsupportedEncodingException
	{
		name+= "=";
		int	start= responseText.indexOf( name );
		if( start < 0 )
		{//	見つからない
			return "";
		}
		start+= name.length();
		responseText= responseText.substring( start );
		int	end= responseText.indexOf( "&" );
		if( end >= 0 )
		{
			responseText= responseText.substring( 0, end );
		}
		//	2012/04/11 Kac URLデコードすべし
		return URLDecoder.decode( responseText, UTF8 );
	}

}

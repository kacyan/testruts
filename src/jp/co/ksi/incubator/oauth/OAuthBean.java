package jp.co.ksi.incubator.oauth;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * OAuthデータを保持する
 * @author kac
 * @since 2012/03/13
 * @version 2012/04/11 getResponseParamValue()を修正
 */
public class OAuthBean implements Serializable
{
	private static Logger	log= Logger.getLogger( OAuthBean.class );

	public static final String UTF8 = "utf-8";

	/**
	 * コンシューマ・キー
	 * コンシューマを特定する値
	 */
	private String consumer_key= "";
	/**
	 * コンシューマー・シークレット
	 */
	private String consumer_secret= "";
	/**
	 * リクエスト・トークン取得用URL
	 */
	private String requestTokenURL= "";
	/**
	 * トークン認可用URL
	 */
	private String authorizeTokenURL= "";
	/**
	 * プロバイダが返すトークン認可用URL
	 * パラメータも付いている
	 */
	private String xoauth_request_auth_url= "";
	/**
	 * アクセス・トークンを受けるリダイレクト先URL(コンシューマ側)
	 */
	private String oauth_callback= "";
	/**
	 * 認可を求めるサービスのURL
	 */
	private String scope= "";
	/**
	 * OAuthのバージョン 1.0固定？
	 */
	private String version= "1.0";

	/**
	 * アクセス・トークン取得用URL
	 */
	private String accessTokenURL= "";
	/**
	 * トークン
	 * リクエスト・トークン -> アクセス・トークン　と推移する？
	 */
	private String oauth_token= "";
	/**
	 * トークン・シークレット
	 */
	private String oauth_token_secret= "";
	/**
	 * べリファイ
	 * プロバイダ側で認可された事を示す
	 * べリファイとリクエスト・トークンをプロバイダに送る事で、アクセス・トークンが得られる
	 */
	private String oauth_verifier= "";

	public String getConsumer_key()
	{
		return consumer_key;
	}

	public void setConsumer_key( String consumerKey )
	{
		consumer_key = consumerKey;
	}

	public String getConsumer_secret()
	{
		return consumer_secret;
	}

	public void setConsumer_secret( String consumerSecret )
	{
		consumer_secret = consumerSecret;
	}

	public String getRequestTokenURL()
	{
		return requestTokenURL;
	}

	public void setRequestTokenURL( String requestTokenURL )
	{
		this.requestTokenURL = requestTokenURL;
	}

	public String getAuthorizeTokenURL()
	{
		return authorizeTokenURL;
	}

	public void setAuthorizeTokenURL( String authorizeTokenURL )
	{
		this.authorizeTokenURL = authorizeTokenURL;
	}

	public String getXoauth_request_auth_url()
	{
		if( xoauth_request_auth_url == null )
		{
			return authorizeTokenURL +"?oauth_token="+ oauth_token;
		}
		if( xoauth_request_auth_url.equals( "" ) )
		{
			return authorizeTokenURL +"?oauth_token="+ oauth_token;
		}
		return xoauth_request_auth_url;
	}

	public void setXoauth_request_auth_url( String xoauthRequestAuthUrl )
	{
		xoauth_request_auth_url = xoauthRequestAuthUrl;
	}

	public String getOauth_nonce()
	{
		return RandomStringUtils.random( 32, "12345678890abcdef" );
	}

	public String getOauth_callback()
	{
		return oauth_callback;
	}

	public void setOauth_callback( String oauthCallback )
	{
		oauth_callback = oauthCallback;
	}

	public String getScope()
	{
		return scope;
	}

	public void setScope( String scope )
	{
		this.scope = scope;
	}

	public String getAccessTokenURL()
	{
		return accessTokenURL;
	}

	public void setAccessTokenURL( String accessTokenURL )
	{
		this.accessTokenURL = accessTokenURL;
	}

	public String getOauth_token()
	{
		return oauth_token;
	}

	public void setOauth_token( String oauthToken )
	{
		oauth_token = oauthToken;
	}

	public String getOauth_token_secret()
	{
		return oauth_token_secret;
	}

	public void setOauth_token_secret( String oauthTokenSecret )
	{
		oauth_token_secret = oauthTokenSecret;
	}

	public String getOauth_verifier()
	{
		return oauth_verifier;
	}

	public void setOauth_verifier( String oauthVerifier )
	{
		oauth_verifier = oauthVerifier;
	}

	public String getVersion()
	{
		return version;
	}

	public String toString()
	{
		String	ret= "consumer_key="+ consumer_key;
		ret+= ", consumer_secret="+ consumer_secret;
		ret+= ", oauth_callback="+ oauth_callback;
		ret+= ", oauth_token="+ oauth_token;
		ret+= ", oauth_token_secret="+ oauth_token_secret;
		ret+= ", oauth_verifier="+ oauth_verifier;
		ret+= ", requestTokenURL="+ requestTokenURL;
		ret+= ", authorizeTokenURL="+ authorizeTokenURL;
		ret+= ", accessTokenURL="+ accessTokenURL;
		ret+= ", scope="+ scope;
		return ret;
	}

	/**
	 * HMAC-SHA1で署名を生成します
	 * @param method
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getSignature( String method, String url, String params )
			throws Exception
	{

		String	signatureKey= URLEncoder.encode( consumer_secret, UTF8 ) +"&"+ URLEncoder.encode( oauth_token_secret, UTF8 );
		log.debug( "signatureKey="+ signatureKey );
		String	signatureBase= URLEncoder.encode( method, UTF8 ) +"&"+ URLEncoder.encode( url, UTF8 ) +"&"+ URLEncoder.encode( params, UTF8 );
		log.debug( "signatureBase="+ signatureBase );

		String	signature= "";
//		BASE64Encoder	base64= new BASE64Encoder();
		try
		{
			Mac	m= Mac.getInstance( "HmacSHA1" );
			SecretKey	key= new SecretKeySpec( signatureKey.getBytes("US-ASCII"), m.getAlgorithm() );
			m.init( key );
			m.update( signatureBase.getBytes() );
//			signature= base64.encode( m.doFinal() );
			signature= new String( Base64.encodeBase64( m.doFinal() ), "US-ASCII" );
			log.debug( signature );
			signature= URLEncoder.encode( signature, UTF8 );
			log.debug( signature );
		}
		catch( Exception e )
		{
			log.error( e.toString(), e );
		}

		return signature;
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

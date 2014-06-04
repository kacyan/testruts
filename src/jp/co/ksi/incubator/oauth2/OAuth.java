package jp.co.ksi.incubator.oauth2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OAuth2.0のbean
 * @author kac
 * @since 2012/05/31
 * @version 2021/06/20 refresh_token対応
 */
public class OAuth
{
	//	本当に必要なデータ
	private String access_token;
	private int expires_in;
	private String token_type;
	private String scope;
	private String serviceURL;
	private Date createDate;
	private Date updateDate;
	
	//	refresh_tokenを使って再取得する時に必要なデータ
	private String refresh_token;
	private String tokenURL;

	//	一から再取得する時に必要なデータ(ここに保持しとくべきか？)
	private String state;
	private String access_type;
	private String approval_prompt;
	private String code;
	private String grant_type;
	
	@Override
	public String toString()
	{
		SimpleDateFormat	sdf= new SimpleDateFormat( "yyyy/MM/dd-HH:mm:ss" );
		
		return "access_token="+ access_token
				+", expires_in="+ expires_in
				+", token_type="+ token_type
				+", refresh_token="+ refresh_token
				+", createDate="+ ( createDate != null ? sdf.format( createDate ) : "null" )
				+", updateDate="+ ( updateDate != null ? sdf.format( updateDate ) : "null" );
	}
	public String getAccess_token()
	{
		return access_token;
	}
	public void setAccess_token( String accessToken )
	{
		access_token = accessToken;
	}
	public int getExpires_in()
	{
		return expires_in;
	}
	public void setExpires_in( int expiresIn )
	{
		expires_in = expiresIn;
	}
	public String getToken_type()
	{
		return token_type;
	}
	public void setToken_type( String tokenType )
	{
		token_type = tokenType;
	}
	public String getRefresh_token()
	{
		return refresh_token;
	}
	public void setRefresh_token( String refreshToken )
	{
		refresh_token = refreshToken;
	}
	public String getServiceURL()
	{
		return serviceURL;
	}
	public void setServiceURL( String serviceURL )
	{
		this.serviceURL = serviceURL;
	}
	public String getTokenURL()
	{
		return tokenURL;
	}
	public void setTokenURL( String tokenURL )
	{
		this.tokenURL = tokenURL;
	}
	public String getScope()
	{
		return scope;
	}
	public void setScope( String scope )
	{
		this.scope = scope;
	}
	public String getState()
	{
		return state;
	}
	public void setState( String state )
	{
		this.state = state;
	}
	public String getAccess_type()
	{
		return access_type;
	}
	/**
	 * 
	 * @param accessType
	 * <pre>
	 * online ---- 
	 * offline --- 
	 * </pre>
	 */
	public void setAccess_type( String accessType )
	{
		access_type = accessType;
	}
	public String getApproval_prompt()
	{
		return approval_prompt;
	}
	/**
	 * 
	 * @param approvalPrompt
	 * <pre>
	 * Googleの仕様だと以下の通り
	 * force --- refresh_tokenを常に再発行する
	 * auto ---- 初回のみrefresh_tokenを発行する
	 * </pre>
	 */
	public void setApproval_prompt( String approvalPrompt )
	{
		approval_prompt = approvalPrompt;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode( String code )
	{
		this.code = code;
	}
	public String getGrant_type()
	{
		return grant_type;
	}
	public void setGrant_type( String grantType )
	{
		grant_type = grantType;
	}
	
	public String getAuthorization()
	{
		return getToken_type() +" "+ getAccess_token();
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate( Date createDate )
	{
		this.createDate = createDate;
	}
	public Date getUpdateDate()
	{
		return updateDate;
	}
	public void setUpdateDate( Date updateDate )
	{
		this.updateDate = updateDate;
	}
}

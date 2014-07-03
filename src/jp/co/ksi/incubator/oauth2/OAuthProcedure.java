package jp.co.ksi.incubator.oauth2;

import jp.co.ksi.incubator.portlet.OAuthConfig;

/**
 * OAuthを保存するインターフェース
 * @author kac
 * @since 2014/07/03
 * @version 2014/07/03
 * <pre>
 * OAuthBaseServiceのsetSaveOAuthProcedure()でセットすると
 * OAuthBaseServiceのrefreshAccessToken()内で呼ばれます。
 * </pre>
 */
public interface OAuthProcedure {

	/**
	 * oauthをoauthConfigに従って保存します
	 * @param oauth
	 * @param oauthConfig
	 */
	public void saveOAuth( OAuth oauth, OAuthConfig oauthConfig ) throws Exception;

}

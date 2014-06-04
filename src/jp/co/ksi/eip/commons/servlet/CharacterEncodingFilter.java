package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
<PRE>
 * ServletRequest#setCharacterEncoding()により日本語文字化けを
 * 回避する文字コード指定を行うフィルタクラスです.
 * web.xmlで指定したservletまたはjspに適用されます.
 * 以下の記述をweb.xmlに追加して下さい.
 * Filterの定義は、必ず&lt;servlet&gt;より前に記述してください
  &lt;filter&gt;
    &lt;filter-name&gt;SetCharacterEncoding&lt;/filter-name&gt;
    &lt;filter-class&gt;jp.co.ksi.eip.commons.servlet.CharacterEncodingFilter&lt;/filter-class&gt;
    &lt;init-param&gt;
      &lt;param-name&gt;encoding&lt;/param-name&gt;
      &lt;param-value&gt;JISAutoDetect&lt;/param-value&gt;
    &lt;/init-param&gt;
  &lt;/filter&gt;
  &lt;filter-mapping&gt;
     &lt;filter-name&gt;SetCharacterEncoding&lt;/filter-name&gt;
     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
  &lt;/filter-mapping&gt;
 * @since Servlet 2.3
</PRE>
 */
public class CharacterEncodingFilter implements Filter {

    /** 指定する文字エンコード */
    protected String encoding = null;

    /** 
     * サーブレットコンテナによって使用されるフィルタ設定のためのオブジェクトです。
     */
    protected FilterConfig filterConfig = null;

    protected boolean ignore = true;

    /**
     * このフィルタがサービス状態を終える際に、Web コンテナによって呼び出されます。
     * フィールドの終了処理を行います。
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }

    /**
     * リクエストが処理される前に呼び出され、リクエストオブジェクトの文字エンコードを設定します。
     * @see javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (ignore || (request.getCharacterEncoding() == null)) {
            String encoding = selectEncoding(request);
            if (encoding != null){
                request.setCharacterEncoding(encoding);
            }
        }
        // 次のフィルタに処理を渡します。
        chain.doFilter(request, response);
    }

    /**
     * サーブレットの初期化パラメータを取得し、フィールドの設定を行います。
     * このフィルタがサービス開始状態になる際に、Web コンテナによって呼び出されます。
     * @see javax.servlet.Filter#init(FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        
        // サーブレットの初期化パラメータ"encoding"を取得し、文字エンコードを設定します。
        this.encoding = filterConfig.getInitParameter("encoding");
        
        // 初期化パラメータ"ignore"について、その値が格納された String を取得します。
        String value = filterConfig.getInitParameter("ignore");
        
        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;
    }

    protected String selectEncoding(ServletRequest request) {
        return (this.encoding);
    }
}
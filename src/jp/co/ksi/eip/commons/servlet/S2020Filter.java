package jp.co.ksi.eip.commons.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * sturtsの脆弱性の対策フィルター
 * @author kac
 * @since 2014/04/25
 * @version 2014/04/25
 * <pre>
 * [2014/04/25 Kac] 本当は、BeanUtil.populate()を修正すべきだと思う。
 * </pre>
 */
public class S2020Filter implements Filter
{
	private static Pattern EXLUDE_PARAMS = Pattern.compile("(^|\\W)[cC]lass\\W");

	@Override
	public void destroy()
	{
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void doFilter( ServletRequest req, ServletResponse res,
			FilterChain chain ) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest)req;
		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			if (isAttack(paramName)) {
				throw new IllegalArgumentException("Attack: " + paramName);
			}
		}
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for  (Cookie c : cookies) {
				String cookieName = c.getName();
				if (isAttack(cookieName)) {
					throw new IllegalArgumentException("Attack: " + cookieName);
				}
			}
		}
		chain.doFilter(request, res);
	}

	private static boolean isAttack(String target)
	{
		return EXLUDE_PARAMS.matcher(target).find();
	}

	@Override
	public void init( FilterConfig config ) throws ServletException
	{
		// TODO 自動生成されたメソッド・スタブ

	}

}

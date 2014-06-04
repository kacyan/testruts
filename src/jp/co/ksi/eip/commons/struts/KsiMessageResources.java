package jp.co.ksi.eip.commons.struts;

import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.PropertyMessageResources;

/**
 * org.apache.struts.util.PropertyMessageResourcesはロケールの選択方法が良くないので
 * それを改善するために独自のMessageResourcesを用意しました。
 * @author kac
 * @since 1.2.0
 * @version 1.2.0
 * <pre>
 * 修正内容は以下のとおりです。
 * 元のgetMessage()の処理
 * １．Resource_en_AU.propertiesをリソースに追加して en_AU.msgKeyに該当するメッセージを探す
 * 　見つかればメッセージを返す
 * ２．Resource_en.propertiesをリソースに追加して en.msgKeyに該当するメッセージを探す
 * 　見つかればリソースにen_AU.msgKeyで追加し、メッセージを返す
 * ３．Resource_{defaultLocale}.propertiesをリソースに追加して、ja_JP.msgKeyに該当する...
 * 　見つかればリソースにen_AU.msgKeyで追加し、メッセージを返す
 * ４．Resource.propertiesをリソースに追加して、msgKeyに該当する...
 * 　見つかればリソースにen_AU.msgKeyで追加し、メッセージを返す
 * 
 * ja_JPのリソースが存在するとen_AUでも日本語リソースが返ってしまいました。
 * ３の処理が明らかに余計だったのです。
 * このクラスのgetMessage()では３の処理をコメントアウトしました。
 * 
 * </pre>
 */
public class KsiMessageResources extends PropertyMessageResources
{
	private static Logger	log= Logger.getLogger( KsiMessageResources.class );

	public KsiMessageResources( MessageResourcesFactory factory, String config )
	{
		super( factory, config );
		log.debug( "created. config="+ config );
	}

	public KsiMessageResources( MessageResourcesFactory factory, String config,
			boolean returnNull )
	{
		super( factory, config, returnNull );
		log.debug( "created. config="+ config +", returnNull="+ returnNull );
	}

	/**
	 * PropertyMessageResourcesからコピーしてきて、良くない箇所を修正しました
	 */
	public String getMessage( Locale locale, String key )
	{
        if (log.isDebugEnabled()) {
            log.debug("getMessage(" + locale + "," + key + ")");
        }

        // Initialize variables we will require
        String localeKey = localeKey(locale);
        String originalKey = messageKey(localeKey, key);
        log.debug( "originalKey="+ originalKey );
        String messageKey = null;
        String message = null;
        int underscore = 0;
        boolean addIt = false;  // Add if not found under the original key

        // Loop from specific to general Locales looking for this message
        while (true) {

            // Load this Locale's messages if we have not done so yet
            loadLocale(localeKey);

            // Check if we have this key for the current locale key
            messageKey = messageKey(localeKey, key);
            //	Kac localeでwhileループ中
            log.debug( "while loop: locale="+ locale );
            log.debug( "while loop: localeKey="+ localeKey );
            log.debug( "while loop: key="+ key );
            log.debug( "while loop: messageKey="+ messageKey );
            synchronized (messages) {
                message = (String) messages.get(messageKey);
                log.debug( "while loop: message="+ message );
                if (message != null) {
                    if (addIt) {
                        messages.put(originalKey, message);
                        log.debug( "while loop: messages.put("+ originalKey +", "+ message +")" );
                    }
                    return (message);
                }
            }

            // Strip trailing modifiers to try a more general locale key
            addIt = true;
            underscore = localeKey.lastIndexOf("_");
            if (underscore < 0) {
                break;
            }
            localeKey = localeKey.substring(0, underscore);

        }

        /*	Kac この処理は良くない
        // Try the default locale if the current locale is different
        if (!defaultLocale.equals(locale)) {
            localeKey = localeKey(defaultLocale);
            messageKey = messageKey(localeKey, key);
            loadLocale(localeKey);
            synchronized (messages) {
                message = (String) messages.get(messageKey);
                if (message != null) {
                    messages.put(originalKey, message);
                    return (message);
                }
            }
        }
        */

        // As a last resort, try the default Locale
        localeKey = "";
        messageKey = messageKey(localeKey, key);
        loadLocale(localeKey);
        log.debug( "last: locale="+ locale );
        log.debug( "last: localeKey="+ localeKey );
        log.debug( "last: key="+ key );
        log.debug( "last: messageKey="+ messageKey );
        synchronized (messages) {
            message = (String) messages.get(messageKey);
            log.debug( "last: message="+ message );
            if (message != null) {
                messages.put(originalKey, message);
                log.debug( "last: messages.put("+ originalKey +", "+ message +")" );
                return (message);
            }
        }

        // Return an appropriate error indication
        if (returnNull) {
            return (null);
        } else {
            return ("???" + messageKey(locale, key) + "???");
        }
	}

	/**
	 * メッセージリソースのdebug用
	 */
	public HashMap getMessages()
	{
		return messages;
	}

}

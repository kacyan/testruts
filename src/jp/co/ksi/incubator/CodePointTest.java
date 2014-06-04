package jp.co.ksi.incubator;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import jp.co.ksi.eip.commons.util.StringUtil;

import org.apache.log4j.Logger;

import JP.co.ksi.util.CharCodeChecker;

/**
 * CodePointの習作
 * サロゲートペアを扱う
 * @author kac
 * @since 2010/04/13
 * @version 2010/04/13
 * <pre>
 * ログを出力する場合は、実行時に-Dlog4j.configuration=file:/${log4j.properties}を指定してください
 * </pre>
 * <pre>
 * サロゲートペアを扱う際の課題は、
 * ・サロゲートペアはchar型２つで表現される
 * ・Stringクラスでindexを指定するようなメソッドは、char型のインデックスである
 * 　このため、サロゲートペアを考慮したインデックスを指定する必要がある
 * 　SJISのFirstByte,SecondByteの判定みたいな感じ
 * ・Stringクラスのlength()もchar型の数を返す
 * 
 * 考慮が要る例：
 * ・文字列を任意の長さでカットするような処理
 * 　String.substring( begin, end ) --- サロゲートペアを分断しないように
 * ・１文字は必ず１つのcharで表現できるとしてる処理
 * 　String.charAt( index ) --- サロゲートペアの場合、char１つでは文字化けする
 * 　String.valueOf( c ) --- サロゲートペアの場合、char１つでは文字化けする
 * ・文字列の長さを使う処理
 * 　String.length() --- 長さではないcharの数だ
 * 
 * 上記とは別に、文字列の長さをSJIS換算のバイト数だとしてきた処理は見直しが要る
 * そもそもSJISでは表現できない文字であるため、外部I/FもUnicode系の文字コードになる
 * そのため、SJISのバイト数は意味が無くなる可能性が出てくる
 * まずは何のために文字列の長さを使っているのかを調べるところから始めるべきである
 * ・DBへの入力サイズ制御や固定長フォーマットのためだろうか
 * ・入力フィールドでのフォーカス移動のためだろうか
 * 
 * Unicodeで扱う場合、固定長という概念は難しいのではないだろうか？
 * </pre>
 */
public class CodePointTest
{
	private static Logger	log= Logger.getLogger( CodePointTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		String	src= "份𡈽₩$€";
		int	cpCount= src.codePointCount( 0, src.length() );
		log.debug( "src="+ src +", length="+ src.length() +", codePointCount="+ cpCount );
		
		for( int i= 0; i < src.length(); i++  )
		{
			int		cp= src.codePointAt( i );
			log.debug( "["+ cp +"]"+ getCharCode( Character.toChars( cp ), "utf-8" ) );
			log.debug( "["+ cp +"]"+ getCharCode( Character.toChars( cp ), "utf-16" ) );
			log.debug( "["+ cp +"]"+ getCharCode( Character.toChars( cp ), "utf-32" ) );
			
			if( Character.isSupplementaryCodePoint( cp ) )
			{//	補助文字(サロゲートペア)なのでcharインデックスを１つ進める
				i++;
			}
		}
		
		/***** step2 *****/
		int[]	code= { 0xe4, 0xbb, 0xbc };
		ByteArrayOutputStream	out= new ByteArrayOutputStream();
		for( int i= 0; i < code.length; i++ )
		{
			out.write( code[i] );
		}
		byte[]	b= out.toByteArray();
		log.debug( CharCodeChecker.byte2hex( b ) );
		String	dst= "";
		try
		{
			dst= new String( b, "utf-8" );
			log.debug( "dst="+ dst );
		}
		catch( UnsupportedEncodingException e )
		{
			e.printStackTrace();
		}
		log.debug( CharCodeChecker.toUnicode( dst ) );
		
		/***** step3 *****/
		log.debug( StringUtil.substrByteLen( src, 0, 7, "" ) );
		log.debug( StringUtil.substrByteLen( src, 0, 14, "", "utf-16" ) );
		log.debug( StringUtil.substrByteLen( src, 0, 14, "", "ms932" ) );
	}

	/**
	 * 指定されたchar配列のコードをHEX表記で返します
	 * @param c
	 * @param enc	文字エンコード(例：ms932, utf-8...)
	 * @return
	 */
	public static String getCharCode( char[] c, String enc )
	{
		StringBuffer buff= new StringBuffer();;
		String	tmp= String.valueOf( c );
		buff.append( tmp +":" );
		try
		{
			byte[]	b= tmp.getBytes( enc );
			for( int i= 0; i < b.length; i++ )
			{
				int	code= b[i] & 255;
				String	prefix= code < 16 ? "0x0" : "0x";
				buff.append( prefix + Integer.toHexString( code ).toUpperCase() +", " );
			}
		}
		catch( Exception e )
		{
			buff.append( e.toString() );
		}
		return buff.toString();
	}
	
}

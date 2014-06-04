package jp.co.ksi.eip.commons.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 文字列操作用のユーティリティクラス
 * @author kac
 * @since 2009/02/13
 * @version 2010/07/15
 */
public class StringUtil
{
	private static Logger	log= Logger.getLogger( StringUtil.class );
	
	/**
	 * 指定されたbyte数で文字列をカットします
	 * @param src	元の文字列
	 * @param len	byte数
	 * @param tail	カット後、末尾に付加する文字列
	 * @return String
	 */
	public static String substrByteLen( String src, int len, String tail )
	{
		return substrByteLen( src, 0, len, tail );
	}

	/**
	 * 指定されたbyte数で文字列をカットします
	 * @param src	元の文字列
	 * @param start	開始位置
	 * @param len	byte数
	 * @param tail	カット後、末尾に付加する文字列
	 * @return String
	 */
	public static String substrByteLen( String src, int start, int len, String tail )
	{
		String dst;
		StringBuffer	buf= new StringBuffer();
		int	count= 0;
		for( int i= 0; i < src.length(); i++ )
		{
			char c= src.charAt( i );
			//	0x0020 -- 0x007E 
			if( (c >= 0x20) && (c <= 0x7e) )
			{//	ASCII=半角
				count++;
			}
			else if( (c > 0xff61) && (c >= 0xff9f) )
			{//	半角カナ
				count++;
			}
			else
			{//	それ以外は全角
				count+= 2;
			}

			if( count > (start+len) )
			{//	足すとlenを超えるので足さずに終了する
				buf.append( tail );
				break;
			}
			if( start <= count )
			{//	抽出範囲内なので足す
	        	String	tmp= "";
				if( Character.isHighSurrogate( c ) )
				{//	サロゲートペア
					i++;
					char	c2= src.charAt( i );
					tmp= String.valueOf( Character.toChars( Character.toCodePoint( c, c2 ) ) );
				}
				else
				{
					tmp= String.valueOf( c );
				}
	        	buf.append( tmp );
				log.debug( tmp +"-"+ Integer.toHexString( c ) );
			}
		}
		dst= buf.toString();
		return dst;
	}
	
	/**
	 * 指定された文字コードとbyte数で文字列をカットします
	 * @param src	元の文字列
	 * @param start	開始位置
	 * @param len	byte数
	 * @param tail	カット後、末尾に付加する文字列(...等)
	 * @param charset	byte数を調べる時の文字コード
	 * @return	String
	 */
	public static String substrByteLen( String src, int start, int len, String tail, String charset )
	{
		StringBuffer	buf= new StringBuffer();
		int	count= 0;
		for( int i= 0; i < src.length(); i++ )
		{
			//	codePointを使って１文字とりだす
			int	cp= src.codePointAt( i );
			String	s1= String.valueOf( Character.toChars( cp ) );
			
			try
			{//	指定された文字コードでbyte長を調べる
				byte[]	b= s1.getBytes( charset );
				count+= b.length;
				log.debug( s1 +" "+ b.length +"/"+ count );
			}
			catch( UnsupportedEncodingException e )
			{
				log.debug( s1 +" "+ e.toString() );
				count++;
			}

			if( count > (start+len) )
			{//	足すとlenを超えるので足さずに終了する
				buf.append( tail );	//	カットしたことになるのでtailを付加する
				break;
			}
			if( start <= count )
			{//	抽出範囲内なので足す
	        	buf.append( s1 );
			}
			if( Character.isSupplementaryCodePoint( cp ) )
			{//	cpが補助文字(サロゲートペア)なのでcharインデックスを１つ進める
				i++;
			}
		}
		return buf.toString();
	}
	
	public static int getByteLength( String src, String charset )
	{
		byte[] b= {};
		try
		{
			b = src.getBytes( charset );
		}
		catch( UnsupportedEncodingException e )
		{
		}
		return b.length;
	}
	
	/**
	 * 変数が含まれた文字列に値をセットして新しい文字列を返します
	 * @param src	変数が含まれた文字列
	 * @param props	変数の値をもつプロパティ
	 * @return	変換された文字列を返します
	 * <pre>
	 * propsが
	 * 　hoge=ほげ
	 * 　huga=ふが
	 * の場合、以下のようになります。
	 * 
	 * 変換前：abcde${hoge}12345${huga}---
	 * 変換後：abcdeほげ12345ふが---
	 * 
	 * 変換前：abcde${ehe}12345${huga}---
	 * 変換後：abcde12345ふが---
	 * </pre>
	 */
	public static String replace( String src, Properties props )
	{
		while( true )
		{
			int	start= src.indexOf( "${" );
			if( start >= 0 )
			{//	開始文字あり
				int	end= src.indexOf( "}", start+2 );
				if( end >= 0 )
				{//	終了文字あり
					String	prefix= src.substring( 0, start );
					log.debug( prefix );
					String	target= src.substring( start+2, end );
					String	replace= props.getProperty( target, "" );
					log.debug( "replace "+ target +"->"+ replace );
					String	suffix= src.substring( end+1 );
					log.debug( suffix );
					
					//	変換した文字で再生成する
					src= prefix + replace + suffix;
				}
				else
				{//	終了文字なし -> 終了
					break;
				}
			}
			else
			{//	開始文字なし -> 終了
				break;
			}
		}

		return src;
	}
}

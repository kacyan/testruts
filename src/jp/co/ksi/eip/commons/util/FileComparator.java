/*
 * 作成日: 2021/09/09
 */
package jp.co.ksi.eip.commons.util;

import java.io.File;
import java.util.Comparator;

import org.apache.log4j.Logger;

/**
 * Fileオブジェクトを比較します
 * @author kac
 */
public class FileComparator implements Comparator<Object>
{
	private static Logger	log= Logger.getLogger(FileComparator.class);

	/**
	 * ファイル名を比較対象にします
	 * 大文字小文字を区別しません
	 */
	public static final int TYPE_NAME_IGNORECASE= 10;
	/**
	 * ファイル名を比較対象にします
	 * 大文字小文字を区別します
	 */
	public static final int TYPE_NAME= 11;
	/**
	 * ファイルサイズを比較対象にします
	 */
	public static final int TYPE_SIZE= 20;
	/**
	 * タイムスタンプを比較対象にします
	 */
	public static final int TYPE_TIMESTAMP= 30;
	/**
	 * 昇順
	 */
	public static final int ASCENT= 1;
	/**
	 * 降順
	 */
	public static final int DESCENT= -1;


	private int	order= ASCENT;
	private int	type= TYPE_NAME_IGNORECASE;

	public FileComparator()
	{
	}

	/**
	 * カスタム・コンストラクタ
	 * @param order	ソート順を指定します
	 * @param type	比較する要素を指定します
	 */
	public FileComparator(
		int	order,
		int	type
		)
	{
		this.order= order;
		this.type= type;
	}

	/**
	 * ファイルオブジェクトを比較し、結果をintで返します
	 * このメソッドは、Arrays.sort()等から呼ばれます
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare( Object o1, Object o2 )
	{
		int	ret= 0;
		File	f1= null;
		File	f2= null;

		if( o1 instanceof File )	f1= (File)o1;
		if( o2 instanceof File )	f2= (File)o2;

		if( f1 == null )
		{
			if( f2 == null )
			{
				ret= 0;
			}
			else
			{
				ret= -1;
			}
		}
		else
		{
			if( f2 == null )
			{
				ret= 1;
			}
			else
			{
				if( f1.isDirectory() && !f2.isDirectory() )
				{
					ret= -1;
				}
				else if( !f1.isDirectory() && f2.isDirectory() )
				{
					ret= 1;
				}
				else
				{
					ret= compare( f1, f2 );
				}
			}
		}
		return ret * order;
	}

	private int compare(
		File	f1,
		File	f2
		)
	{
		int	ret= 0;
		switch (type)
		{
			case TYPE_NAME_IGNORECASE:
			default:
				ret= f1.getName().compareToIgnoreCase( f2.getName() );
				break;
			case TYPE_NAME:
				ret= f1.getName().compareTo( f2.getName() );
				break;
			case TYPE_SIZE:
				ret= f1.length() < f2.length() ? -1 : 1;
				break;
			case TYPE_TIMESTAMP:
				ret= f1.lastModified() < f2.lastModified() ? -1 : 1;
				break;
		}
		log.debug( type +"["+ ret +"]"+ f1 +","+ f2 );
		return ret;
	}

}

/*
 * 作成日: 2006/03/07
 */
package jp.co.ksi.eip.commons.util;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * ページング用の一覧データを保持するためのクラス
 * @author kac
 */
public class PagingArray
{
	private ArrayList<Object>	list= null;
	private int	start= 0;
	private int	max= 20;
	private int	total= 0;
	private int	current= 0;
	private Class<?>	type;
	
	
	public PagingArray()
	{
		list= new ArrayList<Object>();
	}

	/**
	 * カスタム・コンストラクタ
	 *
	 */
	public PagingArray( int initialCapacity )
	{
		list= new ArrayList<Object>( initialCapacity );
	}
	/**
	 * 開始インデックスを返します
	 * @return
	 */
	public int getStart()
	{
		return start;
	}
	public void setStart(int start)
	{
		this.start = start;
	}
	/**
	 * 終了インデックスを返します
	 * @return
	 */
	public int getEnd()
	{
		return getStart() + size() -1;
	}
	public int getMax()
	{
		return max;
	}
	public void setMax(int max)
	{
		this.max = max;
	}
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}

	/**
	 * @return current を返します。
	 */
	public int getCurrent()
	{
		return current;
	}

	/**
	 * @param current を current に設定します。
	 */
	public void setCurrent( int current )
	{
		this.current= current;
	}

	/**
	 * @return type を返します。
	 */
	public Class<?> getType()
	{
		return type;
	}

	/**
	 * @param type を type に設定します。
	 */
	public void setType( Class<?> type )
	{
		this.type= type;
	}

	public int size()
	{
		return list.size();
	}
	
	public Object get(
		int	index
		)
	{
		return (Object)list.get( index );
	}
	
	public void add(
			Object	obj
		)
	{
		list.add( obj );
	}
	
	public void remove(
		int	index
		)
	{
		list.remove( index );
	}

	/**
	 * @return vector を返します。
	 */
	public ArrayList<Object> getList()
	{
		return list;
	}

	/**
	 * 指定したobjが配列の何件目に格納されているかを返します
	 * @param obj
	 * @return int
	 */
	public int getIndex(
		Object	obj,
		Comparator<Object>	comp
		)
	{
		for( int i= 0; i < list.size(); i++ )
		{
			if( comp.compare( obj, list.get(i) ) == 0 )
			{//	一致する要素が見つかった
				return i;
			}
		}
		//	一致する要素は無かった
		return -1;
	}
	
	public String toString()
	{
		return "{"+ getStart() +"-"+ (getStart()+size()) +"/"+ getTotal() +" ("+ getCurrent() +")}";
	}
	
}

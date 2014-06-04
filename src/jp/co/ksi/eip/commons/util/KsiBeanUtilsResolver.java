package jp.co.ksi.eip.commons.util;

import org.apache.commons.beanutils.expression.DefaultResolver;
import org.apache.log4j.Logger;

/**
 * BeanUtilsの脆弱性(CVE-2014-0094)対策用リゾルバ
 * @author kac
 * @since 2014/04/28
 * @version 2014/05/16
 * <pre>
 * commons-beanutil-1.8以上で利用できます。
 * アプリケーションの初期化処理(ContextListener等)で以下のようにリゾルバを設定してください。
 * 	BeanUtilsBean.getInstance().getPropertyUtils().setResolver( new KsiBeanUtilsResolver() );
 * [2015/05/16]
 * describe()で例外を吐くことが判明。
 * 原因：メソッドを呼ぼうとした時にメソッド名がブランクに変えられてるてから
 * 対策：describeの時はオブジェクトを手繰らない。手繰るのが問題だったので、手繰る処理になる場合のみブランクにする
 * 具体例：
 * 	name=class, arg0=class.classloader ---> 手繰らせない。ブランクを返す。
 * 	name=class, arg0=class ----> 手繰るわけでは無いからOK。nameを返す。
 * [2014/05/15]
 * FromBeanのメソッドのうち、プロパティではないもの(=ActionFormに用意されているもの)は、ネストしない。
 * 課題：
 * FormBeanに他のインターフェースを実装している場合は、そのインターフェースのメソッドが手繰られて良くない事が起こりそう...
 * 根本解決の為には、BeanUtils.populate()でネストしない事だと思う。
 * </pre>
 */
public class KsiBeanUtilsResolver extends DefaultResolver
{
	private static Logger	log= Logger.getLogger( KsiBeanUtilsResolver.class );

	@Override
	public String next( String arg0 )
	{
		String name= super.next( arg0 );
		log.debug( "name=["+ name +"], arg0=["+ arg0 +"]" );
		if( name.equals( arg0 ) )	// 2014/05/16
		{//	nameとarg0が同じ場合は、そのまま返す
			return name;
		}
		
		if( "class".equalsIgnoreCase( name ) )
		{
			log.info( "name=["+ name +"] is unsuitable. return blank." );
			return "";
		}
		if( "classLoader".equalsIgnoreCase( name ) )
		{
			log.info( "name=["+ name +"] is unsuitable. return blank." );
			return "";
		}
		if( "multipartRequestHandler".equalsIgnoreCase( name ) )
		{
			log.info( "name=["+ name +"] is unsuitable. return blank." );
			return "";
		}
		if( "servletWrapper".equalsIgnoreCase( name ) )
		{
			log.info( "name=["+ name +"] is unsuitable. return blank." );
			return "";
		}
		if( "servlet".equalsIgnoreCase( name ) )
		{
			log.info( "name=["+ name +"] is unsuitable. return blank." );
			return "";
		}
		
		return name;
	}

}

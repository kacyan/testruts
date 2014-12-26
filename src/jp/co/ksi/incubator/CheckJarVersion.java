package jp.co.ksi.incubator;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * jarファイルのバージョンを調べる習作
 * @author kac
 * @since 2014/10/06
 * @version 2014/10/06
 */
public class CheckJarVersion
{

	private static final String SEPARATER= ",";

	/**
	 * @param args
	 * <pre>
	 * (1)指定されたフォルダ内のjarファイルを列挙します
	 * (2)jarファイルのMANIFESTを調べ、バージョン情報を表示します
	 * </pre>
	 */
	public static void main( String[] args )
	{
		File	f= new File( args[0] );
		File[]	files= f.listFiles();
		
		System.out.println( "file"+ SEPARATER +"specVer"+ SEPARATER +"implVer" );
		Arrays.sort( files );
		for( int i= 0; i < files.length; i++ )
		{
			if( !files[i].isFile() )
			{
				continue;
			}
			
			String	specVer= "none";
			String	implVer= "none";
			JarInputStream	jar= null;
			try
			{
				Manifest	mf = null;
				jar= new JarInputStream( new FileInputStream( files[i] ) );
				mf = jar.getManifest();

				Attributes	attr= mf.getMainAttributes();
				specVer= attr.getValue( java.util.jar.Attributes.Name.SPECIFICATION_VERSION );
				implVer= attr.getValue( java.util.jar.Attributes.Name.IMPLEMENTATION_VERSION );

			}
			catch( Exception e )
			{
				specVer= "Exception";
				implVer= e.toString();
			}
			finally
			{
				try
				{
					jar.close();
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
				System.out.println( files[i].getName() +SEPARATER+ specVer +SEPARATER+ implVer );
			}
		}
	}

}

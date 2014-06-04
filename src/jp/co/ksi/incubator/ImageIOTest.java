package jp.co.ksi.incubator;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageIOTest
{
	private static Logger	log= Logger.getLogger( ImageIOTest.class );
	
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		ImageIOTest	test= new ImageIOTest();
		test.doTest();
	}
	
	public void doTest()
	{
		FileInputStream	in= null;
		FileOutputStream	out= null;
		int	width= 320;
		int	height= 240;
		boolean expand= true;
		int	type= AffineTransformOp.TYPE_BILINEAR;
		try
		{
			out= new FileOutputStream( "misc/out.jpg" );
			in= new FileInputStream( "misc/syogai_after.gif" );
			BufferedImage srcImg= ImageIO.read( in );
		
			log.debug( srcImg );
			int srcWidth = srcImg.getWidth();
			int srcHeight = srcImg.getHeight();
			//	2008/04/21 アスペクト比を意識してみる
			if( ( width / srcWidth ) > ( height / srcHeight ) )
			{//	幅に合わせる
				height= width * srcHeight / srcWidth;
			}
			else
			{//	高さに合わせる
				width= height * srcWidth / srcHeight;
			}
			if( ( width < srcWidth ) || height < srcHeight )
			{//	縮小
			}
			if( ( width > srcWidth ) || height > srcHeight )
			{//	拡大
				if( !expand )
				{//	拡大禁止
					width= srcWidth;
					height= srcHeight;
				}
			}
			//	イメージを拡大・縮小する
			BufferedImage	dstImg= new BufferedImage( width, height, srcImg.getType() );
			double scaleX= (double)width/(double)srcWidth;
			double scaleY= (double)height/(double)srcHeight;
			log.debug( scaleX +","+ scaleY );
			AffineTransform	transform= AffineTransform.getScaleInstance( scaleX, scaleY );
			AffineTransformOp	op= new AffineTransformOp( transform, type );
			op.filter( srcImg, dstImg );
			
			//	イメージをファイルに描く
			JPEGImageEncoder	encoder= JPEGCodec.createJPEGEncoder( out );
			encoder.encode( dstImg );

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				in.close();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			try
			{
				out.close();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
}

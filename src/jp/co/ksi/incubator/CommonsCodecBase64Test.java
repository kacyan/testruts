package jp.co.ksi.incubator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import net.arnx.jsonic.JSON;

/**
 * commons-codesのbase64の習作。
 * @author kac
 * @since 2014/07/07
 * @version 2014/07/07
 */
public class CommonsCodecBase64Test
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{

		String	charset="ISO-2022-JP";
		String	src= "6YCa55+lOg0KDQrjgr_jgqTjg4jjg6s6IOOBpuOBmeOBqA0K5pel5pmCOiAyMDE0LzA3LzAxICjngaspIDEwOjAwIO+9niAxMTowMCDmnbHkuqwNCuOCq+ODrOODs+ODgOODvDoga2Fja3NpLnRlc3RAZ21haWwuY29tDQrlj4LliqDogIU6DQogICAgICog44GL44GP44KE44KT44Gm44GZ44GoLSDnrqHnkIbogIUNCg0K5LqI5a6a44Gu6Kmz57SwOiAgDQpodHRwczovL3d3dy5nb29nbGUuY29tL2NhbGVuZGFyL2V2ZW50P2FjdGlvbj1WSUVXJmVpZD1ZV1YyY1dsdFptTnhPV1JuYW5FeGJYRnNOemxvWlhVMk1UZ2dhMkZqYTNOcExuUmxjM1JBYlEmdG9rPU1qRWphMkZqYTNOcExuUmxjM1JBWjIxaGFXd3VZMjl0TTJaa09HTmtOR1JsWTJWaU1HRmtNekV6TTJVNE1XTXdNamN3T1RSaFl6UmlOekl6TnpBMU1RJmN0ej1Bc2lhL1Rva3lvJmhsPWphDQoNCkdvb2dsZSDjgqvjg6zjg7Pjg4Djg7zjgYvjgonjga7mi5vlvoXnirY6IGh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvDQoNCuOAjGthY2tzaS50ZXN0QGdtYWlsLmNvbeOAjeOCq+ODrOODs+ODgOODvOOBrumAmuefpeOBruWPl+S_oeOBjOioreWumuOBleOCjOOBpuOBhOOCi+OBn+OCgeOAgeacrOODoeODvCANCuODq+OCkiBrYWNrc2kudGVzdEBnbWFpbC5jb20g44Gr44GK6YCB44KK44GX44Gm44GE44G+44GZ44CCDQoNCumAmuefpeOCkuWPl+OBkeWPluOCieOBquOBhOOCiOOBhuOBq+OBmeOCi+OBq+OBr+OAgWh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvIOOBq+ODreOCsOOCpCANCuODs+OBl+OAgeOCq+ODrOODs+ODgOODvOOBrumAmuefpeioreWumuOCkuWkieabtOOBl+OBpuOBj+OBoOOBleOBhOOAgg0K";
		try
		{
			//	Shift_JIS dGhpcyBtYWlsIGlzIHRlc3QuDQrjgYLjgYTjgYbjgYjjgYrjgIINCg0KDQoNCl5fXg0K
			src= "dGhpcyBtYWlsIGlzIHRlc3QuDQrjgYLjgYTjgYbjgYjjgYrjgIINCg0KDQoNCl5fXg0K";
			charset= "utf-8";	//	Shift_JIS -> utf-8
//			src= "6YCa55-lOg0KDQrjgr_jgqTjg4jjg6s6IOOBpuOBmeOBqA0K5pel5pmCOiAyMDE0LzA3LzAxICjngaspIDEwOjAwIO-9niAxMTowMCDmnbHkuqwNCuOCq-ODrOODs-ODgOODvDoga2Fja3NpLnRlc3RAZ21haWwuY29tDQrlj4LliqDogIU6DQogICAgICog44GL44GP44KE44KT44Gm44GZ44GoLSDnrqHnkIbogIUNCg0K5LqI5a6a44Gu6Kmz57SwOiAgDQpodHRwczovL3d3dy5nb29nbGUuY29tL2NhbGVuZGFyL2V2ZW50P2FjdGlvbj1WSUVXJmVpZD1ZV1YyY1dsdFptTnhPV1JuYW5FeGJYRnNOemxvWlhVMk1UZ2dhMkZqYTNOcExuUmxjM1JBYlEmdG9rPU1qRWphMkZqYTNOcExuUmxjM1JBWjIxaGFXd3VZMjl0TTJaa09HTmtOR1JsWTJWaU1HRmtNekV6TTJVNE1XTXdNamN3T1RSaFl6UmlOekl6TnpBMU1RJmN0ej1Bc2lhL1Rva3lvJmhsPWphDQoNCkdvb2dsZSDjgqvjg6zjg7Pjg4Djg7zjgYvjgonjga7mi5vlvoXnirY6IGh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvDQoNCuOAjGthY2tzaS50ZXN0QGdtYWlsLmNvbeOAjeOCq-ODrOODs-ODgOODvOOBrumAmuefpeOBruWPl-S_oeOBjOioreWumuOBleOCjOOBpuOBhOOCi-OBn-OCgeOAgeacrOODoeODvCANCuODq-OCkiBrYWNrc2kudGVzdEBnbWFpbC5jb20g44Gr44GK6YCB44KK44GX44Gm44GE44G-44GZ44CCDQoNCumAmuefpeOCkuWPl-OBkeWPluOCieOBquOBhOOCiOOBhuOBq-OBmeOCi-OBq-OBr-OAgWh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvIOOBq-ODreOCsOOCpCANCuODs-OBl-OAgeOCq-ODrOODs-ODgOODvOOBrumAmuefpeioreWumuOCkuWkieabtOOBl-OBpuOBj-OBoOOBleOBhOOAgg0K";
//			charset= "utf-8";	//	ISO-2022-JP -> utf-8

			src= "6YCa55-lOg0KDQrjgr_jgqTjg4jjg6s6IOS8keaahw0K5pel5pmCOiAyMDE0LzA0LzI0ICjmnKgpDQrjgqvjg6zjg7Pjg4Djg7w6IGthY2tzaS50ZXN0QGdtYWlsLmNvbQ0K5Y-C5Yqg6ICFOg0KICAgICAqIOOBi-OBj-OChOOCk-OBpuOBmeOBqC0g566h55CG6ICFDQoNCuS6iOWumuOBruips-e0sDogIA0KaHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS9jYWxlbmRhci9ldmVudD9hY3Rpb249VklFVyZlaWQ9TVRjNWNtSm9kalJtT1RsaGEyWmpPRzQzYTI1c2RUUnJNbWNnYTJGamEzTnBMblJsYzNSQWJRJnRvaz1NakVqYTJGamEzTnBMblJsYzNSQVoyMWhhV3d1WTI5dE9UVTRNbUV4WmpVeFlXRTJPV1JoWkRCbE1ESTFPRGN6WlRObU1HSm1OMk5qTWpVeE16WTVZUSZjdHo9QXNpYS9Ub2t5byZobD1qYQ0KDQpHb29nbGUg44Kr44Os44Oz44OA44O844GL44KJ44Gu5oub5b6F54q2OiBodHRwczovL3d3dy5nb29nbGUuY29tL2NhbGVuZGFyLw0KDQrjgIxrYWNrc2kudGVzdEBnbWFpbC5jb23jgI3jgqvjg6zjg7Pjg4Djg7zjga7pgJrnn6Xjga7lj5fkv6HjgYzoqK3lrprjgZXjgozjgabjgYTjgovjgZ_jgoHjgIHmnKzjg6Hjg7wgDQrjg6vjgpIga2Fja3NpLnRlc3RAZ21haWwuY29tIOOBq-OBiumAgeOCiuOBl-OBpuOBhOOBvuOBmeOAgg0KDQrpgJrnn6XjgpLlj5fjgZHlj5bjgonjgarjgYTjgojjgYbjgavjgZnjgovjgavjga_jgIFodHRwczovL3d3dy5nb29nbGUuY29tL2NhbGVuZGFyLyDjgavjg63jgrDjgqQgDQrjg7PjgZfjgIHjgqvjg6zjg7Pjg4Djg7zjga7pgJrnn6XoqK3lrprjgpLlpInmm7TjgZfjgabjgY_jgaDjgZXjgYTjgIINCg==";
//			src= "6YCa55+lOg0KDQrjgr_jgqTjg4jjg6s6IOOBpuOBmeOBqA0K5pel5pmCOiAyMDE0LzA3LzAxICjngaspIDEwOjAwIO+9niAxMTowMCDmnbHkuqwNCuOCq+ODrOODs+ODgOODvDoga2Fja3NpLnRlc3RAZ21haWwuY29tDQrlj4LliqDogIU6DQogICAgICog44GL44GP44KE44KT44Gm44GZ44GoLSDnrqHnkIbogIUNCg0K5LqI5a6a44Gu6Kmz57SwOiAgDQpodHRwczovL3d3dy5nb29nbGUuY29tL2NhbGVuZGFyL2V2ZW50P2FjdGlvbj1WSUVXJmVpZD1ZV1YyY1dsdFptTnhPV1JuYW5FeGJYRnNOemxvWlhVMk1UZ2dhMkZqYTNOcExuUmxjM1JBYlEmdG9rPU1qRWphMkZqYTNOcExuUmxjM1JBWjIxaGFXd3VZMjl0TTJaa09HTmtOR1JsWTJWaU1HRmtNekV6TTJVNE1XTXdNamN3T1RSaFl6UmlOekl6TnpBMU1RJmN0ej1Bc2lhL1Rva3lvJmhsPWphDQoNCkdvb2dsZSDjgqvjg6zjg7Pjg4Djg7zjgYvjgonjga7mi5vlvoXnirY6IGh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvDQoNCuOAjGthY2tzaS50ZXN0QGdtYWlsLmNvbeOAjeOCq+ODrOODs+ODgOODvOOBrumAmuefpeOBruWPl+S_oeOBjOioreWumuOBleOCjOOBpuOBhOOCi+OBn+OCgeOAgeacrOODoeODvCANCuODq+OCkiBrYWNrc2kudGVzdEBnbWFpbC5jb20g44Gr44GK6YCB44KK44GX44Gm44GE44G+44GZ44CCDQoNCumAmuefpeOCkuWPl+OBkeWPluOCieOBquOBhOOCiOOBhuOBq+OBmeOCi+OBq+OBr+OAgWh0dHBzOi8vd3d3Lmdvb2dsZS5jb20vY2FsZW5kYXIvIOOBq+ODreOCsOOCpCANCuODs+OBl+OAgeOCq+ODrOODs+ODgOODvOOBrumAmuefpeioreWumuOCkuWkieabtOOBl+OBpuOBj+OBoOOBleOBhOOAgg0K";
//			charset= "utf-8";
			System.out.println( decodeBodyData( src, charset ) );
			
			src= src.replaceAll( "-", "+" );
			sun.misc.BASE64Decoder	decoder= new sun.misc.BASE64Decoder();
			System.out.println( new String( decoder.decodeBuffer( src ), charset ) );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		//	test1();
	}

	/**
	 * body.dataのエンコードされた本文をデコードします
	 * @param body
	 * @param charset
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * <pre>
	 * -を+に変換する
	 * base64デコードする
	 * </pre>
	 */
	private static String decodeBodyData( String body, String charset )
			throws UnsupportedEncodingException, IOException
	{
		body= body.replaceAll( "-", "+" );	//	-を+に変換してbase64デコード
		
		try
		{
			return new String( Base64.decodeBase64( body.getBytes() ), charset );
		}
		catch( Exception e )
		{
			return e.toString();
		}
	}

}

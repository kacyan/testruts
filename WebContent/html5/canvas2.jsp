<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- canvas2.jsp update:2015/03/31 -->
<%
String	lineDashDot= request.getParameter( "lineDashDot" );
if( lineDashDot == null ) {
	lineDashDot= "";
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">

//	矩形を描画する
function drowRect( context, x, y, width, high, text, textFillStyle ) {

	context.save();	//	save/restoreを使う

	var rectX= x - width/2;
	var rectY= y - high/2;
	//	矩形を塗りつぶす
	context.fillRect( rectX, rectY, width, high );
	//	矩形の枠線を描く
	context.strokeRect( rectX-0.5, rectY-0.5, width+1, high+1 );
	//	文字を描く
//	context.font= "bold 14px 'ＭＳ　ゴシック'";
	if( textFillStyle == undefined ) {
		context.fillStyle = 'rgb(0,0,0)';
	}else{
		context.fillStyle = textFillStyle;
	}

	var textX = rectX + ( width - context.measureText( text ).width ) / 2;
	var textY = rectY + high / 2;
	context.fillText( text, textX, textY );

	context.restore();
}

//楕円を描画する
function drowArc( context, x, y, r, text, textFillStyle ) {

	var cx= 2;
	var cy= 1;

	context.save();	//	save/restoreを使う
	context.beginPath();
	context.scale( cx, cy );
	context.lineWidth= 1;
	context.arc( x/cx, y/cy, r, 0, Math.PI*2 );
	context.fill();
	context.stroke();
	context.restore();	//	元に戻す

	//	200,35に、文字を描く
	context.save();	//	save/restoreを使う
//	context.font= "bold 14px 'ＭＳ　ゴシック'";
	context.fillStyle = 'rgb(0,0,0)'; //文字色は黒
	var textX= x - context.measureText( text ).width / 2;
	var textY= y;
	context.fillText( text, textX, textY );
	context.restore();	//	元に戻す
}

function drowLine( context, startX, startY, endX, endY, lineDashDot ) {
	
	context.save();	//	save/restoreを使う
	context.beginPath();
	
	if( lineDashDot != undefined ) {
		if ( context.setLineDash != undefined )   context.setLineDash([<%=lineDashDot %>]);
	}
	context.moveTo( startX, startY );
	context.lineTo( endX, endY );
	context.closePath();
	context.stroke();
	
	context.restore();

}

function sample() {

	//描画コンテキストの取得
	var canvas = document.getElementById('sample1');
	if (canvas.getContext) {
		var context = canvas.getContext('2d');

		//色を指定する
		context.strokeStyle = 'rgb(0,0,0)'; //枠線の色
		context.fillStyle = 'rgb(256,256,128)'; //塗りつぶしの色
		context.textBaseline = "middle";	//	ベースラインを中央にしとく

		//	座標は左上が0,0

		//	中心(80,40)、幅100高さ50のS
		var s1X= 80;
		var s1Y= 40;
		var s1Width= 100;
		var s1High= 50;
		var s1Text= "営業担当者";
		//	中心(520,100)、幅100高さ50のS2
		var s2X= 520;
		var s2Y= 100;
		var s2Width= 100;
		var s2High= 50;
		var s2Text= "営業G長";
		//	中心(520,160)、幅100高さ50のS3
		var s3X= 520;
		var s3Y= 160;
		var s3Width= 100;
		var s3High= 50;
		var s3Text= "開発G長";

		//	中心(220,40)、半径25のV
		var v1X= 220;
		var v1Y= 40;
		var v1R= 25;
		var v1Text= "作成する";
		
		//	中心(220,100)、半径25のV2
		var v2X= 220;
		var v2Y= 100;
		var v2R= 25;
		var v2Text= "渡す";

		//	中心(220,160)、半径25のV3
		var v3X= 220;
		var v3Y= 160;
		var v3R= 25;
		var v3Text= "承認する";

		//	中心(380,40)、幅100高さ50のO
		var o1X= 380;
		var o1Y= 40;
		var o1Width= 100;
		var o1High= 50;
		var o1Text= "見積依頼書";

		//	SとVを結ぶ
		drowLine( context, s1X, s1Y, v1X, v1Y );
		drowLine( context, s1X, s1Y, v2X, v2Y );
		drowLine( context, s2X, s2Y, v2X, v2Y );
		drowLine( context, s3X, s3Y, v3X, v3Y );
		
		//	VとOを結ぶ
		drowLine( context, v1X, v1Y, o1X, o1Y, "true" );
		drowLine( context, v2X, v2Y, o1X, o1Y, "true" );
		drowLine( context, v3X, v3Y, o1X, o1Y, "true" );
		
		//	Sを描画する
		drowRect( context, s1X, s1Y, s1Width, s1High, s1Text );
		drowRect( context, s2X, s2Y, s2Width, s2High, s2Text );
		drowRect( context, s3X, s3Y, s3Width, s3High, s3Text );
		
		//	Vを描画する
		drowArc( context, v1X, v1Y, v1R, v1Text );
		drowArc( context, v2X, v2Y, v2R, v2Text );
		drowArc( context, v3X, v3Y, v3R, v3Text );

		//	Oを描画する
		drowRect( context, o1X, o1Y, o1Width, o1High, o1Text );
	}

}
</script>
</head>

<body onload="sample()">
<h2>canvas Test</h2>
<%=request.getRequestURI() %><br/>
<%=request.getHeader( "user-agent" ) %>
<form action="">
lineDashDot:<input type="text" name="lineDashDot" value="<%=lineDashDot %>"><br/>
<input type="submit">
</form>
<hr/>

<canvas id="sample1" width="800" height="300">
図形を表示するには、canvasタグをサポートしたブラウザが必要です。
</canvas>

<hr/>
</body>
</html>

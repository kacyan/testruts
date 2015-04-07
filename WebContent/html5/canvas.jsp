<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="ksi.tld" prefix="ksi" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>
<!-- canvas.jsp update:2015/03/30 -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">

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
		
		//	20,10 に、幅100高さ50の四角を塗りつぶす
		var rectX= 20;
		var rectY= 10;
		var rectWidth= 100;
		var rectHigh= 50;
		var text= "営業担当者";
		//	矩形を塗りつぶす
		context.fillRect( rectX, rectY, rectWidth, rectHigh );
		//	矩形の枠線を描く
		context.strokeRect( rectX, rectY, rectWidth, rectHigh );
		//	文字を描く
		context.save();	//	save/restoreを使う
//		context.font= "bold 14px 'ＭＳ　ゴシック'";
		context.fillStyle = 'rgb(0,0,0)'; //文字色は黒
		var textX = rectX + ( rectWidth - context.measureText( text ).width ) / 2;
		var textY = rectY + rectHigh / 2;
		context.fillText( text, textX, textY );
		context.restore();
		
		//	220,35 に、半径25の楕円を描く
		var cx= 2;
		var cy= 1;
		var arcX= 220;
		var arcY= 35;
		var arcR= 25;
		text= "作成する";
		context.save();	//	save/restoreを使う
		context.beginPath();
		context.scale( cx, cy );
		context.lineWidth= 1;
		context.arc( arcX/cx, arcY/cy, arcR, 0, Math.PI*2 );
		context.fill();
		context.stroke();
		context.restore();	//	元に戻す
		//	200,35に、文字を描く
		context.save();	//	save/restoreを使う
		context.font= "bold 14px 'ＭＳ　ゴシック'";
		context.fillStyle = 'rgb(0,0,0)'; //文字色は黒
		x= arcX - context.measureText( text ).width / 2;
		y= arcY;
		context.fillText( text, x, y );
		context.restore();
		
		//	320,10に、幅100高さ50の矩形を描く
		rectX= 320;
		rectY= 10;
		rectWidth= 100;
		rectHigh= 50;
		text= "開発見積依頼書";
		//	矩形を塗りつぶす
		context.fillRect( rectX, rectY, rectWidth, rectHigh );
		//	矩形の枠線を描く
		context.strokeRect( rectX, rectY, rectWidth, rectHigh );
		//	文字を描く
		context.save();	//	save/restoreを使う
//		context.font= "bold 14px 'ＭＳ　ゴシック'";
		context.fillStyle = 'rgb(0,0,0)'; //文字色は黒
		textX = rectX + ( rectWidth - context.measureText( text ).width ) / 2;
		textY = rectY + rectHigh / 2;
		context.fillText( text, textX, textY );
		context.restore();
		
		//	120,35から170,35に実線を引く
		context.save();	//	save/restoreを使う
		context.beginPath();
		context.moveTo(120,35);
		context.lineTo(170,35);
		context.closePath();
		context.stroke();
		context.restore();
		
		//	120,35から170,35に点線を引く
		context.save();	//	save/restoreを使う
		context.beginPath();
		if ( context.setLineDash !== undefined )   context.setLineDash([3,3]);
		context.moveTo(270,35);
		context.lineTo(320,35);
		context.closePath();
		context.stroke();
		context.restore();
		
	}

}
</script>
</head>

<body onload="sample()">
<h2>canvas Test</h2>
<%=request.getRequestURI() %><br/>
<%=request.getHeader( "user-agent" ) %>
<hr/>

<canvas id="sample1" width="800" height="300">
図形を表示するには、canvasタグをサポートしたブラウザが必要です。
</canvas>

<hr/>
</body>
</html>

<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!-- canvas3.jsp update:2015/03/31 -->
<html>
<head>
<meta charset="UTF-8">
<title>マウス座標の取得</title>
<script type="text/javascript">

function sample() {

	var canvas = document.getElementById("sample2");
	if(!canvas.getContext) return false;

	var ctx = canvas.getContext("2d");
	ctx.font= "bold 14px 'ＭＳ　ゴシック'";

	//options
	var mouseX = 0, mouseY = 0;//マウスの相対座標
	var timer;
	var delay = 10;
	var	down= 0;
	
	//mousemoveでマウスの座標を取得    
	canvas.addEventListener( "mousemove", onMouseMove, false );
	canvas.addEventListener( "mousedown", onMouseDown, false );
	canvas.addEventListener( "mouseup", onMouseUp, false );
	
	function onMouseMove( e ) {
		
		var rect = e.target.getBoundingClientRect();
		mouseX = e.clientX - rect.left;
		mouseY = e.clientY -rect.top;
		draw( mouseX, mouseY, "move "+ down );
	}
	
	function onMouseDown( e ) {
		
		down= 1;
		var rect = e.target.getBoundingClientRect();
		mouseX = e.clientX - rect.left;
		mouseY = e.clientY -rect.top;
		draw( mouseX, mouseY, "down" );
	}
	function onMouseUp( e ) {
		
		down= 0;
		var rect = e.target.getBoundingClientRect();
		mouseX = e.clientX - rect.left;
		mouseY = e.clientY -rect.top;
		draw( mouseX, mouseY, "up" );
	}
	
	//描画処理
	function draw( x, y, text ) {
		ctx.clearRect( 0, 0, canvas.width, canvas.height ); //一度canvasをクリア
		ctx.fillText( "["+ text +"] x="+ x +", y="+ y +"", 30, 30 );
	}

}

</script>

<style type="text/css" media="screen">
/* <![CDATA[ */
div#stage{
	width:800px;
	height:300px;
	position:absolute;
	top:50px;
	left:50px;
	background:#ffffa0;
}
/* ]]> */
</style>

</head>

<body onload="sample()">
<div id="stage">
<canvas id="sample2" width="800" height="300">
図形を表示するには、canvasタグをサポートしたブラウザが必要です。
</canvas>
</div>
</body>
</html>

<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!-- dd.jsp update:2015/04/02 -->
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link href="../EaselJS-0.8.0/_assets/css/shared.css" rel="stylesheet" type="text/css"/>
 	<link href="../EaselJS-0.8.0/_assets/css/examples.css" rel="stylesheet" type="text/css"/>
	<script src="../EaselJS-0.8.0/_assets/js/examples.js"></script>
	<script src="../EaselJS-0.8.0/lib/easeljs-NEXT.combined.js"></script>
<script id="editable">

var canvas, stage;

var mouseTarget;	// the display object currently under the mouse, or being dragged
var dragStarted;	// indicates whether we are currently in a drag operation
var offset;
var update = true;

function init() {

	//	これ何してるのか不明
	examples.showDistractor();
	
	//	キャンバスを取得する
	canvas = document.getElementById("testCanvas");
	//	ステージを生成する(createjsの概念)
	stage = new createjs.Stage(canvas);

	//	タッチ可能にする？
	createjs.Touch.enable(stage);

	//	マウスオーバーを可能にする？
	stage.enableMouseOver(10);
	stage.mouseMoveOutside = true; // keep tracking the mouse even when it leaves the canvas

	//	画像を読込む
	var image = new Image();
//	image.src = "../EaselJS-0.8.0/_assets/art/daisy.png";
	image.src = "s1.png";
	image.onload = handleImageLoad;
	
}

function stop() {
	createjs.Ticker.removeEventListener("tick", tick);
}

function handleImageLoad(event) {
	var image = event.target;
	var bitmap;
	var container = new createjs.Container();
	stage.addChild(container);

	// create and populate the screen with random daisies:
	bitmap = new createjs.Bitmap(image);
	container.addChild(bitmap);
	bitmap.x = 0;
	bitmap.y = 0;
	bitmap.scaleX = bitmap.scaleY = bitmap.scale = 1;
	bitmap.name = "bmp_" + 0;
	bitmap.cursor = "pointer";

	// using "on" binds the listener to the scope of the currentTarget by default
	// in this case that means it executes in the scope of the button.
	bitmap.on("mousedown", function (evt) {
		this.parent.addChild(this);
		this.offset = {x: this.x - evt.stageX, y: this.y - evt.stageY};
	});

	// the pressmove event is dispatched when the mouse moves after a mousedown on the target until the mouse is released.
	bitmap.on("pressmove", function (evt) {
		this.x = evt.stageX + this.offset.x;
		this.y = evt.stageY + this.offset.y;
		// indicate that the stage should be updated on the next tick:
		update = true;
	});

	bitmap.on("rollover", function (evt) {
		this.scaleX = this.scaleY = this.scale * 1.1;
		update = true;
	});

	bitmap.on("rollout", function (evt) {
		this.scaleX = this.scaleY = this.scale;
		update = true;
	});

	examples.hideDistractor();
	
	//	描画更新のためのタイマーをリスナー登録する
	createjs.Ticker.addEventListener("tick", tick);
}

//	タイマーのリスナー
function tick(event) {
	// this set makes it so the stage only re-renders when an event handler indicates a change has happened.
	if (update) {
		update = false; // only update once
		stage.update(event);	//	描画を更新する
	}
}

</script>

</head>

<body onload="init();">
<header class="EaselJS">
	<h1><%=request.getServletPath() %></h1>
</header>

<div>
	<canvas id="testCanvas" width="960" height="400"></canvas>
</div>
</body>
</html>

<%@	page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<!-- createjs2.jsp update:2015/04/02 -->
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link href="../EaselJS-0.8.0/_assets/css/shared.css" rel="stylesheet" type="text/css"/>
	<link href="../EaselJS-0.8.0/_assets/css/examples.css" rel="stylesheet" type="text/css"/>
	<script src="../EaselJS-0.8.0/_assets/js/examples.js"></script>
	<script src="../EaselJS-0.8.0/lib/easeljs-NEXT.combined.js"></script>
<script id="editable">

var update = true;

function init() {
	
	
	var canvas = document.getElementById( "demoCanvas" );
	var stage = new createjs.Stage( canvas );
	
	//Alternatively use can also use the graphics property of the Shape class to renderer the same as above.
	var shape = new createjs.Shape();
	shape.graphics.beginFill("#ff0000").drawRect(0, 0, 200, 100);
	stage.addChild( shape );
	
	//	Textオブジェクトを作る
	var text = new createjs.Text("shape["+ shape.graphics.r +"]", "36px Arial", "#777");
	text.textAlign = "center";
	text.textBaseline= "middle";

	// add the text as a child of the stage. This means it will be drawn any time the stage is updated
	// and that its transformations will be relative to the stage coordinates:
	stage.addChild(text);

	// position the text on screen, relative to the stage coordinates:
	text.x = canvas.width / 2;
	text.y = canvas.height / 2;
	
	stage.update();
}

</script>

</head>

<body onload="init();">
<header class="EaselJS">
	<h1>Stage1</h1>
</header>

<div>
	<canvas id="demoCanvas" width="960" height="400"></canvas>
</div>
</body>
</html>

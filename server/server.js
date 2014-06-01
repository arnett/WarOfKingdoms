// convert JSON into objects Move
function createMoveObjects(movesJson) {
	var formattedMoves = [];

	for (var i = 0; i < movesJson.length; i++) { 
		var move = new gameLogicModule.Move(movesJson[i].action, movesJson[i].targetTerritory);
		formattedMoves.push(move);
	}

	return formattedMoves;
}

// POST function that receives the moves of the user
function sendMoves(req, res) {	
    var moveObjects = createMoveObjects(req.body);
    console.log(moveObjects);
    res.send("[]");
}

// this convert any "object" in JSON string 
// (be careful with the attributes name to match with the application sintax)
function objToJSON(object) {
	return JSON.stringify(object);
}

// importing the module that contains the "objects" signature
var gameLogicModule = require('./gameLogic.js');

// required imports
var express = require('express');
var bodyParser = require('body-parser');

var app = express();

app.use(bodyParser());
app.use(bodyParser.json({ type: 'application/vnd.api+json' }))

// POST functions
app.post('/sendMoves', sendMoves);


// GET functions
// app.get('/getX', getX)

// port that the server will be listening
var port = 3000;
app.listen(port);

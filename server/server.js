
// POST function that receives the moves of the user
function sendMoves(req, res) {	
    var moveObjects = createMoveObjects(req.body);
    console.log(moveObjects);
    res.send("[]");
}

// POST function that create a new user
function connect(req, res) {

	var id = req.body.id;
	var name = req.body.name;
	var aPlayer = new gameLogicModule.Player(id, name)
	playerList.push(aPlayer);

	var chosenTerritoryIndex = getRandomNumber(0, 25);
	while (territoriesWithOwners.contains(chosenTerritoryIndex)) {
		chosenTerritoryIndex = getRandomNumber(0, 25);
	}

	territoriesWithOwners.push(chosenTerritoryIndex);
	var playersTerritory = territories[chosenTerritoryIndex];
	res.send(objToJSON(playersTerritory));
}

function getRandomNumber(start, end) {
	return Math.floor(Math.random() * end) + start;
}

// convert JSON into objects Move
function createMoveObjects(movesJson) {
	var formattedMoves = [];

	for (var i = 0; i < movesJson.length; i++) { 
		var move = new gameLogicModule.Move(movesJson[i].action, movesJson[i].targetTerritory);
		formattedMoves.push(move);
	}

	return formattedMoves;
}


Array.prototype.contains = function(elem)
{
   for (var i in this)
   {
       if (this[i] == elem) return true;
   }
   return false;
}

// this convert any "object" in JSON string 
// (be careful with the attributes name to match with the application sintax)
function objToJSON(object) {
	return JSON.stringify(object);
}

// importing external module
var gameLogicModule = require('./gameLogic.js');
var utilsModule = require('./utils.js');

// required imports
var express = require('express');
var bodyParser = require('body-parser');

var app = express();

app.use(bodyParser());
app.use(bodyParser.json({ type: 'application/vnd.api+json' }))


var playerList = new Array();
var territories = utilsModule.createTerritories();
var territoriesWithOwners = new Array();

// POST functions
app.post('/sendMoves', sendMoves);
app.post('/connect', connect);


// GET functions
// app.get('/getX', getX)

// port that the server will be listening
var port = 3000;
app.listen(port);

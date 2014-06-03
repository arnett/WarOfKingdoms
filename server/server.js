
// POST function that receives the moves of the user
/*
	Usage Example:
	[{"action": "Attack", "targetTerritory": {"name": "J"} }, {"action": "Attack", "targetTerritory": {"name": "E"}}]
*/
function sendMoves(req, res) {
    var moveObjects = createMoveObjects(req.body);
    console.log(moveObjects);
    res.send("[]");
}

// POST function that receives the dice value to check who won the conflict
/*
	Usage Example:
	{"conflict": true, "diceValue": 1}
*/
function sendDiceValue(req, res) {
    console.log(req.body);
    res.send("[]");
}

// GET function that returns the territories and its owner
function getTerritories(req, res) {
    res.send("[]");
}

// GET function that returns if the game ended
function getIsGameFinished(req, res) {
    res.send("[]");
}

// POST function that create a new user
/*
	Usage Example:
	{"name": "Player 1", "id": "MAC 1", "session": 1, "territory": 1}
*/
function connect(req, res) {

	var id = req.body.id;
	var name = req.body.name;
  	var session = req.body.session;
  	var territory = req.body.territory;

	var aPlayer = new gameLogicModule.Player(id, name)
	playerList.push(aPlayer);
  	gameLogicModule.addPlayerToSession(session, aPlayer);

	var chosenTerritoryIndex = territory;
	if (territoriesWithOwners.contains(chosenTerritoryIndex)) {
		//TODO use a meaningful error code, using this just to test passing error codes
		res.send(400, {});
	} else {
		territoriesWithOwners.push(chosenTerritoryIndex);
		var playersTerritory = territories[chosenTerritoryIndex];
		res.send(objToJSON(playersTerritory));
	}
}

// convert JSON into objects Move
function createMoveObjects(movesJson) {
	console.log(movesJson)
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


var session = new gameLogicModule.Session(1)
//var aPlayer = new gameLogicModule.Player(1, "Andre")
gameLogicModule.addSession(session)
//gameLogicModule.addPlayerToSession(1, aPlayer);


var playerList = new Array();
var territories = utilsModule.createTerritories();
var territoriesWithOwners = new Array();

// POST functions
app.post('/sendMoves'        , sendMoves);
app.post('/sendDiceValue'    , sendDiceValue);
app.get ('/getTerritories'   , getTerritories);
app.get ('/getIsGameFinished', getIsGameFinished);
app.post('/connect'          , connect);


// GET functions
// app.get('/getX', getX)

// port that the server will be listening
var port = 3000;
app.listen(port);

console.log("Server running...");

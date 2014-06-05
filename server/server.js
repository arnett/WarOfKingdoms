
// POST function that receives the moves of the user
/*
	Usage Example:
	[{"action": "Attack", "targetTerritory": {"name": "J"} }, {"action": "Attack", "targetTerritory": {"name": "E"}}]
*/
function sendMoves(req, res) {
	var id      = req.body.id;
	var session = req.body.session;

    var moveObjects = createMoveObjects(id, req.body.moves);

    var session = sessionController.getSession(session)
    if (session.getPlayerTerritories(id).length != moveObjects.length) {
    	res.send("[]");
    	return undefined
    }

    for (var i = 0; i < moveObjects.length; i++)
    	session.movesInCurrentTurn.push(moveObjects[i])
    // session.movesInCurrentTurn.push(moveObjects);
    session.movesInCurrentTurnConsumed++;
    if (session.movesInCurrentTurnConsumed >= session.playerList.length)
    	session.movesInCurrentTurnComplete = true;

    console.log(session.movesInCurrentTurn)
    var interval = setInterval( function() {

    	if (session.movesInCurrentTurnComplete) {
    		clearInterval(interval);

    		session.consumeMove()

    		var conflict = "[ "
    		for (var i = 0; i < session.playersInConflict.length; i++) {
    			conflict = conflict.concat(session.playersInConflict[i]).concat(",")
    		};
    		conflict = conflict.concat("0 ]")

    		res.send("{ 'user_in_conflict': ".concat(conflict).concat(" }"));

    		//session.processMoves()
    	} else {
    		console.log('Waiting for users. '.concat(session.movesInCurrentTurnConsumed).concat('/').concat(session.playerList.length))
    	}

    }, 1000);
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
	{"name": "Player 1", "id": 1, "session": 1, "territory": "A"}
*/
function connect(req, res) {

	var id        = req.body.id;
	var name      = req.body.name;
  	var session   = req.body.session;
  	var territory = req.body.territory;

	var aPlayer = new gameLogicModule.Player(id, name)

	sessionController.addPlayerToSession(session, aPlayer);
  	
	if (sessionController.isTerritoryOwned(session, territory)) {
		//TODO use a meaningful error code, using this just to test passing error codes
		// http://en.wikipedia.org/wiki/List_of_HTTP_status_codes
		res.send(400, {});
	} else {
		var session = sessionController.getSession(session)
		sessionController.addTerritoryToPlayerInSession(session.id, aPlayer.id, territory)
		
		console.log(session)
		res.send(objToJSON(session.getTerritory(territory)));
	}
}

var count = 0;

function teste(req, res) {
	count++;
	console.log(count)

	
	var interval = setInterval(
		function() {
			if (count == 2) {
				clearInterval(interval);
				res.send('{"count":' + count +'}');
			} else {
				console.log("Ainda não")
			}
		}
		, 1000);
	//setTimeout( function(){ if (count > 3) res.send('{"count":' + count +'}'); } , 1000);
}

// convert JSON into objects Move
function createMoveObjects(player_id, movesJson) {
	var formattedMoves = [];

	for (var i = 0; i < movesJson.length; i++) {
		var move = new gameLogicModule.Move(player_id, movesJson[i].action, movesJson[i].targetTerritoryName);
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


var sessionController = new gameLogicModule.SessionController()
sessionController.addNewSession(new gameLogicModule.Session(1))
sessionController.addTerritoriesToSession(1, utilsModule.createTerritories())

//var session = new gameLogicModule.Session(1)
//var aPlayer = new gameLogicModule.Player(1, "Andre")
//gameLogicModule.addSession(session)
//gameLogicModule.addPlayerToSession(1, aPlayer);


//var playerList = new Array();
//var territories = utilsModule.createTerritories();
var territoriesWithOwners = new Array();

// POST functions
app.post('/sendMoves'        , sendMoves);
app.post('/sendDiceValue'    , sendDiceValue);
app.get ('/getTerritories'   , getTerritories);
app.get ('/getIsGameFinished', getIsGameFinished);
app.post('/connect'          , connect);
app.get ('/teste'            , teste);


// GET functions
// app.get('/getX', getX)

// port that the server will be listening
var port = 3000;
app.listen(port);

console.log("Server running...");

//--------------------------------  IMPORTS -----------------------------------------//

// EXTERNAL-IMPORTS
var gameLogicModule = require('./gameLogic.js');
var utilsModule     = require('./utils.js');

// REQUIRED-IMPORTS
var express     = require('express');
var bodyParser  = require('body-parser');


// POST function that receives the moves of the user
/*
    Usage Example:

    [
        {
            "origin":{
                "name":"A", 
                "owner":{"name":"Stark"}
                }, 
            "target":{
                "name":"B", 
                "owner":{"name":"Greyjoy"}
                },
            "action":"attack"
        }
    ]
    
*/

function sendMoves(req, res) {

    var playerMoves = utilsModule.createMoveObjects(req.body);
    numPlayersThatSentMoves++;

    allMovesRound = allMovesRound.concat(playerMoves);   // saving all moves in one list per round

    console.log("Moves Sent");
    console.log(playerMoves);

    var busyWait = setInterval(function(){

        if (numPlayersThatSentMoves == NUM_MAX_PLAYERS_ROOM) {

            // first thread to process moves data
            if (responsesSentSendMovesCount == 0) { 

                // this object is created to be possible to generate a JSONArray using JSON.stringify
                conflicts = utilsModule.generateConflicts(allMovesRound);
                var nonConflictingMoves = utilsModule.getNonConflictingMoves(allMovesRound, conflicts);

                territoriesList = utilsModule.updateTerritories(territoriesList, conflicts, nonConflictingMoves);
            }

            // this object is created to be possible to generate a JSONArray using JSON.stringify
            var returnObject = new utilsModule.SendmovesReturnObj(conflicts, territoriesList);

            res.send(utilsModule.objToJSON(returnObject));
            responsesSentSendMovesCount++;

            if (responsesSentSendMovesCount == NUM_MAX_PLAYERS_ROOM) {
                numPlayersThatSentMoves = 0;
                responsesSentSendMovesCount = 0;
                allMovesRound = new Array();
                conflicts = new Array();
            }

            clearInterval(busyWait);
        } else {
            // do nothing (waiting for the room to full up) - explicatory 'else'
        }
    },1000);
}



// POST function to connect the new user and send the start data
/*
	Usage Example:
	{"id": 1, "name": "Player 1", "house": {whatever cause I'm gonna define here in connect the players house}}
*/

function connect(req, res) {

	var id        = req.body.id;
	var name      = req.body.name;
  	var house 	  = chooseHouse();

	var aPlayer = new gameLogicModule.Player(id, name, house);
	playerList.push(aPlayer);

    console.log(id + "Player Connected");

	var busyWait = setInterval(function(){
		
		if (playerList.length == NUM_MAX_PLAYERS_ROOM) {

			// this object is created to be possible to generate a JSONArray using JSON.stringify
			var returnObject = new utilsModule.ConnectReturnObj(territoriesList, playerList);
			
			res.send(utilsModule.objToJSON(returnObject));

			clearInterval(busyWait);
		} else {
			// do nothing (waiting for the room to full up) - explicatory 'else'
		}
	},1000);
}


Array.prototype.contains = function(elem)
{
   for (var i in this)
   {
       if (this[i] == elem) return true;
   }
   return false;
}

chooseHouse = function() {

    var houseIndex = Math.floor((Math.random() * 5) + 0);   // random from 0 to 5 (has 6 houses total)
    while (housesAlreadyChosen.contains(houseIndex)) {
        houseIndex = Math.floor((Math.random() * 5) + 0);
    }
    housesAlreadyChosen.push(houseIndex);
    return availableHouses[houseIndex];
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// NODE VARIABLES
var app = express();
app.use(bodyParser());
app.use(bodyParser.json({ type: 'application/vnd.api+json' }))


// GAME VARIABLES
var playerList                  = new Array();
var territoriesList             = utilsModule.createTerritories();
var availableHouses             = utilsModule.createHouses();
var NUM_MAX_PLAYERS_ROOM        = 2;
var housesAlreadyChosen         = new Array();

// SendMoves Global Variables
var numPlayersThatSentMoves     = 0;
var responsesSentSendMovesCount = 0;
var conflicts                   = new Array();
var allMovesRound               = new Array();


// POST functions
app.post('/sendMoves'        , sendMoves);
app.post('/connect'          , connect);

// GET functions


// port that the server will be listening
var port = 3000;
app.listen(port);

console.log("Server running...");

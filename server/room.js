var gameLogicModule = require('./gameLogic.js');
var utilsModule = require('./utils.js');

Array.prototype.contains = function(elem)
{
   for (var i in this)
   {
       if (this[i] == elem) return true;
   }
   return false;
}

exports.Room = function(numberOfPlayers, roomId) {
    this.roomId = roomId;
    this.NUM_MAX_PLAYERS_ROOM = numberOfPlayers;
    this.reset()
}

this.Room.prototype.reset = function() {
    // Game Variables
    this.playerList = new Array();
    this.housesAlreadyChosen = new Array();
    this.territoriesList = utilsModule.createTerritories();
    this.numTurns = 2;
    this.actualTurn = 1;
    this.availableHouses = utilsModule.createHouses();

    // SendMoves Global Variables
    this.numPlayersThatSentMoves = 0;
    this.responsesSentSendMovesCount = 0;
    this.conflicts = new Array();
    this.allMovesRound = new Array();
}

this.Room.prototype.sendMoves = function(req, res) {
    console.log(req.body.moves);
    var playerMoves = utilsModule.createMoveObjects(JSON.parse(req.body.moves));
    this.numPlayersThatSentMoves++;

    this.allMovesRound = this.allMovesRound.concat(playerMoves);   // saving all moves in one list per round

    console.log("Moves Sent");
    console.log(playerMoves);

    var f = function(roomObject) {

        if (roomObject.numPlayersThatSentMoves == roomObject.NUM_MAX_PLAYERS_ROOM) {

            // first thread to process moves data
            if (roomObject.responsesSentSendMovesCount == 0) { 

                // this object is created to be possible to generate a JSONArray using JSON.stringify
                roomObject.conflicts = utilsModule.generateConflicts(roomObject.allMovesRound);
                var nonConflictingMoves = utilsModule.getNonConflictingMoves(roomObject.allMovesRound, roomObject.conflicts);

                territoriesList = utilsModule.updateTerritories(roomObject.territoriesList, roomObject.conflicts, nonConflictingMoves);
            }

            var gamestate = utilsModule.generateGameState(roomObject.territoriesList, roomObject.playerList, roomObject.actualTurn, roomObject.numTurns)

            // this object is created to be possible to generate a JSONArray using JSON.stringify
            var returnObject = new utilsModule.SendmovesReturnObj(roomObject.conflicts, roomObject.territoriesList,
                                       gamestate);

            res.send(utilsModule.objToJSON(returnObject));
            roomObject.responsesSentSendMovesCount++;

            if (roomObject.responsesSentSendMovesCount == roomObject.NUM_MAX_PLAYERS_ROOM) {
                roomObject.numPlayersThatSentMoves = 0;
                roomObject.responsesSentSendMovesCount = 0;
                roomObject.allMovesRound = new Array();
                roomObject.conflicts = new Array();
                roomObject.actualTurn++

                if (gamestate.isGameEnd) {
                    roomObject.reset()
                }
            }

            clearInterval(busyWait);
        } else {
            // do nothing (waiting for the room to full up) - explicatory 'else'
        }
    }

    var busyWait = setInterval(f, 1000, this);
}

this.Room.prototype.chooseHouse = function() {
    var houseIndex = Math.floor((Math.random() * 5) + 0);   // random from 0 to 5 (has 6 houses total)
    while (this.housesAlreadyChosen.contains(houseIndex)) {
        houseIndex = Math.floor((Math.random() * 5) + 0);
    }
    this.housesAlreadyChosen.push(houseIndex);
    return this.availableHouses[houseIndex];
}

this.Room.prototype.connect = function(req, res) {
	var id        = req.body.id;
	var name      = req.body.name;
  	var house 	  = this.chooseHouse();

	var aPlayer = new gameLogicModule.Player(id, name, house);
	this.playerList.push(aPlayer);

    console.log(id + " Player Connected");

    var f = function(roomObject){
		if (roomObject.playerList.length == roomObject.NUM_MAX_PLAYERS_ROOM) {

			// this object is created to be possible to generate a JSONArray using JSON.stringify
			var returnObject = new utilsModule.ConnectReturnObj(roomObject.territoriesList, roomObject.playerList, roomObject.roomId);

            console.log(utilsModule.objToJSON(returnObject));
			res.send(utilsModule.objToJSON(returnObject));

			clearInterval(busyWait);
		} else {
			// do nothing (waiting for the room to full up) - explicatory 'else'
		}
	}

	var busyWait = setInterval(f, 1000, this);
}

this.Room.prototype.isFull = function() {
    return this.playerList.length >= this.NUM_MAX_PLAYERS_ROOM;
}

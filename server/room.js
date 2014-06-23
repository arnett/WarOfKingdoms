var gameLogicModule = require('./gameLogic.js');
var utilsModule = require('./utils.js');

var mapModule    = require('./mapController.js');
var playerModule = require('./playerController.js');

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
    this.numMaxOfPlayers = numberOfPlayers;
    this.reset()
}

this.Room.prototype.reset = function() {
    // Game Variables
    this.players = new playerModule.PlayerController()
    // this.playerList = new Array();
    this.housesAlreadyChosen = new Array();
    this.map = new mapModule.Map();
    // this.territoriesList = utilsModule.createTerritories();
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

        if (roomObject.numPlayersThatSentMoves == roomObject.numMaxOfPlayers) {

            // first thread to process moves data
            if (roomObject.responsesSentSendMovesCount == 0) { 

                // this object is created to be possible to generate a JSONArray using JSON.stringify
                roomObject.conflicts = utilsModule.generateConflicts(roomObject.allMovesRound);
                var nonConflictingMoves = utilsModule.getNonConflictingMoves(roomObject.allMovesRound, roomObject.conflicts);

                roomObject.map.update(roomObject.conflicts, nonConflictingMoves);
                roomObject.players.updateStatus(roomObject.map);
            }

            // this object is created to be possible to generate a JSONArray using JSON.stringify
            var winners = roomObject.players.getWinners(roomObject.map);
            var gameState = new gameLogicModule.GameState(winners.length > 0, winners, roomObject.players.getById(req.body.id).status)

            var returnObject = new utilsModule.SendmovesReturnObj(roomObject.conflicts, roomObject.map.map, gameState);

            console.log(utilsModule.objToJSON(returnObject))
            res.send(utilsModule.objToJSON(returnObject));
            roomObject.responsesSentSendMovesCount++;

            if (roomObject.responsesSentSendMovesCount == roomObject.numMaxOfPlayers) {
                roomObject.numPlayersThatSentMoves = 0;
                roomObject.responsesSentSendMovesCount = 0;
                roomObject.allMovesRound = new Array();
                roomObject.conflicts = new Array();

                if (gameState.isGameEnd) {
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


this.Room.prototype.connect = function(req, res, numTerritoriesToConquerInNorth, numTerritoriesToConquerInCenter, numTerritoriesToConquerInSouth) {
    var id        = req.body.id;
    var name      = req.body.name;
    var house 	  = this.chooseHouse();

    var status = new gameLogicModule.Status(numTerritoriesToConquerInNorth, numTerritoriesToConquerInCenter, numTerritoriesToConquerInSouth)
    var aPlayer = this.players.createPlayer(id, name, house, status)

    this.players.add(aPlayer);
    this.map.addOwnerInTerritory(aPlayer.house, aPlayer.house.territoryOriginName)

    console.log(aPlayer.name + " connected");

    var f = function(roomObject){
		if (roomObject.isFull()) {
            // this object is created to be possible to generate a JSONArray using JSON.stringify
            var returnObject = new gameLogicModule.ConnectReturnObj(roomObject.map.map, roomObject.players.players, roomObject.roomId);

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
    return this.players.length() >= this.numMaxOfPlayers;
}

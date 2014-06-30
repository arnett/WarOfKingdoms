var gameLogicModule = require('./gameLogic.js');
var utilsModule     = require('./utils.js');

var mapModule    = require('./mapController.js');
var playerModule = require('./playerController.js');

Array.prototype.contains = function(elem) {
   for (var i in this)
       if (this[i] == elem)
        return true;
   
   return false;
}

Room = function(numberOfPlayers, roomId) {
    this.roomId = roomId;
    this.numMaxOfPlayers = numberOfPlayers;
    this.reset()
}

exports.roomController = function() {
    this.rooms = new Array();
}

this.roomController.prototype.isPlayerConnected = function(id) {
    for (var roomId = 0;roomId < this.rooms.length; roomId++)
        if (this.get(roomId).hasPlayer(id))
            return true;

    return false;
}

this.roomController.prototype.add = function(room) {
    this.rooms.push(room)
}

this.roomController.prototype.get = function(i) {
    return this.rooms[i];
}

this.roomController.prototype.getNextAvailableRoom = function(num_players) {
    var roomId = 0;

    for (;roomId < this.rooms.length; roomId++) {
        var room = this.get(roomId);
        if (!room.isFull() && room.numMaxOfPlayers == num_players)
            return room;
    }

    return new Room(num_players, roomId);
}

Room.prototype.reset = function() {
    // Game Variables
    this.players             = new playerModule.PlayerController()
    this.housesAlreadyChosen = new Array();
    this.map                 = new mapModule.Map();
    this.availableHouses     = utilsModule.createHouses();

    // SendMoves Global Variables
    this.playersThatSentMoves        = new Array();
    this.numPlayersThatSentMoves     = 0;
    this.responsesSentSendMovesCount = 0;
    this.conflicts                   = new Array();
    this.allMovesRound               = new Array();
}

Room.prototype.connect = function(req, res, numTerritoriesToConquerInNorth, numTerritoriesToConquerInCenter, numTerritoriesToConquerInSouth) {
    var id        = req.body.id;
    var name      = req.body.name;
    var house     = this.chooseHouse();

    var status  = new gameLogicModule.Status(numTerritoriesToConquerInNorth, numTerritoriesToConquerInCenter, numTerritoriesToConquerInSouth)
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

Room.prototype.hasPlayer = function(id) {
    return this.players.getById(id) != null;
}

Room.prototype.sendMoves = function(req, res) {
    console.log(req.body.moves);
    var playerMoves = utilsModule.createMoveObjects(JSON.parse(req.body.moves));
    this.numPlayersThatSentMoves++;
    console.log(req.body.id)
    if (!this.playersThatSentMoves.contains(req.body.id))
        this.playersThatSentMoves.push(req.body.id)
    else
        return;

    this.allMovesRound = this.allMovesRound.concat(playerMoves);   // saving all moves in one list per round

    console.log("Moves Sent");
    console.log(playerMoves);

    count = 0;
    var f = function(roomObject) {
        count++
        console.log(count)

        if (count >= 1) {
            var left = roomObject.numMaxOfPlayers - roomObject.numPlayersThatSentMoves;
            if (left > 0) {
                for (var i = 0; i < left; i++) {
                    var playerMoves = utilsModule.createMoveObjects([]);
                    roomObject.numPlayersThatSentMoves++;
                    roomObject.allMovesRound = roomObject.allMovesRound.concat(playerMoves);   // saving all moves in one list per round
                }
            }
        }

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
            var winners   = roomObject.players.getWinners(roomObject.map);
            var gameState = new gameLogicModule.GameState(winners.length > 0, winners, roomObject.players.getById(req.body.id).status)

            var returnObject = new utilsModule.SendmovesReturnObj(roomObject.conflicts, roomObject.map.map, gameState);

            console.log(utilsModule.objToJSON(returnObject))
            res.send(utilsModule.objToJSON(returnObject));
            roomObject.responsesSentSendMovesCount++;

            if (roomObject.responsesSentSendMovesCount == roomObject.playersThatSentMoves.length) {
                roomObject.playersThatSentMoves        = new Array();
                roomObject.numPlayersThatSentMoves     = 0;
                roomObject.responsesSentSendMovesCount = 0;
                roomObject.allMovesRound               = new Array();
                roomObject.conflicts                   = new Array();

                if (gameState.isGameFinished)
                    roomObject.reset()
            }

            clearInterval(busyWait);
        } else {
            // do nothing (waiting for the room to full up) - explicatory 'else'
        }
    }

    var busyWait = setInterval(f, 1000, this);
}

Room.prototype.chooseHouse = function() {
    var houseIndex = Math.floor((Math.random() * 3) + 0);   // random from 0 to 5 (has 6 houses total)
    while (this.housesAlreadyChosen.contains(houseIndex)) {
        houseIndex = Math.floor((Math.random() * 3) + 0);
    }
    this.housesAlreadyChosen.push(houseIndex);
    return this.availableHouses[houseIndex];
}

Room.prototype.isFull = function() {
    return this.players.length() >= this.numMaxOfPlayers;
}

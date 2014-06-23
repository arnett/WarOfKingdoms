//--------------------------------  IMPORTS -----------------------------------------//

// EXTERNAL-IMPORTS
var roomModule  = require('./room.js');

// REQUIRED-IMPORTS
var express     = require('express');
var bodyParser  = require('body-parser');

function sendMoves(req, res) {
    var roomId = parseInt(req.body.roomId);
    rooms[roomId].sendMoves(req, res);
}

function getNextAvailableRoom(num_players, rooms) {
    var roomId = 0;

    for (;roomId < rooms.length; roomId++) {
        var room = rooms[roomId];
        if (!room.isFull() && room.numMaxOfPlayers == num_players) {
            return room;
        }
    }

    return new roomModule.Room(num_players, roomId);
}

function connect(req, res) {
    var num_players = req.body.num_players;

    var room = getNextAvailableRoom(num_players, rooms);
    rooms.push(room);
    room.connect(req, res, NUM_TERRITORIES_TO_CONQUER_IN_NORTH, NUM_TERRITORIES_TO_CONQUER_IN_CENTER, NUM_TERRITORIES_TO_CONQUER_IN_SOUTH);
}

//function connect(req, res) {
    //var room = getNextAvailableRoom(NUM_MAX_PLAYERS_ROOM, rooms);
    //rooms.push(room);
    //room.connect(req, res);
//}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// NODE VARIABLES
var app = express();
app.use(bodyParser());
app.use(bodyParser.json({ type: 'application/vnd.api+json' }))

// POST FUNCTIONS
app.post('/sendMoves'        , sendMoves);
app.post('/connect'          , connect);

// port that the server will be listening
var port = 3000;
app.listen(port);

var rooms                                = Array();

var NUM_TERRITORIES_TO_CONQUER_IN_NORTH  = 3;
var NUM_TERRITORIES_TO_CONQUER_IN_CENTER = 3;
var NUM_TERRITORIES_TO_CONQUER_IN_SOUTH  = 3;

console.log("Server running...");

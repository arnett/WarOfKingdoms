//--------------------------------  IMPORTS -----------------------------------------//

// EXTERNAL-IMPORTS
var gameLogicModule = require('./gameLogic.js');
var utilsModule     = require('./utils.js');
var roomModule      = require('./room.js');

// REQUIRED-IMPORTS
var express     = require('express');
var bodyParser  = require('body-parser');

function sendMoves(req, res) {
    var roomId = parseInt(req.body.roomId);
    rooms[roomId].sendMoves(req, res);
}

function newconnect(req, res) {
    var roomId = 0;
    var num_players = req.body.num_players;

    for (;roomId < rooms.length; roomId++) {
        var room = rooms[roomId];
        if (!room.isFull() && room.NUM_MAX_PLAYERS_ROOM == num_players) {
            room.connect(req, res);
            return;
        }
    }

    var room = new roomModule.Room(num_players, roomId);
    rooms.push(room);
    room.connect(req, res);
}

function connect(req, res) {
    var roomId = 0;

    for (;roomId < rooms.length; roomId++) {
        var room = rooms[roomId];
        if (!room.isFull()) {
            room.connect(req, res);
            return;
        }
    }

    var room = new roomModule.Room(NUM_MAX_PLAYERS_ROOM, roomId);
    rooms.push(room);
    room.connect(req, res);
}

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

var rooms                = Array();
var NUM_MAX_PLAYERS_ROOM = 2;

console.log("Server running...");

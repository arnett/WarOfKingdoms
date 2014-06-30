//--------------------------------  IMPORTS -----------------------------------------//

// EXTERNAL-IMPORTS
var roomModule  = require('./room.js');

// REQUIRED-IMPORTS
var express     = require('express');
var bodyParser  = require('body-parser');
var sqlite3     = require('sqlite3')

function sendMoves(req, res) {
    var roomId = parseInt(req.body.roomId);

    console.log("Sending move: ".concat(req.body.id))
    console.log(roomsController.get(roomId).playersThatSentMoves.contains(req.body.id))
    if (roomsController.get(roomId).playersThatSentMoves.contains(req.body.id))
        return;

    roomsController.rooms[roomId].sendMoves(req, res);
}

function connect(req, res) {
    var num_players = req.body.num_players;

    console.log(req.body.id)
    console.log(roomsController.isPlayerConnected(req.body.id))
    if (roomsController.isPlayerConnected(req.body.id))
        return;

    var room = roomsController.getNextAvailableRoom(num_players);
    roomsController.add(room);
    room.connect(req, res, NUM_TERRITORIES_TO_CONQUER_IN_NORTH, NUM_TERRITORIES_TO_CONQUER_IN_CENTER, NUM_TERRITORIES_TO_CONQUER_IN_SOUTH);
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

var roomsController = new roomModule.roomController();

var NUM_TERRITORIES_TO_CONQUER_IN_NORTH  = 3;
var NUM_TERRITORIES_TO_CONQUER_IN_CENTER = 3;
var NUM_TERRITORIES_TO_CONQUER_IN_SOUTH  = 3;

console.log("Server running...");

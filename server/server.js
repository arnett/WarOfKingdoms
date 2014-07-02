//--------------------------------  IMPORTS -----------------------------------------//

// EXTERNAL-IMPORTS
var roomModule  = require('./room.js');
var utilsModule = require('./utils.js');

// REQUIRED-IMPORTS
var express     = require('express');
var bodyParser  = require('body-parser');

function sendMoves(req, res) {
    var roomId = parseInt(req.body.roomId);

    console.log("Sending move: ".concat(req.body.id))
    console.log(roomsController.get(roomId).playersThatSentMoves.contains(req.body.id))
    if (roomsController.get(roomId).playersThatSentMoves.contains(req.body.id))
        return;
    if (!roomsController.isPlayerConnected(req.body.id))
        return;

    roomsController.rooms[roomId].sendMoves(req, res);
}

function connect(req, res) {
    var num_players = req.body.num_players;

    console.log(req.body.id)
    console.log(roomsController.isPlayerConnected(req.body.id))
    if (roomsController.isPlayerConnected(req.body.id))
        return;

    var numAiPlayers = 1;
    var room = roomsController.getNextAvailableRoom(num_players, numAiPlayers);
    room.connect(req, res, utilsModule.NUM_TERRITORIES_TO_CONQUER_IN_NORTH, 
                 utilsModule.NUM_TERRITORIES_TO_CONQUER_IN_CENTER, utilsModule.NUM_TERRITORIES_TO_CONQUER_IN_SOUTH);
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

console.log("Server running...");

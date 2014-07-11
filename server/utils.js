var gameLogicModule = require('./gameLogic.js');

////
// CRUD FUNCTIONS
////

exports.createHouses = function () {

	var aHouse;
	var houses = new Array();

	aHouse = new gameLogicModule.House("Stark", "C");
	houses.push(aHouse);

	aHouse = new gameLogicModule.House("Martell", "Y");
	houses.push(aHouse);

	//aHouse = new gameLogicModule.House("Lannister", "M");
	//houses.push(aHouse);

	//aHouse = new gameLogicModule.House("Baratheon", "R");
	//houses.push(aHouse);

	//aHouse = new gameLogicModule.House("Tyrell", "T");
	//houses.push(aHouse);


	//aHouse = new gameLogicModule.House("Greyjoy", "J");
	//houses.push(aHouse);

	return houses;
}

// convert JSON into objects Move
exports.createMoveObjects = function(movesJson) {
	var formattedMoves = [];

	for (var i = 0; i < movesJson.length; i++) {
		var move = new gameLogicModule.Move(
											movesJson[i].origin, 
											movesJson[i].target, 
											movesJson[i].action);
		formattedMoves.push(move);
	}

	return formattedMoves;
}

// this convert any "object" in JSON string 
exports.objToJSON = function(object) {
	return JSON.stringify(object);
}

////
// CONNECT FUNCTIONS
////

////
// SEND MOVES FUNCTIONS
////

// get ready for a big ass function ;)
exports.generateConflicts = function(moves) {

	var conflicts = new Array();

	for (var i = 0; i < moves.length; i++) {
		
		var aMove = moves[i];
		var aConflict = null;

		// avoiding create multiples conflicts for a territory
		if (conclitAlreadyExists(aMove, conflicts)) {
			continue;
		}

		for (var j = 0; j < moves.length; j++) {
			
			var anotherMove = moves[j];

			// verifying if has a conflict ("they want the same territory")
			if (aMove.target.name == anotherMove.target.name) {

				// avoiding create a conflict with itself
				if (aMove.origin.owner.name != anotherMove.origin.owner.name) {

					if (aConflict == null) {
						aConflict = new gameLogicModule.Conflict(aMove.target, new Array(), new Array());
						aConflict.houses.push(aMove.origin.owner);
						aConflict.diceValues.push(getRandomDiceValue());
					}
					aConflict.houses.push(anotherMove.origin.owner);
					aConflict.diceValues.push(getRandomDiceValue());
				}
			}

		}
		if (aConflict != null) {
			conflicts.push(aConflict);	
		}
		
	}
	return conflicts;
}

exports.getNonConflictingMoves = function(moves, conflicts) {

	var nonConflictingMoves = new Array();

	for (var i = 0; i < moves.length; i++) {

		var isConflicting = false;

		for (var j = 0; j < conflicts.length; j++) {

			if (moves[i].target.name == conflicts[j].territory.name) {
				isConflicting = true;
				break;
			}
		}		

		if (!isConflicting) {
			nonConflictingMoves.push(moves[i]);
		}	
	}

	return nonConflictingMoves;
}

exports.SendmovesReturnObj = function(conflicts, updatedMap, gameState) {
	this.conflicts = conflicts;
	this.updatedMap = updatedMap;
	this.gameState = gameState;
}

////
// OTHERS
////

conclitAlreadyExists = function (move, conflicts) {

	for (var i = 0; i < conflicts.length; i++) {

		// a conflic for this territory was already created
		if (conflicts[i].territory.name == move.target.name) {
			return true;
		}
	}
	return false;
}

// the diceValue index is important cause the diceValue index is equal to its house index
exports.getBiggestDiceValueIndex = function(values) {

	var biggestValue = -1;
	var biggestValueIndex = -1;
	var hasMoreThanOneBiggestValue = false;

	for (var i = 0; i < values.length; i++) {

		if (values[i] > biggestValue) {
			biggestValue = values[i];
			biggestValueIndex = i;
			hasMoreThanOneBiggestValue = false;

		} else if (values[i] == biggestValue) {
			hasMoreThanOneBiggestValue = true;
		}
	}

	if (hasMoreThanOneBiggestValue) {
		return -1;
	}

	return biggestValueIndex;
}

getRandomDiceValue = function() {
	return Math.floor((Math.random() * 6) + 1);
}

module.exports.NUM_TERRITORIES_TO_CONQUER_IN_NORTH = 3;
module.exports.NUM_TERRITORIES_TO_CONQUER_IN_CENTER = 3;
module.exports.NUM_TERRITORIES_TO_CONQUER_IN_SOUTH = 3;

////
// THE END
////

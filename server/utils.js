var gameLogicModule = require('./gameLogic.js');

exports.createTerritories = function () {


	var territory = null;
	var territoriesArray = new Array();

	territory = new gameLogicModule.Territory("A");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("B");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("C");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("D");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("E");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("F");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("G");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("H");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("I");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("J");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("K");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("L");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("M");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("N");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("O");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("P");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Q");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("R");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("S");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("T");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("U");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("V");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("X");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Z");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Y");
	territory.owner = null;
	territoriesArray.push(territory);

    return territoriesArray;
}

exports.createHouses = function () {

	var aHouse;
	var houses = new Array();

	aHouse = new gameLogicModule.House("Stark");
	houses.push(aHouse);

	aHouse = new gameLogicModule.House("Baratheon");
	houses.push(aHouse);

	aHouse = new gameLogicModule.House("Lannister");
	houses.push(aHouse);

	aHouse = new gameLogicModule.House("Tyrell");
	houses.push(aHouse);

	aHouse = new gameLogicModule.House("Martell");
	houses.push(aHouse);

	aHouse = new gameLogicModule.House("Greyjoy");
	houses.push(aHouse);

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

conclitAlreadyExists = function (move, conflicts) {

	for (var i = 0; i < conflicts.length; i++) {

		// a conflic for this territory was already created
		if (conflicts[i].territory.name == move.target.name) {
			return true;
		}
	}
	return false;
}

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
						aConflict = new gameLogicModule.Conflict(aMove.target, new Array());
						aConflict.houses.push(aMove.origin.owner);
					}
					aConflict.houses.push(anotherMove.origin.owner);
				}
			}

		}
		if (aConflict != null) {
			conflicts.push(aConflict);	
		}
		
	}
	return conflicts;
}



// this convert any "object" in JSON string 
exports.objToJSON = function(object) {
	return JSON.stringify(object);
}

exports.ConnectReturnObj = function(territories, players) {
	this.territories = territories;
	this.players = players;
}

var gameLogicModule = require('./gameLogic.js');

////
// CRUD FUNCTIONS
////
exports.createTerritories = function () {
	var territory = null;
	var territoriesArray = new Array();

	// NORTH
	territory = new gameLogicModule.Territory("A");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("B");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("C");
	territory.owner = null;
	territory.house = new gameLogicModule.House("Stark");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("D");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("E");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("F");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("G");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("H");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	// CENTER
	territory = new gameLogicModule.Territory("I");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("J");
	territory.owner = null;
	territory.house = new gameLogicModule.House("Greyjoy");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("K");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("L");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("M");
	territory.owner = null;
	territory.house = new gameLogicModule.House("Lannister");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("N");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("O");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("P");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Q");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	// SOUTH
	territory = new gameLogicModule.Territory("R");
	territory.owner = null;
	territory.house = new gameLogicModule.House("Baratheon");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("S");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("T");
	territory.owner = null;
	territory.house = new gameLogicModule.House("Tyrell");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("U");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("V");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("X");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Z");
	territory.owner = null;
	territory.house = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Y");
	territory.owner = null;
	territory.house = new gameLogicModule.House("Martell");
	territoriesArray.push(territory);

    return territoriesArray;
}

exports.addHouseOwnerInTerritories = function(house, territories) {
	for (var i = 0; i < territories.length; i++) {
		if (territories[i].house != null && territories[i].house.name == house.name)
			territories[i].owner = house;
	}
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

exports.updateTerritories = function(territories, conflicts, nonConflicting) {

	// updating map with nonConflicing moves
	for (var i = 0; i < nonConflicting.length; i++) {

		var territoryIndex = getTerritoryIndex(territories, nonConflicting[i].target.name);
		territories[territoryIndex].owner = nonConflicting[i].origin.owner;
	}		

	// updating map with the result of the conflicts
	for (var i = 0; i < conflicts.length; i++) {	

		var biggestDiceValue = getBiggestDiceValueIndex(conflicts[i].diceValues);

		if (biggestDiceValue != -1) {
			
			var territoryIndex = getTerritoryIndex(territories, conflicts[i].territory.name);

			territories[territoryIndex].owner = conflicts[i].houses[biggestDiceValue];
		}
	}

	return territories;
}

////
// GENERAL FUNCTIONS
////

// this convert any "object" in JSON string 
exports.objToJSON = function(object) {
	return JSON.stringify(object);
}

////
// CONNECT FUNCTIONS
////

exports.ConnectReturnObj = function(territories, players, roomId) {
	this.territories = territories;
	this.players = players;
    this.roomId = roomId;
}

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

exports.generateGameState = function(territories, playerlist) {
	var numTerritoriesInEachRegionToConquer = new Array()
	
	numTerritoriesInEachRegionToConquer.push(3)
	numTerritoriesInEachRegionToConquer.push(3)
	numTerritoriesInEachRegionToConquer.push(3)

	var winners = getWinners(playerlist, territories, numTerritoriesInEachRegionToConquer)

    return new gameLogicModule.GameState(isGameEnd(winners), winners)
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
getBiggestDiceValueIndex = function(values) {

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

getTerritoryIndex = function(territories, territoryName) {
	
	for (var i = 0; i < territories.length; i++) {
	
		if (territories[i].name == territoryName) {
			return i;
		}
	}
	return -1;
}

getRandomDiceValue = function() {
	return Math.floor((Math.random() * 6) + 1);
}

getPlayerById = function(playerList, id) {
    if (id == undefined)
        return null

    for (var i = 0; i < playerList.length; i++) {
        var player = playerList[i];
        if (player.id == id)
            return player;
    }

    return null
}

getWinners = function(playerList, territories, numTerritoriesInEachRegionToConquer) {
	var winners = new Array();

	console.log(isPlayerLastSurvivor(playerList[0], territories))
	console.log(territories)

	for (var i = 0; i < playerList.length; i++) {
		if (( doesPlayerHasSelectedTerritoriesOwned(playerList[i], territories, 
			    	numTerritoriesInEachRegionToConquer) &&
					doPlayerOwnInitialHouse(playerList[i], territories) ) ||
				isPlayerLastSurvivor(playerList[i], territories)) {
			winners.push(playerList[i])
		}
	}

	return winners;
}

isPlayerLastSurvivor = function(player, territories) {
	for (var i = 0; i < territories.length; i++) {
		territory = territories[i];
		if (territory.owner != null && territory.owner.name != player.house.name) {
			return false;
		}
	}

	return true;
}

getNumberOfTerritoriesOwnedByPlayerInRegion = function(player, region) {
	qtd = 0;
	// North
	territories = territoriesInRegions()[0];
	if (region == "C")
		territories = territoriesInRegions()[1];
	else if (region == "S")
		territories = territoriesInRegions()[2];

	for (var i = 0; i < territories.length; i++) {
		var t = territories[i];
		
	}

return qtd;
}

doesPlayerHasSelectedTerritoriesOwned = function(player, territories, numTerritoriesInEachRegionToConquer) {
	var numberOfOwnedTerritories = new Array()

	numberOfOwnedTerritories.push(0)
	numberOfOwnedTerritories.push(0)
	numberOfOwnedTerritories.push(0)

	for (var i = 0; i < territories.length; i++) {
		if (territories[i].owner != null) {
			if (territories[i].owner.name == player.house.name) {
				if (isTerritoryInRegion(territories[i].name, "N")) {
					numberOfOwnedTerritories[0]++
				} else if (isTerritoryInRegion(territories[i].name, "C")) {
					numberOfOwnedTerritories[1]++
				} else if (isTerritoryInRegion(territories[i].name, "S")) {
					numberOfOwnedTerritories[2]++
				}
			}
		}
	};

	for (var i = 0; i < numTerritoriesInEachRegionToConquer.length; i++) {
		if (numberOfOwnedTerritories[i] < numTerritoriesInEachRegionToConquer[i])
			return false;
	};

	return true;
}

isGameEnd = function(winnerList) {
	return winnerList.length > 0;
}

territoriesInRegions = function() {
	var territoriesPerRegion  = new Array()

	var north = new Array()
	north.push("A")
	north.push("B")
	north.push("C")
	north.push("D")
	north.push("E")
	north.push("F")
	north.push("G")
	north.push("H")
	
	var center = new Array()
	center.push("I")
	center.push("J")
	center.push("K")
	center.push("L")
	center.push("M")
	center.push("N")
	center.push("O")
	center.push("P")
	center.push("Q")

	var south = new Array()
	south.push("R")
	south.push("S")
	south.push("T")
	south.push("U")
	south.push("V")
	south.push("W")
	south.push("X")
	south.push("Y")
	south.push("Z")

	territoriesPerRegion.push(north)
	territoriesPerRegion.push(center)
	territoriesPerRegion.push(south)

	return territoriesPerRegion
}

isItemInArray = function(item, array) {
	for (var i = 0; i < array.length; i++) {
		if (item == array[i])
			return true
	};

	return false;
}

isTerritoryInRegion = function(territoryName, regionName) {
	regions = territoriesInRegions()
	if (regionName == "N") {
		return isItemInArray(territoryName, regions[0])
	} else if (regionName == "C") {
		return isItemInArray(territoryName, regions[1])
	} else if (regionName == "S") {
		return isItemInArray(territoryName, regions[2])
	}
}

doPlayerOwnInitialHouse = function(player, territories) {
	var house = player.house

	for (var i = 0; i < territories.length; i++) {

		if (territories[i].house != null && territories[i].owner != null) {
			if (territories[i].house.name == house.name &&
				territories[i].owner.name == player.house.name) {
				return true;
			}
		}
	}

	return false;
}

////
// THE END
////

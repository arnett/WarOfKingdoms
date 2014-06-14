var gameLogicModule = require('./gameLogic.js');

exports.createTerritories = function () {


	var territory = null;
	var territoriesArray = new Array();

	// NORTH
	territory = new gameLogicModule.Territory("A");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("B");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("C");
	territory.owner = new gameLogicModule.House("Stark");
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

	// CENTER
	territory = new gameLogicModule.Territory("I");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("J");
	territory.owner = new gameLogicModule.House("Greyjoy");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("K");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("L");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("M");
	territory.owner = new gameLogicModule.House("Lannister");
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

	// SOUTH
	territory = new gameLogicModule.Territory("R");
	territory.owner = new gameLogicModule.House("Baratheon");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("S");
	territory.owner = null;
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("T");
	territory.owner = new gameLogicModule.House("Tyrell");
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
	territory.owner = new gameLogicModule.House("Martell");
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


// this convert any "object" in JSON string 
exports.objToJSON = function(object) {
	return JSON.stringify(object);
}

exports.ConnectReturnObj = function(territories, players, roomId) {
	this.territories = territories;
	this.players = players;
    this.roomId = roomId;
}

exports.SendmovesReturnObj = function(conflicts, updatedMap, gameState) {
	this.conflicts = conflicts;
	this.updatedMap = updatedMap;
	this.gameState = gameState;
}

exports.isAllTerritoriesOwned = function(territories) {
    for (var i = 0; i < territories.length; i++) {
        if (territories[i].owner == null)
            return false
    };

    return true;
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

indexWithMaxValue = function(array) {
    var index = 0;
    var oneMaxValue = true;
    for (var i = 0; i < array.length; i++) {
        if (array[i] > array[index]) {
            index = i;
            oneMaxValue = true;
        } else if (array[i] == array[index]) {
            oneMaxValue = false
        }
    }

    if (oneMaxValue)
        return index
    return -1;
}

exports.getPlayerWithMostTerritoriesOwned = function(territories) {
    playersCount = new Array()

    for (var i = 0; i < territories.length; i++) {
        var territory = territories[i]
        if (territory.owner != null) {
            if (playersCount[territory.owner.id] == undefined)
                playersCount[territory.owner.id] = 0;
            playersCount[territory.owner.id]++
        }
    };

    var playerWithMostTerritoriesId = indexWithMaxValue(playersCount)
    if (playerWithMostTerritoriesId == -1)
        return null;
    return getPlayerById(playerWithMostTerritoriesId)
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

getWinners = function(playerList, territories, numTerritoriesInEachRegionToConquer) {
	var winners = new Array();

	for (var i = 0; i < playerList.length; i++) {
		if (doesPlayerHasSelectedTerritoriesOwned(playerList[i], territories, numTerritoriesInEachRegionToConquer)) {
			winners.push(playerList[i])
		}
	};

	return winners;
}

isGameEnd = function(winnerList, currentTurn, totalTurns) {
	return winnerList.length > 0 || currentTurn >= totalTurns;
}

exports.generateGameState = function(territories, playerlist, currentTurn, totalTurns) {
	var numTerritoriesInEachRegionToConquer = new Array()
	
	numTerritoriesInEachRegionToConquer.push(3)
	numTerritoriesInEachRegionToConquer.push(3)
	numTerritoriesInEachRegionToConquer.push(3)

	var winners = getWinners(playerlist, territories, numTerritoriesInEachRegionToConquer)

    return new gameLogicModule.GameState(isGameEnd(winners, currentTurn, totalTurns), winners, currentTurn, totalTurns)
}

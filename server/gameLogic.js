exports.ConnectReturnObj = function(territories, players, roomId) {
    this.territories = territories;
    this.players = players;
    this.roomId = roomId;
}

exports.Move = function (origin, target, action) {
    this.origin = origin;
    this.target = target;
    this.action = action;
}

exports.House = function (name, territoryOriginName) {
    this.name            = name;
    this.territoryOriginName = territoryOriginName;
}

exports.Conflict = function (territory, houses, diceValues) {
    this.territory  = territory;
    this.houses     = houses;
    this.diceValues = diceValues
}

exports.Status = function(numTerritoriesToConquerInNorth, numTerritoriesToConquerInCenter, numTerritoriesToConquerInSouth) {
    this.numTerritoriesToConquerInNorth  = numTerritoriesToConquerInNorth;
    this.numTerritoriesToConquerInCenter = numTerritoriesToConquerInCenter;
    this.numTerritoriesToConquerInSouth  = numTerritoriesToConquerInSouth;

    this.numTerritoriesConqueredInNorth  = 0;
    this.numTerritoriesConqueredInCenter = 0;
    this.numTerritoriesConqueredInSouth  = 0;
}

exports.GameState = function (wo, isGameFinished, winnerList, status) {
    this.wo = wo;
    this.isGameFinished = isGameFinished;
    this.winnerList     = winnerList;
    this.status         = status;
}


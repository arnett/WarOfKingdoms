var gameLogicModule = require('./gameLogic.js');

Player = function (id, name, house, status) {
    this.id     = id;
    this.name   = name;
    this.status = status;
    this.house  = house;
}

exports.PlayerController = function () {
    this.players = new Array();
}

this.PlayerController.prototype.createPlayer = function(id, name, house, status) {
	return new Player(id, name, house, status)
}

this.PlayerController.prototype.add = function(player) {
    this.players.push(player);
}

this.PlayerController.prototype.length = function() {
	return this.players.length;
}

this.PlayerController.prototype.get = function(i) {
    return this.players[i];
}

this.PlayerController.prototype.getById = function(id) {
    for (var i = 0; i < this.players.length; i++)
        if (this.players[i].id == id)
            return this.players[i];
    return null;
}

this.PlayerController.prototype.updateStatus = function(mapController) {
    for (var i = 0; i < this.players.length; i++)
        this.players[i].updateStatus(mapController);
}

this.PlayerController.prototype.getWinners = function(mapController) {
    var winners = new Array();

    for (var i = 0; i < this.players.length; i++)
        if (this.players[i].didWin(mapController))
            winners.push(this.players[i]);

    return winners;
}

Player.prototype.updateStatus = function(mapController) {
    this.status.numTerritoriesConqueredInNorth  = mapController.getNumberOfTerritoriesConqueredInRegion(this.house.name, "N");
    this.status.numTerritoriesConqueredInCenter = mapController.getNumberOfTerritoriesConqueredInRegion(this.house.name, "C");
    this.status.numTerritoriesConqueredInSouth  = mapController.getNumberOfTerritoriesConqueredInRegion(this.house.name, "S");
}

Player.prototype.didWin = function(mapController) {
	if (mapController.isLastSurvivor(this.house.name))
		return true;

	if (!mapController.doPlayerKeepTerritory(this.house.name, this.house.territoryOriginName))
		return false;

	return (this.status.numTerritoriesConqueredInNorth  >= this.status.numTerritoriesToConquerInNorth)  &&
           (this.status.numTerritoriesConqueredInCenter >= this.status.numTerritoriesToConquerInCenter) &&
           (this.status.numTerritoriesConqueredInSouth  >= this.status.numTerritoriesToConquerInSouth) ;
}


